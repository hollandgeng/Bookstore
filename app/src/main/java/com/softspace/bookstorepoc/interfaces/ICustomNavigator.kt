package com.softspace.bookstorepoc.interfaces

import kotlinx.coroutines.channels.Channel

interface ICustomNavigator {
    val navigationChannel : Channel<NavigationIntent>

    fun navigateBack(
        route: String? = null,
        inclusive: Boolean = false
    )

     fun navigate(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop : Boolean = false
    )
}

sealed class NavigationIntent
{
    data class NavigateBack(val route: String? = null, val inclusive :Boolean = false) : NavigationIntent()
    data class Navigate(val route : String, val popUpToRoute: String? = null,
                        val inclusive: Boolean = false, val isSingleTop: Boolean = false) : NavigationIntent()
}