package com.noxis.notes.presentation.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noxis.notes.presentation.state.NoteDetailUiState
import com.noxis.notes.presentation.viewmodel.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen() {
    val viewmodel: NoteDetailViewModel = hiltViewModel()
    val stateUi by viewmodel.noteDetailViewState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    NoteDetailScreen(stateUi = stateUi)
}

@Composable
private fun NoteDetailScreen(
    stateUi: NoteDetailUiState
) {


}

@Composable
@Preview
private fun NoteDetailScreenPreview() {
//    NoteDetailScreen(
//        stateUi = NoteDetailUiState()
//    )
}
