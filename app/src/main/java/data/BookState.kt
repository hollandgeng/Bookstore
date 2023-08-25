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
    Note,
    Image
}

data class BookUiState(val bookState: BookState)