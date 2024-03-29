package com.example.noteappproject.presantation.add_edit_note

import android.util.Log

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappproject.domain.model.InvalidNoteException
import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.domain.use_case.AudioPlayerListener
import com.example.noteappproject.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
const val TAG="AddEditViewModel"
@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),AudioPlayerListener {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle


    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor



    private val _noteCategory = mutableStateOf(0)
    val noteCategory: State<Int> = _noteCategory

    private val _bottamBarIndex = mutableStateOf(3)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(AudioPlayerState())
    val state: State<AudioPlayerState> =_state

    private var currentNoteId: Int? = null

    private val _noteId = mutableStateOf(0)
    private var recordingTimerJob: Job? = null


    private var getAudiosJob: Job? = null

    //val isPlayingLive: MutableLiveData<Boolean> = noteUseCases.audioPlayerUseCase.isPlaying

    //val isFinishedLive: MutableLiveData<Boolean> = noteUseCases.audioPlayerUseCase.isFinished

    // private val isPlayingButtonLive: LiveData<Boolean> = noteUseCases.audioPlayerUseCase.isPlayingButton


    val isPlayingCase= MutableLiveData<Boolean>()
    val isFinishedCase= MutableLiveData<Boolean>()
    val isPausedCase= MutableLiveData<Boolean>()


    var elapsedTime = mutableStateOf(0L)
    var startTime =mutableStateOf(0L)




    private val isFinishedObserver = Observer<Boolean> {
            isFinished->
        if (isFinished) {
            elapsedTime.value=0
            _state.value=state.value.copy(
                currentTime = elapsedTime.value
            )
            }
    }

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteUseCase(noteId)?.also { note ->
                        currentNoteId = noteId
                        _noteId.value= note.id?:0
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                      //  fixedCatId=note.category
                        _noteCategory.value=note.category
                    }
                    getSoundsById(currentNoteId?:-1)
                }
            }
            //getSounds()
        }
        noteUseCases.audioPlayerUseCase.addListener(this)

    }
    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
                viewModelScope.launch {
                    // Notun renk değişikliği
                    _eventFlow.emit(UiEvent.ShowSnackbar("Note color changed successfully"))
                }
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.updateNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId,
                                category = noteCategory.value
                            )
                        )
                       // noteUseCases.audioRecordingUseCase.updateRecords(currentNoteId?:6 )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            // isPlaying = isPlayingLive.value?: false,
            is AddEditNoteEvent.ChangeCategory -> {
                _noteCategory.value=event.categoryId
            }

            is AddEditNoteEvent.PlayAudio -> {

                _state.value= state.value.copy(
                    isPaused = isPausedCase.value?:true,
                    keyID = event.audioRecord.id?:-1,
                    isPlaying = isPlayingCase.value?: false,
                    fullTime = event.audioRecord.duration
                )
                noteUseCases.audioPlayerUseCase.play(event.audioRecord)

                startRecordingTimer(state.value.fullTime)

                Log.w(TAG," playAudio ViewModel Use case sonrası  ")
            }

            is AddEditNoteEvent.PausePlaying -> {

                _state.value= state.value.copy(
                    isPlaying = false,
                    isPaused = true
                )
                Log.w(TAG," Pause içinde state değişim sonrası "+state.value.isPlaying +state.value.isPaused)

                noteUseCases.audioPlayerUseCase.pause()

            }
            is AddEditNoteEvent.ResumePlaying -> {

                _state.value= state.value.copy(
                    isPlaying = isPlayingCase.value?: false,
                    isPaused = false
                    //focusedItem = event.focusedItem
                )
                Log.w(TAG,"Resume içinde state değişim   sonrası "+state.value.isPlaying +state.value.isPaused)

                noteUseCases.audioPlayerUseCase.resume()
                startRecordingTimer(state.value.fullTime)


            }
            is AddEditNoteEvent.DeleteRecording -> {
                _state.value= state.value.copy(
                    isPlaying = false,
                    isPaused = false
                )
                viewModelScope.launch { noteUseCases.audioPlayerUseCase.deleteRecording(event.deletedAudio) }
            }

            is AddEditNoteEvent.FocusedPlaying -> {
                _state.value= state.value.copy(
                    focusedItem = event.focusedItem
                )
            }
            is AddEditNoteEvent.SeektoPosition -> {
                _state.value= state.value.copy(
                )
                noteUseCases.audioPlayerUseCase.seekToPosition(event.time)
            }
            is AddEditNoteEvent.KillProcesses -> {
                _state.value= state.value.copy(
                )
                noteUseCases.audioPlayerUseCase.stop()
            }

            is AddEditNoteEvent.ChanceBBarIndex ->
            {
                _bottamBarIndex.value= event.index
            }

            is AddEditNoteEvent.RestoreAudioRecord ->
            {

            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }

    private fun getSoundsById(id: Int) {
        getAudiosJob?.cancel()
        getAudiosJob = noteUseCases.audioPlayerUseCase.getAudiosByNoteId(id)
            .onEach { audioRecords ->
                _state.value = state.value.copy(
                    audioRecords = audioRecords
                )
            }
            .launchIn(viewModelScope)
    }


     private fun startRecordingTimer(time: Long) {
        recordingTimerJob?.cancel()
        recordingTimerJob = viewModelScope.launch {
            isFinishedCase.observeForever(isFinishedObserver)

             startTime.value = System.currentTimeMillis() - elapsedTime.value

            while ( isPausedCase.value == false && isFinishedCase.value != true)
             {
                delay(1) // 1ms gecikme

                elapsedTime.value = System.currentTimeMillis() - startTime.value
                _state.value=state.value.copy(
                    currentTime = elapsedTime.value
                )
            }
            if (isFinishedCase.value == true ) {
                elapsedTime.value=0
                _state.value=state.value.copy(
                    currentTime = elapsedTime.value
                )
            }
        }
    }

    override fun onPlayingStateChanged(isPlaying: Boolean) {
        viewModelScope.launch {
            isPlayingCase.value = isPlaying
        }
    }

    override fun onFinishedStateChanged(isFinished: Boolean) {
        viewModelScope.launch {
            isFinishedCase.value = isFinished
        }
    }

    override fun onPausedStateChanged(isPaused: Boolean) {
        viewModelScope.launch {
            isPausedCase.value = isPaused
        }
    }

}