package com.softspace.bookstorepoc.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.softspace.bookstorepoc.interfaces.ICustomNavigator
import com.softspace.bookstorepoc.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import data.loginData
import helper.CustomNavigator
import helper.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigator: ICustomNavigator,
    private val userRepo : UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(loginData())
    val uiState : StateFlow<loginData> = _uiState

    fun Login(context: Context,id:String, password:String)
    {
        _uiState.update { current ->
            current.copy(
                userId = id,
                password = password
            )
        }

        val result = userRepo.ValidateUser_Mock(id,password)

        if (result.success)
        {
            navigator.navigate(Screen.BooklistScreen.fullRoute,Screen.LoginScreen.fullRoute, true)
        }
        else
        {
            Toast.makeText(context,result.error,Toast.LENGTH_SHORT).show()
        }
    }



}