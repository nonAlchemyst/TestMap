package com.example.testmap.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.testmap.BottomSheet;
import com.example.testmap.MapAdapter;
import com.example.testmap.MyLocation;
import com.example.testmap.R;
import com.example.testmap.databinding.ActivityMainBinding;
import com.example.testmap.viewmodel.MainViewModel;
import com.example.testmap.viewmodel.User;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.CustomZoomButtonsController;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private MapAdapter adapter;
    private MyLocation myLocation;
    private boolean locationLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    private void setup(){
        viewModel.loadData();
        setupMap();
        tryLoadLocation();
        setupButtons();
    }

    private void tryLoadLocation(){
        switch(myLocation.loadLocation()){
            case PERMISSIONS_NOT_GRANTED: {
                requestPermissions();
                break;
            }
            case GPS_NOT_ENABLED: {
                Toast.makeText(this, "Для работы требуется GPS", Toast.LENGTH_SHORT).show();
                binding.reloadLocationBtn.setVisibility(View.VISIBLE);
                break;
            }
            case NETWORK_NOT_ENABLED: {
                Toast.makeText(this, "Для работы требуется интернет", Toast.LENGTH_SHORT).show();
                break;
            }
            case OK: {
                locationLoaded = true;
                Toast.makeText(this, "Немного подождите", Toast.LENGTH_SHORT).show();
                binding.reloadLocationBtn.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void setupMap(){
        myLocation = new MyLocation(this);
        myLocation.location.observe(this, location -> {
            if(adapter != null){
                adapter.updateMeLocation(
                        new User((float) location.getLatitude(), (float) location.getLongitude())
                );
                adapter.animateToMe();
            }
        });

        Configuration.getInstance().load(
                getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );
        binding.map.getController().setZoom(19D);
        binding.map.setMaxZoomLevel(19D);
        binding.map.setMinZoomLevel(12D);
        binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        binding.map.setMultiTouchControls(true);
        adapter = new MapAdapter(binding.map);
        viewModel.users.observe(this, adapter::updateMap);
        adapter.listener = data -> {
            showBottomSheet(new BottomSheet.Data(data.getImage(), data.getName(), data.getDate()));
        };
    }

    private void setupButtons(){
        binding.plusBtn.setOnClickListener(v -> {
            binding.map.getController().zoomIn();
        });
        binding.minusBtn.setOnClickListener(v -> {
            binding.map.getController().zoomOut();
        });
        binding.focusMeBtn.setOnClickListener(v -> {
            if(locationLoaded){
                adapter.animateToMe();
            }
        });
        binding.nextBtn.setOnClickListener(v -> {
            //I don't know wtf is this
            adapter.animateToUser();
        });
        binding.reloadLocationBtn.setOnClickListener((view) -> {
            tryLoadLocation();
        });
    }

    private void showBottomSheet(BottomSheet.Data data){
        BottomSheet bottomSheetDialog = new BottomSheet(this, data);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet);
        bottomSheetDialog.show();
    }

    private void requestPermissions(){
        String[] permissions = new String[2];
        permissions[0] = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[1] = Manifest.permission.ACCESS_FINE_LOCATION;
        int requestCode = 1;
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tryLoadLocation();
    }
}