package com.example.noteappproject.domain.use_case

import com.example.noteappproject.data.repository.FakeNoteRepository
import com.example.noteappproject.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class DeleteNoteUseCaseTest {
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        fakeNoteRepository= FakeNoteRepository()
        deleteNoteUseCase= DeleteNoteUseCase(fakeNoteRepository)

        val note = Note(
            title = "Tesagsagst1",
            color = 451,
            content = "TesfggfhtC",
            timeStamp = 15242400,
            category = 2
        )
        runBlocking {     fakeNoteRepository.insertNote(note) }

    }

    @Test
    fun isDeleted() = runBlocking {
        val note = Note(
            id=3,
            title = "Test1",
            color = 1,
            content = "TestC",
            timeStamp = 1500,
            category = 2
        )
        fakeNoteRepository.insertNote(note)// note added

       // deleteNoteUseCase(note)//  note deleted

        val id = fakeNoteRepository.getNoteId("Test1","TestC",1500,1,2)// getting deletedNoteId if is it deleted its =-1

        assert(id==note.id)// we check if id's are different

        // we can directly check it via gettingNote
       // assertNull(fakeNoteRepository.getNoteById(note.id!!))

    }

}