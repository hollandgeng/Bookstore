package com.softspace.bookstorepoc.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import data.loginData
import helper.CustomNavigator
import helper.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigator: CustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(loginData())
    val uiState : StateFlow<loginData> = _uiState

    fun Login(id:String, password:String)
    {
        _uiState.update { current ->
            current.copy(
                userId = id,
                password = password
            )
        }

        Log.i("DEMO",_uiState.value.userId)
        Log.i("DEMO",_uiState.value.password)

        navigator.navigate(Screen.BooklistScreen.fullRoute)
    }


}