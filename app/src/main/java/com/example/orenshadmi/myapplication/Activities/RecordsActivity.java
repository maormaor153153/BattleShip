package com.example.orenshadmi.myapplication.Activities;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.orenshadmi.myapplication.DB.DatabaseHelper;
import com.example.orenshadmi.myapplication.R;

public class RecordsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editID;
    EditText editName;
    EditText editSureName;
    EditText editScore;
    Button addData, showData, updateData, deleteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        myDb = new DatabaseHelper(this);
        editID = findViewById(R.id.editID);
        editName = findViewById(R.id.editName);
        editSureName= findViewById(R.id.editSurename);
        editScore = findViewById(R.id.editMark);
        addData = findViewById(R.id.button_add);
        showData = findViewById(R.id.show_data);
        updateData = findViewById(R.id.update_data);
        deleteData = findViewById(R.id.delete);
        addData();
        showData();
        updateData();
        deleteData();
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

    public void showData(){
        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0 ){
                    showMessage("error", "Nothing found");
                    return;
                }
                else{
                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("\n\nId: " + res.getString(0));
                        buffer.append("\nName: " + res.getString(1));
                        buffer.append("\nSurename: " + res.getString(2));
                        buffer.append("\nMark: " + res.getString(3));
                    }

                    showMessage("Data", buffer.toString());
                }
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
