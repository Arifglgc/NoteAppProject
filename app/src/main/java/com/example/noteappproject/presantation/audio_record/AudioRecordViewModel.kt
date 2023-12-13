package com.example.noteappproject.presantation.audio_record

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappproject.domain.use_case.NoteUseCases
import com.example.noteappproject.presantation.util.ButtonId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AudioRecordViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    context: Context,
    savedStateHandle: SavedStateHandle

) :ViewModel(){
    private val _state= mutableStateOf(AudioRecordState())
    val state: State<AudioRecordState> = _state

    private val directory = File(context.externalCacheDir!!.absolutePath, "/your_file_name")
    var noteId =0

    private var recordingTimerJob: Job? = null

    private var elapsedTime = mutableStateOf(0L)
    private var startTime =  mutableStateOf(0L)



    init {
        noteId = savedStateHandle.get<Int>("noteId")?: -1
    }
    fun onEvent(event: AudioRecordEvent){

    when(event){
        is AudioRecordEvent.StartRecording->{
            _state.value=state.value.copy(
                isStarted = true,
                isPaused = false,
                isCancelled = false,
                isSaved = false,
                buttonId = ButtonId.pause,

            )
            startRecordingTimer()
            viewModelScope.launch {
                noteUseCases.audioRecordingUseCase.startRecording(directory)
            }

        }
        is AudioRecordEvent.PauseRecording->{
            _state.value=state.value.copy(
                isPaused = !state.value.isPaused,
                buttonId = buttonId(state.value.isPaused)
            )

            startRecordingTimer()
            viewModelScope.launch {
                if(state.value.isPaused) noteUseCases.audioRecordingUseCase.pauseRecording()
                else noteUseCases.audioRecordingUseCase.resumeRecording()
            }

        }

        is AudioRecordEvent.ResumeRecording->{
            _state.value=state.value.copy(
                isPaused = false,
                isResumed = true,
                buttonId = ButtonId.mic

            )
            viewModelScope.launch {
                noteUseCases.audioRecordingUseCase.resumeRecording()
            }

        }
        is AudioRecordEvent.CancelRecording->{
            _state.value=state.value.copy(
                isCancelled = true,
                isPaused = false,
                isStarted = false,
                isSaved = true,
            )
            elapsedTime.value=0
            viewModelScope.launch {
                noteUseCases.audioRecordingUseCase.cancelRecording()
            }

        }
        is AudioRecordEvent.SaveRecording->{// SaveRecording e Screen den parametre oalrak text timer değeri göndeririz-- duration kısmına da bu değeri veririz
            _state.value=state.value.copy(
                isPaused = false,
                isStarted = false,
                isSaved = true
            )
            viewModelScope.launch {
                noteUseCases.audioRecordingUseCase.stopRecordingAndSave(directory,noteId,elapsedTime.value)
            }
            elapsedTime.value=0
        }
    }
}

private fun buttonId(paused: Boolean): Int{
    return if (paused) ButtonId.pause
    else ButtonId.mic

}

     private fun startRecordingTimer() {
        startTime.value = System.currentTimeMillis() - elapsedTime.value

        recordingTimerJob?.cancel()
        recordingTimerJob = viewModelScope.launch {
            while (!state.value.isPaused && state.value.isStarted && !state.value.isCancelled) {
                elapsedTime.value = System.currentTimeMillis() - startTime.value
                _state.value=state.value.copy(
                    durationLong = elapsedTime.value
                )
                delay(1) // 1ms gecikme
            }
        }
    }

}

