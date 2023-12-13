package com.example.noteappproject.data.repository

import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository: NoteRepository {


    private val notes= mutableListOf<Note>()

    override fun getNotes(): Flow<List<Note>> {
        return flow {emit(notes)}
    }

    override suspend fun getNoteById(id: Int): Note? {
       return notes.find { it.id==id }
    }

    override fun getNotesByCategoryId(catId: Int): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    override suspend fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAudioRecord(audioRecord: AudioRecord) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAudioRecord(audioRecord: AudioRecord) {
        TODO("Not yet implemented")
    }

    override fun getAudioRecordsByNoteId(noteId: Int): Flow<List<AudioRecord>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAudioRecordsByNoteId(noteId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAudioRecords(): Flow<List<AudioRecord>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAudioRecordsNoteId(newNoteId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNullAudioRecords() {
        TODO("Not yet implemented")
    }

    override fun getNullAudioRecords(): Flow<List<AudioRecord>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteId(
        title: String,
        content: String,
        timeStamp: Long,
        color: Int,
        category: Int
    ): Int? {
        return notes.find {
        it.color==color && it.timeStamp==timeStamp && it.title== title
        }?.id ?:-1
    }

    override suspend fun deleteNoteWithAudioRecords(note: Note) {
        TODO("Not yet implemented")
    }
}