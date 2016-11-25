package com.thinkdifferent.noteapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thinkdifferent.noteapp.R;
import com.thinkdifferent.noteapp.adapter.NotesAdapter;
import com.thinkdifferent.noteapp.model.Note;
import com.thinkdifferent.noteapp.model.NotesDatabase;
import com.thinkdifferent.noteapp.util.Utility;

import java.util.List;

/**
 * activity to show notes that user saved in app.
 */
public class NotesActivity extends AppCompatActivity {

    //GUI References.
    private RecyclerView notesRecyclerView;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initializeViews();
    }

    /**
     * find and initialize views from layout.
     */
    private void initializeViews() {
        notesRecyclerView = (RecyclerView) findViewById(R.id.rv_notes);
        notes = new NotesDatabase(this).getAllNotes();
        if (notes.isEmpty()) {
            showNoNotesLayout();
        } else {
            int columnsCount = Utility.getIntValueFromPrefs(this, "columns_count", 1);
            showNotes(columnsCount);
        }

        FloatingActionButton addNoteButton = (FloatingActionButton) findViewById(R.id.fab_add_note);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new note.
                startActivityForResult(new Intent(NotesActivity.this, AddUpdateNoteActivity.class), 100);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_list) {
            // show items in list.
            showNotes(1);
            Utility.saveIntValueInPrefs(this, "columns_count", 1);
        } else {
            // show items in grid.
            showNotes(2);
            Utility.saveIntValueInPrefs(this, "columns_count", 2);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * show no notes layout view to user.
     */
    private void showNoNotesLayout() {
        findViewById(R.id.layout_no_notes).setVisibility(View.VISIBLE);
    }

    /**
     * hide no notes view from user.
     */
    private void hideNoNotesLayout() {
        findViewById(R.id.layout_no_notes).setVisibility(View.GONE);
    }

    /**
     * show notes in recycler view .
     */
    private void showNotes(int columnsCount) {
        notesRecyclerView.setLayoutManager(new GridLayoutManager(this, columnsCount));
        notesRecyclerView.setAdapter(new NotesAdapter(this, notes));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            notes = new NotesDatabase(this).getAllNotes();
            hideNoNotesLayout();
            int columnsCount = Utility.getIntValueFromPrefs(this, "columns_count", 1);
            showNotes(columnsCount);
        }
    }
}
