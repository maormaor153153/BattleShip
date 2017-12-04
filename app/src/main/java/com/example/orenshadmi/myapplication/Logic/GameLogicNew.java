package com.example.orenshadmi.myapplication.Logic;

import android.util.Log;

import com.example.orenshadmi.myapplication.Classes.Board;
import java.util.Random;
import com.example.orenshadmi.myapplication.Classes.Coordinate;
import com.example.orenshadmi.myapplication.Classes.Ship;
import java.util.ArrayList;

/**
 * Created by orenshadmi on 27/11/2017.
 */

public class GameLogicNew {


    private static boolean isFirstClick = true;
    private static Coordinate firstClick;
    private static Coordinate secondClick;
    private static int gameLevel;
    private static Board playerBoard;
    private static Board computerBoard;
    private static final int[] SIZE_OF_SHIPS = new int[]{2, 3, 3, 4, 5};

    private static final GameLogicNew ourInstance = new GameLogicNew();

    public static GameLogicNew getInstance() {
        return ourInstance;
    }


    private GameLogicNew() {
        // this.playerBoard = new Board(); //Maor
        //   this.computerBoard = new Board(); Maor
        //this.computerBoard = createComputerBoard();//Maor
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
                    setAvailableToEmpty(computerBoard);
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


    private static final String TAG = GameLogicNew.class.getSimpleName();


    public static boolean placeShip(int positionX, int positionY) {
        Log.d(TAG, "onPlayerClicked: " + positionX + ", " + positionY);
        Ship ship = playerBoard.getQueuedShip();
        if (ship != null) {


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

                    paintOptions(playerBoard, ship.getLength(), positionX, positionY);
                    isFirstClick = false;
                    playerBoard.getMat()[positionX][positionY].setState(Coordinate.status.Occupied);
                }


                if (!isFirstClick) {
                    if (isAvailable(positionX, positionY)) {
                        secondClick = new Coordinate(positionX, positionY);
                        ArrayList<Coordinate> positions = setOccupiedFromStartToEnd(playerBoard, firstClick, secondClick);

                        ship.setShipCoordinates(positions);
                        ship.setState(Ship.configStatus.placed);
                        setAvailableToEmpty(playerBoard);
                        isFirstClick = true;
                        return true;

                    }
                }
            }

        }
        return false;
    }


    private static void setAvailableToEmpty(Board board) {
        for (int i = 0; i < board.getROWS(); i++) {
            for (int j = 0; j < board.getCOLS(); j++) {
                if ((board.getMat()[i][j].isAvailable())) {
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

    public static Board getPlayerBoard() {
        return playerBoard;
    }


    public Board getComputerBoard() {
        return computerBoard;
    }

    public boolean attckComputerBoard(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.updateCellToAttack(computerBoard, positionX, positionY);
        Log.d(TAG, "onPlayerClicked: " + positionX + ", " + positionY);
        return flag;
    }

    public boolean attackPlayerBoard(int positionX, int positionY) {
        boolean flag;
        flag = playerBoard.updateCellToAttack(playerBoard, positionX, positionY);
        return flag;

    }

    public boolean WasAttackedComputer(int positionX, int positionY) {
        boolean flag;
        flag = computerBoard.wasAttack(computerBoard, positionX, positionY);
        return flag;
    }
    public boolean WasAttackedPlayer(int positionX, int positionY) {
        boolean flag;
        flag = playerBoard.wasAttack(playerBoard, positionX, positionY);
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

}

