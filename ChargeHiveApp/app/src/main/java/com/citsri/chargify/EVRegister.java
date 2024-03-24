package com.citsri.chargify;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.citsri.chargify.api.ApiClient;
import com.citsri.chargify.api.ApiInterface;
import com.citsri.chargify.api.body.EVStation;
import com.citsri.chargify.api.body.LatLon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EVRegister extends AppCompatActivity {

    EditText evStatName,evStatAddress;
    TextView lat,lon;

    Spinner spinner;

    Button fetchLocButton,registerEvStatButton;

    private ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            isGranted->{
                isGranted.forEach((y,j)->{
                    if(Boolean.TRUE.equals(isGranted.get(y))){
                        Toast.makeText(this,"permission granted",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(this,"permission denied...",Toast.LENGTH_LONG).show();
                    }
                });


            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evregister);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        evStatName = findViewById(R.id.ev_stat_name);
        evStatAddress = findViewById(R.id.ev_stat_address);
        lat = findViewById(R.id.ev_stat_lat);
        lon = findViewById(R.id.ev_stat_lon);
        spinner = findViewById(R.id.ev_stat_status_spinner);

        fetchLocButton = findViewById(R.id.fetch_loc_but);
        registerEvStatButton = findViewById(R.id.reg_ev_but);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.status_list));
        spinner.setAdapter(arrayAdapter);

        fetchLocButton.setOnClickListener(v->{
            LatLon latLon = fetchLatLon();
            this.lat.setText(String.valueOf(latLon.getLattitude()));
            this.lon.setText(String.valueOf(latLon.getLongitude()));
        });

        final String[] stat = {spinner.getSelectedItem().toString()};

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stat[0] = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        registerEvStatButton.setOnClickListener(v->{

            Toast.makeText(getApplicationContext(), stat[0],Toast.LENGTH_LONG).show();

            if(!evStatAddress.getText().toString().isEmpty() && !evStatName.getText().toString().isEmpty() && !lat.getText().toString().equals("Latitude") && !lon.getText().toString().equals("Longitude")){
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<String> regEvCall = apiInterface.registerEvStation(new EVStation(5,
                        evStatName.getText().toString(),
                        evStatAddress.getText().toString(),
                        (float)3.5,
                        stat[0],
                        Double.parseDouble(lat.getText().toString()),
                        Double.parseDouble(lat.getText().toString())
                        ));

                regEvCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(),response.body(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),"fill all details",Toast.LENGTH_LONG).show();
            }

        });


    }

    private LatLon fetchLatLon() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_MEDIA_IMAGES});
        } else {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                System.out.println(location.getLatitude() + "   " + location.getLongitude());
                return new LatLon(location.getLatitude(),location.getLongitude());
            }
            else System.out.println("unable to fetch");
        }

        return new LatLon(0,0);

    }
}