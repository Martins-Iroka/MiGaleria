package com.martdev.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.login.UserLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class UserLoginViewModel(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel() {
    
    private val _loginRes = MutableStateFlow<ResponseData<Nothing>>(ResponseData.NoResponse)
    val loginRes = _loginRes.asStateFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            userLoginUseCase(email, password)
                .onStart { 
                    _loginRes.value = ResponseData.Loading
                }.catch {
                    _loginRes.value = ResponseData.Error(it.message?: "An unknown error")
                }
                .collect { data ->
                    println(data.toString())
                    Timber.e(data.toString())
                    _loginRes.value = data
                }

        }
    }
}