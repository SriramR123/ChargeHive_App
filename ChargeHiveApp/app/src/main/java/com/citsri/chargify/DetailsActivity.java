package com.citsri.chargify;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class DetailsActivity extends AppCompatActivity {

    CardView evLocateButton,evRegButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        evRegButton = findViewById(R.id.ev_reg_card);
        evLocateButton = findViewById(R.id.ev_locate_card);

        evLocateButton.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), VehiInfoActivity.class)));
        evRegButton.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), EVRegister.class)));
    }



}