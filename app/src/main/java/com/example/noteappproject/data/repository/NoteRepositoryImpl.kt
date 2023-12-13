package com.example.noteappproject.data.repository

import com.example.noteappproject.data.data_source.NoteDao
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class  NoteRepositoryImpl(
    private val dao:NoteDao
):NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
       return  dao.getNoteById(id)

    }

    override  fun getNotesByCategoryId(catId: Int): Flow<List<Note>>{
        return dao.getNotesByCategoryId(catId)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNote(note)
    }

    override suspend fun insertAudioRecord(audioRecord: AudioRecord) {
        dao.insertAudioRecord(audioRecord)
    }

    override suspend fun deleteAudioRecord(audioRecord: AudioRecord) {
        dao.deleteAudioRecord(audioRecord)
    }

    override  fun getAudioRecordsByNoteId(noteId: Int): Flow<List<AudioRecord>> {
        return dao.getAudioRecordsByNoteId(noteId)
    }

    override suspend fun deleteAudioRecordsByNoteId(noteId: Int) {
        dao.deleteAudioRecordsByNoteId(noteId)
    }

    override  fun getAudioRecords(): Flow<List<AudioRecord>> {
        return dao.getAudioRecords()
    }

    override suspend fun updateAudioRecordsNoteId(newNoteId: Int) {
        dao.updateAudioRecordsNoteId(newNoteId)
    }

    override suspend fun deleteNullAudioRecords() {
        dao.deleteteNullAudioRecords()
    }

    override fun getNullAudioRecords(): Flow<List<AudioRecord>> {
        return  dao.getNullAudioRecords()
    }

    override suspend fun getNoteId(
        title: String,
        content: String,
        timeStamp: Long,
        color: Int,
        category: Int
    ): Int? {
        return dao.getNoteId(title,content,timeStamp,color,category)
    }

    override suspend fun deleteNoteWithAudioRecords(note: Note) {
        dao.deleteNote(note)
    }
}