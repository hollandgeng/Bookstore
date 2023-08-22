package com.softspace.bookstorepoc.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.softspace.bookstorepoc.viewmodels.BookInfoViewModel
import data.Book
import data.BookEditingState
import data.BookState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BookInfoScreen(
    viewModel: BookInfoViewModel = hiltViewModel()
) {
    val currentState = viewModel.uiState.collectAsState().value
    val error = viewModel.errorState.collectAsState().value
    val isTitleEmpty = viewModel.titleEmptyState.collectAsState().value
    val isAuthorEmpty = viewModel.authorEmptyState.collectAsState().value
    val context = LocalContext.current

    val bookData by remember{
        mutableStateOf(viewModel.bookDataState.value)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Book") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.NavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "NavigationBack"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    TextButton(
                        enabled = !isTitleEmpty && !isAuthorEmpty,
                        onClick = {
                        viewModel.TabBarActionDelegate(context, bookData)
                    }) {
                        val action = when (currentState.bookState) {
                            BookState.Create -> "Done"
                            BookState.View -> "Edit"
                            BookState.Edit -> "Done"
                        }
                        Text(action)
                    }
                })
        }) { paddingValues ->
        val editable : Boolean = when (currentState.bookState)
        {
            BookState.Edit -> true
            BookState.Create -> true
            else -> false
        }
        BookInfoView(modifier = Modifier.padding(paddingValues),bookData, editable,
            onBookInfoChanged = {text,state ->
            viewModel.UpdateBookInfo(text,state)
        })
    }

    if (!error.isNullOrBlank())
    {
        ErrorToast(context, error)
    }
}


@Composable
fun ErrorToast(context: Context,message:String)
{
    Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
}

@Composable
fun BookInfoView(modifier: Modifier = Modifier,
                 book: Book,
                 editable: Boolean = false,
                 onBookInfoChanged:(text:String, editingState : BookEditingState)->Unit) {

    var bookName by remember {
        mutableStateOf(book.title)
    }

    var author by remember {
        mutableStateOf(book.author)
    }

    var note by remember {
        mutableStateOf(book.note)
    }

    val scrollState = rememberScrollState()
    val isKeyboardVisible by keyboardVisible()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(0.dp, 0.dp, 0.dp, 16.dp)
            .systemBarsPadding()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LaunchedEffect(key1 = isKeyboardVisible)
        {
            if (isKeyboardVisible) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }

        Image(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp), imageVector = Icons.Filled.Image, contentDescription = ""
        )

        OutlinedTextField(
            value = bookName,
            onValueChange = {
                bookName = it
                onBookInfoChanged(it,BookEditingState.Title) },
            prefix = { Text("Name: ") },
            modifier = Modifier.fillMaxWidth(0.8F),
            enabled = editable
        )

        OutlinedTextField(
            value = author,
            onValueChange = {
                author = it
                onBookInfoChanged(it,BookEditingState.Author)
            },
            prefix = { Text("Author: ") },
            modifier = Modifier.fillMaxWidth(0.8F),
            enabled = editable
        )

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
                onBookInfoChanged(it,BookEditingState.Note)
            },
            placeholder = { Text("Add Note Here") },
            modifier = Modifier.fillMaxWidth(0.8F),
            enabled = editable
        )
    }
}

@Preview
@Composable
fun BookInfoViewPreview() {
    //BookInfoScreen({ },{ })
}

@Composable
fun ShowBook(name: String) {
    AlertDialog(onDismissRequest = { }, confirmButton = { /*TODO*/ },
        title = { Text("Book Name") },
        text = { Text(name) })
}

@Composable
fun keyboardVisible(): State<Boolean> {
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0;
    return rememberUpdatedState(isKeyboardVisible)
}

