package data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.mutableStateListOf
import java.time.LocalDateTime
import java.util.UUID

data class Book(val title:String, val author: String, val note:String, val image:String = "", val creationDate : LocalDateTime = LocalDateTime.now(),
                val id : UUID = UUID.randomUUID()) {

}

val BookList_Mock = mutableStateListOf<Book>(
    Book("Java Cookbook 2013","John Doe","123456", creationDate = LocalDateTime.of(2023,8,23,12,0,0)),
    Book("Kotlin Master Handbook 3rd Edition","John Doe Jr Mark II","09876",creationDate = LocalDateTime.of(2023,7,23,12,0,0)),
    Book("C vs C++ vs C#","Doe John Sui","qqqq",creationDate = LocalDateTime.of(2023,6,23,12,0,0)),
    Book("The Brief History of Time","Stephen Hawking","",creationDate = LocalDateTime.of(2023,5,23,12,0,0))
)