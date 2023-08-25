package com.softspace.bookstorepoc.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dagger.Provides
import data.Book
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Singleton


@Dao
interface IBookDAO{
    @Upsert
    suspend fun upsertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM book ORDER BY creationDate DESC")
    fun getBooks() : Flow<List<Book>>

    @Query("SELECT * FROM book WHERE id = :bookId")
    fun getBookById(bookId: UUID) : Book?
}