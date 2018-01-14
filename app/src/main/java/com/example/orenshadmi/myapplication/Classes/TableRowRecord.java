package com.example.orenshadmi.myapplication.Classes;

import android.content.Context;
import android.util.AttributeSet;

import com.example.orenshadmi.myapplication.Fragments.TableFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by orenshadmi on 11/01/2018.
 */

public class TableRowRecord extends android.widget.TableRow{

    private int id;
    private String name;
    private int score;
    private double latitude;
    private double longitude;




    public TableRowRecord(Context context,int id, String name, int score, double longitude, double latitude) {
        super(context);
        this.id = id;
        this.name = name;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public String toString() {
        return "TableRowRecord{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}



