package data

enum class BookState {
    Create,
    View,
    Edit
}

enum class BookEditingState
{
    Title,
    Author,
    Note
}

data class BookUiState(val bookState: BookState)