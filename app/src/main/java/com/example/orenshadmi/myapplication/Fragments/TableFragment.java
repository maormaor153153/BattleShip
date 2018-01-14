package com.example.orenshadmi.myapplication.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orenshadmi.myapplication.Activities.RecordsActivity;
import com.example.orenshadmi.myapplication.Classes.TableRowRecord;
import com.example.orenshadmi.myapplication.DB.DatabaseHelper;
import com.example.orenshadmi.myapplication.R;

/**
 * Created by orenshadmi on 12/01/2018.
 */

public class TableFragment extends Fragment {
    private static final int ARRAY_SIZE = 10;
    DatabaseHelper myDb;
    TableLayout tableLayout;
    TextView place, fullName ,score ;
    View tableView;
    private TableRowRecord[] records;
    private RadioGroup radioGroup;
    onLocationSetListener onLocationSetListener;
    private double latitude;
    private double longitude;
    private int numOfRecords;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tableView = inflater.inflate(R.layout.table_fragment, container, false);
        myDb = new DatabaseHelper(tableView.getContext());
        records = new TableRowRecord[ARRAY_SIZE];
        tableLayout = tableView.findViewById(R.id.table_layout);

        radioGroup = tableView.findViewById(R.id.radio_group);
        RadioButton radioButton = tableView.findViewById(R.id.EASY);
        radioButton.setChecked(true);

        showDataByGameLevel(radioButton.getTag().toString());
        showTableByGameLevel();

        Bundle bundle = getArguments();





        // Inflate the layout for this fragment
        return tableView;
    }

    public void tableRowToMark(String place) {
        if (place != null) {
            final TableRow row = (TableRow) tableLayout.getChildAt(Integer.parseInt(place));
            unMarkOtherRows(place);
            row.setBackgroundColor(Color.RED);

        }
    }

    private void unMarkOtherRows(String place) {
        int size = tableLayout.getChildCount();
        for(int i = 0 ; i < size; i++){
            final TableRow row = (TableRow) tableLayout.getChildAt(i);
            if(!row.getChildAt(0).equals(place)){
                row.setBackgroundColor(Color.WHITE);
            }
        }
    }


    private void setOnTableRowListener() {
        int size = tableLayout.getChildCount();
        for (int i = 0 ; i < size ; i++) {
            final TableRow row= (TableRow) tableLayout.getChildAt(i);
            row.setClickable(true);


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (row.getChildAt(0).getTag() != null) {
                        unMarkOtherRows((String)row.getChildAt(0).getTag());
                        row.setBackgroundColor(Color.YELLOW);

                        Log.d("Location", "" + row.getChildAt(0).getTag());
                        double[] latLng = changeStringToDouble(row.getChildAt(0).getTag());
                        onLocationSetListener.zoomToLocation(latLng[0], latLng[1]);
                    }
                    else{
                        Toast.makeText(tableView.getContext(), "Player didn't add location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private double[] changeStringToDouble(Object tag) {
        String locationCoordinates = (String)tag;
        String[] splited =  locationCoordinates.split("\\s+");


        double [] latLng = {Double.parseDouble(splited[0]),Double.parseDouble(splited[1])};

        return latLng;
    }


    private void showTableByGameLevel() {
        int size = radioGroup.getChildCount();
        for(int i = 0 ; i < size ; i++){
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  tableLayout.removeAllViewsInLayout();
                    tableLayout = tableView.findViewById(R.id.table_layout);
                    showDataByGameLevel(v.getTag().toString());
                    onLocationSetListener.setLocations(records, numOfRecords);
                }
            });

        }
    }


    private void initializeTable() {
        TableRow row= new TableRow(tableView.getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView place = new TextView(tableView.getContext());
        initializeText(place,"#");
        TextView name= new TextView(tableView.getContext());
        initializeText(name,"Name");
        TextView score = new TextView(tableView.getContext());
        initializeText(score,"Score");

        row.addView(place);
        row.addView(name);
        row.addView(score);

        tableLayout.addView(row);

    }




    private void initializeText(TextView textView, String str) {
        textView.setText(str);
        textView.setTextAppearance(tableView.getContext(), android.R.style.TextAppearance_Medium);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
    }

    public void showDataByGameLevel(String gameLevel) {

        int position = 1;
       Cursor cursor = myDb.getDataByGameLevel(gameLevel);

       initializeRecords();


        initializeTable();
        while (cursor.moveToNext()) {
            TableRow row= new TableRow(tableView.getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            place = new TextView(tableView.getContext());
            initializeText(place, ""+position);


            fullName = new TextView(tableView.getContext());
            String nameStr =cursor.getString(1);
            initializeText(fullName,nameStr );

            score = new TextView(tableView.getContext());
            String scoreStr =cursor.getString(2);
            initializeText(score, scoreStr);

            String locationCoordinate = cursor.getString(4);


            if(locationCoordinate != null) {
                place.setTag(locationCoordinate);
                changeStringToDouble(locationCoordinate);
            }

            records[position - 1] = new TableRowRecord(tableView.getContext(),position,nameStr,Integer.parseInt(scoreStr),longitude,latitude );

            row.addView(place);
            row.addView(fullName);
            row.addView(score);
            tableLayout.addView(row);
            position++;

        }
        setOnTableRowListener();


        numOfRecords = position - 1 ;

    }

    private void initializeRecords() {
        for (TableRowRecord row: records)
        {
          row = null;

        }
    }

    private void changeStringToDouble(String locationCoordinate) {
        String[] splited = locationCoordinate.split("\\s+");
        this.longitude = Double.parseDouble(splited[0]);
        this.latitude = Double.parseDouble(splited[1]);


    }






    public interface onLocationSetListener
    {
        public void setLocations(TableRowRecord[] locations, int numOfRecords);

        public void zoomToLocation(double lon,double lat);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onLocationSetListener = (onLocationSetListener) context;
        } catch (Exception e){}
    }
}