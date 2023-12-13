package com.example.noteappproject.presantation.add_edit_note

import com.example.noteappproject.domain.model.AudioRecord

data class AudioPlayerState(
    val audioRecords: List<AudioRecord> = emptyList(),
    val isPlaying: Boolean = false,
    val isPaused: Boolean= false,
    val keyID: Int= -1,
    val focusedItem: AudioRecord?= null,
    val isFinished: Boolean= false,
    val currentTime: Long=0,
    val fullTime: Long=0
)
