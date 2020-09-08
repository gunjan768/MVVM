package com.example.mvvm.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvm.models.NicePlace;

import java.util.ArrayList;
import java.util.List;

// Singleton pattern.
public class NicePlaceRepository
{
    private static NicePlaceRepository instance;
    private ArrayList<NicePlace> dataSet = new ArrayList<>();

    public static NicePlaceRepository getInstance()
    {
        if(instance == null)
        {
            instance = new NicePlaceRepository();
        }

        return instance;
    }

    // Pretend to get data from a webservice or online source. This is the method where you actually want to make the database queries or access your
    // cache or whatever.
    public MutableLiveData<List<NicePlace>> getNicePlaces()
    {
        setNicePlaces();

        MutableLiveData<List<NicePlace>> data = new MutableLiveData<>();
        data.setValue(dataSet);

        return data;
    }

    private void setNicePlaces()
    {
        dataSet.add(
                new NicePlace("https://i.redd.it/tpsnoz5bzo501.jpg",
                        "Havasu Falls")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/tpsnoz5bzo501.jpg",
                        "Trondheim")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/qn7f9oqu7o501.jpg",
                        "Portugal")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/j6myfqglup501.jpg",
                        "Rocky Mountain National Park")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/0h2gm1ix6p501.jpg",
                        "Havasu Falls")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/k98uzl68eh501.jpg",
                        "Mahahual")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/j6myfqglup501.jpg",
                        "Frozen Lake")
        );
        dataSet.add(
                new NicePlace("https://i.redd.it/obx4zydshg601.jpg",
                        "Austrailia")
        );
    }
}