package com.example.testmap.viewmodel;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testmap.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<Data>> _users = new MutableLiveData<>();
    private MutableLiveData<User> _me = new MutableLiveData<>();

    public LiveData<List<Data>> users = (LiveData<List<Data>>) _users;
    public LiveData<User> me = (LiveData<User>) _me;

    public void loadData(){
        List<Data> output = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 6, 16, 15, 30);
        output.add(new Data(59.932348F, 30.354024F, "Илья", R.drawable.ic_avatar_1, calendar.getTime().getTime()));
        calendar.set(2023, 6, 16, 16, 40);
        output.add(new Data(59.931491F, 30.355478F, "Никита", R.drawable.ic_avatar_2, calendar.getTime().getTime()));
        calendar.set(2023, 6, 16, 11, 25);
        output.add(new Data(59.930744F, 30.353472F, "Даниил", R.drawable.ic_avatar_3, calendar.getTime().getTime()));
        _users.setValue(output);
    }

    public void loadMe(){
        User output = new User(59.931F, 30.353472F);
        _me.setValue(output);
    }

}