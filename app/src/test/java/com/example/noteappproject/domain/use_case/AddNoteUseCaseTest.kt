package com.example.noteappproject.domain.use_case

import com.example.noteappproject.data.repository.FakeNoteRepository
import com.example.noteappproject.domain.model.InvalidNoteException
import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.util.NoteOrder
import com.example.noteappproject.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class AddNoteUseCaseTest(){
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var getNotes: GetNotesUseCase
    @Before
    fun setUp(){
        fakeNoteRepository= FakeNoteRepository()
        addNoteUseCase= AddNoteUseCase(fakeNoteRepository)
        getNotes= GetNotesUseCase(fakeNoteRepository)

        val notesToInsert= mutableListOf<Note>()
        ('a'..'z').forEachIndexed{index,c->
            notesToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    color = index,
                    category=1,
                    timeStamp = index.toLong()
                )
            )
        }

        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach{fakeNoteRepository.insertNote(it)}
        }

    }

    @Test
    fun  Ordernotesbytitleascendingcorrectorder() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()

        for (i in 0..notes.size-2){
            val isLess= (notes[i].title) < (notes[i+1].title)
            assert(isLess)
        }
    }

    @Test
    fun invokeWithValidNoteShouldInsertNote() = runBlocking {
        // Arrange

        val note = Note(
            id = 1,
            title = "",
            content = "Valid Content",
            timeStamp = System.currentTimeMillis(),
            color = 0,
            category = 0
        )
        // Act
        addNoteUseCase(note)

        // Assert
        val insertedNote = fakeNoteRepository.getNoteById(1)
        assertEquals(note, insertedNote)
    }

    @Test
    fun invokeWithBlankContentShouldThrowInvalidNoteException() = runBlocking {

        val note = Note(
            id = 1,
            title = "Valid Title",
            content = "",
            timeStamp = System.currentTimeMillis(),
            color = 0,
            category = 0
        )

        // Act and Assert
        val exception = assertThrows<InvalidNoteException> {
            addNoteUseCase(note)
        }

        assertEquals("The content of the note can't be empty", exception.message)
    }
}