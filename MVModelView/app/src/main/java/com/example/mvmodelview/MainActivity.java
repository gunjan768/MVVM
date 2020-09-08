package com.example.mvmodelview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// We can convert the java class into so called entities which basically each represent table in SQLite database. And we have Data Access Object ( DAO )
// which is used to communicate with the SQLite. ViewModel which is a class which has the job of holding and preparing all data for the UI so we don't
// have to put them directly into our activity or fragment instead activity/fragment connects to ViewModel and get all the necessary data from there
// then activity only has job of drawing it onto the screen. So basically ViewModel is a gateway for the UI controller ( activity/fragment ). ViewModel
// also survives the configuration changes like when you rotate the screen or when you change the textSize ( it will not loose the state and will all
// the states ). ViewModel doesn't care from where the data comes, it just gets the data and do it's job. Hence there it an another class b/w the
// ViwModel and the dataSource ( can be webservice or database ) called Repository ( recommended to use but it's not a part of Architecture component ).
// ViewModel connects with the Repository class and in turns this Repository class talks to the data source ( how data is fetched and from which
// source data is fetched ). Hence Repository creates a clean API for fetching data and modularize our app and gives a ViewModel a single access point.

// Above process comes under MVVM ( Model View ViewModel ) architecture. DataSource is the model, activities/fragments ( or UI in other words ) build
// a View and ViewModel class is obviously a ViewModel. Using MVVM we a nice clean architecture where layers are modular and decoupled from each other
// and every part has a well defined responsibility. ViewModel doesn't know how the data are retrieved from different sources. UI controller
// ( activity or fragment ) doesn't store any data but trust the ViewModel for the same and also doesn't initiate any database operation directly.
// LiveData is a wrapper that can hold can type of data including list and it can be observed by the UI controller which means that whenever the data
// in this LiveData changes, the observer will be automatically notified with the new data and can refresh UI. LiveData is lifCycle aware which means
// it knows that when activity/fragment that observes it is in the background and automatically stops updating it until it comes to the foreground so
// we don't have to manually stop or resume observation in our activity/fragment lifeCycle methods. That is saves from potential bug or memory leak.

public class MainActivity extends AppCompatActivity
{
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2 ;

    private NoteModelView noteModelView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteModelView = new NoteModelView(getApplication());

        initRecyclerView();

        // Only update our activity if it is in background. When the activity is destroyed it will automatically clean up the reference and this may
        // avoid memory leak and crashes. Whenever LiveData changes, we just get back the whole new list without any position information. So we need
        // to compare the old list with new list passed and then calculate at which position(s) changes happen. To do we this we have a class called
        // DiffUtil. Better way to use ListAdapter class ( subclass of RecyclerViewAdapter ) that has already implemented DiffUtils.
        noteModelView.getAllNotes().observe(this, new Observer<List<Note>>()
        {
            @Override
            public void onChanged(List<Note> notes)
            {
                // recyclerViewAdapter.setNotes(notes);

                // As we extend ListAdapter in the RecyclerViewAdapter class, hence used submitList() method.
                recyclerViewAdapter.submitList(notes);
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_button_add_note);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        // For SimpleCallback(), we passed two arguments : 0 for the first ( dragDrop ) one which disable the dragDrop facility, Left and Right
        // for the 2nd one which enables both left and right swipe.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                noteModelView.delete(recyclerViewAdapter.getNoteAt(viewHolder.getAdapterPosition()));

                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(Note note)
            {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);

                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            assert data != null;

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteModelView.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            assert data != null;
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if(id != -1)
            {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);

            // If you forget to write the below line, the update operation will not happen because of the primary key room can't identify this entry.
            note.setId(id);
            
            noteModelView.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.delete_all_notes:
                noteModelView.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}