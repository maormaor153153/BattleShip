package com.example.orenshadmi.myapplication.Classes;

import java.util.ArrayList;

/**
 * Created by orenshadmi on 26/11/2017.
 */
public class  Ship {
    public enum configStatus {unplaced , queued , placed , dead};
    private int length;
    private ArrayList<Coordinate> shipCoordinates;
    configStatus state;
    boolean horizontal = false;
    boolean vertical = false;

    public Ship(int length) {
        this.length = length;
        this.state = configStatus.unplaced;
        this.shipCoordinates = new ArrayList<>();
    }
    public void setShipCoordinates(int positionX, int positionY) {
        if (this.shipCoordinates.size() < this.length) {
            Coordinate cor = new Coordinate(positionX, positionY);
            cor.setState(Coordinate.status.Occupied);
            this.shipCoordinates.add(cor);
        }
    }
    public void setHorizontal(boolean horizontal) {
        	        this.horizontal = horizontal;
        	    }

    public void setVertical(boolean vertical) {
        	        this.vertical = vertical;
        	    }

    public boolean isHorizontal() {
        	        return horizontal;
        	    }

    public boolean isVertical() {
        	        return vertical;
        	    }

    public void setShipCoordinates(ArrayList<Coordinate> positions) {
        this.shipCoordinates = positions;
    }

    public void setState(configStatus state) {
        this.state = state;
    }

    public String getState(){
        return  this.state.name();
    }

    public int getLength() {
       return this.length;
    }

    public ArrayList<Coordinate> getShipCoordinates() {
        return shipCoordinates;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "length=" + length +
                ", shipCoordinates=" + shipCoordinates +
                ", state=" + state +
                '}';
    }
}
