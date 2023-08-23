package com.softspace.bookstorepoc.viewmodels

import androidx.lifecycle.ViewModel
import com.softspace.bookstorepoc.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Book
import helper.CustomNavigator
import helper.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BooklistViewModel @Inject constructor(
    private val navigator : CustomNavigator,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(bookRepository.GetBooks())
    val uiState : StateFlow<List<Book>> = _uiState

    fun GetBooks(): List<Book> {
        return uiState.value
    }

    fun RemoveBook(book:Book) : Boolean
    {
        return bookRepository.RemoveBook(book)
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

data class Booklist(val books: MutableList<Book>)
