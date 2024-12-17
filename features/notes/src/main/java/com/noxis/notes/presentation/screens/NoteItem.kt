package com.noxis.notes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noxis.notes.R
import com.noxis.notes.domain.model.NoteUi
import com.noxis.notes.util.getDate
import com.noxis.notes.util.getTime
import java.time.OffsetDateTime

@Composable
internal fun NoteItem(
    note: NoteUi,
    onNoteItemClicked: (note: NoteUi) -> Unit,
    onNoteItemDeleted: (note: NoteUi) -> Unit
) {
    val alpha = if (note.encrypt) 1f else 0f
    val blur = if (note.encrypt) 10.dp else 0.dp
    Box(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .clickable { onNoteItemClicked(note) },
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = note.title,
                        style = TextStyle(
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    IconButton(
                        modifier = Modifier.weight(0.25f),
                        onClick = { onNoteItemDeleted(note) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    style = TextStyle(
                        lineHeight = 20.sp,
                        letterSpacing = 0.sp,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .blur(blur),
                    text = note.description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format(
                            stringResource(id = R.string.note_list_date),
                            note.createdAt.getDate(), note.createdAt.getTime()
                        ),
                        modifier = Modifier.weight(1f),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(12.dp)
                            .weight(0.25f)
                            .alpha(alpha)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun NoteItemPreview() {
    NoteItem(
        note = NoteUi(
            title = "Test",
            description = AnnotatedString("Description test"),
            modifiedAt = OffsetDateTime.now()
        ),
        onNoteItemClicked = {},
        onNoteItemDeleted = {}
    )
}