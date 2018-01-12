package com.example.orenshadmi.myapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.orenshadmi.myapplication.R;

public class resultActivity extends AppCompatActivity {


    Button playAgainBt, returnToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        Intent intent = getIntent();
        String status = intent.getExtras().getString("status");

        playAgainBt = findViewById(R.id.again);
        returnToMenu = findViewById(R.id.return_to_menu);
        TextView text =  findViewById(R.id.status);
        

        
        playAgain();
        returnToMenu();
        customizeStatus(text, status);




    }

    private void customizeStatus(TextView text,String str) {

        text.setText(str);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Sansaul_Petronika.ttf");
        text.setTypeface(custom_font);
        text.setTextColor(Color.BLACK);
    }



    private void playAgain() {
        
        playAgainBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));
            }
        });


    }

    private void returnToMenu() {

        returnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), homeActivity.class));
            }
        });


    }


}
