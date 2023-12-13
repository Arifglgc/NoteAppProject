package com.example.noteappproject.presantation.notes

import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.util.NoteOrder
import com.example.noteappproject.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible:Boolean=false,
    val noteCategory: Int=0
)
