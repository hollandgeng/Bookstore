package data

import java.time.LocalDateTime
import java.util.Date

data class Book(val title:String , val author: String, val note:String) {
    val imagePath : String = ""
    val creationDate : LocalDateTime? = LocalDateTime.now()
}

val BookList_Mock = listOf<Book>(
    Book("ABC","123","123456"),
    Book("bvn","poiuyr","09876"),
    Book("dwe","eurogmvu","qqqq"),
    Book("por","1erere","")
)