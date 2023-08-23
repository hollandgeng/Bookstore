package com.softspace.bookstorepoc.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PackageManagerCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.softspace.bookstorepoc.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Book
import data.BookEditingState
import data.BookState
import data.BookUiState
import helper.CustomNavigator
import helper.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookInfoViewModel @Inject constructor(
    private val navigator: CustomNavigator,
    private val bookRepo : BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState(BookState.View))
    val uiState: StateFlow<BookUiState> = _uiState

    private val _bookDataState = MutableStateFlow(Book("","",""))
    val bookDataState : StateFlow<Book> = _bookDataState

    private val _tempbookDataState = MutableStateFlow(Book("","",""))

    private val _errorState = MutableStateFlow("")
    val errorState : StateFlow<String> = _errorState

    private val _titleEmptyState = MutableStateFlow(true)
    val titleEmptyState:StateFlow<Boolean> = _titleEmptyState

    private val _authorEmptyState = MutableStateFlow(true)
    val authorEmptyState:StateFlow<Boolean> = _authorEmptyState

    init {
        val id = savedStateHandle.get<String>(Screen.BookInfoScreen.BOOK_ID_KEY)

        val bookId = UUID.fromString(id)

        val book = bookRepo.GetBookById(bookId)

        if(book == null)
        {
            UpdateState(BookState.Create)
            val newbook = Book("","","",id = bookId)

            SetBook(newbook)
            PopulateBookInfo(newbook)
        }
        else
        {
            SetBook(book)
            PopulateBookInfo(book)
        }
    }

    fun TabBarActionDelegate(context: Context, book: Book) {
        when (_uiState.value.bookState) {
            BookState.View -> {
                EnableEdit()
            }

            BookState.Create -> {
                SyncBook()
                CreateBook(context)
            }

            BookState.Edit -> {
                SyncBook()
                SaveBook()
            }
        }
    }

    private fun UpdateState(bookState: BookState)
    {
        _uiState.update { state ->
            state.copy(bookState)
        }
    }

    private fun EnableEdit() {
        UpdateState(BookState.Edit)
    }

    private fun CreateBook(context: Context) {
        val bookTitle : String = _bookDataState.value.title

        bookRepo.AddBook(_bookDataState.value)

        UpdateState(BookState.View)

        Toast.makeText(context, "$bookTitle Added", Toast.LENGTH_SHORT).show()
    }

    private fun SetBook(book: Book) {
        _bookDataState.value = book
        _tempbookDataState.value = book

        CheckCanSave()
    }

    fun UpdateBookInfo(content:String, editingState: BookEditingState)
    {
        when (editingState)
        {
            BookEditingState.Title -> {
                _tempbookDataState.update { book ->
                    book.copy(
                        title = content
                    )
                }
            }

            BookEditingState.Author ->
                _tempbookDataState.update { book ->
                    book.copy(
                        author = content
                    )
                }

            BookEditingState.Note -> {
                _tempbookDataState.update { book ->
                    book.copy(
                        note = content
                    )
                }
            }
        }

        CheckCanSave()
    }

    private fun CheckCanSave()
    {
        _titleEmptyState.value = _tempbookDataState.value.title.isEmpty()
        _authorEmptyState.value = _tempbookDataState.value.author.isEmpty()
    }

    private fun SyncBook()
    {
        _bookDataState.update { book ->
            book.copy(
                title = _tempbookDataState.value.title,
                author = _tempbookDataState.value.author,
                note = _tempbookDataState.value.note,
                image = _tempbookDataState.value.image
            )
        }
    }

    private fun SaveBook()
    {
        val result :Boolean = bookRepo.UpdateBook(_bookDataState.value)

        //Then update the current uiState back to View Mode
        UpdateState(BookState.View)
    }

    fun NavigateBack()
    {
        navigator.navigateBack()
    }

    private fun PopulateBookInfo(book : Book?)
    {
        if (book == null)
        {
            SetError("Book Not Found")
            NavigateBack()
        }
        else
        {
            _bookDataState.value = book
        }
    }

    private fun SetError(errorMessage : String)
    {
       _errorState.value = errorMessage
    }


    fun Photo(uri:Uri)
    {
        _tempbookDataState.update { book->
            book.copy(
                image = uri.toString()
            )
        }
    }
}