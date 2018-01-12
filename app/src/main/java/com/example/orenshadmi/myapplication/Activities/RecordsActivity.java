package com.example.orenshadmi.myapplication.Activities;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.orenshadmi.myapplication.Classes.TableRowRecord;
import com.example.orenshadmi.myapplication.DB.DatabaseHelper;
import com.example.orenshadmi.myapplication.R;

public class RecordsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TableLayout tableLayout;
    EditText editID;
    EditText editName;
    EditText editSureName;
    EditText editScore;
    Button addData, showData, updateData, deleteData;
    TextView id , fullName ,score ;
    private static final int NUM_OF_TABLE_ROWS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        myDb = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tabel_layout);
        createTableRows();
//        editID = findViewById(R.id.editID);
//        editName = findViewById(R.id.editName);
//        editSureName= findViewById(R.id.editSurename);
//        editScore = findViewById(R.id.editMark);
//        addData = findViewById(R.id.button_add);
//        showData = findViewById(R.id.show_data);
//        updateData = findViewById(R.id.update_data);
//        deleteData = findViewById(R.id.delete);
//        addData();
        showData();
//        updateData();
//        deleteData();
    }

    public void createTableRows(){
        for (int i = 0; i < NUM_OF_TABLE_ROWS; i++) {

        }
    }

    public void deleteData(){
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedRows = myDb.deleteDate(editID.getText().toString());
                if(deletedRows > 0 ){
                    Toast.makeText(RecordsActivity.this,"Data deleted!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RecordsActivity.this,"Data not deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateData(){
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDb.updateData(editID.getText().toString(), editName.getText().toString(), editSureName.getText().toString(), editScore.getText().toString());
                if(isUpdated){
                    Toast.makeText(RecordsActivity.this,"Data updated!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RecordsActivity.this,"Data not Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addData(){
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(editName.getText().toString(),editSureName.getText().toString(),editScore.getText().toString());

                if(isInserted){
                    Toast.makeText(RecordsActivity.this,"Data Inserted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RecordsActivity.this,"Data Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showData() {
        int rowCount = 1;
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {

            return;
        } else {

            StringBuffer buffer = new StringBuffer();

            while (res.moveToNext()) {
                TableRow row= new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                id = new TextView(this);
                initialzieTextView(id, res.getString(0));

                fullName = new TextView(this);
                initialzieTextView(fullName, res.getString(1) + " " + res.getString(2));

                score = new TextView(this);
                initialzieTextView(score, res.getString(3));

                row.addView(id);
                row.addView(fullName);
                row.addView(score);
                tableLayout.addView(row);

            }


        }
    }

    private void initialzieTextView(TextView textView, String str) {
        textView.setText(str);
        textView.setTextAppearance(this, android.R.style.TextAppearance_Large);
        textView.setTypeface(id.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
    }
}
