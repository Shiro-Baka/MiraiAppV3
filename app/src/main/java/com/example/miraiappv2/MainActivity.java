package com.example.miraiappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Set buttons
    private ImageButton buttonMainMenuBubbleMatch;
    private ImageButton buttonMainMenuInfo;
    private ImageButton buttonMainMenuKanaChallenge;
    private ImageButton buttonMainMenuMagicTrouble;
    private ImageButton buttonMainMenuStudy;
    private ImageButton buttonMainMenuVocabConfusion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

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
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.main_menu_info_popup, null);

                TextView myTextView = popupView.findViewById(R.id.menu_info);


                // Create the PopupWindow
                int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // Set an onClickListener to the close button in the popup layout XML file
                Button closeButton = popupView.findViewById(R.id.infobackbtn);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });

                // Determine the size of the magic_popup window based on the device orientation
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    width = getResources().getDisplayMetrics().widthPixels * 90 / 100; // Set the width to 90% of the screen width
                    height = getResources().getDisplayMetrics().heightPixels * 95 / 100; // Set the height to 80% of the screen height
                } else {
                    width = getResources().getDisplayMetrics().widthPixels * 80 / 100; // Set the width to 80% of the screen width
                    height = getResources().getDisplayMetrics().heightPixels * 90 / 100; // Set the height to 90% of the screen height
                }

                //set width and height for the magic_popup window
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            }
        });

        buttonMainMenuKanaChallenge = findViewById(R.id.button_main_menu_kana_challenge);
        buttonMainMenuKanaChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openKanaChallengeActivity();
            }
        });

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
                openStudySection();
            }
        });

        buttonMainMenuVocabConfusion = findViewById(R.id.button_main_menu_vocab_confusion);
        buttonMainMenuVocabConfusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVocabActivity();
            }
        });

    }
    public void openVocabActivity(){
        Intent intent = new Intent(this, VocabularyConfusionSelectionPage.class);
        startActivity(intent);
    }
    public void openKanaChallengeActivity(){
        Intent intent = new Intent(this, KanaChallengeTopicPage.class);
        startActivity(intent);
    }
    public void openStudySection(){
        Intent intent = new Intent(this, TopicMainActivity.class);
        startActivity(intent);
    }
}