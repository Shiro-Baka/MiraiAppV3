package com.example.miraiappv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

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

        //confetti
        KonfettiView viewKonfetti = findViewById(R.id.konfettiView);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(AppCompatResources.getDrawable(this,R.drawable.ic_android_confetti), true);
        EmitterConfig emitterConfig = new Emitter(300, TimeUnit.MILLISECONDS).max(300);
        viewKonfetti.start(
                new PartyFactory(emitterConfig)
                        .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE, drawableShape)
                        .spread(360)
                        .position(0.5,0.25,1,1)
                        .sizes(new Size(8,50,10))
                        .timeToLive(10000).fadeOutEnabled(true).build()
        );

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