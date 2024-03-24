package com.citsri.chargify;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {


//    Button nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        nextButton = findViewById(R.id.next_button);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

//        nextButton.setOnClickListener(v->{
//            startActivity(new Intent(this, DetailsActivity.class));
//        });

        new Handler().postDelayed(()->{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        },5000);

    }


}