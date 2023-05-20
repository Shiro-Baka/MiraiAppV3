package com.example.miraiappv2;

import static java.util.Collections.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MagicTroubleTopicPage extends AppCompatActivity {

    /*Declaring an array of ToggleButton, which will be used to store the topics selected ToggleButtons.
    The length of the array will be determined by the number of ToggleButtons on the xml file*/
    ToggleButton[] toggleButtons;

    //Set image button names
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        //Set content view to show the xml file
        setContentView(R.layout.activity_magic_trouble_topic_page);

        //Declares the gridlayout from the xml file
        GridLayout gridLayout = findViewById(R.id.grid_layout_magic_trouble);

        //Gets a reference to the existing toggle buttons by their IDs in xml
        ToggleButton essentialToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_essential);
        ToggleButton coloursToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_colours);
        ToggleButton numbersToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_numbers);
        ToggleButton adjectivesToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_adjectives);
        ToggleButton animalsToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_animals);
        ToggleButton dwmyToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_dwmy);
        ToggleButton foodDrinkToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_food_drink);
        ToggleButton greetingsToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_greetings);
        ToggleButton generalToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_general);
        ToggleButton schoolToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_school);
        ToggleButton verbsToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_verbs);
        ToggleButton bodyPartsToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_body_parts);
        ToggleButton familyCountingToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_family_counting);
        ToggleButton seasonsWeatherToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_seasons_weather);

        //Add the toggle buttons to the toggleButtons array
        toggleButtons = new ToggleButton[]{
                essentialToggleButton,
                coloursToggleButton,
                numbersToggleButton,
                adjectivesToggleButton,
                animalsToggleButton,
                dwmyToggleButton,
                foodDrinkToggleButton,
                greetingsToggleButton,
                generalToggleButton,
                schoolToggleButton,
                verbsToggleButton,
                bodyPartsToggleButton,
                familyCountingToggleButton,
                seasonsWeatherToggleButton,
        };


        //Use button name to match id in xml file
        buttonBack = findViewById(R.id.button_return_bm);

        //Onclick listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click it will run the ReturnToMain method below
            public void onClick(View view) {
                ReturnToMain();
            }
        });
    }

    //Starts the quiz by passing the values of the toggle buttons to the next page
    public void startQuiz(View view) {
        //Start new intent to go to the game page
        Intent intent = new Intent(this, MagicTroubleGamePage.class);

        //ArrayList of Strings is created to store the selected topics.
        ArrayList<String> selectedTopics = new ArrayList<>();

        //Get string value of "selected_type" from the previous page
        String selectedType = getIntent().getStringExtra("selected_type");

        //For loop is used to iterate through each togglebutton to check if it is selected or not
        for (ToggleButton toggleButton : toggleButtons) {
            /*If the toggle button is selected, its text value is added to the selected topics
            ArrayList(which is stored in the xml file on the togglebutton. Make sure the the topic in questions.json = xml text).*/
            if (toggleButton != null && toggleButton.isChecked()) {
                String selectedTopic = toggleButton.getText().toString();
                selectedTopics.add(selectedTopic);
            }
        }
        //If no topics are selected, message is displayed to prompt the user to select at least one topic. The game will not start until a topic is selected.
        if (selectedTopics.size() == 0) {

            //Start new custom toast
            View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

            //Set the toast message using text view id from xml layout
            TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
            toastTextView.setText("Please select at least one topic");

            //Create and show the toast
            Toast toast = new Toast(getApplicationContext());

            //Toast Duration
            toast.setDuration(Toast.LENGTH_SHORT);

            //Set toast as the layout started above
            toast.setView(toastLayout);

            //Show toast
            toast.show();

        } else {
            //If one or more topics are selected, the selected topics ArrayList is added to the intent (which pushes the selected topics to the game page), and the quiz game page activity is launched
            intent.putStringArrayListExtra("selected_topics", selectedTopics);

            //Push selected_type in the intent
            intent.putExtra("selected_type", selectedType);

            //Start intent
            startActivity(intent);
        }
    }
    //Created a method to return to the main page
    public void ReturnToMain(){
        //Start new intent to go to main menu
        Intent intent = new Intent(this, MainActivity.class);

        //Start intent
        startActivity(intent);
    }
}