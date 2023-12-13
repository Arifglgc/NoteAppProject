package com.example.noteappproject.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.model.Note

@Database(entities =[Note::class,AudioRecord::class],
    version =4 )
abstract class NoteDatabase: RoomDatabase() {

   abstract val noteDao: NoteDao

   companion object{
       const val DATABASE_NAME= "notes_db"
   }

}