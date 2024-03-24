package com.citsri.chargify;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.citsri.chargify.api.ApiClient;
import com.citsri.chargify.api.ApiInterface;
import com.citsri.chargify.api.body.EVStation;
import com.citsri.chargify.api.body.LatLon;
import com.citsri.chargify.api.body.LatLonWithRoute;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener{

    SupportMapFragment supportMapFragment;

    View mapFragment;

    LottieAnimationView mapLoadAnimView;

    LocationManager locationManager;

    ArrayList<EVstationModel> evStationModels;

    RecyclerView recyclerView;

    EditText searchView;

    Button searchButton;

    GoogleMap map;


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
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        searchView = findViewById(R.id.search_box);
        searchButton = findViewById(R.id.search_but);
        mapLoadAnimView = findViewById(R.id.map_load_animation);
        mapFragment = findViewById(R.id.map);



        searchButton.setOnClickListener(v->{

            if(searchView.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"please provide place name",Toast.LENGTH_LONG).show();
            }else{
                mapFragment.setAlpha((float)0.3);
                mapLoadAnimView.setVisibility(View.VISIBLE);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                String query = searchView.getText().toString();

                LatLon latLon = fetchLatLon();
                Call<LatLonWithRoute> latLonWithRouteCall = apiInterface.getAllEvsNear(query, latLon.getLattitude(), latLon.getLongitude());

                latLonWithRouteCall.enqueue(new Callback<LatLonWithRoute>() {
                    @Override
                    public void onResponse(Call<LatLonWithRoute> call, Response<LatLonWithRoute> response) {
                        Toast.makeText(getApplicationContext(),response.body().getLat()+" "+response.body().getLon(),Toast.LENGTH_LONG).show();
                        locateInMap(new LatLon(response.body().getLat(), response.body().getLon()));
//
//                        LatLon latLon = fetchLatLon();
//                        PolylineOptions polylineOptions = new PolylineOptions();
//                        polylineOptions.add(new LatLng(latLon.getLattitude(),latLon.getLongitude()));
//                        polylineOptions.width(18);
//                        polylineOptions.add(new LatLng(response.body().getLat(),response.body().getLon()));
//                        map.addPolyline(polylineOptions);
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                new LatLng(response.body().getLat(),response.body().getLon()),
                                11,0,0
                        )));
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loc_fuel);
                        for(int i=0;i<response.body().getRoutes().size();i++){

                            EVStation evStation = response.body().getRoutes().get(i);
                            map.addMarker(new MarkerOptions().position(new LatLng(evStation.getLat(),evStation.getLon())).title(evStation.getEvStationName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new EVRecyAdapter(getApplicationContext(),(ArrayList<EVStation>) response.body().getRoutes()));

                        mapFragment.setAlpha(1);
                        mapLoadAnimView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<LatLonWithRoute> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                        mapFragment.setAlpha(1);
                        mapLoadAnimView.setVisibility(View.GONE);
                    }
                });

            }


        });




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_MEDIA_IMAGES});
        }else {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location !=null)
                System.out.println(location.getLatitude()+"   "+location.getLongitude());
            else System.out.println("unable to fetch");

            recyclerView = findViewById(R.id.ev_recy);
            supportMapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.map, supportMapFragment)
                    .commit();



            evStationModels = new ArrayList<>();
            evStationModels.add(new EVstationModel("EV station1", "Electric vehicle charging stationEVP, CARNIVAL CINEMAS, National Highway, Chembarambakkam, Tamil Nadu 600124", "open", 4, 13.040402425548923, 80.0473624151489));
            evStationModels.add(new EVstationModel("EV station2", "Electric vehicle charging stationEVP, CARNIVAL CINEMAS, National Highway, Chembarambakkam, Tamil Nadu 600124", "open", 4.5, 13.04148251862975, 80.11723972615943));
            evStationModels.add(new EVstationModel("EV station3", "Electric vehicle charging stationEVP, CARNIVAL CINEMAS, National Highway, Chembarambakkam, Tamil Nadu 600124", "open", 3.4, 13.048393765593849, 80.134995945529));
            evStationModels.add(new EVstationModel("EV station4", "Electric vehicle charging stationEVP, CARNIVAL CINEMAS, National Highway, Chembarambakkam, Tamil Nadu 600124", "closed", 3.7, 13.071678088850456, 80.18006103497514));
            evStationModels.add(new EVstationModel("EV station5", "Electric vehicle charging stationEVP, CARNIVAL CINEMAS, National Highway, Chembarambakkam, Tamil Nadu 600124", "open", 4, 13.031277039150565, 80.1512472127328));
            evStationModels.add(new EVstationModel("EV station6", "Electric vehicle charging stationEVP, CARNIVAL CINEMAS, National Highway, Chembarambakkam, Tamil Nadu 600124", "closed", 4.5, 13.08077375623537, 80.21221685091335));


            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//            recyclerView.setAdapter(new EVRecyAdapter(getApplicationContext(), evStationModels));

            supportMapFragment.getMapAsync(this);

        }

    }

    private void locateInMap(LatLon latLon){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loc_pic1);
        map.addMarker(new MarkerOptions().position(new LatLng(latLon.getLattitude(),latLon.getLongitude())).title(searchView.getText().toString()));
        map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latLon.getLattitude(),latLon.getLongitude()),10,0,0)));

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"no permission",Toast.LENGTH_LONG).show();
            return;
        }


        this.map = googleMap;

        LocationManager locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager1.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location !=null)
            System.out.println(location.getLatitude()+"   "+location.getLongitude());
        else System.out.println("unable to fetch");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loc_pic1);

        ArrayList<LatLng> latLngs = new ArrayList<>();
        for(int i=0;i<evStationModels.size();i++){

            latLngs.add(new LatLng((float)evStationModels.get(i).getLat(),(float)evStationModels.get(i).getLon()));
//            googleMap.addMarker(new MarkerOptions().position(new LatLng((float)evStationModels.get(i).getLat(),(float)evStationModels.get(i).getLon())).title(evStationModels.get(i).getEvStationName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        }

        if(location!=null){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(13.146480086777569,80.29523594365016)).title("Marker"));
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(13.146480086777569,80.29523594365016),10,0,0)));
        }


        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);


//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.addAll(latLngs);
//        polylineOptions.width(18);
//        googleMap.addPolyline(polylineOptions);

//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(13.040402425548923, 80.0473624151489),10,0,0)));

    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_LONG).show();
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