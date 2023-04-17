package com.example.miraiappv2;

import static java.util.Collections.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MagicTroubleTopicPage extends AppCompatActivity {

    /*Declaring an array of ToggleButton, which will be used to store the topics selected ToggleButtons.
    The length of the array will be determined by the number of ToggleButtons.*/
    private ToggleButton[] toggleButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //sets the view of the page
        setContentView(R.layout.activity_magic_trouble_topic_page);

        //declares the gridlayout from the xml file
        GridLayout gridLayout = findViewById(R.id.grid_layout_magic_trouble);
        gridLayout.setRowCount(7);


        //Gets a reference to the existing toggle buttons by their IDs in xml
        ToggleButton essentialsToggleButton = findViewById(R.id.toggle_button_magic_trouble_topic_essentials);
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
                essentialsToggleButton,
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

    }

    //Starts the quiz by passing the values of the toggle buttons to the next page
    public void startQuiz(View view) {
        //intent to push to game page
        Intent intent = new Intent(this, MagicTroubleGamePage.class);

        //ArrayList of Strings is created to store the selected topics.
        ArrayList<String> selectedTopics = new ArrayList<>();

        //For loop is used to iterate through each togglebutton to check if it is selected or not.
        for (ToggleButton toggleButton : toggleButtons) {
            /*If the toggle button is selected, its text value is added to the selected topics
            ArrayList(which is stored in the xml file on the togglebutton. Make sure the the topic in questions.json = xml text).*/
            if (toggleButton != null && toggleButton.isChecked()) {
                selectedTopics.add(toggleButton.getText().toString());
            }
        }
        //If no topics are selected, message is displayed to prompt the user to select at least one topic. The game will not start until a topic is selected.
        if (selectedTopics.size() == 0) {
            Toast.makeText(this, "Please select at least one topic", Toast.LENGTH_SHORT).show();
        } else {
            /*If one or more topics are selected, the selected topics ArrayList is added to the intent (which pushes the selected topics to the game page),
            and the quiz game page activity is launched.*/
            intent.putStringArrayListExtra("selected_topics", selectedTopics);
            startActivity(intent);
        }
    }
}