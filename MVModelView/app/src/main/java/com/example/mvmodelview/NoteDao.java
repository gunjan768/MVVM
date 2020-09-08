package com.example.mvmodelview;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// using Dao annotation Room tells us that this interface is DAO ( Data Access Object ). DAO is used to talk to SQLite.
@Dao
public interface NoteDao
{
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    // We don't have an inbuilt method in SQLite to delete all at once. Hence we will make it using Query annotation.
    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    // Using Room has one important benefit. If you have any type of mistakes like if you have a typo then app will show error during compilation time
    // only. But if used SQLite without room then mistakes will not be visible during compilation but at runtime and your app will crash.

    // Room can turn LiveData out of the box. Now we can observe the changes i.e as soon as there is any change in the table, List will be automatically
    // be updated and our activity will be notified and Room takes care of all the necessary stuffs to update the LiveData object so we don't have to do
    // this by ourSelf. We have to just mentioned the return type of the function as LiveData.
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}