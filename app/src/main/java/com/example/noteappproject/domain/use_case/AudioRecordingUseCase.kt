package com.example.noteappproject.domain.use_case

import android.media.MediaRecorder
import android.util.Log
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.domain.repository.NoteRepository
import java.io.File
import java.io.IOException

class AudioRecordingUseCase(
    private val repository: NoteRepository
) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentTime: Long=0

     fun startRecording(directory: File) {
         if (mediaRecorder == null) {
             initializeMediaRecorder(directory)
             startMediaRecorder()
         }
    }

    private fun initializeMediaRecorder(directory: File) {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            createOutputFile(directory)
            prepareMediaRecorder()
        }
    }

    private fun createOutputFile(directory: File) {
        if (!directory.exists()) {
            directory.mkdirs()
        }

        currentTime = System.currentTimeMillis()
        Log.d("USETIME", " time is $currentTime start ")

        val audioFile = File(directory, "audioRecording$currentTime.mp4")
        mediaRecorder?.setOutputFile(audioFile.absolutePath)
    }


    private fun prepareMediaRecorder() {
        try {
            mediaRecorder?.prepare()
        } catch (e: IOException) {
            Log.d( "AudioRecordingUseCase",e.message.toString())
        }
    }

    private fun startMediaRecorder() {
        mediaRecorder?.start()
    }


    fun pauseRecording() {
        mediaRecorder?.apply {
            pause()
        }
    }


     fun resumeRecording()  {
        mediaRecorder?.apply {
            resume()
        }
    }

    suspend fun stopRecordingAndSave(directory: File, id: Int,durationLong : Long){
        if(mediaRecorder!= null) {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
                mediaRecorder=null
            }
                 // Kaydedilen sesin süresi
            val audioFilePath = File(directory, "audioRecording$currentTime.mp4").absolutePath

            Log.d("USETIME", " time is $currentTime")

            val audioRecord = AudioRecord(null, id, audioFilePath,durationLong)
            // Veritabanına kaydedilen sesi kaydetme işlemi
            repository.insertAudioRecord(audioRecord)
        }
    }

    suspend fun addAudiosBack(audioItem: AudioRecord){
        repository.insertAudioRecord(audioItem)
    }

     fun cancelRecording() {
        if (mediaRecorder != null) {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
                mediaRecorder = null // Kaydı iptal ettiğinizde mediaRecorder'ı sıfırlanır
            }
        }
    }
    suspend fun updateRecords(noteId: Int){
        repository.updateAudioRecordsNoteId(noteId)
    }

}