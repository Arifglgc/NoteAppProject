package com.example.noteappproject.presantation.audio_record

import com.example.noteappproject.R
import com.example.noteappproject.presantation.util.ButtonId

data class AudioRecordState(
    val isStarted: Boolean= false,
    var isPaused: Boolean = false,
    val isResumed: Boolean= false,
    val isCancelled: Boolean=false,
    val isSaved: Boolean= false,
    val buttonId: Int= ButtonId.pause,
    var durationLong: Long=  0

)
