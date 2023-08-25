package data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.mutableStateListOf
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Book(
    val title:String,
    val author: String,
    val note:String,
    val image:String = "",
    val creationDate : LocalDateTime = LocalDateTime.now(),
    @PrimaryKey
    val id : UUID = UUID.randomUUID(),
    ) {

}

data class BookInfoState(
    val book : Book
)