package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.material.card.MaterialCardView;

public class BubbleMatchTopicPage extends AppCompatActivity {

    private ImageButton button_bubble_match_begin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //set pages
        setContentView(R.layout.activity_bubble_match_topic_page);

        //button with onclick listener
        button_bubble_match_begin = findViewById(R.id.button_bubble_match_begin);
        button_bubble_match_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BubbleMatchTopicPage.this, BubbleMatchGamePage.class);
                startActivity(intent);
            }
        });



    }
}