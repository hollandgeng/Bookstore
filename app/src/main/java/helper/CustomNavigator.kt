package helper

import com.softspace.bookstorepoc.base.ICustomNavigator
import com.softspace.bookstorepoc.base.NavigationIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class CustomNavigator @Inject constructor() : ICustomNavigator
{
    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    override fun navigateBack(route: String?, inclusive:Boolean) {
        navigationChannel.trySend(
            NavigationIntent.NavigateBack(route,inclusive)
        )
    }

    override fun navigate(route: String) {
        navigationChannel.trySend(
            NavigationIntent.Navigate(route)
        )
    }

}