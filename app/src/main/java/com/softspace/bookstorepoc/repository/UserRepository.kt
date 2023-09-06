package com.softspace.bookstorepoc.repository

import data.loginResult
import data.userData
import javax.inject.Inject

class UserRepository @Inject constructor() {
    fun ValidateUser_Mock(userId : String , password : String) : loginResult
    {
        return if(userData.containsKey(userId)) {
            if (password == userData[userId]) {
                loginResult(true)
            } else {
                loginResult("Invalid password")
            }
        } else {
            loginResult("Account Not Found")
        }
    }
}