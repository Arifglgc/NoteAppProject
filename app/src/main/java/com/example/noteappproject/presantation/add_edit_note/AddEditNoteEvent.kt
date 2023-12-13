package com.example.noteappproject.presantation.add_edit_note

import androidx.compose.ui.focus.FocusState
import com.example.noteappproject.domain.model.AudioRecord

sealed class AddEditNoteEvent{
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangeColor(val color: Int): AddEditNoteEvent()
    data class ChangeCategory(val categoryId: Int): AddEditNoteEvent()

    object SaveNote: AddEditNoteEvent()

    object KillProcesses: AddEditNoteEvent()

    data class ChanceBBarIndex(val index: Int): AddEditNoteEvent()

    data class PlayAudio(val audioRecord: AudioRecord): AddEditNoteEvent()
    data class PausePlaying(val audioRecord: AudioRecord): AddEditNoteEvent()
    data class ResumePlaying(val audioRecord: AudioRecord): AddEditNoteEvent()

    data class SeektoPosition( val time: Long): AddEditNoteEvent()
    data class FocusedPlaying(val focusedItem: AudioRecord): AddEditNoteEvent()

    data class DeleteRecording(val deletedAudio: AudioRecord): AddEditNoteEvent()


}