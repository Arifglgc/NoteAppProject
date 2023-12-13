package com.example.noteappproject.domain.use_case


import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
    repository.deleteNote(note)
    }

}