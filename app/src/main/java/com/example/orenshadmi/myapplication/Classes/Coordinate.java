package com.example.orenshadmi.myapplication.Classes;

/**
 * Created by orenshadmi on 27/11/2017.
 */

public class Coordinate {

    public enum status {Empty,Optional, Occupied,Available, Attacked, Miss}
    private int positionX;
    private int positionY;
    private status state;

    public Coordinate(int x, int y) {
        this.positionX = setPositionX(x);
        this.positionY = setPositionY(y);
        this.state=status.Empty;
    }
    public Coordinate(int x, int y,status state){
        this.positionX = setPositionX(x);
        this.positionY = setPositionY(y);
        this.state = state.Occupied;
    }
    public void setState(status state) {
        this.state = state;
    }

     public String getState(){
        return this.state.name();
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int setPositionX(int positionX) {
        this.positionX = positionX;
        return positionX;
    }

    public int setPositionY(int positionY) {
        return this.positionY = positionY;
    }

    public boolean isOccupied() {
        if (this.state != status.Occupied) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAttacked(){
        if(this.state != status.Attacked){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean isMiss(){

        if(this.state != status.Miss){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean isAvailable() {
        if (this.state == status.Available) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        if (this.state == status.Empty) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isOptional() {
        if (this.state == status.Optional) {
            return true;
        }

        return false;
    }
    @Override
    public String toString() {
        return "\t" +state.toString() +"\t [" + positionX +" , " + positionY +"]" ;
    }
}

