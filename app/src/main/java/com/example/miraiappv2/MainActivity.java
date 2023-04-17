package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    //Set buttons
    private ImageButton buttonMainMenuBubbleMatch;
    private ImageButton buttonMainMenuInfo;
    //private ImageButton buttonMainMenuKanaChallenge;
    private ImageButton buttonMainMenuMagicTrouble;
    private ImageButton buttonMainMenuStudy;
    //private ImageButton buttonMainMenuVocabConfusion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //sets screen
        setContentView(R.layout.activity_main);


        buttonMainMenuBubbleMatch = findViewById(R.id.button_main_menu_bubble_match);
        buttonMainMenuBubbleMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BubbleMatchSelectionPage.class);
                startActivity(intent);
            }
        });

        buttonMainMenuInfo = findViewById(R.id.button_main_menu_info);
        buttonMainMenuInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BubbleMatchSelectionPage.class);
                startActivity(intent);
            }
        });

        //buttonMainMenuKanaChallenge = findViewById(R.id.button_main_menu_kana_challenge);
        //buttonMainMenuKanaChallenge.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent(MainActivity.this, KanaChallengeTopicSelectionPage.class);
        //        startActivity(intent);
        //    }
        //});

        buttonMainMenuMagicTrouble = findViewById(R.id.button_main_menu_magic_trouble);
        buttonMainMenuMagicTrouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MagicTroubleSelectionPage.class);
                startActivity(intent);
            }
        });

        buttonMainMenuStudy = findViewById(R.id.button_main_menu_study);
        buttonMainMenuStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, StudyTopicPage.class);
                //startActivity(intent);
            }
        });

        //buttonMainMenuVocabConfusion = findViewById(R.id.button_main_menu_vocab_confusion);
        //buttonMainMenuVocabConfusion.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        //Intent intent = new Intent(MainActivity.this, VocabConfusionSelectionPage.class);
                //startActivity(intent);
        //    }
        //});
    }
}