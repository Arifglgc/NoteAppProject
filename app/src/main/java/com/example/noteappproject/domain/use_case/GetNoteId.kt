package com.example.noteappproject.domain.use_case

import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.repository.NoteRepository

class GetNoteId(
    private val repository: NoteRepository
) {

    suspend operator fun  invoke(note:Note): Int? {
        return repository.getNoteId(note.title,note.content,note.timeStamp,note.color,note.category)
    }
}

