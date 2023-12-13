package com.example.noteappproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.noteappproject.data.data_source.NoteDatabase
import com.example.noteappproject.data.repository.NoteRepositoryImpl
import com.example.noteappproject.domain.repository.NoteRepository
import com.example.noteappproject.domain.use_case.AddNoteUseCase
import com.example.noteappproject.domain.use_case.AudioPlayerUseCase
import com.example.noteappproject.domain.use_case.AudioRecordingUseCase
import com.example.noteappproject.domain.use_case.DeleteNoteUseCase
import com.example.noteappproject.domain.use_case.GetNoteId
import com.example.noteappproject.domain.use_case.GetNoteUseCase
import com.example.noteappproject.domain.use_case.GetNotesByCategoryUseCase
import com.example.noteappproject.domain.use_case.GetNotesUseCase
import com.example.noteappproject.domain.use_case.NoteUseCases
import com.example.noteappproject.domain.use_case.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db:NoteDatabase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }
    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext appContext: Context
    ): Context = appContext



    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases{

       return NoteUseCases(
           getNotesUseCase = GetNotesUseCase(repository),
           deleteNoteUseCase = DeleteNoteUseCase(repository),
           addNoteUseCase = AddNoteUseCase(repository),
           getNoteUseCase= GetNoteUseCase(repository),
           getNotesByCategoryUseCase = GetNotesByCategoryUseCase(repository),
           audioRecordingUseCase = AudioRecordingUseCase(repository),
           audioPlayerUseCase = AudioPlayerUseCase(repository),
           getNoteId = GetNoteId(repository),
           updateNoteUseCase = UpdateNoteUseCase(repository)
        )

    }

}