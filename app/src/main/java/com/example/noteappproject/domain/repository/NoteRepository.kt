package com.example.noteappproject.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

     fun getNotesByCategoryId(catId:Int): Flow<List<Note>>

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)


    suspend fun insertAudioRecord(audioRecord: AudioRecord)

    suspend fun deleteAudioRecord(audioRecord: AudioRecord)


    fun getAudioRecordsByNoteId(noteId: Int): Flow<List<AudioRecord>>

    suspend fun deleteAudioRecordsByNoteId(noteId: Int)

    fun getAudioRecords(): Flow<List<AudioRecord>>

    suspend fun updateAudioRecordsNoteId(newNoteId: Int)

    suspend fun deleteNullAudioRecords()

    fun getNullAudioRecords(): Flow<List<AudioRecord>>

    suspend fun getNoteId(title: String, content: String, timeStamp: Long, color: Int, category: Int): Int?

    suspend fun deleteNoteWithAudioRecords(note: Note)


}