package com.example.orenshadmi.myapplication.Classes;

import android.content.Context;

/**
 * Created by orenshadmi on 11/01/2018.
 */

public class TableRowRecord extends android.widget.TableRow{



    private long location;


    public TableRowRecord(Context context) {
        super(context);
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }
}



