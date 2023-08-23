package helper

import data.BookState
import java.util.UUID
import kotlin.text.StringBuilder

sealed class Screen(protected val route : String, vararg params:String)
{
    val queries : String = if (params.isEmpty()) "" else
    {
        val builder = StringBuilder()
        params.forEach { builder.append("/{${it}}")}
        builder.toString()
    }

    val fullRoute : String = if (params.isEmpty()) route else
    {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}")}
        builder.toString()
    }

    val baseRoute : String = route

    object LoginScreen : Screen("Login")

    object BooklistScreen : Screen("Booklist")

    object BookInfoScreen : Screen("BookInfo", "bookId")
    {
        const val BOOK_ID_KEY = "bookId"

        operator fun invoke(bookId:UUID) : String = route.pairParam(
            BOOK_ID_KEY to bookId
        )
    }

}

internal fun String.pairParam(vararg params:Pair<String,Any?>) : String
{
    val builder = StringBuilder(this)

    params.forEach {
        val para = it.second?.toString()

        para?.let {arg->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}


