package com.martdev.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserLoginViewModel(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel() {
    
    private val _loginRes = MutableStateFlow<ResponseData<Nothing>>(ResponseData.NoResponse)
    val loginRes = _loginRes.asStateFlow()

    private val _loginResponse = MutableSharedFlow<ResponseData<Nothing>>()
    val loginResponse = _loginResponse.asSharedFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            userLoginUseCase(email, password)
                .onStart {
                    _loginResponse.emit(ResponseData.Loading)
                }.catch {
                    _loginResponse.emit(ResponseData.Error(it.message?: "An unknown error"))
                }
                .collect { data ->
                    _loginResponse.emit(data)
                }

        }
    }
}