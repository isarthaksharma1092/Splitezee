package com.isarthaksharma.splitezee.repository

import android.content.Context
import android.net.Uri
import com.isarthaksharma.splitezee.dataClass.SMSDataClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositorySMS @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val bankMessages = mutableMapOf<String, SMSDataClass>()

    // Different regex patterns for various bank SMS formats
    private val accountPatterns = listOf(
        Regex("A/c\\s*(\\.\\.\\.\\d+|\\d+)"), // Common format
        Regex("Account\\s*Number:\\s*(\\d+)"), // Alternative format
        Regex("Acc\\s*:\\s*(\\d+)") // Shorter format
    )

    private val balancePatterns = listOf(
        Regex("Total Bal: Rs\\.([0-9,.]+)"),
        Regex("Balance\\s*Rs\\.([.]+[0-9]+)"),
        Regex("Available Balance:\\s*Rs\\.([0-9,.]+)")
    )

    private val dateRegex = Regex("\\((\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2})\\)")

    private val bankKeywords = listOf("bank", "A/c", "Acc", "Balance", "credited", "debited")

    suspend fun SMSCheck(): List<SMSDataClass> = withContext(Dispatchers.IO) {
        val cursor = context.contentResolver.query(
            Uri.parse("content://sms/inbox"),
            arrayOf("_id", "address", "body", "date"),
            null,
            null,
            "date DESC" // Sort by latest date
        )

        cursor?.use {
            while (it.moveToNext()) {
                val sender = it.getString(it.getColumnIndexOrThrow("address"))
                val messageBody = it.getString(it.getColumnIndexOrThrow("body"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))

                if (bankKeywords.any { keyword -> messageBody.contains(keyword, ignoreCase = true) }) {
                    val extractedData = SMSDataClass(
                        bankName = sender,
                        accountNumber = extractUsingPatterns(accountPatterns, messageBody),
                        totalBalance = extractUsingPatterns(balancePatterns, messageBody),
                        availableBalance = extractUsingPatterns(balancePatterns, messageBody),
                        lastUpdated = date
                    )

                    val accountNumber = extractedData.accountNumber

                    // Store only the latest message per unique bank account
                    if (!bankMessages.containsKey(accountNumber) || date > bankMessages[accountNumber]!!.lastUpdated) {
                        bankMessages[accountNumber] = extractedData
                    }
                }
            }
        }

        return@withContext bankMessages.values.toList()
    }

    private fun extractUsingPatterns(patterns: List<Regex>, text: String): String {
        for (pattern in patterns) {
            val match = pattern.find(text)
            if (match != null) {
                return match.groupValues[1]
            }
        }
        return "N/A"
    }
}