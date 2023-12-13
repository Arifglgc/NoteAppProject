package com.example.noteappproject.domain.use_case

data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val updateNoteUseCase: UpdateNoteUseCase,
    val getNotesByCategoryUseCase: GetNotesByCategoryUseCase,
    val audioRecordingUseCase: AudioRecordingUseCase,
    val audioPlayerUseCase: AudioPlayerUseCase,
    val getNoteId: GetNoteId
)
