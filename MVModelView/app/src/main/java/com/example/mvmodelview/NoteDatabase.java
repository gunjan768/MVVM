package com.example.mvmodelview;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

// Whenever we make changes to our databases we have to increment the version number and provide an migration strategy.
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase
{
    // We have created this instance to convert our class into Singleton. Singleton means we can't create multiple instances instead we use the same
    // instance everywhere in our app.
    private static NoteDatabase instance;

    // We use this method to access our DAO and we didn't need to provide the body as Room takes care of all the codes. Normally we can't call the
    // abstract methods because they don't have the body. But since we created a NoteDatabase instance below, Room auto generates all the necessary
    // codes for noteDao() method. Hence we can call noteDaa() method.
    public abstract NoteDao noteDao();

    // synchronized means only one thread at a time can access this method. This way you don't accidentally create two instances of this database when two
    // different threads try to access this method at the same time because this can happen in multiThread environment.
    public static synchronized NoteDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            // fallbackToDestructiveMigration() : When we increase the version number of this database we have to tell room how to migrate to the new schema.
            // If you don't do this and increase the version number app will crash and fallbackToDestructiveMigration() will avoid this by deleting all the
            // database and creates new from scratch.
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)      // When our instance is created it will populate our data using callback by triggering onCreate.
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);

            new PopulateDbAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase noteDatabase)
        {
            // In this class we don't have member variable for our NoteDao hence we passed NoteDatabase instance as an argument. This was possible
            // because onCreate was called after database was created.
            noteDao = noteDatabase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            noteDao.insert(new Note("Tile 1", "Description 1", 1));
            noteDao.insert(new Note("Tile 2", "Description 2", 2));
            noteDao.insert(new Note("Tile 3", "Description 3", 3));

            return null;
        }
    }
}