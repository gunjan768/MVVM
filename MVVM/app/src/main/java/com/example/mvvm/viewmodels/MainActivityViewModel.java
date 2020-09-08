package com.example.mvvm.viewmodels;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.models.NicePlace;
import com.example.mvvm.repositories.NicePlaceRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel
{
    // MutableLiveData is actually a sub class of live data. MutableLiveData means we can change the object. There is a method called postValue()
    // which will change the value. But in LiveData there is not such method as it can't be changed. You can observe the changes in liveData but
    // you can't change the liveData as a programmer. But liveData can be changed indirectly. MutableLiveData is a LiveData object whose value can be changed.
    // MutableLiveData is a generic class, so you need to specify the type of data that it holds. LiveData and MutableLiveData are the data types used in
    // the MVVM to observe changes in data which is sent to the views or in other words to the activity or fragment.

    // setValue() : Sets the value. If there are active observers, the value will be dispatched to them. This method must be called from the main thread.
    // If you need set a value from a background thread, you can use postValue(Object)
    // postValue() : Posts a task to a main thread to set the given value. If you called this method multiple times before a main thread executed a posted
    // task, only the last value would be dispatched.
    // getValue() : Returns the current value. Note that calling this method on a background thread does not guarantee that the latest value set will
    // be received.

    private MutableLiveData<List<NicePlace>> nicePlaces;

    private NicePlaceRepository nicePlaceRepository;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public void init()
    {
        if(nicePlaces != null)
        {
            return;
        }

        nicePlaceRepository = NicePlaceRepository.getInstance();
        nicePlaces = nicePlaceRepository.getNicePlaces();
    }

    @SuppressLint("StaticFieldLeak")
    public void addNewValue(final NicePlace nicePlace)
    {
        isUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                List<NicePlace> currentPlaces = nicePlaces.getValue();

                assert currentPlaces != null;
                currentPlaces.add(nicePlace);

                nicePlaces.postValue(currentPlaces);
                isUpdating.postValue(false);
            }
        }.execute();
    }

    public LiveData<Boolean> getIsUpdating()
    {
        return isUpdating;
    }

    public LiveData<List<NicePlace>> getNicePlaces()
    {
        return nicePlaces;
    }
}