package com.noxis.notes.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noxis.database.util.AnnotatedStringConverter
import com.noxis.notes.R
import com.noxis.notes.domain.model.NoteUi
import com.noxis.notes.presentation.components.EmptyScreen
import com.noxis.notes.presentation.components.LoadingItem
import com.noxis.notes.presentation.components.ProvideAppBarTitle
import com.noxis.notes.presentation.intent.NoteIntent
import com.noxis.notes.presentation.state.NoteUiViewState
import com.noxis.notes.presentation.viewmodel.NoteViewModel
import java.time.OffsetDateTime

@Composable
fun NotesScreen() {
    val viewmodel: NoteViewModel = hiltViewModel()
    val stateUi by viewmodel.notesViewState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    NotesScreen(stateUi = stateUi) { intent ->
        viewmodel.handleIntent(intent)
    }
}

@Composable
private fun NotesScreen(
    stateUi: NoteUiViewState,
    handleIntent: (NoteIntent) -> Unit
) {
    // Toolbar title
    ProvideAppBarTitle {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
    when {
        stateUi.isLoading -> LoadingItem()
        stateUi.notes.isEmpty() -> EmptyScreen()
        else -> {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 12.dp,
                    end = 12.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                columns = StaggeredGridCells.Adaptive(minSize = 140.dp),
            ) {
                val notes = stateUi.notes
                items(notes.size) {
                    NoteItem(
                        note = notes[it],
                        onNoteItemClicked = { note ->
                            handleIntent(NoteIntent.OpenNoteClicked(note))
                        },
                        onNoteItemDeleted = { note ->
                            handleIntent(NoteIntent.DeleteNote(note))
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun NotesScreenPreview() {
    NotesScreen(
        stateUi = NoteUiViewState(
            notes = listOf(
                NoteUi(
                    title = "Test1",
                    description = AnnotatedString("Description test1"),
                    modifiedAt = OffsetDateTime.now()
                ),
                NoteUi(
                    title = "Test2",
                    description = AnnotatedString("Description test2"),
                    modifiedAt = OffsetDateTime.now()
                ),
                NoteUi(
                    title = "Test3",
                    description = AnnotatedString("Description test3"),
                    modifiedAt = OffsetDateTime.now()
                )
            )
        )
    ) {

    }
}


