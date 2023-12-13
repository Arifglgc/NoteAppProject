import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.noteappproject.presantation.add_edit_note.BottomMenuItem

@Composable
fun BottomMenuBar(
    modifier: Modifier = Modifier,
    items: List<BottomMenuItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.DarkGray)
            .border(1.dp, color = Color.Black)
    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                text = item.text,
                icon = item.icon, // Yeni eklenen özellik
                isSelected = index == selectedIndex,
                onClick = { onItemClick(index) }
            )
        }
    }
}


@Composable
fun BottomMenuItem(
    text: String,
    icon: Int, // Yeni eklenen özellik
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier= Modifier.padding(10.dp)
                    .padding(start = 8.dp),
                painter=painterResource(id = icon ) ,
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    Color.White
                }
            )

        }
    }
}