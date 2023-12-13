package com.example.noteappproject.domain.use_case

import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.repository.NoteRepository

class GetNoteUseCase(
   private val repository: NoteRepository
) {

    suspend operator fun  invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }

}