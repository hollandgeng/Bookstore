package com.softspace.bookstorepoc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softspace.bookstorepoc.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Book
import helper.CustomNavigator
import helper.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BooklistViewModel @Inject constructor(
    private val navigator : CustomNavigator,
    private val bookRepository: BookRepository
) : ViewModel() {



    private val _bookState = bookRepository.GetBooks()
    val bookState = _bookState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList()
    )


    fun RemoveBook(book:Book) : Boolean
    {
        viewModelScope.launch {
            bookRepository.RemoveBook(book)
        }

        return  true
    }

    fun AddNewBookButton()
    {
        navigator.navigate(Screen.BookInfoScreen(UUID.randomUUID()))
    }

    fun ViewBook(bookId : UUID)
    {
        navigator.navigate(Screen.BookInfoScreen(bookId))
    }


    fun Back()
    {
        navigator.navigateBack()
    }
}

data class Booklist(val books: List<Book> = emptyList())
