package com.isarthaksharma.splitezee.repository

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.util.Log
import androidx.core.content.ContextCompat
import com.isarthaksharma.splitezee.dataClass.SMSDataClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.net.toUri

class RepositorySMS @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val bankMessages = mutableMapOf<String, SMSDataClass>()

    // Bank configurations
    private val bankConfigurations = mapOf(
        "BOB" to BankConfig(
            senderIds = listOf("BOB", "BankofBaroda"),
            balancePatterns = listOf(
                Regex("Total Bal:Rs\\.([0-9,.]+)"),
                Regex("Available Balance:\\s*Rs\\.([0-9,.]+)")
            ),
            accountPatterns = listOf(
                Regex("A/c\\s*(\\.\\.\\.\\d+|\\d+)"),
                Regex("Account\\s*Number:\\s*(\\d+)")
            ),
            bankNamePatterns = listOf(Regex("Bank of Baroda"))
        ),
        "SBI" to BankConfig(
            senderIds = listOf("SBI", "SBIBANK"),
            balancePatterns = listOf(
                Regex("Your a/c no XXXXXXXXXXXX(\\d+) has an available balance of INR ([0-9,.]+)"),
                Regex("Dear Customer, Your Account XXXXXXXXXXXX(\\d+) has a balance of INR ([0-9,.]+) as on")
            ),
            accountPatterns = listOf(
                Regex("a/c no XXXXXXXXXXXX(\\d+)"),
                Regex("Account XXXXXXXXXXXX(\\d+)")
            ),
            bankNamePatterns = listOf(Regex("STATE BANK OF INDIA"), Regex("SBI"))
        ),
    )

    private data class BankConfig(
        val senderIds: List<String>,
        val balancePatterns: List<Regex>,
        val accountPatterns: List<Regex>,
        val bankNamePatterns: List<Regex>
    )

    suspend fun SMSCheck(): List<SMSDataClass> = withContext(Dispatchers.IO) {
        // ✅ **Check if permission is granted before querying**
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("RepositorySMS", "SMS permission not granted. Cannot fetch SMS data.")
            return@withContext emptyList()
        }

        val cursor: Cursor? = context.contentResolver.query(
            "content://sms/inbox".toUri(),
            arrayOf("_id", "address", "body", "date"),
            null,
            null,
            "date DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val sender = it.getString(it.getColumnIndexOrThrow("address"))
                val messageBody = it.getString(it.getColumnIndexOrThrow("body"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))

                for ((bankIdentifier, config) in bankConfigurations) {
                    if (config.senderIds.any { id -> sender.contains(id, ignoreCase = true) } ||
                        config.bankNamePatterns.any { pattern -> pattern.containsMatchIn(messageBody) }
                    ) {
                        val bankName = config.bankNamePatterns.firstOrNull { pattern -> pattern.containsMatchIn(messageBody) }?.find(messageBody)?.value ?: bankIdentifier
                        val accountNumber = extractUsingPatterns(config.accountPatterns, messageBody)
                        val totalBalance = extractUsingPatterns(config.balancePatterns, messageBody)
                        val availableBalance = extractUsingPatterns(config.balancePatterns, messageBody)

                        // ✅ **Ensure we don’t overwrite a correct value with "N/A"**
                        val extractedData = SMSDataClass(
                            bankName = bankName,
                            accountNumber = if (accountNumber != "N/A") accountNumber else bankMessages[sender]?.accountNumber ?: "N/A",
                            totalBalance = if (totalBalance != "N/A") totalBalance else bankMessages[sender]?.totalBalance ?: "N/A",
                            availableBalance = if (availableBalance != "N/A") availableBalance else bankMessages[sender]?.availableBalance ?: "N/A",
                            lastUpdated = date
                        )

                        // ✅ **Ensure latest data is used**
                        val key = extractedData.accountNumber.takeIf { it != "N/A" } ?: sender
                        if (!bankMessages.containsKey(key) || date > bankMessages[key]!!.lastUpdated) {
                            bankMessages[key] = extractedData
                        }

                        break // ✅ **Once matched, break to avoid unnecessary checks**
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
                return match.groupValues[1].replace(",", "") // Remove commas
            }
        }
        return "N/A"
    }
}
