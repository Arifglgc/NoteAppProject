package com.example.noteappproject.presantation.add_edit_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.noteappproject.R
import com.example.noteappproject.domain.model.AudioRecord
import com.example.noteappproject.presantation.add_edit_note.AddEditNoteViewModel
@Composable
fun AudioRecordItem(
    audioRecord: AudioRecord,
     isItPlaying: Boolean,
    isPaused: Boolean,
    playingId: Int,
    focusedItem: AudioRecord?,
    backgroundColor: Color,
    onPlayClick: () -> Unit,
    seekToPosition: (time: Long) ->Unit,
    onDeleteClick: () -> Unit,
    isFinished: LiveData<Boolean>,
    // onSeek: (Float) -> Unit,
    valueThis: LiveData<Boolean>,
    viewModel: AddEditNoteViewModel= hiltViewModel()
) {

    val currentPositionn= remember{ mutableStateOf(viewModel.state.value.currentTime) }
    // var new = valueThis
    // val myLiveData by rememberUpdatedState(newValue = valueThis.value)
    //val updatedIsPlaying by rememberUpdatedState(isPlaying)
    // var value = rememberUpdatedState(newValue = viewModel.isPlayingLive.value)

    var isPlaying by remember { mutableStateOf(false) }
    var updatedPlayIcon by remember { mutableStateOf(R.drawable.baseline_play_arrow_24) }
    val updatedPlayingId by rememberUpdatedState(playingId)

    val state= viewModel.state.value

    DisposableEffect(valueThis.value,updatedPlayingId,isFinished.value) {
        val observer = Observer<Boolean> { newValue ->
                isPlaying = newValue

            val playIcon = if (isPlaying && updatedPlayingId == audioRecord.id && isItPlaying) {
                R.drawable._11871_pause_icon // Değiştirilmek istenen icon

            } else {
                R.drawable.baseline_play_arrow_24 // Değiştirilmek istenen icon
            }
            // Icon'u güncelleme
            updatedPlayIcon = playIcon
        }
        valueThis.observeForever(observer)

        onDispose {
            valueThis.removeObserver(observer)
        }
    }
    // Icon'un güncellendiği bir değişkeni kullanabilirsiniz
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor)
            .padding(8.dp)
            .border(2.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ses çalma / duraklatma düğmesi
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier.size(25.dp),
            ) {
                Icon(
                    painterResource(id = updatedPlayIcon) ,
                    tint = Color.Black,
                    contentDescription = if (isItPlaying) {
                        "Duraklat"
                    } else {
                        "Çal"
                    }
                )
            }
            Text(
                text = if ( (updatedPlayingId != audioRecord.id) || (isFinished.value == true) ) formatElapsedTime(audioRecord.duration)  else formatElapsedTime(state.currentTime) ,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(3.dp))
            // Ses kaydı kaydırma çubuğu
            Slider(
                //currentPositionn.value.toFloat()
                value = if ((updatedPlayingId == audioRecord.id && isFinished.value != true)) state.currentTime.toFloat() else 0.toFloat(),
                onValueChange = { newPosition ->
                    if ((updatedPlayingId == audioRecord.id)){
                    currentPositionn.value= newPosition.toLong()
                    seekToPosition(currentPositionn.value)
                    }
                },
                valueRange = 0f..(audioRecord.duration).toFloat(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(10.dp)
                    .size(2.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Blue,
                    activeTrackColor = Color.Blue,
                    inactiveTrackColor = Color.LightGray
                )
            )
            // Ses kaydı silme düğmesi
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Sil",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun formatElapsedTime(duration: Long): String {
    val minutes = duration / 60000
    val seconds = ((duration % 60000) / 1000)
    return String.format("%02d:%02d", minutes, seconds)
}