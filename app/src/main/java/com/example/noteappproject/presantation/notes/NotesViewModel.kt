package com.example.noteappproject.presantation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.use_case.NoteUseCases
import com.example.noteappproject.domain.util.NoteOrder
import com.example.noteappproject.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(
    public val noteUseCases: NoteUseCases
) : ViewModel(){

    private val _state= mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note?=null
    private var recentlyDeletedNoteAudios: List<AudioRecord>? =null

    private var getNotesJob: Job? = null
    private var getAudioJob: Job? = null


    init {
        Log.w("noteView "," Init Part")
        getNotes(NoteOrder.Date(OrderType.Descending),state.value.noteCategory)
    }
    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order ->{
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType== event.noteOrder.orderType )
                {
                    return
                }
                getNotes(event.noteOrder,state.value.noteCategory)

            }
            is NotesEvent.DeleteNote->{
                recentlyDeletedNote=event.note
                val noteId=event.note.id
                viewModelScope.launch {
                    if (noteId != null) {
                        recentlyDeletedNoteAudios= noteUseCases.audioPlayerUseCase.getAudiosByNoteId(recentlyDeletedNote!!.id!!).first()
                        Log.d("ReStore", "this is after the  ${recentlyDeletedNoteAudios!!.size}")

                    }
                    val noteUs= event.note
                    noteUseCases.deleteNoteUseCase(noteUs)
                }

            }

            is NotesEvent.RestoreNote->{
                viewModelScope.launch {
                        if (recentlyDeletedNote!= null){
                            noteUseCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
                        }

                        if (recentlyDeletedNoteAudios != null) {
                        Log.d("ReStore", " its back ? ${recentlyDeletedNoteAudios!!.size}")
                        recentlyDeletedNoteAudios?.forEach { item ->
                            noteUseCases.audioRecordingUseCase.addAudiosBack(item)
                            Log.d("ReStore", " ${item.audioFilePath}")
                    }
                        }

                    recentlyDeletedNote= null
                    recentlyDeletedNoteAudios= null

                }

            }
            is NotesEvent.DeleteAudios->{
                viewModelScope.launch {
                    Log.d("testingoff",recentlyDeletedNote!!.id!!.toString())
                    noteUseCases.audioPlayerUseCase.deleteAll(recentlyDeletedNote!!.id!!)
                }
            }

            is NotesEvent.ToggleOrderSection->{
                _state.value=state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.DeleteNullAudio->{
                deleteNullAudios()
            }

            is NotesEvent.FilterNoteByCategory -> {
                getNotes(state.value.noteOrder,event.categoryId)
                _state.value = state.value.copy(
                    noteCategory = event.categoryId
                )

            }
        }
    }
    private fun getNotes(noteOrder: NoteOrder,categoryId: Int) {
        getNotesJob?.cancel()
        if(categoryId>0){
        getNotesJob = noteUseCases.getNotesByCategoryUseCase(noteOrder,categoryId)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder

                )
            }
            .launchIn(viewModelScope)}
        else{
            getNotesJob = noteUseCases.getNotesUseCase(noteOrder)
                .onEach { notes ->
                    _state.value = state.value.copy(
                        notes = notes,
                        noteOrder = noteOrder

                    )
                }
                .launchIn(viewModelScope)}

        }

    private fun deleteNullAudios(){
        getAudioJob?.cancel()
            getAudioJob = noteUseCases.audioPlayerUseCase.getNullAudios()
                .onEach { audioRecord ->
                  noteUseCases.audioPlayerUseCase.deleteNullRecording(audioRecord)
                }
                .launchIn(viewModelScope)
    }
    }
