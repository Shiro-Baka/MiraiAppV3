package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class BubbleMatchSelectionPage extends AppCompatActivity {

    //Set buttons
    //private ImageButton buttonReturn;
    private ImageButton buttonBubbleMatchSelectionRomaji;
    private ImageButton buttonBubbleMatchSelectionKana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //sets screen
        setContentView(R.layout.activity_bubble_match_selection_page);

        //buttonReturn = findViewById(R.id.button_bubble_match_return);
        //buttonReturn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        finish();
        //    }
        //});

        buttonBubbleMatchSelectionRomaji = findViewById(R.id.button_bubble_match_selection_romaji);
        buttonBubbleMatchSelectionRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BubbleMatchSelectionPage.this, BubbleMatchTopicPage.class);
                startActivity(intent);
            }
        });

        buttonBubbleMatchSelectionKana = findViewById(R.id.button_bubble_match_selection_kana);
        buttonBubbleMatchSelectionKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BubbleMatchSelectionPage.this, BubbleMatchTopicPage.class);
                startActivity(intent);
            }
        });

    }
}