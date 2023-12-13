package com.example.noteappproject.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note where id= :id")
    suspend fun getNoteById(id:Int): Note?

    @Query("SELECT * FROM note where category= :id")
     fun getNotesByCategoryId(id:Int): Flow<List<Note>>

    @Insert(onConflict = REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Transaction
    suspend fun deleteNoteWithAudioRecords(note: Note) {
        deleteNote(note)
        deleteAudioRecordsByNoteId(note.id?:0)
    }

    @Insert(onConflict = REPLACE)
    suspend fun insertAudioRecord(audioRecord: AudioRecord)
    @Delete
    suspend fun deleteAudioRecord(audioRecord: AudioRecord)

    @Query("SELECT * FROM audiorecord WHERE noteId = :noteId")
     fun getAudioRecordsByNoteId(noteId: Int): Flow<List<AudioRecord>>

    @Query("SELECT * FROM audiorecord WHERE noteId  = -1 ")
    fun getNullAudioRecords(): Flow<List<AudioRecord>>


    @Query("DELETE FROM audiorecord WHERE noteId = :noteId")
    suspend fun deleteAudioRecordsByNoteId(noteId: Int)

    @Query("SELECT * FROM audiorecord")
     fun getAudioRecords(): Flow<List<AudioRecord>>

    @Query("UPDATE audiorecord SET noteId = :newNoteId WHERE noteId = -1")
    suspend fun updateAudioRecordsNoteId(newNoteId: Int)

    @Query("Delete From audiorecord  WHERE noteId = -1 or null")
    suspend fun deleteteNullAudioRecords()


    @Query("SELECT id FROM note WHERE title = :title AND content = :content AND timeStamp = :timeStamp AND color = :color AND category = :category")
    suspend fun getNoteId(title: String, content: String, timeStamp: Long, color: Int, category: Int): Int?

}