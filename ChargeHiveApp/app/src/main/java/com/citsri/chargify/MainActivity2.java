package com.citsri.chargify;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mapbox.common.MapboxOptions;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.StyleManager;
import com.mapbox.maps.extension.style.StyleContract;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.annotation.Annotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapView = findViewById(R.id.map_view);
        MapboxOptions.setAccessToken("pk.eyJ1IjoiY2FiaW5lZXNoIiwiYSI6ImNsdHYycjJpcDFkd3cycXA3NmxpbG9lazMifQ.MYVfLZLYFkbDgpw4FWhRQQ");




        MapboxMap mapboxMap = mapView.getMapboxMap();

        mapboxMap.loadStyle(Style.STANDARD);
//        System.out.println(Objects.requireNonNull(mapboxMap.getStyle()).getStyleImports());

        mapboxMap.setCamera(new CameraOptions.Builder()
                        .center(Point.fromLngLat(13.0827, 80.2707))
                        .zoom(8.0)
                .build());
//        PointAnnotationManager pointAnnotationManager;
//        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
//                .withPoint(Point.fromLngLat(18.06, 59.31));
//        pointAnnotationManager.create(pointAnnotationOptions);

    }
}