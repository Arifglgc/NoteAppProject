package com.example.noteappproject.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("noteId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AudioRecord(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val noteId: Int?= null, // Bu alan, Note ile ilişkilendirmek için kullanılacak
    val audioFilePath: String,
    val duration: Long
)

