package com.example.orenshadmi.myapplication.Logic;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.orenshadmi.myapplication.Activities.GameActivity;
import com.example.orenshadmi.myapplication.Classes.Board;
import java.util.Random;
import com.example.orenshadmi.myapplication.Classes.Coordinate;
import com.example.orenshadmi.myapplication.Classes.Ship;
import java.util.ArrayList;

/**
 * Created by orenshadmi on 27/11/2017.
 */

public class GameLogicNew {

    private static final String TAG = GameLogicNew.class.getSimpleName();
    private static final GameLogicNew ourInstance = new GameLogicNew();

    private static boolean isFirstClick ;
    private static Coordinate firstClick;
    private static Coordinate secondClick;
   // private static int gameLevel;
    private static Board playerBoard;
    private static Board computerBoard;
    private static final int[] SIZE_OF_SHIPS = new int[]{2, 3, 3, 4, 5};
    private static int size;
    private static int gameLevel;


    //private static final GameLogicNew ourInstance = new GameLogicNew();

    public static GameLogicNew getInstance() {
        return ourInstance;
    }


    private GameLogicNew() {
        // this.playerBoard = new Board(); //Maor
        //   this.computerBoard = new Board(); Maor
        //this.computerBoard = createComputerBoard();//Maor
        this.size = 0;
        	        this.isFirstClick = true;
    }
    public static void setSize(int size) {
        	        GameLogicNew.size = size;
        	    }
    public void createBoards(int size) {
        this.playerBoard = new Board(size);
        this.computerBoard = new Board(size);

    }


    public static int getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public Board createComputerBoard() { //TODO: random

        //Board computerBoard= new Board();
        //Random rand = new Random();


        // int positionX = rand.nextInt(computerBoard.getROWS()) + 0;
        // int positionY = rand.nextInt(computerBoard.getCOLS())+ 0;
        //computerBoard.updateCellToOccupied(positionX,positionY);

        //  return computerBoard;


        Board computerBoard = getComputerBoard();
        Random rand = new Random();


        int numOfShips = getNumOfShipsByGamLevel(gameLevel);
        int positionX;
        int positionY;
        boolean isApproved;

        if (numOfShips != -1) {
            for (int i = 0; i < numOfShips; i++) {
                Ship ship = new Ship(getShipSizeByIndex(i));
                do {
                    isApproved = false;
                    positionX = rand.nextInt((computerBoard.getROWS() - 1)) + 0;
                    positionY = rand.nextInt((computerBoard.getCOLS() - 1)) + 0;


                    Log.d("appproved?", " " + !computerBoard.getMat()[positionX][positionY].isOccupied());
                    if (!computerBoard.getMat()[positionX][positionY].isOccupied()) {
                        isApproved = true;
                    }

                } while (!isApproved);

                firstClick = new Coordinate(positionX, positionY);
                Log.d("first click?", " " + firstClick);

                ship.getShipCoordinates().add(firstClick);
                paintOptions(computerBoard, ship.getLength(), positionX, positionY);


                secondClick = getAvailableCor(computerBoard.getMat());
                Log.d("second click?", " " + secondClick);

                if (secondClick != null) {

                    ship.getShipCoordinates().add(secondClick);
                    ArrayList<Coordinate> positions = setOccupiedFromStartToEnd(computerBoard, firstClick, secondClick);

                    ship.setShipCoordinates(positions);
                    ship.setState(Ship.configStatus.placed);
                    addShipToFleet(computerBoard, ship);
                    setEmptyIfNotOccupied(computerBoard);
                }
            }


        }

        Log.d("Mat", computerBoard.toString());

        return computerBoard;


    }

    private Coordinate getAvailableCor(Coordinate[][] mat) {


        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (!mat[i][j].isOccupied()) {
                    if (mat[i][j].isAvailable())
                        return mat[i][j];
                }

            }
        }
        return null;
    }

    private int getNumOfShipsByGamLevel(int gameLevel) {
        if (gameLevel == 1) {
            return 2;
        } else if (gameLevel == 2) {
            return 3;
        } else if (gameLevel == 3) {
            return 5;
        }

        return -1;

    }


   // private static final String TAG = GameLogicNew.class.getSimpleName();


    public static boolean placeUserShips(int positionX, int positionY) {
      //      Log.d(TAG, "onPlayerClicked: " + positionX + ", " + positionY);
       // Ship ship = playerBoard.getQueuedShip();
        //if (ship != null) {
        if(size != 0) {
            Ship ship = new Ship(size);


            if (!(playerBoard.getMat()[positionX][positionY].isOccupied())) {
                Log.d("coordinate", "positionX " + positionX + "positionY" + positionY);

                if (isFirstClick) {
                    firstClick = new Coordinate(positionX, positionY);

                    if (ship.getLength() == 1) {
                        firstClick.setState(Coordinate.status.Occupied);
                        ship.getShipCoordinates().add(firstClick);
                        ship.setState(Ship.configStatus.placed);
                        playerBoard.getMat()[positionX][positionY].setState(Coordinate.status.Occupied);
                        return true;
                    }
                    playerBoard.getMat()[positionX][positionY].setState(Coordinate.status.Optional);
                    paintOptions(playerBoard, ship.getLength(), positionX, positionY);
                    isFirstClick = false;
                   // playerBoard.getMat()[positionX][positionY].setState(Coordinate.status.Occupied);
                }


                if (!isFirstClick) {
                    if (isAvailable(positionX, positionY)) {
                        secondClick = new Coordinate(positionX, positionY);
                        ArrayList<Coordinate> positions = setOccupiedFromStartToEnd(playerBoard, firstClick, secondClick);

                        ship.setShipCoordinates(positions);
                        ship.setState(Ship.configStatus.placed);
                        setEmptyIfNotOccupied(playerBoard);
                        addShipToFleet(playerBoard, ship);

                        isFirstClick = true;
                        size = 0;

                        return true;
                    }else {
                        	                        firstClick = new Coordinate(positionX, positionY);
                        	                        setEmptyIfNotOccupied(playerBoard);
                        	                        playerBoard.getMat()[positionX][positionY].setState(Coordinate.status.Optional);
                        	                        paintOptions(playerBoard, ship.getLength(), positionX, positionY);

                    }
                }
            }

        }
        return false;
    }


    private static void setEmptyIfNotOccupied(Board board) {
        for (int i = 0; i < board.getROWS(); i++) {
            for (int j = 0; j < board.getCOLS(); j++) {
                if (!(board.getMat()[i][j].isOccupied())) {
                    board.getMat()[i][j].setState(Coordinate.status.Empty);
                }
            }
        }

    }


    private static ArrayList<Coordinate> setOccupiedFromStartToEnd(Board board, Coordinate firstClick, Coordinate secondClick) {

        // horizon left
        ArrayList<Coordinate> shipPositions = new ArrayList<>();
        Log.d("positions: ", "first click cor: " + firstClick.getPositionX() + "," + firstClick.getPositionY() + "\n second click cor " + secondClick.getPositionX() + ", " + secondClick.getPositionY());

        if (firstClick.getPositionX() > secondClick.getPositionX()) {
            //TODO

            shipPositions = setShipCoordinates(board, secondClick.getPositionX(), firstClick.getPositionX(), firstClick.getPositionY(), true);
        }

        //horizon right
        if (firstClick.getPositionX() < secondClick.getPositionX()) {
            shipPositions = setShipCoordinates(board, firstClick.getPositionX(), secondClick.getPositionX(), firstClick.getPositionY(), true);
        }

        //vertical top
        if (firstClick.getPositionY() > secondClick.getPositionY()) {
            shipPositions = setShipCoordinates(board, secondClick.getPositionY(), firstClick.getPositionY(), firstClick.getPositionX(), false);
        }

        //vertical bottom
        if (firstClick.getPositionY() < secondClick.getPositionY()) {
            shipPositions = setShipCoordinates(board, firstClick.getPositionY(), secondClick.getPositionY(), firstClick.getPositionX(), false);
        }


        return shipPositions;


    }


    private static ArrayList<Coordinate> setShipCoordinates(Board board, int start, int end, int constant, boolean isVertical) {
        ArrayList<Coordinate> shipPositions = new ArrayList<>();
        if (isVertical) {
            //TODO
            for (int i = start; i <= end; i++) {
                board.getMat()[i][constant].setState(Coordinate.status.Occupied);
                Coordinate cor = new Coordinate(i, constant);
                cor.setState(Coordinate.status.Occupied);
                shipPositions.add(cor);
            }

        } else {
            for (int i = start; i <= end; i++) {
                board.getMat()[constant][i].setState(Coordinate.status.Occupied);
                Coordinate cor = new Coordinate(constant, i);
                cor.setState(Coordinate.status.Occupied);
                shipPositions.add(cor);
            }

        }
        return shipPositions;
    }
    private static void paintOptions(Board boardToPaint, int length, int positionX, int positionY) {

        int optionBottom = positionX + (length - 1);
        int optionTop = positionX - (length - 1);
        int optionLeft = positionY - (length - 1);
        int optionRight = positionY + (length - 1);

        if (optionRight >= 0 && optionRight < boardToPaint.getCOLS()) {
            if (isPossibleToPaint(boardToPaint, optionRight, positionY, positionX, false))
                boardToPaint.getMat()[positionX][positionY + (length - 1)].setState(Coordinate.status.Available);
        }

        if (optionLeft >= 0 && optionLeft < boardToPaint.getCOLS()) {
            if (isPossibleToPaint(boardToPaint, positionY, optionLeft, positionX, false)) {
                boardToPaint.getMat()[positionX][positionY - (length - 1)].setState(Coordinate.status.Available);
            }
        }
        if (optionBottom >= 0 && optionBottom < boardToPaint.getROWS()) {
            if (isPossibleToPaint(boardToPaint, optionBottom, positionX, positionY, true))
                boardToPaint.getMat()[positionX + (length - 1)][positionY].setState(Coordinate.status.Available);
        }
        if (optionTop >= 0 && optionTop < boardToPaint.getROWS()) {
            if (isPossibleToPaint(boardToPaint, positionX, optionTop, positionY, true))
                boardToPaint.getMat()[positionX - (length - 1)][positionY].setState(Coordinate.status.Available);
        }


    }

    private static boolean isPossibleToPaint(Board board, int end, int start, int constant, boolean isVertical) {
        if (isVertical) {
            for (int i = start; i <= end; i++) {
                if (board.getMat()[i][constant].isOccupied()) {
                    return false;
                }
            }
        } else {
            for (int i = start; i <= end; i++) {
                if (board.getMat()[constant][i].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }


    public static int getShipSizeByIndex(int index) {
        return SIZE_OF_SHIPS[index];
    }

    private static boolean isAvailable(int positionX, int positionY) {
        Coordinate[][] mat = playerBoard.getMat();

        if (mat[positionX][positionY].isAvailable()) {
            return true;
        }

        return false;
    }


    public static void placeShipComputerRandom() {
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(9) + 1;
            int y = rand.nextInt(9) + 1;

            computerBoard.updateCellToOccupiedComputer(x, y);
            Log.d(TAG, "onComputerRandom: " + x + ", " + y);
        }
    }

    public static void addShipToFleet(Board board, Ship ship){
        	        board.getFleet().add(ship);
        	    }


        	    public void printfleet(Board board){

        	        for (int i = 0 ; i < board.getFleet().size(); i ++){
            	          Log.d("Fleet: ",board.getFleet().get(i).getShipCoordinates().toString());

            	        }
        	    }

    public static Board getPlayerBoard() {
        return playerBoard;
    }


    public Board getComputerBoard() {
        return computerBoard;
    }

    public boolean attckComputerBoard(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.updateCellToAttack(computerBoard, positionX, positionY);
      //  Log.d(TAG, "onPlayerClicked: " + positionX + ", " + positionY);
        return flag;
    }

    public boolean attackPlayerBoard(int positionX, int positionY) {
        boolean flag;
        flag = playerBoard.updateCellToAttack(playerBoard, positionX, positionY);
        return flag;

    }

    public boolean wasAttackedComputer(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.wasAttack(computerBoard, positionX, positionY);
        return flag;
    }


    public boolean winPlayerLog()
    {
        boolean flag;
        flag = computerBoard.winner(computerBoard);
        return flag;
    }
    public boolean winComputerLog()
    {
        boolean flag;
        flag = playerBoard.winner(playerBoard);
        return flag;
    }


    public void setShipBySizeButton(Object size) {
        Ship ship = new Ship((Integer) size);
        playerBoard.addShipToFleet(ship);
    }

    public boolean wasMissPlayer(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.wasMiss(computerBoard, positionX, positionY);
        return flag;
    }



    public boolean IfAvailablePlayer(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.IfAvailable(computerBoard, positionX, positionY);
        return flag;
    }

    public boolean IfOccupiedPlayer(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.IfOccupied(computerBoard, positionX, positionY);
        return flag;
    }

    public int destroyedShipByPlayer(int positionX, int positionY) {

        int forReturn = -1 ;
        for (int i = 0 ; i < computerBoard.getFleet().size(); i ++) {
            boolean flagForDead = true;


            for (int j = 0; j < computerBoard.getFleet().get(i).getShipCoordinates().size(); j++) {
                if (computerBoard.getFleet().get(i).getShipCoordinates().get(j).getPositionX() == positionX &&
                        computerBoard.getFleet().get(i).getShipCoordinates().get(j).getPositionY() == positionY) {
                        computerBoard.getFleet().get(i).getShipCoordinates().get(j).setState(Coordinate.status.Attacked );
                }
            }
            for (int j = 0; j < computerBoard.getFleet().get(i).getShipCoordinates().size(); j++) {
                        if(!computerBoard.getFleet().get(i).getShipCoordinates().get(j).getState().toString().equals(Coordinate.status.Attacked.toString()))
                        {
                            Log.d("Loop",""+j);
                            flagForDead = false;
                            //break;
                        }
            }
            Log.d("FlagFlag ",""+flagForDead);
            if(flagForDead == true && (!computerBoard.getFleet().get(i).getState().toString().equals(Ship.configStatus.dead.toString())))
            {
                Log.d("flagForDead","true");
                computerBoard.getFleet().get(i).setState(Ship.configStatus.dead);
                forReturn = i;
            }
           // Log.d("FleetComputer: ", computerBoard.getFleet().get(i).getShipCoordinates().toString());
            Log.d("FleetPlayer: ",computerBoard.getFleet().get(i).toString());
        }
        return forReturn;
    }


    public int destroyedShipByComputer(int positionX, int positionY) {

        int forReturn = -1 ;
        for (int i = 0 ; i < playerBoard.getFleet().size(); i ++) {
            boolean flagForDead = true;


            for (int j = 0; j < playerBoard.getFleet().get(i).getShipCoordinates().size(); j++) {
                if (playerBoard.getFleet().get(i).getShipCoordinates().get(j).getPositionX() == positionX &&
                        playerBoard.getFleet().get(i).getShipCoordinates().get(j).getPositionY() == positionY) {
                    playerBoard.getFleet().get(i).getShipCoordinates().get(j).setState(Coordinate.status.Attacked );
                }
            }
            for (int j = 0; j < playerBoard.getFleet().get(i).getShipCoordinates().size(); j++) {
                if(!playerBoard.getFleet().get(i).getShipCoordinates().get(j).getState().toString().equals(Coordinate.status.Attacked.toString()))
                {
                    Log.d("Loop",""+j);
                    flagForDead = false;
                    //break;
                }
            }
            Log.d("FlagFlag ",""+flagForDead);
            if(flagForDead == true && (!playerBoard.getFleet().get(i).getState().toString().equals(Ship.configStatus.dead.toString())))
            {
                Log.d("flagForDead","true");
                playerBoard.getFleet().get(i).setState(Ship.configStatus.dead);
                forReturn = i;
            }
            Log.d("FleetPlayer: ",playerBoard.getFleet().get(i).toString());
        }
        return forReturn;
    }

    public boolean wasMissComputer(int positionX, int positionY) {
        boolean flag;
        flag = playerBoard.wasMiss(playerBoard, positionX, positionY);
        return flag;
    }

    public boolean wasAttackedPlayer(int positionX, int positionY) {
        boolean flag;
        flag = playerBoard.wasAttack(playerBoard, positionX, positionY);
        return flag;
    }

    public void updateMissPlayer(int positionX, int positionY) {
         computerBoard.updateMiss(computerBoard, positionX, positionY);
    }
    public void updateMissComputer(int positionX, int positionY) {
        playerBoard.updateMiss(playerBoard, positionX, positionY);
    }



}

