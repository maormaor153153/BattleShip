package com.example.orenshadmi.myapplication.Activities;
import android.content.Context;
import android.util.Log;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

import android.view.ViewParent;
import android.widget.LinearLayout;
import com.example.orenshadmi.myapplication.Classes.Coordinate;
import android.view.ViewParent;
import android.widget.Toast;
import com.example.orenshadmi.myapplication.Classes.Coordinate;

import com.example.orenshadmi.myapplication.Logic.GameLogicNew;
import com.example.orenshadmi.myapplication.R;
import com.example.orenshadmi.myapplication.Views.GridButton;

import static com.example.orenshadmi.myapplication.R.id;
import static com.example.orenshadmi.myapplication.R.layout;

public class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConfigurationActivity.class.getSimpleName();
    GameLogicNew gameLogic = GameLogicNew.getInstance();

    private int shipSize;
    LinearLayout linearLayout;

    private static final int EASY = 1;
	    private static final  int MEDIUM = 2;
	    private static final int HARD = 3;
    private static final int EASY_GRID_SIZE = 5;
	    private static final  int MEDIUM__GRID_SIZE = 7;
	    private static final int HARD__GRID_SIZE = 10;
	    private static  int gameLevel;
	    private static boolean isAllShipPlaced ;
	    private static int numOfShips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        setContentView(layout.activity_configuration);
       // Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Sansaul_Petronika.ttf");

                TextView tx = findViewById(id.place_your_ships);
        //	        tx.setTypeface(custom_font);
        	        tx.setTextColor(Color.BLACK);

       findViewById(id.play).setOnClickListener(new View.OnClickListener() {

           @Override
            public void onClick(View v) {
               isAllShipPlaced = isAllButtonsDisabled();
               if (isAllShipPlaced) {
                   gameLogic.createComputerBoard();
                   Intent intent = new Intent(ConfigurationActivity.this, GameActivity.class);
                   startActivity(intent);
               } else {
                   Context context = getApplicationContext();
                   CharSequence text = "Please place all ships!";
                   int duration = Toast.LENGTH_SHORT;

                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               }
           }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setGameLevel();
        createBoard();
        this.linearLayout = createShips();

    }

    private void setGameLevel() {
        this.gameLevel = gameLogic.getGameLevel();
        GridLayout gridLayout =  findViewById(id.grid_layout);
        if(gameLevel == EASY){
            gridLayout.setColumnCount(EASY_GRID_SIZE);
            gridLayout.setRowCount(EASY_GRID_SIZE);
            gameLogic.createBoards(EASY_GRID_SIZE);
            this.numOfShips = 2;
        }
        else if(gameLevel == MEDIUM){
            gridLayout.setColumnCount(MEDIUM__GRID_SIZE);
            gridLayout.setRowCount(MEDIUM__GRID_SIZE);
            gameLogic.createBoards(MEDIUM__GRID_SIZE);
            this.numOfShips = 3;
        }
        else if(gameLevel == HARD){
            gridLayout.setColumnCount(HARD__GRID_SIZE);
            gridLayout.setRowCount(HARD__GRID_SIZE);
            gameLogic.createBoards(HARD__GRID_SIZE);
            this.numOfShips = 5;
        }
    }
    private LinearLayout createShips() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int numOfButtons = 5;


        LinearLayout linearLayout = findViewById(id.linear_layout); // TODO:

        int cellSize = screenWidth / numOfShips;

        if (gameLevel == EASY) {
            cellSize -= 220;
        } else if (gameLevel == MEDIUM) {
            cellSize -= 120;
        } else if (gameLevel == HARD) {
            cellSize -= 50;
        }

        for (int i = 0; i < numOfShips; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setText("" + gameLogic.getShipSizeByIndex(i));
            gridButton.setTag((gameLogic.getShipSizeByIndex(i)));
            gridButton.setShip(true);
            gridButton.setOnClickListener(this);
            Drawable border = ContextCompat.getDrawable(this, R.drawable.shape);
            gridButton.setBackgroundDrawable(border);
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            linearLayout.addView(gridButton);
        }
        return linearLayout;

    }

    void createBoard() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;

        GridLayout gridLayout = findViewById(id.grid_layout);
        //Log.d("ScreenWidth", "" + screenWidth);
        int cellSize = screenWidth / gridLayout.getColumnCount();
        cellSize -= 10;

        if (gameLevel == EASY) {
            cellSize -= 50;
        } else if (gameLevel == MEDIUM) {
            cellSize -= 30;
        } else if (gameLevel == HARD) {
            cellSize -= 10;
        }

        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setPositionX(i / gridLayout.getColumnCount());
            gridButton.setPositionY(i % gridLayout.getColumnCount());
            gridButton.setTag(i);
            gridButton.setOnClickListener(this);
            Drawable border = ContextCompat.getDrawable(this, R.drawable.shape);
            gridButton.setBackgroundDrawable(border);
            gridButton.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            gridLayout.addView(gridButton);
            gridLayout.setClickable(false);

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v ) {
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;

            if(gridButton.isShip) {
                LinearLayout linearLayout = (LinearLayout) gridButton.getParent();
                shipSize = (Integer) gridButton.getTag();
                gridButton.setPlaced(true);
                SetAllButtonsThatNotPlacedToEnableShape(linearLayout, gridButton);

                // gameLogic.setShipBySizeButton(gridButton.getTag());
                //disableAllButtonThatNotPlaced(shipSize, linearLayout);
                gameLogic.setSize(shipSize);
                setAllButtonThatNotQueuedToShape(linearLayout);

                Drawable queued = getResources().getDrawable(R.drawable.white);
                gridButton.setBackgroundDrawable(queued);
                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                        Drawable white = getResources().getDrawable(R.drawable.white);
                        gridButton.setBackgroundDrawable(white);
                    }
                }).start();
            }
            else {
                boolean placed =  gameLogic.placeUserShips(gridButton.getPositionX(), gridButton.getPositionY());
                if(placed){
                    v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                            Drawable occupied = getResources().getDrawable(R.drawable.occupied_shape);
                            gridButton.setBackgroundDrawable(occupied);
                        }
                    }).start();
                    disableShipButtonThatPlaced(this.linearLayout);
                   // enableShipButtonThatnotPlaced(this.linearLayout);

                }

                updateBoard(gridButton.getParent());

            }

        }
    }
  /*  private void enableShipButtonThatnotPlaced(LinearLayout linearLayout) {
        for ( int i = 0; i < linearLayout.getChildCount();  i++ ) {
            GridButton button = (GridButton) linearLayout.getChildAt(i);
            if (! button.isPlaced ) {
                button.setEnabled(true);
                Drawable shape = getResources().getDrawable(R.drawable.shape);
                button.setBackgroundDrawable(shape);
            }
        }
    }*/
  private void SetAllButtonsThatNotPlacedToEnableShape(LinearLayout linearLayout, GridButton gridButton) {
      int size = linearLayout.getChildCount();
      for( int i = 0 ; i < size ; i++){
          GridButton shipButton = (GridButton)linearLayout.getChildAt(i);
          if(shipButton.isEnabled() && !(shipButton.equals(gridButton))){
              shipButton.setPlaced(false);
              Drawable shape = getResources().getDrawable( R.drawable.shape);
              shipButton.setBackgroundDrawable(shape);
          }

      }


  }
    private void disableShipButtonThatPlaced(LinearLayout linearLayout) {
        for ( int i = 0; i < linearLayout.getChildCount();  i++ ) {
            GridButton button = (GridButton) linearLayout.getChildAt(i);
            if (button.isPlaced) {
                button.setEnabled(false);
                Drawable shape = getResources().getDrawable(R.drawable.occupied_shape);
                button.setBackgroundDrawable(shape);
              //  button.setTag((Integer)placedTag);
                button.setPlaced(true);

            }
        }
    }
    public boolean isAllButtonsDisabled() {
        LinearLayout linearLayout = findViewById(id.linear_layout);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            GridButton button = (GridButton) linearLayout.getChildAt(i);
            if (button.isEnabled()) {
                return false;
            }
        }
        return true;
    }
    private void updateBoard(ViewParent parent) {
        if(parent instanceof  GridLayout) {
            GridLayout gridLayout = (GridLayout) parent;

            Coordinate[][] board = gameLogic.getPlayerBoard().getMat();

            int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
            for (int i = 0; i < squaresCount; i++) {
                if (board[i / gridLayout.getRowCount()][i % gridLayout.getColumnCount()].isOccupied()) {
                    GridButton button = (GridButton) gridLayout.getChildAt(i);
                    Drawable occupied = ContextCompat.getDrawable(this, R.drawable.occupied_shape);
                    button.setBackgroundDrawable(occupied);

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
                if (board[i / gridLayout.getRowCount()][i % gridLayout.getColumnCount()].isOptional()) {
                    	                    GridButton button = (GridButton) gridLayout.getChildAt(i);
                    	                    Drawable optional = ContextCompat.getDrawable(this, R.drawable.optional);
                    	                    button.setBackgroundDrawable(optional);
                    	                }

            }
        }
    }

    private void setAllButtonThatNotQueuedToShape(View v) {
        if(v instanceof LinearLayout){
            LinearLayout layout = (LinearLayout) v;
            for ( int i = 0; i < layout.getChildCount();  i++ ){
                GridButton button = (GridButton) layout.getChildAt(i);
                if(!button.isPlaced) {
                    Drawable empty = ContextCompat.getDrawable(this, R.drawable.shape);
                    button.setBackgroundDrawable(empty);
                }
            }
        }
    }
}

