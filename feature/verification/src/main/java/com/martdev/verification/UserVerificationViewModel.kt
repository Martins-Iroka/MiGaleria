package com.martdev.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.resendOTP.ResendOTPUseCase
import com.martdev.domain.verification.UserVerificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserVerificationViewModel(
    private val userVerificationUseCase: UserVerificationUseCase,
    private val resendOTPUseCase: ResendOTPUseCase
) : ViewModel() {

    private val _response = MutableStateFlow<ResponseData<Nothing>>(
        ResponseData.NoResponse
    )
    val response = _response.asStateFlow()

    private val _resendOTPResponse = MutableStateFlow<ResponseData<String>>(ResponseData.NoResponse)
    val resendOTPResponse = _resendOTPResponse.asStateFlow()

    fun verifyCode(code: String, emailID: String) {
        viewModelScope.launch {
            userVerificationUseCase(code = code, emailID = emailID)
                .onStart {
                    _response.value = ResponseData.Loading
                }.catch {
                    _response.value = ResponseData.Error(it.message ?: "An unknown error")
                }.collect {
                    _response.value = it
                }
        }
    }

    fun resendOTP(email: String) {
        viewModelScope.launch {
            resendOTPUseCase(email)
                .catch {
                    _resendOTPResponse.value = ResponseData.Error(it.message ?: "An unknown error")
                }.collect {
                    _resendOTPResponse.value = it
                }
        }
    }

    fun resetResendOTPResponse() {
        _resendOTPResponse.value = ResponseData.NoResponse
    }

}