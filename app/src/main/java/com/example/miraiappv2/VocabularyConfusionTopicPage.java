package com.example.miraiappv2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class VocabularyConfusionTopicPage extends AppCompatActivity {

    private ImageButton greetingslesson;
    private ImageButton essentiallesson;
    private ImageButton numberslesson;
    private ImageButton colorslesson;
    private ImageButton schoollesson;
    private ImageButton generallesson;
    private ImageButton adjectiveslesson;
    private ImageButton verbslesson;
    private ImageButton backpressed;
    private ImageButton foodlesson;
    private ImageButton seasonslesson;
    private ImageButton dayslesson;
    private ImageButton animalslesson;
    private ImageButton bodylesson;
    private ImageButton familylesson;
    private ImageButton beginGame;

    boolean topicselected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the view
        setContentView(R.layout.activity_voabulary_confusion_topic_page);

        //buttons
        greetingslesson = (ImageButton) findViewById(R.id.GreetingsButton);
        essentiallesson = (ImageButton) findViewById(R.id.EssentialButton);
        numberslesson = (ImageButton) findViewById(R.id.NumbersButton);
        colorslesson = (ImageButton) findViewById(R.id.ColorsButton);
        schoollesson = (ImageButton) findViewById(R.id.SchoolButton);
        generallesson = (ImageButton) findViewById(R.id.GeneralButton);
        adjectiveslesson = (ImageButton) findViewById(R.id.AdjectivesButton);
        verbslesson = (ImageButton) findViewById(R.id.VerbsButton);
        backpressed = (ImageButton) findViewById(R.id.OrangeBackButton);
        familylesson = (ImageButton) findViewById(R.id.familyButton);
        foodlesson = (ImageButton) findViewById(R.id.foodButton);
        seasonslesson = (ImageButton) findViewById(R.id.seasonsButton);
        dayslesson = (ImageButton) findViewById(R.id.daysButton);
        animalslesson = (ImageButton) findViewById(R.id.animalsButton);
        bodylesson = (ImageButton) findViewById(R.id.bodyButton);
        beginGame = (ImageButton) findViewById(R.id.startButton);

        //topic selected array
        ArrayList<String> all_topics = new ArrayList<>();

        //on click

        greetingslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (greetingslesson.isPressed()){
                    greetingslesson.setImageResource(R.drawable.button_topic_greetings_selected);
                    topicselected = true;
                    all_topics.add("greetings");
                    System.out.println(all_topics);

                }
            }
        });

        essentiallesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (essentiallesson.isPressed()){
                    essentiallesson.setImageResource(R.drawable.button_topic_essential_selected);
                    topicselected = true;
                    all_topics.add("essential");
                }
            }
        });

        numberslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberslesson.isPressed()){
                    numberslesson.setImageResource(R.drawable.button_topic_numbers_selected);
                    topicselected = true;
                    all_topics.add("numbers");
                }
            }
        });

        colorslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorslesson.isPressed()){
                    colorslesson.setImageResource(R.drawable.button_topic_colours_selected);
                    topicselected = true;
                    all_topics.add("color");
                }
            }
        });


        schoollesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schoollesson.isPressed()){
                    schoollesson.setImageResource(R.drawable.button_topic_school_selected);
                    topicselected = true;
                    all_topics.add("school");
                }
            }
        });

        generallesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generallesson.isPressed()){
                    generallesson.setImageResource(R.drawable.button_topic_general_selected);
                    topicselected = true;
                    all_topics.add("general");
                }
            }
        });

        adjectiveslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adjectiveslesson.isPressed()){
                    adjectiveslesson.setImageResource(R.drawable.button_topic_adjectives_selected);
                    topicselected = true;
                    all_topics.add("adjectives");
                }
            }
        });

        verbslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verbslesson.isPressed()){
                    verbslesson.setImageResource(R.drawable.button_topic_verbs_selected);
                    topicselected = true;
                    all_topics.add("verbs");
                }
            }
        });

        animalslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animalslesson.isPressed()){
                    animalslesson.setImageResource(R.drawable.button_topic_animals_selected);
                    topicselected = true;
                    all_topics.add("animals");
                }
            }
        });

        bodylesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bodylesson.isPressed()){
                    bodylesson.setImageResource(R.drawable.button_topic_body_parts_selected);
                    topicselected = true;
                    all_topics.add("body");
                }
            }
        });

        dayslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dayslesson.isPressed()){
                    dayslesson.setImageResource(R.drawable.button_topic_dwmy_selected);
                    topicselected = true;
                    all_topics.add("days");
                }
            }
        });

        seasonslesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seasonslesson.isPressed()){
                    seasonslesson.setImageResource(R.drawable.button_topic_seasons_weather_selected);
                    topicselected = true;
                    all_topics.add("seasons");
                }
            }
        });

        familylesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (familylesson.isPressed()){
                    familylesson.setImageResource(R.drawable.button_topic_family_counting_selected);
                    topicselected = true;
                    all_topics.add("family");
                }
            }
        });

        foodlesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodlesson.isPressed()){
                    foodlesson.setImageResource(R.drawable.button_topic_food_drink_selected);
                    topicselected = true;
                    all_topics.add("food");
                }
            }
        });

        beginGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beginGame.isPressed() && topicselected == false){
                    Toast.makeText(VocabularyConfusionTopicPage.this, "Please Select one or more topics",
                            Toast.LENGTH_SHORT).show();
                }else{
                    String topics = TextUtils.join(",",all_topics);
                    String language = getIntent().getStringExtra("language");
                    Intent intent = new Intent(getApplicationContext(), VocabularyConfusionGamePage.class);
                    intent.putExtra("language",language);
                    intent.putStringArrayListExtra("all_topics",all_topics);
                    startActivity(intent);
                    finish();
                }
            }
        });



        backpressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backpressed.isPressed()){
                    ReturnToMain();
                }
            }
        });

    }

    public void ReturnToMain(){
        Intent intent = new Intent(this, VocabularyConfusionSelectionPage.class);
        startActivity(intent);
    }




}