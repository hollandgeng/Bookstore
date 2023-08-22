package com.softspace.bookstorepoc.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.softspace.bookstorepoc.viewmodels.BooklistViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.internal.InjectedFieldSignature
import data.Book
import data.BookList_Mock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

class BookRepository @Inject constructor() {
    val data = BookList_Mock

    fun GetBooks() : List<Book>
    {
        return data
    }

    fun AddBook(book: Book) : Boolean
    {
        if (IsBookExist(book))
        {
            return false;
        }
        else
        {
            data.add(book)
            return true
        }
    }

    fun RemoveBook(book: Book) : Boolean
    {
        return data.remove(book)
    }

    /**
    * @return true if book was successfully updated;
     * false if book not found
    */
    fun UpdateBook(book:Book) : Boolean
    {
        if(!IsBookExist(book)) return false

        val index = BookList_Mock.indexOfFirst { it.id == book.id }
        BookList_Mock[index] = book

        return true
    }

    private fun IsBookExist(book: Book) : Boolean
    {
        return data.any{it.id == book.id}
    }

    fun GetBookById(bookId:UUID) : Book?
    {
        val book : Book?

        try {
            book = data.find { it.id == bookId }
        }
        catch (exception : Exception )
        {
            return null
        }

        return book
    }
}