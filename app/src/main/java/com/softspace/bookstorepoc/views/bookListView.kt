package com.softspace.bookstorepoc.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import data.BookList_Mock
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@Composable
fun Booklist()
{
    LazyColumn{
        items (BookList_Mock) { book ->
            val dismissState = rememberDismissState()

            Row(horizontalArrangement = Arrangement.spacedBy(10. dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)){
                Image(imageVector = Icons.Filled.ShoppingBag, contentDescription = "")
                Column (horizontalAlignment = Alignment.Start, modifier = Modifier.weight(0.7F)) {
                    Text(book.title)
                    Text(book.author)
                }
                book.creationDate?.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"))
                    ?.let { Text(text = it) }

                Image(imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "",
                    modifier = Modifier.height(10. dp))
            }

            Divider()
        }

    }
}

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BooklistPreview()
{
    Booklist()
}