package com.softspace.bookstorepoc.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.ui.Alignment
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.DismissDirection
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.softspace.bookstorepoc.viewmodels.BooklistViewModel
import data.Book
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooklistScreen(viewModel: BooklistViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Booklist") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    TextButton(onClick = {
                        viewModel.Back()
                    }) {
                        Text(
                            "Logout",
                            color = Color.Red
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.AddNewBookButton()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add New Book"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Booklist(viewModel = viewModel, modifier = Modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun Booklist(viewModel: BooklistViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(viewModel.GetBooks(),
            key = { book -> book.id },
            itemContent = { book ->

                //val currentBook by rememberUpdatedState(book)
                val context = LocalContext.current
                val showDeleteAlert = remember { mutableStateOf(false) }
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            showDeleteAlert.value = true
                        }

                        true
                    }
                )

                if (showDeleteAlert.value) {
                    val coroutineScope = rememberCoroutineScope()

                    AlertDialog(
                        onDismissRequest = {
                            coroutineScope.launch {
                                dismissState.reset()
                                showDeleteAlert.value = false
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDeleteAlert.value = false
                                    val removed = viewModel.RemoveBook(book)
                                    if (removed) {
                                        ShowDeleteToast(context, bookName = book.title)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        dismissState.reset()
                                        showDeleteAlert.value = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                            ) {
                                Text("Cancel", color = Color.Black)
                            }
                        },
                        title = { Text("Are you sure you want to delete this book?") },
                        text = { Text("This will delete this book permanently. You cannot undo this action.") }
                    )
                }

                SwipeToDismiss(state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.4F) },
                    modifier = Modifier.animateItemPlacement(),
                    background = {
                        val bgcolor = when (dismissState.targetValue) {
                            DismissValue.DismissedToEnd -> Color.Transparent
                            DismissValue.DismissedToStart -> Color.Red
                            DismissValue.Default -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Item"
                            )
                        }
                    },
                    dismissContent = {
                        bookRow(context, book) {
                            viewModel.ViewBook(book.id)
                        }
                    })

            })
    }
}

@Composable
fun bookRow(context: Context, book: Book, onSelect: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(5.dp))
            .background(Color.White, shape = RoundedCornerShape(5.dp))
            .padding(10.dp)
            .clickable(onClick = {
                onSelect()
            })
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(book.image).crossfade(true).build(),
            error = rememberVectorPainter(image = Icons.Filled.Book),
            contentDescription = "Book Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .border(0.5.dp, Color.Black, shape = CircleShape)
                .clip(shape = CircleShape)
        )
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(0.7F)) {
            Text(book.title, overflow = TextOverflow.Ellipsis, maxLines = 2)
            Text(
                text = book.author,
                color = Color.LightGray,
                fontSize = TextUnit(10F, TextUnitType.Sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        book.creationDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            ?.let {
                Text(
                    text = it,
                    color = Color.LightGray,
                    fontSize = TextUnit(10F, TextUnitType.Sp)
                )
            }

        Image(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = "",
            modifier = Modifier.height(10.dp)
        )
    }

    Divider()
}

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BooklistPreview() {
    //BooklistScreen()
}


fun ShowDeleteToast(context: Context, bookName: String) {
    Toast.makeText(
        context,
        "$bookName has been deleted",
        Toast.LENGTH_SHORT
    ).show()
}