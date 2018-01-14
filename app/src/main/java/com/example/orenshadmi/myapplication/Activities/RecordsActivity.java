package com.example.orenshadmi.myapplication.Activities;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.orenshadmi.myapplication.Classes.TableRowRecord;
import com.example.orenshadmi.myapplication.DB.DatabaseHelper;
import com.example.orenshadmi.myapplication.Fragments.TableFragment;
import com.example.orenshadmi.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RecordsActivity extends AppCompatActivity implements TableFragment.onLocationSetListener , LocationListener{

    DatabaseHelper myDb;
    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    TableFragment tableFragment;
    Button btn;
    GoogleMap mGoogleMap;
    private TableRowRecord[] mLocations;
    private  MarkerOptions[] options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Sansaul_Petronika.ttf");
        TextView tx = findViewById(R.id.recordsTitle);
        tx.setTypeface(custom_font);
        tx.setTextColor(Color.BLACK);


        this.tableFragment = (TableFragment) getSupportFragmentManager().findFragmentById(R.id.table_fragment);

        if(googleServicesAvailable()){
            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            MapFragment mapFragment = MapFragment.newInstance();
            transaction.add(R.id.mapsPlaceHolder, mapFragment);
            transaction.commit();



            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    setGoogleMap(googleMap);

                }
            });

        }else {
            FrameLayout mapsPlaceHolder = (FrameLayout) findViewById(R.id.mapsPlaceHolder);
            TextView errorMessageTextView = new TextView(getApplicationContext());
            errorMessageTextView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            errorMessageTextView.setTextColor(Color.RED);
            mapsPlaceHolder.addView(errorMessageTextView);
        }


        myDb = new DatabaseHelper(this);




    }

    private void setOnMarkerListener() {
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();

                String[] split = title.split("\\.");
                String place = split[0];
                marker.showInfoWindow();
                tableFragment.tableRowToMark(place);
                 return true;
            }
        });
    }




    @Override
    public void setLocations(TableRowRecord[] locations, int numOfRecords) {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        updateInfo(locations,numOfRecords);


    }

    @Override
    public void zoomToLocation(double lon, double lat) {
        goToLocation(lon,lat,10);
    }





    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }
        else if ( api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0 );
            dialog.show();;
        }
        else {
            Toast.makeText(this, "Cannot connect to google play services", Toast.LENGTH_SHORT).show();
        }
        return false;

    }



    public void setGoogleMap(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }

    public void updateInfo(TableRowRecord[] locations, int numOfRecords){

        mLocations = locations;
        options = new MarkerOptions[numOfRecords];
        Log.d("TAG", "" + numOfRecords);
        mGoogleMap.animateCamera( CameraUpdateFactory.zoomTo( 2.0f ) );
        removeAllMarkers();
        addMarkersToMap(numOfRecords);

    }



    public void addMarkersToMap(int numOfRecords) {
        for(int i = 0 ; i < numOfRecords; i++) {
            options[i] = new MarkerOptions().title(""+mLocations[i].getId()+". " + mLocations[i].getName())
                    .position(new LatLng(mLocations[i].getLongitude(), mLocations[i].getLatitude()))
                    .snippet("Score: " + mLocations[i].getScore());
            mGoogleMap.addMarker(options[i]);
        }

        setOnMarkerListener();
    }

    public void removeAllMarkers(){
        mGoogleMap.clear();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void goToLocation(double lat, double lon, float zoom ) {
        LatLng ll= new LatLng(lat,lon);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }

    private void goToLocation(double lat, double lon) {
        LatLng ll= new LatLng(lat,lon);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);

    }


}
