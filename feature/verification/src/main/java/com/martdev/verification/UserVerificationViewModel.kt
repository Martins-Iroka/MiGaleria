package com.martdev.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.verification.UserVerificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserVerificationViewModel(
    private val userVerificationUseCase: UserVerificationUseCase
) : ViewModel() {

    private val _response = MutableStateFlow<ResponseData<Nothing>>(
        ResponseData.NoResponse
    )

    val response = _response.asStateFlow()

    fun verifyCode(code: String, email: String) {
        viewModelScope.launch {
            userVerificationUseCase(code = code, email = email)
                .onStart {
                    _response.value = ResponseData.Loading
                }.catch {
                    _response.value = ResponseData.Error(it.message ?: "An unknown error")
                }.collect {
                    _response.value = it
                }
        }
    }

}