package com.example.testmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.testmap.databinding.LayoutBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;

public class BottomSheet extends BottomSheetDialog {

    private LayoutBottomSheetBinding _binding;
    private final Data _data;

    public BottomSheet(@NonNull Context context, Data data) {
        super(context);
        _data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = LayoutBottomSheetBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        setupUI();
    }

    @SuppressLint("SimpleDateFormat")
    private void setupUI(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        _binding.image.setImageResource(_data._image);
        _binding.name.setText(_data._title);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        _binding.date.setText(sdf.format(_data._date));
        sdf = new SimpleDateFormat("HH:mm");
        _binding.time.setText(sdf.format(_data._date));
    }

    public static class Data{

        private int _image;
        private String _title;
        private long _date;

        public Data(int image, String title, long date){
            _image = image;
            _title = title;
            _date = date;
        }

        public int getImage() {
            return _image;
        }

        public String getTitle() {
            return _title;
        }

        public long getDate() {
            return _date;
        }
    }

}
