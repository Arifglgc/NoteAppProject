package com.example.noteappproject.presantation.util

sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")
    object AudioRecordScreen: Screen( "audio_record_screen")
}
