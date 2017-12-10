package com.example.orenshadmi.myapplication.Views;

import android.content.Context;
import android.widget.GridView;

/**
 * Created by orenshadmi on 26/11/2017.
 */

public class GridButton extends android.support.v7.widget.AppCompatButton {
    private int positionX;
    private int positionY;
    public boolean isShip;
    public boolean isPlaced;
    public GridButton(Context context) {
        super(context);
        this.isShip = false;
        this.isPlaced = false;

    }
    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    public boolean isShip() {
        return isShip;
    }
    public void setShip(boolean ship) {
        isShip = ship;
    }
    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }
    public boolean isPlaced() {
        return isPlaced;
    }

}
