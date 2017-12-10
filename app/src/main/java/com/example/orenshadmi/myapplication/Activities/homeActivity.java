package com.example.orenshadmi.myapplication.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.orenshadmi.myapplication.Logic.GameLogicNew;
import com.example.orenshadmi.myapplication.R;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    GameLogicNew gameLogic = GameLogicNew.getInstance();
    boolean isLevelPicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.home_activity);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.home_activity);

        Button mainButton = (Button) findViewById(R.id.startgame);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLevelPicked) {
                    startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Please choose game level";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        Button rulesButton = findViewById(R.id.rules);
        rulesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RuleActivity.class));
            }
        });
        TextView tx = findViewById(R.id.battleshiptitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Sansaul_Petronika.ttf");
        tx.setTypeface(custom_font);
        tx.setTextColor(Color.BLACK);

        chooseGameLevel();

    }
    private void chooseGameLevel() {
        LinearLayout linearLayout = findViewById(R.id.levels);
        int size = linearLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            Button button = (Button) linearLayout.getChildAt(i);
            button.setTag((i + 1));
            button.setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View v) {
        if( v instanceof Button){
            this.isLevelPicked = true;
            Button gameLevelButton =(Button) v;
            gameLogic.setGameLevel((Integer)gameLevelButton.getTag());
           // disableAllButtonWithDifferentTag((Integer)gameLevelButton.getTag(), gameLevelButton.getParent());
            setAllButtonThatNotQueuedToShape((Integer)gameLevelButton.getTag(), gameLevelButton.getParent());
            Drawable queued = getResources().getDrawable( R.drawable.white);
            gameLevelButton.setBackgroundDrawable(queued);
        }

    }
    private void setAllButtonThatNotQueuedToShape(int tag, ViewParent parent) {
        if( parent instanceof LinearLayout){
            LinearLayout linearLayout = (LinearLayout)parent;
            int size = linearLayout.getChildCount();
            for( int i = 0 ; i < size ; i++){
                Button gameLevelButton = (Button)linearLayout.getChildAt(i);
                if((Integer)gameLevelButton.getTag() != tag){
                   // gameLevelButton.setEnabled(false);
                    Drawable shape = getResources().getDrawable( R.drawable.shape);
                    	                    gameLevelButton.setBackgroundDrawable(shape);
                }
            }
        }
    }
}
