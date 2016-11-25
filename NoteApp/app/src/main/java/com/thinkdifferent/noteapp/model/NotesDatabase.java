package com.thinkdifferent.noteapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * class for notes database connection and database operations.
 */

public class NotesDatabase extends SQLiteOpenHelper {
    // constants.
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    // Object members.
    private SQLiteDatabase database;

    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create database table.
        String TABLE_QUERY = "create table `notes` (`id` integer primary key autoincrement , " +
                "`title` text,`subject` text ,`type` integer , `lastUpdate` text,`background_color` text)";
        sqLiteDatabase.execSQL(TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * open connection in database.
     */
    private void openDB() {
        database = getWritableDatabase();
    }

    /**
     * close connection in database.
     */
    private void closeDB() {
        if (database != null) {
            database.close();
        }
    }

    /**
     * add new note.
     *
     * @param note note object to save in database.
     * @return true if add new note else false.
     */
    public boolean addNote(Note note) {
        openDB();
        ContentValues row = new ContentValues(5);
        row.put("title", note.title);
        row.put("subject", note.subject);
        row.put("type", note.type);
        row.put("lastUpdate", note.lastUpdate);
        row.put("background_color", note.backgroundColor);
        boolean addNote = database.insert("notes", null, row) > 0;
        closeDB();
        return addNote;
    }

    /**
     * delete note .
     *
     * @param noteID note id to delete note from table with this id.
     * @return true if note deleted else false.
     */
    public boolean deleteNote(int noteID) {
        openDB();
        boolean deleteNote = database.delete("notes", "`id`=?", new String[]{String.valueOf(noteID)}) > 0;
        closeDB();
        return deleteNote;
    }

    /**
     * update not info .
     *
     * @param note note object to update with it's data.
     * @return true if note is updated else false.
     */
    public boolean updateNote(Note note) {
        openDB();
        ContentValues row = new ContentValues(5);
        row.put("title", note.title);
        row.put("subject", note.subject);
        row.put("type", note.type);
        row.put("lastUpdate", note.lastUpdate);
        row.put("background_color", note.backgroundColor);
        boolean updateNote = database.update("notes", row, "`id`=?", new String[]{String.valueOf(note.id)}) > 0;
        closeDB();
        return updateNote;

    }

    /**
     * get all notes.
     *
     * @return list of notes.
     */
    public List<Note> getAllNotes() {
        openDB();
        Cursor cursor = database.rawQuery("select * from `notes`", null);
        List<Note> notes = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(cursor.getInt(0), cursor.getString(1)
                        , cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return notes;
    }

}
