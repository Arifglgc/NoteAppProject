package com.example.noteappproject.presantation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.noteappproject.domain.model.Note
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawRoundRect(
                color = Color(note.color),
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )

            val holeRadius = 12f
            val holeCenter = Offset(canvasWidth - holeRadius - 10f, holeRadius + 10f)

            drawCircle(
                color = Color.Black,
                center = holeCenter,
                radius = holeRadius
            )

            val ropeLength = 60f
            val ropeStart = Offset(canvasWidth - 20f, 30f)
            val ropeEnd = Offset(canvasWidth - 20f, 30f + ropeLength)

            val ropeControlPoint1 = Offset(
                canvasWidth - 20f + 15f,
                30f + ropeLength / 2
            )
            val ropeControlPoint2 = Offset(
                canvasWidth - 20f - 15f,
                30f + ropeLength / 2
            )

            drawLine(
                color = Color(0xFFC0C0C0),
                start = ropeStart,
                end = ropeEnd,
                strokeWidth = 4f
            )

            // Kıvrılmış gri ipi çizin
            drawPath(
                path = Path().apply {
                    moveTo(ropeStart.x, ropeStart.y)
                    cubicTo(
                        ropeControlPoint1.x, ropeControlPoint1.y,
                        ropeControlPoint2.x, ropeControlPoint2.y,
                        ropeEnd.x, ropeEnd.y
                    )
                },
                color = Color(0xFFC0C0C0),
                style = Stroke(4f)
            )
        }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .padding(end = 25.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                modifier= Modifier.size(16.dp),
                imageVector = Icons.Default.Delete,

                contentDescription = "Delete note",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}