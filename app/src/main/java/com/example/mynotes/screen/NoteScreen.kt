package com.example.mynotes.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.R
import com.example.mynotes.components.NoteButton
import com.example.mynotes.components.NoteInputText
import com.example.mynotes.data.NotesDataSource
import com.example.mynotes.model.Note
import java.time.format.DateTimeFormatter

@ExperimentalComposeUiApi
@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(6.dp)) {

        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name,),
                    color = Color.Black
                )
            },
            actions = {
                Icon(imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Icon",
                    tint = Color.Black
                )
            },
            backgroundColor = Color(0xffdadfe3),
            elevation = 5.dp)

        //content

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            NoteInputText(
                modifier = Modifier.padding(
                    top = 9.dp,
                    bottom = 8.dp
                ),
                text = title,
                label = "Title",
                onTextChange = {
                    if(it.all { char ->
                    char.isLetter() || char.isWhitespace()
                        }) title = it
            })

            NoteInputText(
                modifier = Modifier.padding(
                    top = 9.dp,
                    bottom = 8.dp
                ),
                text = description,
                label = "Add a note",
                onTextChange = {
                    if(it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) description = it
                })
            Spacer(modifier = Modifier.height(20.dp))
            NoteButton(
                modifier = Modifier
                    .height(45.dp)
                    .width(90.dp),
                text = "Save",
                onClick = {
                    if(title.isNotEmpty() && description.isNotEmpty()){
                        // save/add two list
                            onAddNote(Note(title = title, description = description))
                        title = ""
                        description = ""
                        Toast.makeText(context, "Note Added",
                            Toast.LENGTH_SHORT ).show()
                    }
                })
        }

        Divider(modifier = Modifier.padding(10.dp))

        LazyColumn{
            items(notes){note ->
                NoteRow(note = note, onNoteClicked = {
                    onRemoveNote(it)
                })
            }
        }

        
    }
}


@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note:Note,
    onNoteClicked: (Note) -> Unit){


    Surface(
        modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(topEnd = 33.dp, bottomStart = 33.dp))
            .fillMaxWidth(),
        color = Color.DarkGray,
        elevation = 6.dp
    ) {
        Column(modifier = Modifier
            .clickable {
                onNoteClicked(note)
            }
            .padding(horizontal = 14.dp, vertical = 6.dp)) {

            Text(text = note.title,
                style = MaterialTheme.typography.subtitle2
            )
            Text(text = note.description,
                style = MaterialTheme.typography.subtitle1
            )
            Text(text = note.entryDate.format(DateTimeFormatter.ofPattern("EEE, d MMM")),
                style = MaterialTheme.typography.caption
            )
            // Jul 18, Wed
            
        }

    }

}





@ExperimentalComposeUiApi
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun NoteScreenPreview(){
    NoteScreen(notes = NotesDataSource().loadNotes(), onAddNote = {}, onRemoveNote = {})
}
