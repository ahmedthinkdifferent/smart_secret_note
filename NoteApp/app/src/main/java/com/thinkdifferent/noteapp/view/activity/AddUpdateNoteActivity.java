package com.thinkdifferent.noteapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.thinkdifferent.noteapp.R;
import com.thinkdifferent.noteapp.adapter.NotesTypesAdapter;
import com.thinkdifferent.noteapp.model.Note;
import com.thinkdifferent.noteapp.model.NotesDatabase;
import com.thinkdifferent.noteapp.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * activity to add new note or update current note.
 */
public class AddUpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    //GUI References.
    private View selectedColorView;
    private EditText noteTitleEditText, noteSubjectEditText;
    Spinner noteTypesSpinner;
    //Variables.
    private boolean updateNote;
    private String selectedColor;
    private int noteType;
    //Objects.
    Note note;
    private ArrayList<String> colors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_note);
        selectedColorView = findViewById(R.id.view_color);
        updateNote = getIntent().getBooleanExtra("update_note", false);
        if (updateNote) {
            note = getIntent().getParcelableExtra("note");
        }
        noteTitleEditText = (EditText) findViewById(R.id.et_note_title);
        noteSubjectEditText = (EditText) findViewById(R.id.et_note_subject);
        noteTypesSpinner = (Spinner) findViewById(R.id.spn_note_types);
        colors = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.colors)));
        noteTypesSpinner.setAdapter(new NotesTypesAdapter(this));
        if (note != null) {
            noteTitleEditText.setText(note.title);
            noteSubjectEditText.setText(note.subject);
            noteType = note.type;
            noteTypesSpinner.setSelection(noteType);
            selectedColor = note.backgroundColor;
            selectedColorView.setBackgroundColor(Color.parseColor(selectedColor));
        } else {
            noteType = 0;
        }
        Button changeColorButton = (Button) findViewById(R.id.btn_change_color);
        changeColorButton.setOnClickListener(this);
        Button saveNoteButton = (Button) findViewById(R.id.btn_save_note);
        saveNoteButton.setOnClickListener(this);
    }


    /**
     * show color picker dialog to user to choose note background color.
     */
    private void showColorChooserDialog() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setColors(R.array.typed_colors);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                // put code
                selectedColor = colors.get(position);
                selectedColorView.setBackgroundColor(Color.parseColor(selectedColor));
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_change_color) {
            // change note background color.
            showColorChooserDialog();
        } else {
            // save note
            String noteTitle = noteTitleEditText.getText().toString();
            String noteSubject = noteSubjectEditText.getText().toString();
            if (noteTitle.equals("")) {
                showInputLayoutError(R.id.til_note_title, getString(R.string.empty_field));
            } else if (noteSubject.equals("")) {
                showInputLayoutError(R.id.til_note_subject, getString(R.string.empty_field));
            } else if (selectedColor == null) {
                Utility.showToastMessage(this, R.string.choose_color);
            } else {
                if (updateNote) {
                    // update note.
                    note.title = noteTitle;
                    note.subject = noteSubject;
                    note.lastUpdate = getCurrentTime();
                    note.backgroundColor = selectedColor;
                    boolean updateNote = new NotesDatabase(this).updateNote(note);
                    if (updateNote) {
                        // update note successfully.
                        Utility.showToastMessage(this, R.string.update_note_successfully);
                        finishActivity();
                    } else {
                        Utility.showToastMessage(this, R.string.cannot_update_note);
                    }
                } else {
                    // add new note.
                    Note note = new Note(noteTitle, noteSubject, noteType, getCurrentTime(), selectedColor);
                    boolean addNote = new NotesDatabase(this).addNote(note);
                    if (addNote) {
                        Utility.showToastMessage(this, R.string.add_note_successfully);
                        finishActivity();
                    } else {
                        Utility.showToastMessage(this, R.string.cannot_add_note);
                    }
                }
            }
        }
    }


    /**
     * finish current activity.
     */
    private void finishActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    /**
     * show error input layout to user.
     *
     * @param id           error layout id.
     * @param errorMessage error message to show to user.
     */
    private void showInputLayoutError(int id, String errorMessage) {
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(id);
        textInputLayout.setError(errorMessage);
    }

    /**
     * get current time to set it to last update to note.
     *
     * @return current time.
     */
    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }
}
