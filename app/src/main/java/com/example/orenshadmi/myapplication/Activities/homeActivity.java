package com.example.orenshadmi.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
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

    private static final String TAG = homeActivity.class.getSimpleName();
    GameLogicNew gameLogic = GameLogicNew.getInstance();
    boolean isLevelPicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Button mainButton = (Button) findViewById(R.id.startgame);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText urlEditText = (EditText) findViewById(R.id.image_url_edit_text);
//                String imageUrl = urlEditText.getText().toString();
//                downloadImage(imageUrl, (ImageView) findViewById(R.id.image_view));
                //  startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));
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


        chooseGameLevel();

    }
    private void chooseGameLevel() {
        LinearLayout linearLayout =  findViewById(R.id.levels);

        int size = linearLayout.getChildCount();

        for ( int i = 0 ; i < size ; i++){
            Button button = (Button)linearLayout.getChildAt(i);
            button.setTag((i+1));

            button.setOnClickListener(this);


        }


    }
    @Override
    public void onClick(View v) {
        if( v instanceof Button){
            this.isLevelPicked = true;
            Button gameLevelButton =(Button) v;
            gameLogic.setGameLevel((Integer)gameLevelButton.getTag());
            disableAllButtonWithDifferentTag((Integer)gameLevelButton.getTag(), gameLevelButton.getParent());
            Drawable queued = getResources().getDrawable( R.drawable.white);
            gameLevelButton.setBackgroundDrawable(queued);
        }

    }

    private void disableAllButtonWithDifferentTag(int tag, ViewParent parent) {
        if( parent instanceof LinearLayout){
            LinearLayout linearLayout = (LinearLayout)parent;
            int size = linearLayout.getChildCount();
            for( int i = 0 ; i < size ; i++){
                Button gameLevelButton = (Button)linearLayout.getChildAt(i);
                if((Integer)gameLevelButton.getTag() != tag){
                    gameLevelButton.setEnabled(false);
                }

            }

        }
    }

}
