package com.example.miraiappv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BubbleMatchEndPage extends AppCompatActivity {

    TextView tv_result1, tv_result2, tv_score;
    ImageButton buttonRestart, buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_bubble_match_end_page);

        // Retrieve the background image resource identifier from the intent
        int background = getIntent().getIntExtra("background", 0);

        // Set the background image for the root ConstraintLayout
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ConstraintLayout rootLayout = findViewById(R.id.bubble_match_layout);
        rootLayout.setBackgroundResource(background);

        tv_score = findViewById(R.id.score);
        int score = getIntent().getIntExtra("score", 0);
        tv_score.setText("" + score);

        tv_result1 = findViewById(R.id.result1);
        int correct = getIntent().getIntExtra("correct", 0);
        tv_result1.setText("" + correct);

        tv_result2 = findViewById(R.id.result2);
        int wrong = getIntent().getIntExtra("wrong",0);
        tv_result2.setText("" + wrong);


        buttonMenu = findViewById(R.id.button_main_menu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnToMain();
            }
        });
        buttonRestart = findViewById(R.id.button_restart_magic);
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestartBubble();
            }
        });
    }
    public void ReturnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void RestartBubble(){
        Intent intent = new Intent(this, BubbleMatchSelectionPage.class);
        startActivity(intent);
    }
}