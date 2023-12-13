package com.example.noteappproject.domain.use_case

import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.repository.NoteRepository
import com.example.noteappproject.domain.util.NoteOrder
import com.example.noteappproject.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesByCategoryUseCase(
    private val repository: NoteRepository

) {
    operator  fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        catId: Int
    ): Flow<List<Note>> {

        return repository.getNotesByCategoryId(catId).map { notes->
            when(noteOrder.orderType){
                is OrderType.Ascending -> {
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                        is NoteOrder.Color-> notes.sortedBy { it.color }

                    }
                }
                is OrderType.Descending -> {
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date ->notes.sortedByDescending { it.timeStamp }
                        is NoteOrder.Color-> notes.sortedByDescending { it.color }
                    }
                }

            }
        }
    }

}