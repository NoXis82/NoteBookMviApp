package com.noxis.notes.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noxis.database.editor.FormattingAction
import com.noxis.notes.R
import com.noxis.notes.domain.model.NoteUi
import com.noxis.notes.presentation.components.ProvideAppBarAction
import com.noxis.notes.presentation.components.ProvideAppBarTitle
import com.noxis.notes.presentation.intent.NoteIntent
import com.noxis.notes.presentation.state.NoteDetailUiState
import com.noxis.notes.presentation.viewmodel.NoteDetailViewModel
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen() {
    val viewmodel: NoteDetailViewModel = hiltViewModel()
    val stateUi by viewmodel.noteDetailViewState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    NoteDetailScreen(stateUi = stateUi) { intent ->
        viewmodel.handleIntent(intent)
    }
}

@Composable
private fun NoteDetailScreen(
    stateUi: NoteDetailUiState,
    handleIntent: (NoteIntent) -> Unit
) {
// Toolbar title
    ProvideAppBarTitle {
        // Note title
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            value = stateUi.note.title,
            onValueChange = { handleIntent(NoteIntent.UpdateNoteTitle(it)) },
            placeholder = { Text(stringResource(id = R.string.add_note_title)) },
            textStyle = MaterialTheme.typography.displaySmall,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    var activeFormats by rememberSaveable { mutableStateOf(setOf<FormattingAction>()) }
    var formattingSpans by remember { mutableStateOf(listOf<FormattingSpan>()) }

    Column {
        EditorToolbar(
            activeFormats = activeFormats,
            onFormatToggle = { format ->
                activeFormats = if (activeFormats.contains(format)) {
                    activeFormats - format
                } else {
                    if (format == FormattingAction.Heading || format == FormattingAction.SubHeading) {
                        activeFormats - FormattingAction.Heading - FormattingAction.SubHeading + format
                    } else {
                        activeFormats + format
                    }
                }
            }
        )

        ComposeTextEditor(
            annotatedString = stateUi.note.description,
            activeFormats = activeFormats,
            onAnnotatedStringChange = { updatedAnnotatedString ->
                handleIntent(NoteIntent.UpdateNoteDescription(updatedAnnotatedString))
            },
            onFormattingSpansChange = { updatedSpans ->
                formattingSpans = updatedSpans
            },
            modifier = Modifier.padding(10.dp)
        )
    }


    // Toolbar action buttons
    ProvideAppBarAction {
        // Update/add button
        if (stateUi.showSaveIcon) {
            IconButton(onClick = { handleIntent(NoteIntent.AddOrSaveNote) }) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}

@Composable
@Preview
private fun NoteDetailScreenPreview() {
    NoteDetailScreen(
        stateUi = NoteDetailUiState(
            note = NoteUi(
                title = "Test1",
                description = AnnotatedString("Description test1"),
                modifiedAt = OffsetDateTime.now()
            ),
            showDeleteIcon = true
        )
    ) {

    }
}
