package com.example.noteappproject.presantation.add_edit_note

import BottomMenuBar
import android.Manifest
import android.annotation.SuppressLint

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteappproject.domain.model.Note
import com.example.noteappproject.presantation.add_edit_note.components.TransparentHintTextField
import com.example.noteappproject.presantation.notes.components.DropdownMenuBox
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.noteappproject.presantation.add_edit_note.components.AudioRecordItem
import com.example.noteappproject.presantation.util.ButtonId
import com.example.noteappproject.presantation.util.Screen
@OptIn(ExperimentalMaterial3Api::class,
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    noteId: Int,
    noteCategory:Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val snackbarHostState = remember{SnackbarHostState()}
    var selectedIndex by remember { mutableStateOf(3) }
    val state= viewModel.state.value

    val bottomBarIndex= viewModel.bottamBarIndex.value

    val items = listOf(
        BottomMenuItem(ButtonId.mic, "Audio Record",Screen.AudioRecordScreen.route+"?noteId=${noteId}"),
        BottomMenuItem(ButtonId.category, "Choose Category"),
        BottomMenuItem(ButtonId.colorPalette, "Choose Color"),
        BottomMenuItem(ButtonId.cursorIcon, "Cancel"),
    )


    var isOptionsMenuVisible by remember { mutableStateOf(false) }

    var isDropdownMenuVisible by remember { mutableStateOf(false) }

    var isColorMenuVisible by remember { mutableStateOf(false) }

    var microphonePermissionGranted by remember { mutableStateOf(false) }

    val isDialogVisible= remember{ mutableStateOf(false) }

    val microphonePermissionRequest =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            microphonePermissionGranted = isGranted
            if (isGranted) {
                navController.navigate(Screen.AudioRecordScreen.route+"?noteId=${noteId}")
            }
            else {
                isDialogVisible.value= true
            }
        }

    DisposableEffect(Unit) {
        onDispose {
          viewModel.onEvent(AddEditNoteEvent.KillProcesses)
        }
    }

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    LaunchedEffect(noteBackgroundAnimatable){
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Save note")
            }
        },
        bottomBar = {
            if (isOptionsMenuVisible){
            // Ekranın altında görünen menüyü burada ekliyoruz
                BottomMenuBar(
                    items = items,
                    selectedIndex = selectedIndex,
                ) { newIndex ->
                    selectedIndex = newIndex
                    if (newIndex==1)  isDropdownMenuVisible= true else isDropdownMenuVisible= false
                    if (newIndex==2)  isColorMenuVisible= true else isColorMenuVisible= false
                    if(newIndex==3)   {isDropdownMenuVisible = false
                        isDropdownMenuVisible= false}
                    val selectedMenuItem = items[newIndex]
                    if (newIndex==0){
                        if (!microphonePermissionGranted) {
                            microphonePermissionRequest.launch(Manifest.permission.RECORD_AUDIO)
                        }
                        else if(microphonePermissionGranted){
                            selectedMenuItem.route?.let {route->
                               navController.navigate(route)
                            }
                        }
                    }
                }
            }
        }

        // scaffoldState = scaffoldState
    ) {

        if (isDialogVisible.value) {
            val context = LocalContext.current
            AlertDialog(
                onDismissRequest = {
                    isDialogVisible.value= false
                    selectedIndex=3 },
                title = { Text("The Permission is denied!") },
                text = {
                    Text("In order to record sound, you must allow the use of the microphone in the settings.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val settingsIntent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                            val uri = Uri.fromParts("package", context.packageName, null)
                            settingsIntent.data = uri
                            context.startActivity(settingsIntent)
                            isDialogVisible.value= false
                            selectedIndex=3
                        }
                    ) {
                        Text("Open Setting")
                    }
                }
            )
        }

        if (isColorMenuVisible) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Note.noteColors) { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            if (isColorMenuVisible) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Note.noteColors) { color ->
                        val colorInt = color.toArgb()
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 3.dp,
                                    color = if (viewModel.noteColor.value == colorInt) {
                                        Color.Black
                                    } else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {
                                        noteBackgroundAnimatable.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(
                                                durationMillis = 500
                                            )
                                        )
                                    }
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }
                        )
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .imePadding(),
                horizontalArrangement = Arrangement.Center

            ) {
                DropdownMenuBox(
                    onCategoryChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeCategory(it))
                    },
                    selectedIndexOne = if (noteCategory!=0) noteCategory else viewModel.noteCategory.value,
                    isVisible = isDropdownMenuVisible)
              }

            Spacer(modifier = Modifier.height(7.dp))

                TransparentHintTextField(
                    text = titleState.text,
                    hint = titleState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))

                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))

                    },
                    isHintVisible = titleState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(0.96F)

            ) {
                item {
                    TransparentHintTextField(
                        text = contentState.text,
                        hint = contentState.hint,
                        onValueChange = {
                            viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                        },
                        onFocusChange = { focusState ->
                            viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(focusState))
                            isOptionsMenuVisible = focusState.isFocused
                        },
                        isHintVisible = contentState.isHintVisible,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                }

                items(state.audioRecords) { audioRecord ->
                    AudioRecordItem(
                        audioRecord = audioRecord,
                        playingId = state.keyID,
                        isItPlaying = viewModel.isPlayingCase.value ?: false,
                        focusedItem = state.focusedItem,
                        isPaused = state.isPaused,
                        backgroundColor = Color.Transparent,
                        onPlayClick = { viewModel.onEvent(AddEditNoteEvent.PlayAudio(audioRecord)) },
                        valueThis = viewModel.isPlayingCase,
                        isFinished = viewModel.isFinishedCase,
                        seekToPosition = { viewModel.onEvent(AddEditNoteEvent.SeektoPosition(state.currentTime)) },
                        onDeleteClick = { viewModel.onEvent(AddEditNoteEvent.DeleteRecording(audioRecord)) }
                    )
                }
            }
            if (isOptionsMenuVisible){ Spacer(modifier = Modifier.height(50.dp))}
            }
        }
    }

data class BottomMenuItem(
    val icon: Int,
    val text: String,
    val route: String? = null // Yönlendirme rotası
)

