package com.example.orenshadmi.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.view.ViewParent;


import com.example.orenshadmi.myapplication.Classes.Coordinate;
import com.example.orenshadmi.myapplication.Logic.GameLogicNew;
import com.example.orenshadmi.myapplication.R;
import com.example.orenshadmi.myapplication.Views.GridButton;

import java.util.Random;

import static android.app.PendingIntent.getActivity;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConfigurationActivity.class.getSimpleName();
    boolean stateForComptuerToPlay = false;

    boolean flagPlayer;
    boolean flagForComputer;
    public static final int LENGTH_LONG = 1000;


    boolean flagWinnerPlayer = false;
    boolean flagWinnerComputer= false;

    private static final int EASY = 1;
    private static final  int MEDIUM = 2;
    private static final int HARD = 3;
    private static final int EASY_GRID_SIZE = 5;
    private static final  int MEDIUM__GRID_SIZE = 7;
    private static final int HARD__GRID_SIZE = 10;
    private static int  gameLevel;



    GameLogicNew gameLogic = GameLogicNew.getInstance();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }

    @Override
    protected void onResume() {
        super.onResume();

        setGridsByGameLevel();
        createPlayerBoard();
        createBotBoard();
        updateBoard((GridLayout)findViewById(R.id.computer_layout));

    }
    private void setGridsByGameLevel() {
        this.gameLevel = gameLogic.getGameLevel();


        GridLayout playerLayout =  findViewById(R.id.player_layout);
        GridLayout computerLayout =  findViewById(R.id.computer_layout);

        if(gameLevel == EASY){
            playerLayout.setColumnCount(EASY_GRID_SIZE);
            playerLayout.setRowCount(EASY_GRID_SIZE);
            computerLayout.setColumnCount(EASY_GRID_SIZE);
            computerLayout.setRowCount(EASY_GRID_SIZE);


        }
        else if(gameLevel == MEDIUM){
            playerLayout.setColumnCount(MEDIUM__GRID_SIZE);
            playerLayout.setRowCount(MEDIUM__GRID_SIZE);
            computerLayout.setColumnCount(MEDIUM__GRID_SIZE);
            computerLayout.setRowCount(MEDIUM__GRID_SIZE);

        }
        else if(gameLevel == HARD){
            playerLayout.setColumnCount(HARD__GRID_SIZE);
            playerLayout.setRowCount(HARD__GRID_SIZE);
            computerLayout.setColumnCount(HARD__GRID_SIZE);
            computerLayout.setRowCount(HARD__GRID_SIZE);
        }

    }


    private void createBotBoard() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        GridLayout gridLayout = findViewById(R.id.computer_layout);
        int cellSize = screenWidth / gridLayout.getColumnCount();

       // cellSize -= 25;

        if(gameLevel == EASY){
            cellSize -= 45;
        }
        else if (gameLevel == MEDIUM){
            cellSize -= 35;
        }
        else if ( gameLevel == HARD){
            cellSize -= 25;
        }

        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        Coordinate[][] computerBoard;
        computerBoard = gameLogic.getComputerBoard().getMat();


        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setPositionX(i % gridLayout.getColumnCount());
            gridButton.setPositionY(i / gridLayout.getColumnCount());
            Drawable border = ContextCompat.getDrawable(this, R.drawable.shape);
            gridButton.setBackgroundDrawable(border);
            gridButton.setOnClickListener(this);
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            gridLayout.addView(gridButton);
        }
    }

    public void createPlayerBoard() { // This Board in Up Top
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        GridLayout gridLayout = findViewById(R.id.player_layout);
        int cellSize = screenWidth / gridLayout.getColumnCount();

        //cellSize -= 45;

        if(gameLevel == EASY){
            	            cellSize -= 110;
            	        }
        	        else if (gameLevel == MEDIUM){
            	                cellSize -= 70;
            	            }
        	        else if ( gameLevel == HARD){
            	                cellSize -= 45;
            	            }
        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        Coordinate[][] playerBoard;
        playerBoard = gameLogic.getPlayerBoard().getMat();


        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setPositionX(i / gridLayout.getColumnCount());
            gridButton.setPositionY(i %  gridLayout.getColumnCount());
            if (playerBoard[i / gridLayout.getColumnCount()][i % gridLayout.getColumnCount()].isOccupied()) {
                //Drawable occupiedBorder = ContextCompat.getDrawable(this, R.drawable.occupied_shape);
                //gridButton.setBackgroundDrawable(occupiedBorder);
                gridButton.setBackgroundColor(0xff000000);



            } else {
                Drawable border = ContextCompat.getDrawable(this, R.drawable.shape);
                gridButton.setBackgroundDrawable(border);



            }
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            gridLayout.addView(gridButton);

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            flagPlayer = gameLogic.attckComputerBoard(gridButton.getPositionX(), +gridButton.getPositionY());
            if (gameLogic.wasMissPlayer(gridButton.getPositionX(), gridButton.getPositionY()) == true)
            {

            } else if (flagPlayer == true ) {
                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                    }
                }).start();
                Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
                gridButton.setBackgroundDrawable(occupied);
               // gameLogic.destroyedShipByPlayer();
                WinPlayer();



                computerAttack();



            } else if(gameLogic.wasAttackedComputer(gridButton.getPositionX(), gridButton.getPositionY()) == false){
                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                    }
                }).start();
                gameLogic.updateMissPlayer(gridButton.getPositionX(), gridButton.getPositionY());
                gridButton.setBackgroundColor(0xff0000ff);
                computerAttack();

            }

        }

    }





    @SuppressLint("NewApi")
    public void computerAttack() {
        int randomIndex;
        randomIndex = foundLevelGame();
        View gridButtoN;
        GridLayout gridLayoutattck = findViewById(R.id.player_layout);
        gridButtoN = gridLayoutattck.getChildAt(randomIndex);
        final GridButton gridButton = (GridButton) gridButtoN;

        flagForComputer = gameLogic.attackPlayerBoard(gridButton.getPositionX(), gridButton.getPositionY());
        if (gameLogic.wasMissComputer(gridButton.getPositionX(), gridButton.getPositionY()) == true) {
            computerAttack();
        } else if (flagForComputer == true) {
            gridButtoN.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                }
            }).start();
            Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
            gridButton.setBackgroundDrawable(occupied);
            WinComputer();

        } else if (gameLogic.wasAttackedPlayer(gridButton.getPositionX(), gridButton.getPositionY()) == false) {
            gridButtoN.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                }
            }).start();
            gameLogic.updateMissComputer(gridButton.getPositionX(), gridButton.getPositionY());
            gridButton.setBackgroundColor(0xff0000ff);

        }
    }

    private int foundLevelGame() {
        Random rand = new Random();
        if(gameLevel ==EASY)
        {
            return rand.nextInt(24)+1;
        }
        if(gameLevel ==MEDIUM)
        {
            return rand.nextInt(48)+1;
        }
        if(gameLevel ==HARD)
        {
            return rand.nextInt(99) + 1;
        }
        return 0;
    }


    private void updateBoard(ViewParent parent) {
        if(parent instanceof  GridLayout) {
            GridLayout gridLayout = (GridLayout) parent;

            Coordinate[][] board = gameLogic.getComputerBoard().getMat();

            int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
            for (int i = 0; i < squaresCount; i++) {
                if (board[i / gridLayout.getRowCount()][i % gridLayout.getColumnCount()].isOccupied()) {
                    GridButton button = (GridButton) gridLayout.getChildAt(i);
                   //Drawable occupied = ContextCompat.getDrawable(this, R.drawable.occupied_shape);
                    //button.setBackgroundDrawable(occupied);

                }

                if (board[i / gridLayout.getRowCount()][i % gridLayout.getColumnCount()].isAvailable()) {
                    GridButton button = (GridButton) gridLayout.getChildAt(i);
                    Drawable available = ContextCompat.getDrawable(this, R.drawable.white);
                    button.setBackgroundDrawable(available);
                }

                if (board[i / gridLayout.getRowCount()][i % gridLayout.getColumnCount()].isEmpty()) {
                    GridButton button = (GridButton) gridLayout.getChildAt(i);
                    Drawable empty = ContextCompat.getDrawable(this, R.drawable.shape);
                    button.setBackgroundDrawable(empty);
                }


            }
        }
    }

    private void WinPlayer() {
        boolean flag ;
      flag =  gameLogic.winPlayerLog();

        String status = "You Win";

        if(flag == true)
      {
          Intent intent = new Intent(GameActivity.this,resultActivity.class);
          intent.putExtra("status", status);

          startActivity(intent);
      }
    }
    private void WinComputer() {
        boolean flag ;
        flag =  gameLogic.winComputerLog();

        String status = "You Lose";
        if(flag == true)
        {
            Intent intent = new Intent(GameActivity.this,resultActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        }
    }
}




