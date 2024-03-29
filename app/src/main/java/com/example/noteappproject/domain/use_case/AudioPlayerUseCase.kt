package com.example.noteappproject.domain.use_case

import android.media.MediaPlayer
import android.util.Log

import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.IOException

interface AudioPlayerListener {
    fun onPlayingStateChanged(isPlaying: Boolean)
    fun onFinishedStateChanged(isFinished: Boolean)
    fun onPausedStateChanged(isPaused: Boolean)
}
class AudioPlayerUseCase(
    private val repository: NoteRepository
) {
    private val listeners = mutableListOf<AudioPlayerListener>()

    private var player: MediaPlayer? = null
    private var isPausedCase = true
    private var isPlayingCase = false
    private var isFinishedCase = false
    private var lastIdCase = 0

    fun addListener(listener: AudioPlayerListener) {
        listeners.add(listener)
    }

    private fun notifyPlayingState(isPlaying: Boolean) {
        listeners.forEach { it.onPlayingStateChanged(isPlaying) }
    }

    private fun notifyFinishedState(isFinished: Boolean) {
        listeners.forEach { it.onFinishedStateChanged(isFinished) }
    }

    private fun notifyPausedState(isPaused: Boolean) {
        listeners.forEach { it.onPausedStateChanged(isPaused) }
    }

    private fun createMediaPlayer(audioRecord: AudioRecord) {
        player = MediaPlayer().apply {
            try {
                isFinishedCase = false
                notifyFinishedState(false)
                isPlayingCase = true
                notifyPlayingState(true)
                isPausedCase = false
                notifyPausedState(false)

                setDataSource(audioRecord.audioFilePath)
                prepare()
                start()

            } catch (e: IOException) {
                Log.w("usecase", "Hata bulundu ${e.message}  ")
            }
        }

        player?.setOnCompletionListener {
            handleCompletion()
        }
    }

    private fun handleCompletion() {
        isFinishedCase = true
        notifyFinishedState(true)
        isPlayingCase = false
        notifyPlayingState(false)
        isPausedCase = false
        notifyPausedState(false)

        stop()
    }

    fun play(audioRecord: AudioRecord) {
        if (lastIdCase == audioRecord.id) {
            if (player == null) {
                lastIdCase = audioRecord.id
                createMediaPlayer(audioRecord)
            } else if (isPlayingCase) {
                pause()
            } else if (!isPlayingCase) {
                resume()
            } else {
                stop()
                play(audioRecord)
            }
        } else {
            lastIdCase = audioRecord.id!!
            stop()
            play(audioRecord)
        }
    }

    fun pause() {
        player?.apply {
            if (isPlaying) {


                pause()
                isPausedCase = true
                notifyPausedState(true)
                isPlayingCase = false
                notifyPlayingState(false)

            }
        }
    }

    fun resume() {
        isPausedCase = false
        notifyPausedState(false)

        isPlayingCase = true
        notifyPlayingState(true)

        isFinishedCase = false
        notifyFinishedState(false)

        player?.apply {
            if (!isPlaying) {
                start()
            }
        }

        player?.setOnCompletionListener {
            handleCompletion()
        }
    }

    fun seekToPosition(time: Long) {
        player?.seekTo(time.toInt())
    }

    fun stop() {
        player?.apply {
            isFinishedCase = true
            notifyFinishedState(true)
            reset()
            release()
            player = null
        }
    }

    suspend fun deleteRecording(audioRecord: AudioRecord) {
        stop()
       // player?.stop()
        //player?.release()
        //player = null

        val fileToDelete = File(audioRecord.audioFilePath)
        if (fileToDelete.exists()) {
            if (!fileToDelete.delete()) {
                Log.e("AudioPlayerUseCase", "Dosya silinemedi: ${audioRecord.audioFilePath}")
            }
        }
        repository.deleteAudioRecord(audioRecord)
    }

    suspend fun deleteAll(noteId: Int) {
        val listOfAudioRecords = repository.getAudioRecordsByNoteId(noteId)
        listOfAudioRecords.collect { audioRecords ->
            audioRecords.forEach { audioRecord ->
                deleteAudioRecord(audioRecord)
            }
        }
    }

    suspend fun deleteNullRecording(audioList: List<AudioRecord>) {
        audioList.forEach { audioRecord ->
            deleteAudioRecord(audioRecord)
        }
    }

    private suspend fun deleteAudioRecord(audioRecord: AudioRecord) {
        val fileToDelete = File(audioRecord.audioFilePath)
        if (fileToDelete.exists() && !fileToDelete.delete()) {
            Log.e("AudioPlayerUseCase", "Dosya silinemedi: ${audioRecord.audioFilePath}")
        }
        repository.deleteAudioRecord(audioRecord)
    }

    fun getNullAudios(): Flow<List<AudioRecord>> {
        return repository.getNullAudioRecords()
    }
    fun getAudiosByNoteId(id: Int): Flow<List<AudioRecord>> {
        return repository.getAudioRecordsByNoteId(id)
    }
}

