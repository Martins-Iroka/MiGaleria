package com.martdev.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserRegistrationViewModel(
    private val userRegistrationUseCase: UserRegistrationUseCase
) : ViewModel() {

    private val _response = MutableStateFlow<ResponseData<Nothing>>(
        ResponseData.NoResponse
    )
    val response = _response.asStateFlow()

    fun registerUser(email: String, password: String, username: String) {
        viewModelScope.launch {
            userRegistrationUseCase(email, password, username)
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