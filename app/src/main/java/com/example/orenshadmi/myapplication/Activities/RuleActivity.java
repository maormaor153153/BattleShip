package com.example.orenshadmi.myapplication.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.orenshadmi.myapplication.R;




//newnew8
public class RuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        overridePendingTransition(R.anim.enter, R.anim.exit);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Sansaul_Petronika.ttf");
        TextView tx = findViewById(R.id.rules_title);
         tx.setTypeface(custom_font);
        tx.setTextColor(Color.BLACK);
    }
}
