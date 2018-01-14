package com.example.orenshadmi.myapplication.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orenshadmi.myapplication.DB.DatabaseHelper;
import com.example.orenshadmi.myapplication.Logic.GameLogicNew;
import com.example.orenshadmi.myapplication.R;

public class resultActivity extends AppCompatActivity {

    GameLogicNew gameLogic = GameLogicNew.getInstance();
    Button playAgainBt, returnToMenu;
    TextView brokeARecord;
    StringBuilder brokeARecordStr;
    EditText editText;
    DatabaseHelper myDB;
    private String score;
    private LocationManager locationManager;
    Button bt ;
    private LocationListener listener;
    private String locationCoordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myDB = new DatabaseHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        Intent intent = getIntent();
        String status = intent.getExtras().getString("status");
        brokeARecord = findViewById(R.id.broke_a_record);
        if(gameLogic.isIsPlayerWon() ){
            if(isScoreHighEnough(gameLogic.getPlayerScore())){
                score = "" + gameLogic.getPlayerScore();

                initilaizeRecordStr();

                editText = findViewById(R.id.input);
                editText.getBackground().mutate().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

                customizeText(editText, editText.getText().toString());
                customizeText(brokeARecord, brokeARecordStr.toString());
            }
            else{

                customizeText(brokeARecord, "Your score is:" + gameLogic.getPlayerScore());
                editText = findViewById(R.id.input);
                editText.setVisibility(View.INVISIBLE);
            }

        }
        else
        {
            editText = findViewById(R.id.input);
            editText.setVisibility(View.INVISIBLE);
        }

        playAgainBt = findViewById(R.id.again);
        returnToMenu = findViewById(R.id.return_to_menu);
        final TextView text =  findViewById(R.id.status);




        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationCoordinates = location.getLongitude() + " " + location.getLatitude();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        gameLogic.setNumOfMiss(0);

        configure_button();



        playAgain();
        returnToMenu();
        customizeText(text, status);
    }

    private boolean isScoreHighEnough(double playerScore) {

        Cursor cursor = myDB.getDataByGameLevel("" +gameLogic.getGameLevel());
        cursor.moveToLast();
        if((Double.parseDouble(cursor.getString(2)) < playerScore) || (cursor.getCount() < 10)){
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }




    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.


                locationManager.requestLocationUpdates("gps", 5000, 0, listener);

    }





    private void initilaizeRecordStr() {
        brokeARecordStr = new StringBuilder();
        brokeARecordStr.append("Congartulations! You broke a record!\n");
        brokeARecordStr.append("Your score is: "+ score);
    }

    private void addPlayerToDB(String name, String score, String gameLevel) {

        boolean isInserted = myDB.insertData(name, score, gameLevel, locationCoordinates);

        if(isInserted){
            Toast.makeText(resultActivity.this,"Data Inserted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(resultActivity.this,"Data Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void customizeText(TextView text, String str) {

        text.setText(str);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Sansaul_Petronika.ttf");
        text.setTypeface(custom_font);
        text.setTextColor(Color.BLACK);
        text.setGravity(Gravity.CENTER);

    }


    private void playAgain() {
        
        playAgainBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUserInsertedText = !TextUtils.isDigitsOnly(editText.getText());
                if(isUserInsertedText) {
                    addPlayerToDB(editText.getText().toString(), score, "" + gameLogic.getGameLevel());
                }
                startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));
            }
        });


    }

    private void returnToMenu() {

        returnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUserInsertedText = !TextUtils.isDigitsOnly(editText.getText());
                if(isUserInsertedText) {
                    addPlayerToDB(editText.getText().toString(), score, "" + gameLogic.getGameLevel());
                }
                startActivity(new Intent(getApplicationContext(), homeActivity.class));
            }
        });


    }


}
