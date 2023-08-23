package com.softspace.bookstorepoc.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.softspace.bookstorepoc.viewmodels.BookInfoViewModel
import data.Book
import data.BookEditingState
import data.BookState
import kotlinx.coroutines.launch
import layouts.BottomSheetLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

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


    val bookData by remember {
        mutableStateOf(viewModel.bookDataState.value)
    }

    val focusManager = LocalFocusManager.current

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
                            focusManager.clearFocus()
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
        val editable: Boolean = when (currentState.bookState) {
            BookState.Edit -> true
            BookState.Create -> true
            else -> false
        }
        BookInfoView(modifier = Modifier
            .padding(paddingValues)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }, bookData, editable,
            onBookInfoChanged = { text, state ->
                viewModel.UpdateBookInfo(text, state)
            },
            onPhotoTaken = { uri ->
                viewModel.Photo(uri)
            })
    }

    if (!error.isNullOrBlank()) {
        ErrorToast(context, error)
    }
}


@Composable
fun ErrorToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookInfoView(
    modifier: Modifier = Modifier,
    book: Book,
    editable: Boolean = false,
    onBookInfoChanged: (text: String, editingState: BookEditingState) -> Unit,
    onImageClick: () -> Unit = {},
    onPhotoTaken: (uri: Uri) -> Unit
) {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture(),
            onResult = { isSuccess ->
                if (isSuccess) {
                    capturedImageUri = uri
                    onPhotoTaken(uri)
                } else {
                    file.delete()
                }
            })

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    capturedImageUri = uri
                    onPhotoTaken(uri)
                }
            })


    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { wasGranted ->
            if (wasGranted) {
                // TODO do work (ie forward to viewmodel)
                Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }

    var isPermissionGranted by remember {
        mutableStateOf(IsPermissionGranted(context))
    }

    var bookName by remember {
        mutableStateOf(book.title)
    }

    var author by remember {
        mutableStateOf(book.author)
    }

    var note by remember {
        mutableStateOf(book.note)
    }

    var image by remember {
        mutableStateOf(book.image)
    }

    val couroutineScope = rememberCoroutineScope()

    val bottomModalState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
            skipHalfExpanded = true
        )

    val scrollState = rememberScrollState()

    BackHandler {
        if (bottomModalState.isVisible)
            couroutineScope.launch {
                bottomModalState.hide()
            }
    }

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

        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(5.dp))
                .clickable(
                    enabled = editable,
                    onClick = {
                        couroutineScope.launch {
                            if (!bottomModalState.isVisible)
                                bottomModalState.show()
                        }
                    }),
            model = ImageRequest.Builder(context).data(image).crossfade(true).build(),
            error = rememberVectorPainter(image = Icons.Filled.Book),
            contentDescription = "",
            contentScale = ContentScale.Fit
        )

        OutlinedTextField(
            value = bookName,
            onValueChange = {
                bookName = it
                onBookInfoChanged(it, BookEditingState.Title)
            },
            prefix = { Text("Name: ") },
            modifier = Modifier.fillMaxWidth(0.8F),
            enabled = editable
        )

        OutlinedTextField(
            value = author,
            onValueChange = {
                author = it
                onBookInfoChanged(it, BookEditingState.Author)
            },
            prefix = { Text("Author: ") },
            modifier = Modifier.fillMaxWidth(0.8F),
            enabled = editable
        )

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
                onBookInfoChanged(it, BookEditingState.Note)
            },
            placeholder = { Text("Add Note Here") },
            modifier = Modifier.fillMaxWidth(0.8F),
            enabled = editable
        )

        if (capturedImageUri.path?.isNotEmpty() == true) {
            image = capturedImageUri.toString()
            Log.i("IMAGE_CAPTURED", image)
        }
    }

    LaunchedEffect(scrollState.maxValue)
    {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    BottomSheetLayout(
        btmSheetState = bottomModalState,
        onCamera = {
            if (isPermissionGranted) {
                cameraLauncher.launch(uri)
            } else {
                if (activity != null) {
                    if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                        Log.i("DEMO", "no")
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }
        },
        onGallery = {
            val mediaRequest =
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            galleryLauncher.launch(mediaRequest)
        })
}

@Preview
@Composable
fun BookInfoViewPreview() {
    BookInfoScreen()
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

private fun IsPermissionGranted(context: Context): Boolean {
    val permissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    return permissionResult == PackageManager.PERMISSION_GRANTED
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "booksotre_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

