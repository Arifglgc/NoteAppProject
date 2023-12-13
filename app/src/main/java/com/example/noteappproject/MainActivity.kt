package com.example.noteappproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteappproject.presantation.add_edit_note.AddEditNoteScreen
import com.example.noteappproject.presantation.audio_record.AudioRecordScreen
import com.example.noteappproject.presantation.notes.NotesScreen
import com.example.noteappproject.presantation.util.Screen
import com.example.noteappproject.ui.theme.NoteAppProjectTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            NoteAppProjectTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)

                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}&categoryId={categoryId}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "categoryId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = 0
                                },
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            val noteId = it.arguments?.getInt("noteId") ?: -1

                            val noteCategory=it.arguments?.getInt(  "categoryId")?: 0
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color,
                                noteCategory = noteCategory,
                                noteId = noteId
                            )
                        }

                        composable(
                            route=Screen.AudioRecordScreen.route+ "?noteId={noteId}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },

                                )){
                            AudioRecordScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

