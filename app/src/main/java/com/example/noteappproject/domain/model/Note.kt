package com.example.noteappproject.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteappproject.ui.theme.Fuchsia
import com.example.noteappproject.ui.theme.IndianRed
import com.example.noteappproject.ui.theme.LightBlue
import com.example.noteappproject.ui.theme.LightGreen
import com.example.noteappproject.ui.theme.Violet


@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int?=null,
    val title: String,
    val content: String,
    val color: Int,
    val category: Int,
    val timeStamp: Long,
)
{
    companion object{
        val noteColors = listOf(LightBlue, Fuchsia, IndianRed, Violet,
          Color.Magenta,Color(0xFFB0C4DE),
            Color(0xFFFFA07A ),Color(0xFFDDA0DD ),
            Color(0xFF008000 ),Color(0xFF808080 ),Color(0xFF800080 ),
             Color(0xFFD2B48C ), Color(0xFF40E0D0 ),  Color(0xFF6495ED ),
             Color(0xFFFF0000 ),
            Color(0xFF254B25), Color(0xFF4A4E88),Color(0xFF297E66),Color(0xFF2D992D),Color(0xFF228B22 ),


        )

        val noteCategories= listOf("Daily","Education","Hobby","Shopping","Work","Todo","Other")
    }
}
    class InvalidNoteException(message: String): Exception(message)





