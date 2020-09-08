package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mvvm.adapters.RecyclerViewAdapter;
import com.example.mvvm.models.NicePlace;
import com.example.mvvm.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ProgressBar progressBar;
    private MainActivityViewModel mainActivityViewModel;

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.recycler_view);

        recyclerViewAdapter = new RecyclerViewAdapter(this, mainActivityViewModel.getNicePlaces().getValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mainActivityViewModel.init();

        mainActivityViewModel.getNicePlaces().observe(this, new Observer<List<NicePlace>>()
        {
            @Override
            public void onChanged(List<NicePlace> nicePlaces)
            {
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        mainActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean aBoolean)
            {
                if(aBoolean)
                {
                    showProgressBar();
                }
                else
                {
                    hideProgressBar();

                    recyclerView.smoothScrollToPosition(Objects.requireNonNull(mainActivityViewModel.getNicePlaces().getValue()).size()-1);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainActivityViewModel.addNewValue(new NicePlace("https://i.imgur.com/ZcLLrkY.jpg", "Washington"));
            }
        });

        initRecyclerView();
    }

    private void showProgressBar()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }
}