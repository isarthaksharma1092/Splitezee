package com.isarthaksharma.splitezee.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isarthaksharma.splitezee.dataClass.SMSDataClass
import com.isarthaksharma.splitezee.repository.RepositorySMS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSMS @Inject constructor(private val repositorySMS:RepositorySMS):ViewModel() {

    private val _smsData = MutableStateFlow<List<SMSDataClass>>(emptyList()) // Hold SMS data
    val smsData: StateFlow<List<SMSDataClass>> = _smsData

    init {
        fetchSMS()
    }

    private fun fetchSMS() {
        viewModelScope.launch {
            _smsData.value = repositorySMS.SMSCheck() // Fetch SMS and update state
        }
    }
}