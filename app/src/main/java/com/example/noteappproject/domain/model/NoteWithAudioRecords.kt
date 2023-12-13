package com.example.noteappproject.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithAudioRecords(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val audioRecords: List<AudioRecord>
)