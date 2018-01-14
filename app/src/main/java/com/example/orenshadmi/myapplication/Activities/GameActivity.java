package com.example.orenshadmi.myapplication.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;


import com.example.orenshadmi.myapplication.Classes.Coordinate;
import com.example.orenshadmi.myapplication.Classes.Ship;
import com.example.orenshadmi.myapplication.Logic.GameLogicNew;
import com.example.orenshadmi.myapplication.Service.MyService;
import com.example.orenshadmi.myapplication.R;
import com.example.orenshadmi.myapplication.Views.GridButton;
import com.example.orenshadmi.myapplication.Classes.Board;

import java.util.Random;

import tyrantgit.explosionfield.ExplosionField;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    boolean flagPlayer;
    boolean flagForComputer;
    private static final int EASY = 1;
    private static final int MEDIUM = 2;
    private static final int HARD = 3;
    private static final int EASY_GRID_SIZE = 5;
    private static final int MEDIUM__GRID_SIZE = 7;
    private static final int HARD__GRID_SIZE = 10;
    private static int gameLevel;
    private static final String Computer_Turn = "Computer turn";
    private static final String Player_Turn = "Your turn";
    float[] sensorSamplingFirst = new float[3]; //X Y Z

    Animation animRotate;
    MyService mService;
    boolean mBound = false;
    GameLogicNew gameLogic = GameLogicNew.getInstance();
    Handler handler = new Handler();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        overridePendingTransition(R.anim.enter, R.anim.exit);



    }

    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGridsByGameLevel();
        createPlayerBoard();
        createBotBoard();
        updateBoard((GridLayout) findViewById(R.id.computer_layout));
     //   getSensorAccelemetorForStart();
    //    checkAlltheTimeForSensor();

    }
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }
    private void setGridsByGameLevel() {
        this.gameLevel = gameLogic.getGameLevel();
        GridLayout playerLayout = findViewById(R.id.player_layout);
        GridLayout computerLayout = findViewById(R.id.computer_layout);

        if (gameLevel == EASY) {
            playerLayout.setColumnCount(EASY_GRID_SIZE);
            playerLayout.setRowCount(EASY_GRID_SIZE);
            computerLayout.setColumnCount(EASY_GRID_SIZE);
            computerLayout.setRowCount(EASY_GRID_SIZE);
        } else if (gameLevel == MEDIUM) {
            playerLayout.setColumnCount(MEDIUM__GRID_SIZE);
            playerLayout.setRowCount(MEDIUM__GRID_SIZE);
            computerLayout.setColumnCount(MEDIUM__GRID_SIZE);
            computerLayout.setRowCount(MEDIUM__GRID_SIZE);
        } else if (gameLevel == HARD) {
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
        GridLayout gridLayout = findViewById(R.id.computer_layout);
        int cellSize = screenWidth / gridLayout.getColumnCount();
        if (gameLevel == EASY) {
            cellSize -= 45;
        } else if (gameLevel == MEDIUM) {
            cellSize -= 35;
        } else if (gameLevel == HARD) {
            cellSize -= 25;
        }
        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setPositionX(i / gridLayout.getColumnCount());
            gridButton.setPositionY(i % gridLayout.getColumnCount());
            Drawable border = ContextCompat.getDrawable(this, R.drawable.shape);
            gridButton.setBackgroundDrawable(border);
            gridButton.setOnClickListener(this);
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            gridLayout.addView(gridButton);
        }
    }
    @SuppressLint("NewApi")
    public void createPlayerBoard() { // This Board in Up Top
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        GridLayout gridLayout = findViewById(R.id.player_layout);
        int cellSize = screenWidth / gridLayout.getColumnCount();
        if (gameLevel == EASY) {
            cellSize -= 110;
        } else if (gameLevel == MEDIUM) {
            cellSize -= 70;
        } else if (gameLevel == HARD) {
            cellSize -= 45;
        }
        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        Coordinate[][] playerBoard;
        playerBoard = gameLogic.getPlayerBoard().getMat();
        Board test = gameLogic.getPlayerBoard();

        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setPositionX(i / gridLayout.getColumnCount());
            gridButton.setPositionY(i % gridLayout.getColumnCount());
            if (playerBoard[i / gridLayout.getColumnCount()][i % gridLayout.getColumnCount()].isOccupied()) {
                Drawable playerShip = ContextCompat.getDrawable(this, R.drawable.playership);
                gridButton.setBackground(playerShip);
            } else {
                Drawable border = ContextCompat.getDrawable(this, R.drawable.shape);
                gridButton.setBackgroundDrawable(border);
            }
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            gridLayout.addView(gridButton);
        }
        gameLogic.printfleet(test);

    }
    @SuppressLint("NewApi")
    @Override
    public void onClick(final View v) {
        TextView turn = findViewById(R.id.turn);
        turn.setText(Player_Turn);
        GridLayout gridLayoutattck = findViewById(R.id.computer_layout);
        int flagDestroyed;
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            flagPlayer = gameLogic.attckComputerBoard(gridButton.getPositionX(), gridButton.getPositionY());
            if (gameLogic.wasMissPlayer(gridButton.getPositionX(), gridButton.getPositionY()) == true) {

            } else if (flagPlayer == true) {
               animateForAttack(gridButton,gridLayoutattck , 0);
                Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
                gridButton.setBackgroundDrawable(occupied);
                flagDestroyed = gameLogic.destroyedShipByPlayer(gridButton.getPositionX(), gridButton.getPositionY());
                if (flagDestroyed >= 0) {
                    destroyedFullShipByPlayer(flagDestroyed, gridButton);
                    toastOfDestroyedShip();
                }
                WinPlayer();
                turn.setText(Computer_Turn);
                forDelayComputerDisable();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forDelayComputerEnabled();
                        computerAttack();
                    }
                }, 1000);
            } else if (gameLogic.wasAttackedComputer(gridButton.getPositionX(), gridButton.getPositionY()) == false) {
                animateForMiss(gridButton, v);
                gameLogic.updateMissPlayer(gridButton.getPositionX(), gridButton.getPositionY());
                gameLogic.incrementNumOfMisses();
                Drawable miss = ContextCompat.getDrawable(this, R.drawable.miss);
                gridButton.setBackground(miss);
                turn.setText(Computer_Turn);
                forDelayComputerDisable();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forDelayComputerEnabled();
                        computerAttack();
                    }
                }, 1000);
            }

        }
    }
    @SuppressLint("NewApi")
    public void computerAttack() {
        TextView turn = findViewById(R.id.turn);
        boolean flagforexit = false;

        do {
            int randomIndex;
            int flagDestroyed;
            randomIndex = foundLevelGame();
            View gridButtoN;
            GridLayout gridLayoutattck = findViewById(R.id.player_layout);
            gridButtoN = gridLayoutattck.getChildAt(randomIndex);
            final GridButton gridButton = (GridButton) gridButtoN;
            flagForComputer = gameLogic.attackPlayerBoard(gridButton.getPositionX(), gridButton.getPositionY());
            if (gameLogic.wasMissComputer(gridButton.getPositionX(), gridButton.getPositionY()) == true) {
            } else if (flagForComputer == true) {
                animateForAttack(gridButton, gridLayoutattck,1);
                Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
                gridButton.setBackgroundDrawable(occupied);
                flagDestroyed = gameLogic.destroyedShipByComputer(gridButton.getPositionX(), gridButton.getPositionY());
                if (flagDestroyed >= 0) {
                    destroyedFullShipByComputer(flagDestroyed, gridButton);
                    toastOfDestroyedShip();
                }
                WinComputer();
                flagforexit = true;
            } else if (gameLogic.wasAttackedPlayer(gridButton.getPositionX(), gridButton.getPositionY()) == false) {
                animateForMiss(gridButton, gridButtoN);
                gameLogic.updateMissComputer(gridButton.getPositionX(), gridButton.getPositionY());
                Drawable miss = ContextCompat.getDrawable(this, R.drawable.miss);
                gridButton.setBackground(miss);
                flagforexit = true;
            }
        }
        while (flagforexit != true);
        turn.setText(Player_Turn);
    }
    private void toastOfDestroyedShip() {
        Context context = getApplicationContext();
        CharSequence text = "Ship destroyed!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @SuppressLint("NewApi")
    private void animateForAttack(final GridButton gridButton,GridLayout gridLayout,int identification) {

        final ExplosionField explosionField = ExplosionField.attach2Window(this);
        int index = gridLayout.indexOfChild(gridButton);
        Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);

        gridButton.setBackgroundDrawable(occupied);
        explosionField.explode(gridButton);

        int X = gridButton.getPositionX();
        int Y = gridButton.getPositionY();
        gridLayout.removeView(gridButton);

        if(identification == 1 ) {
            createButtonForGridPlayer(X, Y,index);
        }else {
          createButtonForGridComputer(X, Y,index);
        }

       /* gridButtoN.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();

            }
        }).start();
        */


    }



    @SuppressLint("NewApi")
    private void animateForMiss(final GridButton gridButton, final View gridButtoN) {
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        gridButton.startAnimation(animRotate);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animRotate.reset();
                animRotate.cancel();
                gridButtoN.clearAnimation();

            }
        }, 1500);
        
    }
    private int foundLevelGame() {
        Random rand = new Random();
        if (gameLevel == EASY) {
            return rand.nextInt(24) + 0;
        }
        if (gameLevel == MEDIUM) {
            return rand.nextInt(48) + 0;
        }
        if (gameLevel == HARD) {
            return rand.nextInt(99) + 0;
        }
        return 0;
    }

    public int foundChildLevelGame() {
        if (gameLevel == EASY) {
            return 24;
        }
        if (gameLevel == MEDIUM) {
            return 48;
        }
        if (gameLevel == HARD) {
            return 99;
        }
        return 0;
    }

    private void updateBoard(ViewParent parent) {
        if (parent instanceof GridLayout) {
            GridLayout gridLayout = (GridLayout) parent;

            Coordinate[][] board = gameLogic.getComputerBoard().getMat();


            int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
            for (int i = 0; i < squaresCount; i++) {
                if (board[i / gridLayout.getRowCount()][i % gridLayout.getColumnCount()].isOccupied()) {
                    GridButton button = (GridButton) gridLayout.getChildAt(i);


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
        boolean flag;
        flag = gameLogic.winPlayerLog();
        String status = "You Win";

        if (flag == true) {
            gameLogic.calculateTotalScore();
            Log.d("Score", "" + gameLogic.getPlayerScore());
            Intent intent = new Intent(GameActivity.this, resultActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.rotate);

        }
    }

    private void WinComputer() {
        boolean flag;
        flag = gameLogic.winComputerLog();
        String score = null;
        String status = "You Lose";
        if (flag == true) {
            Intent intent = new Intent(GameActivity.this, resultActivity.class);
            intent.putExtra("status", status);
            intent.putExtra("score", score);
            startActivity(intent);

        }
    }

    private boolean WinComputerSensor() {
        boolean flag;
        flag = gameLogic.winComputerLog();
        String status = "You Lose";

        if (flag == true) {
            Intent intent = new Intent(GameActivity.this, resultActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        }
        if (flag)
        {
            return true;
        }

        return false;

    }

    public void forDelayComputerDisable() {
        GridLayout computerLayout = (GridLayout) findViewById(R.id.computer_layout);
        for (int i = 0; i < foundChildLevelGame(); i++) {
            computerLayout.getChildAt(i).setEnabled(false);
        }
    }

    public void forDelayComputerEnabled() {
        GridLayout computerLayout = findViewById(R.id.computer_layout);
        for (int i = 0; i < foundChildLevelGame(); i++) {
            computerLayout.getChildAt(i).setEnabled(true);
        }
    }

    @SuppressLint("NewApi")
    public void destroyedFullShipByPlayer(int flagDestroyed, GridButton gridButton) {
        Context context = getApplicationContext();
        CharSequence text = "Your destroyed ship";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        for (int i = 0; i < gameLogic.getComputerBoard().getFleet().get(flagDestroyed).getShipCoordinates().size(); i++) {
            int number;
            Drawable destroyed = getResources().getDrawable(R.drawable.destroyed);
            number = gridButton.findChildByCoordtion(gameLogic.getComputerBoard().getFleet().get(flagDestroyed).getShipCoordinates().get(i).getPositionX(),
                    gameLogic.getComputerBoard().getFleet().get(flagDestroyed).getShipCoordinates().get(i).getPositionY(),
                    gameLogic.gamelevelForGridButoon(gameLevel));
            View gridButtoN;
            GridLayout gridLayoutattck = findViewById(R.id.computer_layout);
            gridButtoN = gridLayoutattck.getChildAt(number);
            gridButtoN.setBackground(destroyed);
        }
    }

    @SuppressLint("NewApi")
    private void destroyedFullShipByComputer(int flagDestroyed, GridButton gridButton) {
        Context context = getApplicationContext();
        CharSequence text = "Ship destroyed!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        for (int i = 0; i < gameLogic.getPlayerBoard().getFleet().get(flagDestroyed).getShipCoordinates().size(); i++) {
            int number;
            Drawable destroyed = getResources().getDrawable(R.drawable.destroyed);
            number = gridButton.findChildByCoordtion(gameLogic.getPlayerBoard().getFleet().get(flagDestroyed).getShipCoordinates().get(i).getPositionX(),
                    gameLogic.getPlayerBoard().getFleet().get(flagDestroyed).getShipCoordinates().get(i).getPositionY(),
                    gameLogic.gamelevelForGridButoon(gameLevel));
            View gridButtoN;
            GridLayout gridLayoutattck = findViewById(R.id.player_layout);
            gridButtoN = gridLayoutattck.getChildAt(number);
            gridButtoN.setBackground(destroyed);
        }


    }

//    private void getSensorAccelemetorForStart() {
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                sensorSamplingFirst[0] = mService.getXaccelemetor();
//                sensorSamplingFirst[1] = mService.getYaccelemetor();
//                sensorSamplingFirst[2] = mService.getZaccelemetor();
//
//                Log.d("sensorSamplingFirst: X", " " + sensorSamplingFirst[0]);
//                Log.d("sensorSamplingFirst: Y", " " + sensorSamplingFirst[1]);
//                Log.d("sensorSamplingFirst: Z", " " + sensorSamplingFirst[2]);
//            }
//        }, 1000);
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkIfSensorCheckingAllTheTime() {
        boolean forCheckWin = false;
        int randomIndex = foundLevelGame();
        View gridButtoN;
        GridLayout gridLayoutattck = findViewById(R.id.player_layout);
        gridButtoN = gridLayoutattck.getChildAt(randomIndex);
        final GridButton gridButton = (GridButton) gridButtoN;
        int flagDestroyed = 0;

        Log.d("fleet", "size" + gameLogic.getPlayerBoard().getFleet().size());
        Log.d("fleet", "state0" + gameLogic.getPlayerBoard().getFleet().get(0).getState().toString());
        Log.d("fleet", "state1" + gameLogic.getPlayerBoard().getFleet().get(1).getState().toString());

        for (int i = 0; i < gameLogic.getPlayerBoard().getFleet().size(); i++) {
            if (gameLogic.getPlayerBoard().getFleet().get(i).getState().toString().equals("placed")) {
                Log.d("flagDestroyed", "i:" + i);
                flagDestroyed = i;
                gameLogic.getPlayerBoard().getFleet().get(i).setState(Ship.configStatus.dead);
                for (int g = 0; g < gameLogic.getPlayerBoard().getFleet().get(i).getLength(); g++) {
                    gameLogic.attackPlayerBoard(gameLogic.getPlayerBoard().getFleet().get(i).getShipCoordinates().get(g).getPositionX()
                            , gameLogic.getPlayerBoard().getFleet().get(i).getShipCoordinates().get(g).getPositionY());
                }
                break;
            }
        }
        for (int i = 0; i < gameLogic.getPlayerBoard().getFleet().get(flagDestroyed).getShipCoordinates().size(); i++) {
            int number;
            Drawable destroyed = getResources().getDrawable(R.drawable.destroyed);
            number = gridButton.findChildByCoordtion(gameLogic.getPlayerBoard().getFleet().get(flagDestroyed).getShipCoordinates().get(i).getPositionX(),
                    gameLogic.getPlayerBoard().getFleet().get(flagDestroyed).getShipCoordinates().get(i).getPositionY(),
                    gameLogic.gamelevelForGridButoon(gameLevel));
            gridButtoN = gridLayoutattck.getChildAt(number);
            gridButtoN.setBackground(destroyed);
        }


       forCheckWin = WinComputerSensor();
        return forCheckWin;
    }

    private boolean conditionForSensor() {
        boolean stateForAttackBySensor = false;
        if (sensorSamplingFirst[0] + 5 < mService.getXaccelemetor() || sensorSamplingFirst[0] - 5 > mService.getXaccelemetor()) {
            stateForAttackBySensor = true;
        }
        if (sensorSamplingFirst[1] + 5 < mService.getYaccelemetor() || sensorSamplingFirst[1] - 5 > mService.getYaccelemetor()) {
            stateForAttackBySensor = true;
        }
        if (sensorSamplingFirst[2] + 5 < mService.getZaccelemetor() || sensorSamplingFirst[2] - 5 > mService.getZaccelemetor()) {
            stateForAttackBySensor = true;
        }

        return stateForAttackBySensor;
    }

    private void checkAlltheTimeForSensor() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if (conditionForSensor() != true)
                    handler.postDelayed(this, 2000);// here is the loop all the time

                else {
                    boolean check;
                   check = checkIfSensorCheckingAllTheTime();
                    if(check == true)
                    {

                    }
                    else
                    {
                        handler.postDelayed(this, 2000);// here is the loop all the time

                    }
                }
            }
        }, 2000);


    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createButtonForGridPlayer(int positionX, int positionY, int index) {
    Log.d("before", "positionX" + positionX);
    Log.d("before", "positionY" + positionY);


    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int screenWidth = size.x;
    GridLayout gridLayout = findViewById(R.id.player_layout);
    int cellSize = screenWidth / gridLayout.getColumnCount();
    if (gameLevel == EASY) {
        cellSize -= 110;
    } else if (gameLevel == MEDIUM) {
        cellSize -= 70;
    } else if (gameLevel == HARD) {
        cellSize -= 45;
    }
    Coordinate[][] playerBoard;
    playerBoard = gameLogic.getPlayerBoard().getMat();
    Board test = gameLogic.getPlayerBoard();

    GridButton gridButton = new GridButton(this);
    gridButton.setPositionX(positionX);
    gridButton.setPositionY(positionY);

    if (playerBoard[positionX][positionY].isOccupied()) {
        Drawable playerShip = ContextCompat.getDrawable(this, R.drawable.playership);
        gridButton.setBackground(playerShip);
    } else {
        Drawable border = ContextCompat.getDrawable(this, R.drawable.occupied_shape);
        gridButton.setBackgroundDrawable(border);
    }

   gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
    gridLayout.addView(gridButton,index);

    gameLogic.printfleet(test);

    Log.d("afterPlayer", "positionX" + gridButton.getPositionX());
    Log.d("afterPlayer", "positionY" + gridButton.getPositionY());
}
    private void createButtonForGridComputer(int positionX, int positionY,int index) {



        Log.d("beforeComputer", "positionX" + positionX);
        Log.d("beforeComputer", "positionY" +positionY);



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        GridLayout gridLayout = findViewById(R.id.computer_layout);
        int cellSize = screenWidth / gridLayout.getColumnCount();
        if (gameLevel == EASY) {
            cellSize -= 45;
        } else if (gameLevel == MEDIUM) {
            cellSize -= 35;
        } else if (gameLevel == HARD) {
            cellSize -= 25;
        }
            GridButton gridButton = new GridButton(this);
            gridButton.setPositionX(positionX);
            gridButton.setPositionY(positionY);
            Drawable border = ContextCompat.getDrawable(this, R.drawable.occupied_shape);
           gridButton.setBackgroundDrawable(border);
            gridButton.setOnClickListener(this);
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            gridLayout.addView(gridButton,index);


        Log.d("afterComputer", "positionX" + gridButton.getPositionX());
        Log.d("afterComputer", "positionY" + gridButton.getPositionY());


    }




}





