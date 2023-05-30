package com.example.miraiappv2;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VocabularyConfusionGamePage extends AppCompatActivity {

    private ImageButton endgame;

    private ImageButton info_popup;
    private ImageButton info_back;

    TextView game_question;
    TextView display_score;
    Button b_answer1, b_answer2, b_answer3, b_answer4;

    List<VocabularyConfusionQuestionItem> questionItems;
    int currentQuestion = 0;
    int correct = 0;
    int incorrect = 0;
    int score = 0;

    //info pop-up
    Dialog pop_up;

    ArrayList<String> all_topics = new ArrayList<>();
    String language = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set the view
        setContentView(R.layout.activity_voabulary_confusion_game_page);

        //array of all the topics selected
        all_topics = getIntent().getStringArrayListExtra("all_topics");
        language = getIntent().getStringExtra("language");


        //buttons
        endgame = findViewById(R.id.endgame);

        //pop_up
        pop_up = new Dialog(this);
        pop_up.getWindow().setBackgroundDrawable(getDrawable(R.drawable.ipopup));
        info_popup = findViewById(R.id.info);


        //questions and answers
        game_question = findViewById(R.id.question);
        b_answer1 = findViewById(R.id.button1);
        b_answer2 = findViewById(R.id.button2);
        b_answer3 = findViewById(R.id.button3);
        b_answer4 = findViewById(R.id.button4);
        display_score = findViewById(R.id.score);


        //fonts for kana and romaji
        if (language.equals("romaji_questions.json")){
            game_question.setTypeface(Typeface.createFromAsset(getAssets(), "comicsans.ttf"));
            display_score.setTypeface(Typeface.createFromAsset(getAssets(), "comicsans.ttf"));
            b_answer1.setTypeface(Typeface.createFromAsset(getAssets(), "comicsans.ttf"));
            b_answer2.setTypeface(Typeface.createFromAsset(getAssets(), "comicsans.ttf"));
            b_answer3.setTypeface(Typeface.createFromAsset(getAssets(), "comicsans.ttf"));
            b_answer4.setTypeface(Typeface.createFromAsset(getAssets(), "comicsans.ttf"));
        } else if (language.equals("kana_questions.json")) {
            Typeface typeface = ResourcesCompat.getFont(this, R.font.comicsans);
            Typeface questionFont = ResourcesCompat.getFont(this, R.font.comicsans);
            game_question.setTypeface(questionFont);
            display_score.setTypeface(questionFont);
            b_answer1.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
            b_answer2.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
            b_answer3.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
            b_answer4.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        }


        //get the questions
        loadAllQuestions();

        //randomize the questions
        Collections.shuffle(questionItems);

        //load the first question
        setQuestionScreen(currentQuestion);

        //endgame
        endgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endgame.isPressed()){
                    Intent intent = new Intent(getApplicationContext(), VocabularyConfusionScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
            }
        });





        //questions and answers
        b_answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if answer is correct
                if (questionItems.get(currentQuestion).getAnswer1()
                        .equals(questionItems.get(currentQuestion).getCorrect())){
                    correct++;
                    score+=200;
                    display_score.setText(String.valueOf(score));
                    b_answer1.setBackgroundResource(R.drawable.greenleft);
                    b_answer2.setBackgroundResource(R.drawable.redright);
                    b_answer3.setBackgroundResource(R.drawable.redleft);
                    b_answer4.setBackgroundResource(R.drawable.redright);


                    //play the sound
                    String SoundName = questionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(SoundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 1500);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + SoundName);
                    }

                }else{
                    incorrect++;
                    b_answer1.setBackgroundResource(R.drawable.redleft);


                    //make the correct answer green and wrong answer red
                    if(b_answer2.getText().equals(questionItems.get(currentQuestion).getCorrect())){
                        b_answer2.setBackgroundResource(R.drawable.greenright);
                        b_answer3.setBackgroundResource(R.drawable.redleft);
                        b_answer4.setBackgroundResource(R.drawable.redright);


                    } else if (b_answer3.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer2.setBackgroundResource(R.drawable.redright);
                        b_answer3.setBackgroundResource(R.drawable.greenleft);
                        b_answer4.setBackgroundResource(R.drawable.redright);
                        //System.out.println("correct2");
                    } else if (b_answer4.getText().equals(questionItems.get(currentQuestion).getCorrect())) {

                        b_answer2.setBackgroundResource(R.drawable.redright);
                        b_answer3.setBackgroundResource(R.drawable.redleft);
                        b_answer4.setBackgroundResource(R.drawable.greenright);
                        //System.out.println("correct3");
                    }

                }
                //load next question
                if (currentQuestion < 15-1){
                    currentQuestion++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);
                            // Set the background color of the button to default
                            b_answer4.setBackgroundColor(Color.TRANSPARENT);
                            b_answer3.setBackgroundColor(Color.TRANSPARENT);
                            b_answer2.setBackgroundColor(Color.TRANSPARENT);
                            b_answer1.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }, 1700);
                }else{
                    //game over go to score screen
                    Intent intent = new Intent(getApplicationContext(), VocabularyConfusionScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
            }
        });

        b_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if answer is correct
                if (questionItems.get(currentQuestion).getAnswer2()
                        .equals(questionItems.get(currentQuestion).getCorrect())){
                    correct++;
                    score+=200;
                    display_score.setText(String.valueOf(score));
                    b_answer1.setBackgroundResource(R.drawable.redleft);
                    b_answer2.setBackgroundResource(R.drawable.greenright);
                    b_answer3.setBackgroundResource(R.drawable.redleft);
                    b_answer4.setBackgroundResource(R.drawable.redright);

                    //play the sound
                    String SoundName = questionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(SoundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 1500);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + SoundName);
                    }

                }else{
                    incorrect++;
                    b_answer2.setBackgroundResource(R.drawable.redright);

                    //make the correct answer green and wrong answer red
                    if(b_answer1.getText().equals(questionItems.get(currentQuestion).getCorrect())){
                        b_answer1.setBackgroundResource(R.drawable.greenleft);
                        b_answer3.setBackgroundResource(R.drawable.redleft);
                        b_answer4.setBackgroundResource(R.drawable.redright);

                    } else if (b_answer3.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer1.setBackgroundResource(R.drawable.redleft);
                        b_answer3.setBackgroundResource(R.drawable.greenleft);
                        b_answer4.setBackgroundResource(R.drawable.redright);
                        //System.out.println("correct2");
                    } else if (b_answer4.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer1.setBackgroundResource(R.drawable.redleft);
                        b_answer3.setBackgroundResource(R.drawable.redleft);
                        b_answer4.setBackgroundResource(R.drawable.greenright);
                        //System.out.println("correct3");
                    }
                }
                //load next question
                if (currentQuestion < 15-1){
                    currentQuestion++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);
                            // Set the background color of the button to default
                            b_answer4.setBackgroundColor(Color.TRANSPARENT);
                            b_answer3.setBackgroundColor(Color.TRANSPARENT);
                            b_answer2.setBackgroundColor(Color.TRANSPARENT);
                            b_answer1.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }, 1700);
                }else{
                    //game over go to score screen
                    Intent intent = new Intent(getApplicationContext(), VocabularyConfusionScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
            }
        });

        b_answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if answer is correct
                if (questionItems.get(currentQuestion).getAnswer3()
                        .equals(questionItems.get(currentQuestion).getCorrect())){
                    correct++;
                    score+=200;
                    display_score.setText(String.valueOf(score));
                    b_answer1.setBackgroundResource(R.drawable.redleft);
                    b_answer2.setBackgroundResource(R.drawable.redright);
                    b_answer3.setBackgroundResource(R.drawable.greenleft);
                    b_answer4.setBackgroundResource(R.drawable.redright);
                    //play the sound
                    String SoundName = questionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(SoundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 1500);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + SoundName);
                    }
                }else{
                    incorrect++;
                    b_answer3.setBackgroundResource(R.drawable.redleft);

                    //make the correct answer green and wrong answer red
                    if(b_answer2.getText().equals(questionItems.get(currentQuestion).getCorrect())){

                        b_answer2.setBackgroundResource(R.drawable.greenright);
                        b_answer1.setBackgroundResource(R.drawable.redleft);
                        b_answer4.setBackgroundResource(R.drawable.redright);

                    } else if (b_answer1.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer2.setBackgroundResource(R.drawable.redright);
                        b_answer1.setBackgroundResource(R.drawable.greenleft);
                        b_answer4.setBackgroundResource(R.drawable.redright);
                        //System.out.println("correct2");
                    } else if (b_answer4.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer2.setBackgroundResource(R.drawable.redright);
                        b_answer1.setBackgroundResource(R.drawable.redleft);
                        b_answer4.setBackgroundResource(R.drawable.greenright);
                        //System.out.println("correct3");
                    }
                }
                //load next question
                if (currentQuestion < 15-1){
                    currentQuestion++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);
                            // Set the background color of the button to default
                            b_answer4.setBackgroundColor(Color.TRANSPARENT);
                            b_answer3.setBackgroundColor(Color.TRANSPARENT);
                            b_answer2.setBackgroundColor(Color.TRANSPARENT);
                            b_answer1.setBackgroundColor(Color.TRANSPARENT);
                            //set text color back to white


                        }
                    }, 1700);
                }else{
                    //game over go to score screen
                    Intent intent = new Intent(getApplicationContext(), VocabularyConfusionScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
            }
        });

        b_answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if answer is correct
                if (questionItems.get(currentQuestion).getAnswer4()
                        .equals(questionItems.get(currentQuestion).getCorrect())){
                    correct++;
                    score+=200;
                    display_score.setText(String.valueOf(score));
                    b_answer2.setBackgroundResource(R.drawable.redright);
                    b_answer1.setBackgroundResource(R.drawable.redleft);
                    b_answer3.setBackgroundResource(R.drawable.redleft);
                    b_answer4.setBackgroundResource(R.drawable.greenright);

                    //play the sound
                    String SoundName = questionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(SoundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 1500);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + SoundName);
                    }

                }else{
                    incorrect++;
                    b_answer4.setBackgroundResource(R.drawable.redright);
                    //make the correct answer green and wrong answer red
                    if(b_answer2.getText().equals(questionItems.get(currentQuestion).getCorrect())){
                        b_answer1.setBackgroundResource(R.drawable.redleft);
                        b_answer2.setBackgroundResource(R.drawable.greenright);
                        b_answer3.setBackgroundResource(R.drawable.redleft);

                    } else if (b_answer3.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer1.setBackgroundResource(R.drawable.redleft);
                        b_answer2.setBackgroundResource(R.drawable.redright);
                        b_answer3.setBackgroundResource(R.drawable.greenleft);

                    } else if (b_answer1.getText().equals(questionItems.get(currentQuestion).getCorrect())) {
                        b_answer1.setBackgroundResource(R.drawable.greenleft);
                        b_answer2.setBackgroundResource(R.drawable.redright);
                        b_answer3.setBackgroundResource(R.drawable.redleft);

                    }
                }
                //load next question
                if (currentQuestion < 15-1){
                    currentQuestion++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);
                            // Set the background color of the button to default
                            b_answer4.setBackgroundColor(Color.TRANSPARENT);
                            b_answer3.setBackgroundColor(Color.TRANSPARENT);
                            b_answer2.setBackgroundColor(Color.TRANSPARENT);
                            b_answer1.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }, 1700);
                }else{
                    //game over go to score screen
                    Intent intent = new Intent(getApplicationContext(), VocabularyConfusionScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    public void showpopup(View v){
        ImageButton infoback;
        pop_up.setContentView(R.layout.infopopup);
        infoback = (ImageButton) pop_up.findViewById(R.id.infoback);
        infoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up.dismiss();
            }
        });
        pop_up.show();
    }

    //font

    //set the questions on the screen
    private void setQuestionScreen(int number){
        game_question.setText(questionItems.get(number).getQuestion());
        b_answer1.setText(questionItems.get(number).getAnswer1());
        b_answer2.setText(questionItems.get(number).getAnswer2());
        b_answer3.setText(questionItems.get(number).getAnswer3());
        b_answer4.setText(questionItems.get(number).getAnswer4());
    }

    //load the sound
    private MediaPlayer loadAudio(String soundName) {
        int resID = getResources().getIdentifier(soundName, "raw", getPackageName());

        if (resID != 0) {
            return MediaPlayer.create(this, resID);
        } else {
            return null;
        }
    }
    //make a list with all the questions

    private void loadAllQuestions(){
        questionItems = new ArrayList<>();

        //read JSON file from Assets folder
        String jsonString = loadJSONFROMAsset(language);

        try{
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray questions = jsonObj.getJSONArray("questions");

            for (int i = 0; i <questions.length(); i++){
                JSONObject question = questions.getJSONObject(i);
                String topicString = question.getString("topic");
                String questionString = question.getString("question");
                String answer1isString = question.getString("answer1");
                String answer2isString = question.getString("answer2");
                String answer3isString = question.getString("answer3");
                String answer4isString = question.getString("answer4");
                String correctisString = question.getString("correct");
                String soundisString = question.getString("sound");

                if(all_topics.contains(topicString)){
                    questionItems.add(new VocabularyConfusionQuestionItem(
                            questionString,
                            answer1isString,
                            answer2isString,
                            answer3isString,
                            answer4isString,
                            correctisString,
                            soundisString
                    ));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    //to load the json file from the assets folder
    private String loadJSONFROMAsset(String file){
        String json = "";
        try{
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }

}