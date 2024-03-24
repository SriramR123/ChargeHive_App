package com.citsri.chargify;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.citsri.chargify.api.body.EVStation;

public class EVstationFullDetails extends AppCompatActivity {

    EditText rateByCustomer,feedback;
    TextView evName,evAddress,evStats;

    Spinner statusList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evstation_full_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        EVStation model = (EVStation) getIntent().getSerializableExtra("evDetails");
        System.out.println(model);


        rateByCustomer = findViewById(R.id.rate_by_customer);
        feedback = findViewById(R.id.feedback);
        evName = findViewById(R.id.ev_station_name);
        evAddress = findViewById(R.id.ev_station_loc);
        evStats = findViewById(R.id.ev_station_status);
        statusList = findViewById(R.id.status_list);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.status_list));
        statusList.setAdapter(arrayAdapter);

        if(model!=null) {
            evAddress.setText(model.getSvStationAddress());
            evName.setText(model.getEvStationName());
            evStats.setText(model.getStatus());
        }





    }
}