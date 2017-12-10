package com.example.orenshadmi.myapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.orenshadmi.myapplication.R;

public class resultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        setContentView(R.layout.activity_result);


        Intent intent = getIntent();
        String states = intent.getExtras().getString("status");

        TextView text =  findViewById(R.id.textView);
        text.setText(states);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Sansaul_Petronika.ttf");

        text.setTypeface(custom_font);
        text.setTextColor(Color.BLACK);

        Button playAgainBt = findViewById(R.id.again);
        playAgainBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));

            }
        });


        Button returnToMenuBt = findViewById(R.id.return_to_menu);
        returnToMenuBt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), homeActivity.class));

            }
        });

    }


}
