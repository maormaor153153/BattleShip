package com.example.orenshadmi.myapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
        setContentView(R.layout.activity_result);


        Intent intent = getIntent();
        String states = intent.getExtras().getString("status");

        TextView text = (TextView)findViewById(R.id.textView);
        text.setText(states);

        Button mainButton = (Button) findViewById(R.id.again);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(getApplicationContext(), homeActivity.class));

            }
        });
        }

}
