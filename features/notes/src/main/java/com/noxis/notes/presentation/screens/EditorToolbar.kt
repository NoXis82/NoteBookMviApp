package com.noxis.notes.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.StrikethroughS
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noxis.database.editor.FormattingAction

@Composable
internal fun EditorToolbar(
    activeFormats: Set<FormattingAction>,
    onFormatToggle: (FormattingAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FormattingAction.entries.forEach { format ->
            IconButton(
                onClick = { onFormatToggle(format) }
            ) {
                Icon(
                    imageVector = getIconForFormat(format),
                    contentDescription = format.name,
                    tint = if (activeFormats.contains(format)) {
                        MaterialTheme.colorScheme.primary
                    } else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun getIconForFormat(format: FormattingAction): ImageVector {
    return when (format) {
        FormattingAction.Heading -> Icons.Default.Title
        FormattingAction.SubHeading -> Icons.Default.Subtitles
        FormattingAction.Bold -> Icons.Default.FormatBold
        FormattingAction.Italics -> Icons.Default.FormatItalic
        FormattingAction.Underline -> Icons.Default.FormatUnderlined
        FormattingAction.Strikethrough -> Icons.Default.StrikethroughS
        FormattingAction.Highlight -> Icons.Default.Highlight
    }
}

@Composable
@Preview
private fun EditorToolbarPreview(){
    EditorToolbar(activeFormats = setOf<FormattingAction>()) {
        
    }
}