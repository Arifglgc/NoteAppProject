package com.example.noteappproject.presantation.audio_record

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteappproject.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioRecordScreen(
   navController: NavController,
   viewModel: AudioRecordViewModel = hiltViewModel()
){

    val idofButton= remember {
        mutableStateOf(R.drawable._11871_pause_icon)
    }
    val state= viewModel.state.value
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Audio Record") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .padding(bottom = 100.dp)

            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {

                    if (!state.isStarted) {
                        Text(
                            text = "Tap for recording...",
                            Modifier
                                .padding(5.dp)
                                .align(CenterHorizontally)
                        )
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .padding(25.dp)
                                .align(CenterHorizontally)
                                .clip(CircleShape)
                                .background(color = Color.LightGray)
                                .clickable {
                                    viewModel.onEvent(AudioRecordEvent.StartRecording)
                                }
                                .border(1.dp, Color.Blue, CircleShape)

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_mic_24),
                                contentDescription = "Microphone Icon",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            )
                        }
                    } else {

                        Text(
                            text = "Audio record",
                            Modifier
                                .padding(5.dp)
                                .align(CenterHorizontally)
                        )

                        Text(
                            text = "${state.durationLong / 60000}:${((state.durationLong % 60000) / 1000).toString().padStart(2, '0')}",
                            Modifier
                                .padding(2.dp)
                                .align(CenterHorizontally)
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(30.dp)
                                    .clip(CircleShape)
                                    .background(color = Color.LightGray)
                                    .clickable {

                                        viewModel.onEvent(AudioRecordEvent.CancelRecording)
                                        idofButton.value =
                                            if (state.isPaused) R.drawable.baseline_mic_24 else {
                                                R.drawable._11871_pause_icon
                                            }
                                    }
                                    .border(1.dp, Color.Blue, CircleShape)

                            ) {
                                Image(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Microphone Icon",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(30.dp)
                                    .clip(CircleShape)
                                    .background(color = Color.LightGray)
                                    .clickable {
                                        viewModel.onEvent(AudioRecordEvent.PauseRecording)
                                        idofButton.value =
                                            state.buttonId
                                    }
                                    .border(1.dp, Color.Blue, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = state.buttonId),
                                    contentDescription = "Microphone Icon",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(30.dp)
                                    .clip(CircleShape)
                                    .background(color = Color.LightGray)
                                    .clickable {
                                        viewModel.onEvent(AudioRecordEvent.SaveRecording)
                                        Toast.makeText(context,"Audio is recorded succesfully!",Toast.LENGTH_SHORT).show()
                                    }
                                    .border(1.dp, Color.Blue, CircleShape)

                            ) {
                                Image(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Microphone Icon",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                )
                            }

                        }
                    }

                }

            }

        }
    }
}