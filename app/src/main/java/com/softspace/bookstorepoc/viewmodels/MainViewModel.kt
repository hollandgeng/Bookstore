package com.softspace.bookstorepoc.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import helper.CustomNavigator
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    customNavigator: CustomNavigator
) : ViewModel(){
    val navigationChannel = customNavigator.navigationChannel
}