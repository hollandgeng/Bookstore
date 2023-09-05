package com.softspace.bookstorepoc.viewmodels

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softspace.bookstorepoc.interfaces.ICustomNavigator
import com.softspace.bookstorepoc.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Book
import data.BookEditingState
import data.BookState
import data.BookUiState
import helper.CustomNavigator
import helper.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val navigator: ICustomNavigator,
    private val bookRepo: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var book by mutableStateOf(Book("","",""))
        private set

    fun GetBookData()
    {
        val id = savedStateHandle.get<String>(Screen.BookInfoScreen.BOOK_ID_KEY)
        val bookId = UUID.fromString(id)
        viewModelScope.launch(Dispatchers.IO){
            val result = bookRepo.GetBookById(bookId)
            if (result == null)
            {
                UpdateState(BookState.Create)
                book = Book("","","")
            }
            else
            {
                book = result
            }
        }
    }

    private val _uiState = MutableStateFlow(BookUiState(BookState.View))
    val uiState: StateFlow<BookUiState> = _uiState

    private val _bookDataState = MutableStateFlow(Book("", "", ""))
    val bookDataState: StateFlow<Book> = _bookDataState

    private val _tempbookDataState = MutableStateFlow(Book("", "", ""))

    private val _errorState = MutableStateFlow("")
    val errorState: StateFlow<String> = _errorState

    private val _titleEmptyState = MutableStateFlow(true)
    val titleEmptyState: StateFlow<Boolean> = _titleEmptyState

    private val _authorEmptyState = MutableStateFlow(true)
    val authorEmptyState: StateFlow<Boolean> = _authorEmptyState

    private val _canSaveState = MutableStateFlow(false)
    val canSaveState : StateFlow<Boolean> = _canSaveState


    fun TabBarActionDelegate(context: Context, book: Book) {
        when (_uiState.value.bookState) {
            BookState.View -> {
                EnableEdit()
            }

            BookState.Create -> {
                CreateBook(context)
            }

            BookState.Edit -> {
                SaveBook()
            }
        }
    }

    private fun UpdateState(bookState: BookState) {
        _uiState.update { state ->
            state.copy(bookState)
        }

        CheckCanSave()
    }

    private fun EnableEdit() {
        UpdateState(BookState.Edit)
    }

    private fun CreateBook(context: Context) {
        val bookTitle: String = book.title

        viewModelScope.launch {
            bookRepo.AddBook(book)
        }

        UpdateState(BookState.View)

        Toast.makeText(context, "$bookTitle Added", Toast.LENGTH_SHORT).show()
    }


    fun UpdateBookInfo(content: String, editingState: BookEditingState) {
        when (editingState) {
            BookEditingState.Title -> {
                book = book.copy(
                    title = content
                )
            }

            BookEditingState.Author -> {
                book = book.copy(
                    author = content
                )
            }

            BookEditingState.Note -> {
                book = book.copy(
                    note = content
                )
            }

            BookEditingState.Image -> {
                book = book.copy(
                    image = content
                )
            }
        }

        CheckCanSave()
    }

    private fun CheckCanSave() {
        _canSaveState.value = book.title.isNotBlank() && book.author.isNotBlank()
    }

    private fun SaveBook() {
        viewModelScope.launch {
            bookRepo.UpdateBook(book)
        }

        //Then update the current uiState back to View Mode
        UpdateState(BookState.View)
    }

    fun NavigateBack() {
        navigator.navigateBack()
    }



    private fun SetError(errorMessage: String) {
        _errorState.value = errorMessage
    }


    fun Photo(uri: Uri) {
        _tempbookDataState.update { book ->
            book.copy(
                image = uri.toString()
            )
        }
    }
}