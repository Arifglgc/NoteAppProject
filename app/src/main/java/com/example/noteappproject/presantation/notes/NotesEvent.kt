package com.example.noteappproject.presantation.notes

import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.util.NoteOrder

sealed class NotesEvent{
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note):NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
    data class FilterNoteByCategory(val categoryId: Int): NotesEvent()
    object DeleteNullAudio: NotesEvent()
    object DeleteAudios: NotesEvent()

}
