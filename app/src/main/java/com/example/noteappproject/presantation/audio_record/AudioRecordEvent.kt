package com.example.noteappproject.presantation.audio_record

sealed class AudioRecordEvent{

    object StartRecording: AudioRecordEvent()
    object CancelRecording : AudioRecordEvent()
    object SaveRecording : AudioRecordEvent()
    object PauseRecording: AudioRecordEvent()
    object ResumeRecording: AudioRecordEvent()



}
