package com.example.miraiappv2;

import static java.util.Collections.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicTroubleGamePage extends AppCompatActivity {

    //declare textview from xml file
    TextView bubbleMatchQuestion;
    TextView bubbleMatchScore;

    //declare buttons from xml file
    Button b_answer1, b_answer2,b_answer3,b_answer4;

    //bring in array list of selected topics from previous page
    private ArrayList<String> selectedTopics;

    // declare a list of QuestionItem objects to store the questions and their answers
    List<QuestionItem> questionItems;

    // initialize the current question index to zero
    int currentQuestion = 0;

    // initialize correct, wrong and score counters to zero
    int correct = 0, wrong = 0, score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //sets the view to the correct xml page
        setContentView(R.layout.activity_magic_trouble_game_page);

        //get the reference to the elements from the XML layout file
        bubbleMatchQuestion = findViewById(R.id.text_view_magic_trouble_question);
        b_answer1 = findViewById(R.id.answer1);
        b_answer2 = findViewById(R.id.answer2);
        b_answer3 = findViewById(R.id.answer3);
        b_answer4 = findViewById(R.id.answer4);


        //Check if the intent is not null before retrieving extras
        Intent intent = getIntent();
        if (intent != null) {
            selectedTopics = intent.getStringArrayListExtra("selected_topics");
        }

        // Check if selectedTopics is not null before passing it to loadQuestionsTopic
        if (selectedTopics != null && !selectedTopics.isEmpty()) {
            loadQuestionsTopic(selectedTopics);
        } else {
            Toast.makeText(this, "There are no questions available for this topic", Toast.LENGTH_SHORT).show();
            finish();
        }

        //gets all questions
        loadQuestionsTopic(selectedTopics);

        //shuffle questions
        Collections.shuffle(questionItems);

        //load first question
        setQuestionScreen(currentQuestion);


        b_answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(questionItems.get(currentQuestion).getAnswer1().equals(questionItems.get(currentQuestion).getCorrect())){
                    //correct
                    correct +=1;
                    score +=100;
                    TextView bubbleMatchScore = findViewById(R.id.text_view_magic_trouble_score);
                    bubbleMatchScore.setText("" + score);
                    Toast.makeText(MagicTroubleGamePage.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    //wrong
                    wrong +=1;
                    Toast.makeText(MagicTroubleGamePage.this, "Wrong! Correct Answer: " + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }

                //Load next set of questions if any
                if(currentQuestion < questionItems.size()-1){
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                } else {
                    //game over
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        });
        b_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(questionItems.get(currentQuestion).getAnswer2().equals(questionItems.get(currentQuestion).getCorrect())){
                    //correct
                    correct +=1;
                    score +=100;
                    TextView bubbleMatchScore = findViewById(R.id.text_view_magic_trouble_score);
                    bubbleMatchScore.setText("" + score);
                    Toast.makeText(MagicTroubleGamePage.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    //wrong
                    wrong +=1;
                    Toast.makeText(MagicTroubleGamePage.this, "Wrong! Correct Answer: " + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }

                //Load next set of questions if any
                if(currentQuestion < questionItems.size()-1){
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                } else {
                    //game over
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        });
        b_answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(questionItems.get(currentQuestion).getAnswer3().equals(questionItems.get(currentQuestion).getCorrect())){
                    //correct
                    correct +=1;
                    score +=100;
                    TextView bubbleMatchScore = findViewById(R.id.text_view_magic_trouble_score);
                    bubbleMatchScore.setText("" + score);
                    Toast.makeText(MagicTroubleGamePage.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    //wrong
                    wrong +=1;
                    Toast.makeText(MagicTroubleGamePage.this, "Wrong! Correct Answer: " + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }

                //Load next set of questions if any
                if(currentQuestion < questionItems.size()-1){
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                } else {
                    //game over
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        });
        b_answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(questionItems.get(currentQuestion).getAnswer4().equals(questionItems.get(currentQuestion).getCorrect())){
                    //correct
                    correct +=1;
                    score +=100;
                    TextView bubbleMatchScore = findViewById(R.id.text_view_magic_trouble_score);
                    bubbleMatchScore.setText("" + score);
                    Toast.makeText(MagicTroubleGamePage.this, "Correct!", Toast.LENGTH_SHORT).show();
                }else {
                    //wrong
                    wrong +=1;
                    Toast.makeText(MagicTroubleGamePage.this, "Wrong! Correct Answer: " + questionItems.get(currentQuestion).getCorrect(), Toast.LENGTH_SHORT).show();
                }

                //Load next set of questions if any
                if(currentQuestion < questionItems.size()-1){
                    currentQuestion++;
                    setQuestionScreen(currentQuestion);
                } else {
                    //game over
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    //set question to the screen
    private  void setQuestionScreen(int number){
        bubbleMatchQuestion.setText(questionItems.get(number).getQuestion());
        b_answer1.setText(questionItems.get(number).getAnswer1());
        b_answer2.setText(questionItems.get(number).getAnswer2());
        b_answer3.setText(questionItems.get(number).getAnswer3());
        b_answer4.setText(questionItems.get(number).getAnswer4());
    }



    //make list with all the questions
    private void loadQuestionsTopic(ArrayList<String> selectedTopics) {
        questionItems = new ArrayList<>();

        //load questions into json string
        String jsonStr = loadJSONFromAsset("questions.json");

        //load all data into the list
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray questions = jsonObj.getJSONArray("questions");

            for (int i = 0; i < questions.length(); i++) {
                JSONObject question = questions.getJSONObject(i);

                String questionString = question.getString("question");
                String answer1String = question.getString("answer1");
                String answer2String = question.getString("answer2");
                String answer3String = question.getString("answer3");
                String answer4String = question.getString("answer4");
                String correctString = question.getString("correct");
                String topicString = question.getString("topic");

                // Check if the question topic is one of the selected topics
                if (selectedTopics.contains(topicString)) {
                    questionItems.add(new QuestionItem(
                            questionString,
                            answer1String,
                            answer2String,
                            answer3String,
                            answer4String,
                            correctString,
                            topicString
                    ));
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    //Load json file from folder
    private String loadJSONFromAsset(String file){
        String json = "";
        try{
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }
}















