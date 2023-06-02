package com.example.miraiappv2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class KanaChallengeGamePage extends AppCompatActivity {

    TextView preview;
    TextView question;
    Button b_answer1, b_answer2, b_answer3, b_answer4,b_answer5, b_answer6, b_answer7, b_answer8,b_answer9, b_answer10;
    List<KanaChallengeQuestionItem> questionItems;
    String letter = "";
    int score = 0;
    TextView display_score;

    int currentQuestion = 0;

    private ImageButton endgame;
    private ImageButton skip;


    private ImageButton info_popup;
    private ImageButton info_back;


    int correct = 0;
    int incorrect = 0;

    //info pop-up
    Dialog pop_up;

    ArrayList<String> all_topics = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the default title bar from the top of the application for screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_kana_challenge_game_page);

        preview = findViewById(R.id.preview);
        question = findViewById(R.id.question);
        b_answer1 = findViewById(R.id.answer1);
        b_answer2 = findViewById(R.id.answer2);
        b_answer3 = findViewById(R.id.answer3);
        b_answer4 = findViewById(R.id.answer4);
        b_answer5 = findViewById(R.id.answer5);
        b_answer6 = findViewById(R.id.answer6);
        b_answer7 = findViewById(R.id.answer7);
        b_answer8 = findViewById(R.id.answer8);
        b_answer9 = findViewById(R.id.answer9);
        b_answer10 = findViewById(R.id.answer10);
        display_score = findViewById(R.id.score);

        //info pop-up


        //array of all the topics selected
        all_topics = getIntent().getStringArrayListExtra("all_topics");


        //buttons
        endgame = findViewById(R.id.endgame);
        skip = findViewById(R.id.skip);




        //pop_up
        pop_up = new Dialog(this);
        pop_up.getWindow().setBackgroundDrawable(getDrawable(R.drawable.kcinfopopup));
        info_popup = findViewById(R.id.info);

        //font for kana characters
        Typeface typeface = ResourcesCompat.getFont(this, R.font.comicsans);
        Typeface questionFont = ResourcesCompat.getFont(this, R.font.comicsans);
        question.setTypeface(questionFont);
        display_score.setTypeface(questionFont);
        preview.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer1.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer2.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer3.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer4.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer5.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer6.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer7.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer8.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer9.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        b_answer10.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));

        //load all questions
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
                    Intent intent = new Intent(getApplicationContext(), KanaChallengeScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //endgame
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skip.isPressed()){

                    if (currentQuestion < 15-1){
                        currentQuestion++;
                        setQuestionScreen(currentQuestion);
                        preview.setText("");
                        preview.setTextColor(Color.WHITE);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), KanaChallengeScorePage.class);
                        intent.putExtra("correct",correct);
                        intent.putExtra("incorrect", incorrect);
                        intent.putExtra("score",score);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        b_answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letter = b_answer1.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer2.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer3.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer4.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer5.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer6.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer7.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer8.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer9.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });
        b_answer10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letter = b_answer10.getText().toString();
                enterpreview(letter);
                String position=preview.getText().toString();
                //index
                int selectedposition = position.lastIndexOf(letter);
                int correctposition = questionItems.get(currentQuestion).getCorrect().indexOf(letter);
                //characters
                char selectedletter = position.charAt(selectedposition);
                char kanaletter =  questionItems.get(currentQuestion).getCorrect().charAt(selectedposition);
                if (selectedletter == kanaletter){
                    preview.setTextColor(Color.GREEN);
                    System.out.println(kanaletter);
                    System.out.println(selectedposition);
                }else{
                    incorrect++;
                    WrongAnswer();
                }
            }
        });

    }

    public void showpopup(View v){
        ImageButton infoback;
        pop_up.setContentView(R.layout.kcinfopop);
        infoback = (ImageButton) pop_up.findViewById(R.id.infoback);
        infoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up.dismiss();
            }
        });
        pop_up.show();
    }

    private void setQuestionScreen(int number){

        question.setText(questionItems.get(number).getQuestion());
        b_answer1.setText(questionItems.get(number).getAnswer1());
        b_answer2.setText(questionItems.get(number).getAnswer2());
        b_answer3.setText(questionItems.get(number).getAnswer3());
        b_answer4.setText(questionItems.get(number).getAnswer4());
        b_answer5.setText(questionItems.get(number).getAnswer5());
        b_answer6.setText(questionItems.get(number).getAnswer6());
        b_answer7.setText(questionItems.get(number).getAnswer7());
        b_answer8.setText(questionItems.get(number).getAnswer8());
        b_answer9.setText(questionItems.get(number).getAnswer9());
        b_answer10.setText(questionItems.get(number).getAnswer10());
    }


    //append to the preview
    private void enterpreview(String letter){
        int dashcount = questionItems.get(currentQuestion).getCorrect().length();
        System.out.println(dashcount);
        String word = preview.getText().toString();
        preview.setText(word + letter);

        if(preview.getText().toString().equals(questionItems.get(currentQuestion).getCorrect())){
            preview.setTextColor(Color.GREEN);
            Congratulate();
            System.out.println("You got it correct!");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    score+=200;
                    correct++;
                    display_score.setText(String.valueOf(score));

                    if (currentQuestion < 15-1){
                        currentQuestion++;
                        setQuestionScreen(currentQuestion);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), KanaChallengeScorePage.class);
                        intent.putExtra("correct",correct);
                        intent.putExtra("incorrect", incorrect);
                        intent.putExtra("score",score);
                        startActivity(intent);
                        finish();
                    }
                    preview.setText("");
                    preview.setTextColor(Color.WHITE);
                }
            }, 2000);
        }

    }

    //if wrong answer selected
    private void WrongAnswer(){
        preview.setTextColor(Color.RED);
        b_answer1.setEnabled(false);
        b_answer2.setEnabled(false);
        b_answer3.setEnabled(false);
        b_answer4.setEnabled(false);
        b_answer5.setEnabled(false);
        b_answer6.setEnabled(false);
        b_answer7.setEnabled(false);
        b_answer8.setEnabled(false);
        b_answer9.setEnabled(false);
        b_answer10.setEnabled(false);
        showAnswer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentQuestion < 15-1){
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                    b_answer1.setEnabled(true);
                    b_answer2.setEnabled(true);
                    b_answer3.setEnabled(true);
                    b_answer4.setEnabled(true);
                    b_answer5.setEnabled(true);
                    b_answer6.setEnabled(true);
                    b_answer7.setEnabled(true);
                    b_answer8.setEnabled(true);
                    b_answer9.setEnabled(true);
                    b_answer10.setEnabled(true);
                }else{
                    Intent intent = new Intent(getApplicationContext(), KanaChallengeScorePage.class);
                    intent.putExtra("correct",correct);
                    intent.putExtra("incorrect", incorrect);
                    intent.putExtra("score",score);
                    startActivity(intent);
                    finish();
                }
                int dashcount = questionItems.get(currentQuestion).getCorrect().length();
                System.out.println(dashcount);
                preview.setText("");
                preview.setTextColor(Color.WHITE);
            }
        }, 3500);
    }
    //make a list with all the questions
    private void loadAllQuestions(){
        questionItems = new ArrayList<>();
        String jsonString = loadJSONFROMAsset("kanachallenge.json");
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
                String answer5isString = question.getString("answer5");
                String answer6isString = question.getString("answer6");
                String answer7isString = question.getString("answer7");
                String answer8isString = question.getString("answer8");
                String answer9isString = question.getString("answer9");
                String answer10isString = question.getString("answer10");
                String correctisString = question.getString("correct");

                if(all_topics.contains(topicString)) {
                    questionItems.add(new KanaChallengeQuestionItem(
                            questionString,
                            answer1isString,
                            answer2isString,
                            answer3isString,
                            answer4isString,
                            answer5isString,
                            answer6isString,
                            answer7isString,
                            answer8isString,
                            answer9isString,
                            answer10isString,
                            correctisString
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
    public void showAnswer(){
        View layout = getLayoutInflater().inflate(R.layout.kana_correct_popup, (ViewGroup) findViewById(R.id.kana_layout));
        TextView toastTextView = (TextView) layout.findViewById(R.id.correctanswer);
        toastTextView.setText("Answer: " + questionItems.get(currentQuestion).getCorrect());

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void Congratulate(){
        View layout = getLayoutInflater().inflate(R.layout.kana_correct_popup, (ViewGroup) findViewById(R.id.kana_layout));
        TextView toastTextView = (TextView) layout.findViewById(R.id.correctanswer);
        toastTextView.setText("Correct!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}