package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.os.Handler;
import android.widget.TextView;

import java.util.Random;

public class BubbleMatchGamePage extends AppCompatActivity implements View.OnClickListener {

    private int numberOfElements;
    private BubbleMatchButton[] buttons;
    private int[] buttonGraphicLocations;
    private int[] buttonGraphics;
    private BubbleMatchButton selectedButton1;
    private BubbleMatchButton selectedButton2;
    private boolean isBusy = false;
    private int score = 0;
    private TextView scoreTextView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bubble_match_game_page);

        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid_layout_bubble_match);

        int numCols = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfElements = numCols * numRows;

        buttons = new BubbleMatchButton[numberOfElements];

        buttonGraphics = new int[numberOfElements / 2];

        buttonGraphics[0] = R.drawable.button_1;
        buttonGraphics[1] = R.drawable.button_2;
        buttonGraphics[2] = R.drawable.button_3;
        buttonGraphics[3] = R.drawable.button_4;
        buttonGraphics[4] = R.drawable.button_5;
        buttonGraphics[5] = R.drawable.button_6;
        buttonGraphics[6] = R.drawable.button_7;
        buttonGraphics[7] = R.drawable.button_8;

        buttonGraphicLocations = new int[numberOfElements];

        shuffleButtonGraphics();

        for(int r = 0; r < numRows; r++)
        {
            for(int c = 0; c < numCols; c++)
            {
                BubbleMatchButton tempButton = new BubbleMatchButton(this, r, c, buttonGraphics[buttonGraphicLocations[r * numCols + c] ]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r * numCols + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }
    }
    protected void shuffleButtonGraphics(){
        Random rand = new Random();

        for(int i = 0; i < numberOfElements; i++)
        {
            buttonGraphicLocations[i] = i % (numberOfElements / 2);
        }
        for(int i = 0; i < numberOfElements; i++)
        {
            int temp = buttonGraphicLocations[i];

            int swapIndex = rand.nextInt(16);
            buttonGraphicLocations[i] = buttonGraphicLocations[swapIndex];

            buttonGraphicLocations[swapIndex] = temp;
        }
    }

    @Override
    public void onClick(View view) {

        if(isBusy)
            return;

        BubbleMatchButton button = (BubbleMatchButton) view;

        if(button.isMatched) {
            return;
        }
        if(selectedButton1 == null){
            selectedButton1 = button;
            //selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId() == button.getId()){
            return;
        }

        if(selectedButton1.getFrontDrawableId() == button.getFrontDrawableId())
        {
            selectedButton1.setMatched(true);
            button.setMatched(true);

            GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout_bubble_match);
            gridLayout.removeView(selectedButton1);
            gridLayout.removeView(button);

            //selectedButton1.setVisibility(View.GONE);
            //button.setVisibility(View.GONE);

            selectedButton1 = null;

            score +=100;
            TextView scoreTextView = findViewById(R.id.bubble_match_score_textview);
            scoreTextView.setText("" + score);

            return;
        }
        else
        {
            selectedButton2 = button;
            //selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable(){
                @Override
                public void run()
                {
                    //selectedButton2.flip();
                    //selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                }
            }, 500);


        }
    }

}