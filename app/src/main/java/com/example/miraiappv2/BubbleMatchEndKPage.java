package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class BubbleMatchEndKPage extends AppCompatActivity {


    TextView tv_result1, tv_result2, tv_score;
    ImageButton buttonRestart, buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bubble_match_end_k_page);

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