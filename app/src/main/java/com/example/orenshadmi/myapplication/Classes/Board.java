package com.example.orenshadmi.myapplication.Classes;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by orenshadmi on 26/11/2017.
 */

public class Board {

    private static  int ROWS ;
    private static  int COLS;




    private Coordinate mat[][];
    private Coordinate matComputer[][];
    ArrayList<Ship> fleet;
    Ship s;

    public Board(int size) {

        createEmptyMat(size);

        this.fleet = new ArrayList<>();
    }



    public void updateCellToOccupied(int positionX, int positionY){
        this.mat[positionX][positionY].setState(Coordinate.status.Occupied);

    }
    public void updateCellToOccupiedComputer(int positionX, int positionY)
    {
        this.matComputer[positionX][positionY].setState(Coordinate.status.Occupied);
    }
    public boolean updateCellToAttack(Board board ,int positionX, int positionY)
    {
        boolean flag = false;
        if(board.mat[positionX][positionY].isOccupied()) {
            board.mat[positionX][positionY].setState(Coordinate.status.Attacked);
           flag = true;
           Log.d("stateCheck",board.mat[positionX][positionY].toString());
       }
        return flag;
    }
    public boolean winner(Board board)
    {
        boolean flag = true;
        for ( int i = 0 ; i < board.mat.length ; i++){
            for ( int j = 0 ; j < board.mat[0].length; j++)
            {
                   if(board.mat[i][j].isOccupied()== true)
               {
                    flag = false;
                    break;
               }
            }
            if(flag == false)
            {
                 break;
            }
        }
        return flag;
    }
    private Coordinate[][] createEmptyMat(int size) {
        this.ROWS = size;
        this.COLS = size;
        this.mat = new Coordinate[ROWS][COLS];
        for ( int i = 0 ; i < mat.length ; i++){
            for ( int j = 0 ; j < mat[0].length; j++){

                mat[i][j] = new Coordinate(i, j);

            }
        }
        return mat;
    }
    private Coordinate[][] createEmptyMatComptuer() {
        this.matComputer = new Coordinate[ROWS][COLS];
        Log.d("@@@@@@@@@@@@@",matComputer.toString());
        for ( int i = 0 ; i < matComputer.length ; i++){
            for ( int j = 0 ; j < matComputer[0].length; j++){

                matComputer[i][j] = new Coordinate(i, j);

            }
        }
        return matComputer;
    }


    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLS() {
        return COLS;
    }

    public Coordinate[][] getMat() {
        return mat;
    }

    public Coordinate[][] getMatComputer() {
        return matComputer;
    }


    public ArrayList<Ship> getFleet() {
        return fleet;
    }

    public void addShipToFleet(Ship ship){
        ship.setState(Ship.configStatus.queued);
        this.fleet.add(ship);
    }

    public Ship getQueuedShip(){
        for ( int i = 0 ; i <fleet.size() ; i++){
            if(fleet.get(i).state == Ship.configStatus.queued) {
                return fleet.get(i);

            }
        }

        return  null;
    }

    public static void setROWS(int ROWS) {
        Board.ROWS = ROWS;
    }

    public static void setCOLS(int COLS) {
        Board.COLS = COLS;
    }


    @Override
    public String toString() {
        String matString = "";
        for (int i = 0; i < ROWS ; i++) {
            for (int j = 0; j < COLS ; j++) {
                matString += mat[i][j].toString() +" ";
            }
            matString += "\n";
        }
        return matString;
    }

    public boolean wasAttack(Board board,int positionX, int positionY)
    {
        boolean flag = false;
        if(board.mat[positionX][positionY].isAttacked() == true) {
            flag = true;
        }
        return flag;
    }



    public boolean wasMiss(Board board, int positionX, int positionY) {

        boolean flag = false;
        if(board.mat[positionX][positionY].isMiss() == true) {
            flag = true;
        }
        return flag;

    }

    public void updateMiss(Board computerBoard, int positionX, int positionY) {
        //boolean flag = false;

      //  if(computerBoard.mat[positionX][positionY].isOccupied()) {
            computerBoard.mat[positionX][positionY].setState(Coordinate.status.Miss);
         //   flag = true;
       // }
      //  return flag;
    }

    public boolean IfAvailable(Board Board, int positionX, int positionY) {

        boolean flag = false;
        if(Board.mat[positionX][positionY].isAvailable() == true) {
            flag = true;
        }
        return flag;
    }

    public boolean IfOccupied(Board Board, int positionX, int positionY) {
        boolean flag = false;
        if(Board.mat[positionX][positionY].isOccupied() == true) {
            flag = true;
        }
        return flag;
    }


}
