package com.softspace.bookstorepoc.viewmodels

import androidx.lifecycle.ViewModel
import com.softspace.bookstorepoc.interfaces.ICustomNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import helper.CustomNavigator
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    customNavigator: ICustomNavigator
) : ViewModel(){
    val navigationChannel = customNavigator.navigationChannel
}