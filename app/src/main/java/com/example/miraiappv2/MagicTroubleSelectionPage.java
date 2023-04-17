package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MagicTroubleSelectionPage extends AppCompatActivity {

    ImageButton buttonBubbleMatchSelectionRomaji;
    ImageButton buttonBubbleMatchSelectionKana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_magic_trouble_selection_page);


        buttonBubbleMatchSelectionRomaji = findViewById(R.id.button_bubble_match_selection_romaji);
        buttonBubbleMatchSelectionRomaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MagicTroubleSelectionPage.this, MagicTroubleTopicPage.class);
                startActivity(intent);
            }
        });

        buttonBubbleMatchSelectionKana = findViewById(R.id.button_bubble_match_selection_kana);
        buttonBubbleMatchSelectionKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MagicTroubleSelectionPage.this, MagicTroubleTopicPage.class);
                startActivity(intent);
            }
        });
    }

}



