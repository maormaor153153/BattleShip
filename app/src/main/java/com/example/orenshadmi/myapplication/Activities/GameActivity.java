package com.example.orenshadmi.myapplication.Activities;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;
import com.example.orenshadmi.myapplication.Classes.Coordinate;
import com.example.orenshadmi.myapplication.Logic.GameLogicNew;
import com.example.orenshadmi.myapplication.R;
import com.example.orenshadmi.myapplication.Views.GridButton;
import com.example.orenshadmi.myapplication.Classes.Board;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    boolean flagPlayer;
    boolean flagForComputer;
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
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
                gridButton.setBackgroundColor(0xff000000);
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
    public void onClick(View v) {
        TextView turn = findViewById(R.id.turn);
        turn.setText("It's your turn");
        int flagDestroyed;
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            flagPlayer = gameLogic.attckComputerBoard(gridButton.getPositionX(), gridButton.getPositionY());
            if (gameLogic.wasMissPlayer(gridButton.getPositionX(), gridButton.getPositionY()) == true) {

            } else if (flagPlayer == true) {
                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                    }
                }).start();
                Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
                gridButton.setBackgroundDrawable(occupied);
                flagDestroyed = gameLogic.destroyedShipByPlayer(gridButton.getPositionX(), gridButton.getPositionY());
                if (flagDestroyed >= 0) {
                    Context context = getApplicationContext();
                    CharSequence text = "Your destroyed ship";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                WinPlayer();
                turn.setText("It's Computer turn");
                forDelayComputerDisable();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        forDelayComputerEnabled();
                        computerAttack();

                    }

                }, 2000);
            } else if (gameLogic.wasAttackedComputer(gridButton.getPositionX(), gridButton.getPositionY()) == false) {
                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                    }
                }).start();
                gameLogic.updateMissPlayer(gridButton.getPositionX(), gridButton.getPositionY());
                gridButton.setBackgroundColor(0xff0000ff);
                turn.setText("It's Computer turn");
                forDelayComputerDisable();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forDelayComputerEnabled();
                        computerAttack();

                    }
                }, 2000);
            }

        }
    }
    @SuppressLint("NewApi")
    public void computerAttack() {
        TextView turn = findViewById(R.id.turn);
        boolean flagforexit = false;
       do {
           int randomIndex;
           int flagDestroyed ;
           randomIndex = foundLevelGame();
           View gridButtoN;
           GridLayout gridLayoutattck = findViewById(R.id.player_layout);
           gridButtoN = gridLayoutattck.getChildAt(randomIndex);
           final GridButton gridButton = (GridButton) gridButtoN;
           flagForComputer = gameLogic.attackPlayerBoard(gridButton.getPositionX(), gridButton.getPositionY());
           if (gameLogic.wasMissComputer(gridButton.getPositionX(), gridButton.getPositionY()) == true) {
           } else if (flagForComputer == true) {
               gridButtoN.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                   @Override
                   public void run() {
                       gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                   }
               }).start();
               Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
               gridButton.setBackgroundDrawable(occupied);

               flagDestroyed = gameLogic.destroyedShipByComputer(gridButton.getPositionX(), gridButton.getPositionY());
               if (flagDestroyed >= 0) {
                   Context context = getApplicationContext();
                   CharSequence text = "Your ship destroyed by computer ";
                   int duration = Toast.LENGTH_SHORT;
                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               }
               WinComputer();
               flagforexit = true;
           } else if (gameLogic.wasAttackedPlayer(gridButton.getPositionX(), gridButton.getPositionY()) == false) {
               gridButtoN.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                   @Override
                   public void run() {
                       gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                   }
               }).start();
               gameLogic.updateMissComputer(gridButton.getPositionX(), gridButton.getPositionY());
               gridButton.setBackgroundColor(0xff0000ff);
               flagforexit = true;
           }
       }
        while(flagforexit != true);
        turn.setText("It's your turn");
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
    public int foundChildLevelGame()
    {
        if(gameLevel ==EASY)
        {
            return 24;
        }
        if(gameLevel ==MEDIUM)
        {
            return 48;
        }
        if(gameLevel ==HARD)
        {
            return 99;
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
            Intent intent = new Intent(GameActivity.this, resultActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        }
    }
    private void WinComputer() {
        boolean flag;
        flag = gameLogic.winComputerLog();
        String status = "You Lose";
        if (flag == true) {
            Intent intent = new Intent(GameActivity.this, resultActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        }
    }
    public void forDelayComputerDisable()
    {
        GridLayout computerLayout = (GridLayout) findViewById(R.id.computer_layout);
        for(int i = 0 ; i <foundChildLevelGame();i++)
        {
            computerLayout.getChildAt(i).setEnabled(false);
        }
    }
    public void forDelayComputerEnabled()
    {
        GridLayout computerLayout =  findViewById(R.id.computer_layout);
        for(int i = 0 ; i <foundChildLevelGame();i++)
        {
            computerLayout.getChildAt(i).setEnabled(true);
        }
    }

}




