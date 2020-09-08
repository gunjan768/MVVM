package com.example.mvmodelview;

// When we rotate our android device, the activity on the screen gets destroyed and recreated which means that the state of all it's member variable
// are lost. ViewModel on the other side stays alive and keeps it's data and new activity just retrieve the same ViewModel instance and can
// immediately access the same variable. But why the activity gets destroyed when we rotate the phone ? Because when you change the screen orientation
// of a device, it is considered as a configuration change because you basically provide the complete different screen. Another example when the
// activity is recreated is when you change the language of the device. So you can't just avoid configuration change by locking the screen orientation
// as there are many causes for configuration change.

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// AndroidViewModel is a subclass of ViewModel. Difference b/t twos is that in AndroidViewModel we pass Application to the constructor which we can whenever
// the application context is needed. We have to pass the context to Repository because we needed there to instantiate our database instance. Application
// class is a base class of Android app containing components like Activities and Services. Application or its sub classes are instantiated before all the
// activities or any other application objects have been created in Android app. You don't have to import or extend application class, they are predefined.
public class NoteModelView extends AndroidViewModel
{
    private static final String TAG = "NoteModelView";

    private NoteRepository repository;
    private LiveData<List<Note>>allNotes;

    public NoteModelView(@NonNull Application application)
    {
        super(application);

        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note)
    {
        repository.insert(note);
    }

    public void update(Note note)
    {
        repository.update(note);
    }

    public void delete(Note note)
    {
        repository.delete(note);
    }

    public void deleteAllNotes()
    {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        // Log.d(TAG, "getAllNotes: ggggggggggggggggggggggggggggggggggggggggggggg");

        return allNotes;
    }
}