package data

import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class Book(val title:String , val author: String, val note:String, val creationDate : LocalDateTime? = LocalDateTime.now(),
    val id : UUID = UUID.randomUUID()) {

}

val BookList_Mock = mutableStateListOf<Book>(
    Book("Java Cookbook 2013","John Doe","123456"),
    Book("Kotlin Master Handbook 3rd Edition","John Doe Jr Mark II","09876"),
    Book("C vs C++ vs C#","Doe John Sui","qqqq"),
    Book("The Brief History of Time","Stephen Hawking","")
)