package com.softspace.bookstorepoc.repository

import data.Book
import com.softspace.bookstorepoc.interfaces.IBookDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val dbContext : IBookDAO
) {

    fun GetBooks() : Flow<List<Book>>
    {
        return dbContext.getBooks()
    }

    fun GetBookById(bookId: UUID) : Book?
    {
        return dbContext.getBookById(bookId)
    }

    suspend fun AddBook(book: Book)
    {
        dbContext.upsertBook(book)
    }

    suspend fun RemoveBook(book: Book)
    {
        return dbContext.deleteBook(book)
    }

    /**
    * @return true if book was successfully updated;
     * false if book not found
    */
    suspend fun UpdateBook(book:Book)
    {
        dbContext.upsertBook(book)
    }

//    private fun IsBookExist(book: Book) : Boolean
//    {
//        return data.any{it.id == book.id}
//    }


}