package com.example.mvmodelview;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.List;

public class NoteRepository
{
    private static final String TAG = "NoteRepository";

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    // Application is a subclass of context, we can use it as a context to create our NoteDatabase instance.
    public NoteRepository(Application application)
    {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    // Room will automatically executes the database operation ( i.e getAllNotes method ) that turns the LiveData on the background thread but for all
    // operations we have to execute the code on the background thread ourSelf because Room doesn't allow the operation on the main UI thread.

    public void insert(Note note)
    {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note)
    {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note)
    {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes()
    {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }


    public LiveData<List<Note>> getAllNotes()
    {
        // Log.d(TAG, "getAllNotes: hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

        return allNotes;
    }

    // It has to be static so that it doesn't have reference to the repository itself otherwise this could cause a memory leak.
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes)
        {
            noteDao.insert(notes[0]);

            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes)
        {
            noteDao.update(notes[0]);

            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes)
        {
            noteDao.delete(notes[0]);

            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            noteDao.deleteAllNotes();

            return null;
        }
    }
}