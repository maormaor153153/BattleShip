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
    private LocationListener listener;
    private String locationCoordinates;
    private Location currentLocation = null;
    private boolean didAlreadyRequestLocationPermission;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        myDB = new DatabaseHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final TextView txt = findViewById(R.id.location_text);
        didAlreadyRequestLocationPermission = false;



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
                currentLocation = location;

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

//        configure_button();



        playAgain();
        returnToMenu();
        customizeText(text, status);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCurrentLocation();
        locationCoordinates = currentLocation.getLongitude() + " " + currentLocation.getLatitude();
        Log.d("LOCATION:" , locationCoordinates);
    }

    private boolean isScoreHighEnough(double playerScore) {

        Cursor cursor = myDB.getDataByGameLevel("" +gameLogic.getGameLevel());
        cursor.moveToLast();
        if((Double.parseDouble(cursor.getString(2)) < playerScore) || (cursor.getCount() < 10)){
            return true;
        }
        return false;
    }



    private boolean isPermissionForLocationServicesGranted() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (!(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));

    }

    private void getCurrentLocation() {
        if (requestLocationPermissionsIfNeeded(false)) {
            if (currentLocation == null) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }

            float metersToUpdate = 1;
            long intervalMilliseconds = 1000;
            locationManager.requestLocationUpdates("gps", intervalMilliseconds, metersToUpdate, listener);
        }
    }

    private boolean requestLocationPermissionsIfNeeded(boolean byUserAction) {
        boolean isAccessGranted;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
            String coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION;
            isAccessGranted = getApplicationContext().checkSelfPermission(fineLocationPermission) == PackageManager.PERMISSION_GRANTED &&
                    getApplicationContext().checkSelfPermission(coarseLocationPermission) == PackageManager.PERMISSION_GRANTED;
            if (!isAccessGranted) { // The user blocked the location services of THIS app / not yet approved

                if (!didAlreadyRequestLocationPermission || byUserAction) {
                    didAlreadyRequestLocationPermission = true;
                    String[] permissionsToAsk = new String[]{fineLocationPermission, coarseLocationPermission};
                    // IllegalArgumentException: Can only use lower 16 bits for requestCode
                    ActivityCompat.requestPermissions(this, permissionsToAsk, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        } else {
            // Because the user's permissions started only from Android M and on...
            isAccessGranted = true;
        }

        return isAccessGranted;
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
