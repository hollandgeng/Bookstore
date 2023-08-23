package com.softspace.bookstorepoc.repository

import javax.inject.Inject

class UserRepository @Inject constructor() {
    fun ValidateUser_Mock(userId : String , password : String) : Boolean
    {
        return userId == "SS" && password == "11111"
    }
}