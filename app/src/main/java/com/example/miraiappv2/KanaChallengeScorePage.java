package com.example.miraiappv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KanaChallengeScorePage extends AppCompatActivity {

    TextView game_result;
    TextView game_score;
    TextView game_incorrect;
    ImageButton GameMenu;
    ImageButton MainMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the default title bar from the top of the application for screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_kana_challenge_score_page);

        //scores
        game_result = findViewById(R.id.result);
        game_score = findViewById(R.id.total_score);
        game_incorrect = findViewById(R.id.incorrect);


        //return buttons
        GameMenu = findViewById(R.id.GameMenuButton);
        MainMenu = findViewById(R.id.MainMenuButton);


        int correct = getIntent().getIntExtra("correct", 0);
        int wrong = getIntent().getIntExtra("incorrect", 0);
        int score = getIntent().getIntExtra("score", 0);

        //display score
        game_score.setText("" + score);
        game_result.setText("" + correct);
        game_incorrect.setText("" + wrong);

        GameMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GameMenu.isPressed()){
                    GameMenuActivity();
                }
            }
        });
        MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainMenu.isPressed()){
                    MainMenuActivity();
                }
            }
        });
    }

    public void GameMenuActivity(){
        Intent intent = new Intent(this, KanaChallengeTopicPage.class);
        startActivity(intent);
    }
    public void MainMenuActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}