package com.example.miraiappv2;

import static android.content.ContentValues.TAG;
import static java.util.Collections.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miraiappv2.MagicTroubleEndKPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicTroubleGamePage extends AppCompatActivity {
    //Create media player for sounds
    MediaPlayer MagicTroubleMediaPlayer;

    //Set textview names
    TextView magictroubleQuestion;

    //Set button names
    Button b_answer1, b_answer2, b_answer3, b_answer4;

    //Set image button names
    ImageButton magic_troubleendbtn, magic_troubleinfobtn;

    //Bring in array list of selected topics from previous page
    private ArrayList<String> selectedTopics;

    //Bring in string of selected type from previous page and page before that
    private String selectedType;

    //Declare a list of MagicTroubleQuestionItem objects to store the questions and their answers
    List<MagicTroubleQuestionItem> magicTroubleQuestionItems;

    //Set the current question index to zero
    int currentQuestion = 0;

    //Set correct, wrong and score counters to zero
    int correct = 0, wrong = 0, score = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Set content view to show the xml file
        setContentView(R.layout.activity_magic_trouble_game_page);

        //Get the reference to the elements from the XML layout file
        magictroubleQuestion = findViewById(R.id.text_view_magic_trouble_question);
        b_answer1 = findViewById(R.id.answer1);
        b_answer2 = findViewById(R.id.answer2);
        b_answer3 = findViewById(R.id.answer3);
        b_answer4 = findViewById(R.id.answer4);
        magic_troubleendbtn = findViewById(R.id.magic_trouble_endbtn);
        magic_troubleinfobtn = findViewById(R.id.magic_trouble_infobtn);

        //Reset colour on buttons to white
        b_answer1.setBackgroundColor(getResources().getColor(R.color.white));
        b_answer2.setBackgroundColor(getResources().getColor(R.color.white));
        b_answer3.setBackgroundColor(getResources().getColor(R.color.white));
        b_answer4.setBackgroundColor(getResources().getColor(R.color.white));

        //Check if the intent is not null before retrieving extras
        Intent intent = getIntent();
        if (intent != null) {
            selectedTopics = intent.getStringArrayListExtra("selected_topics");
            selectedType = intent.getStringExtra("selected_type");
        }

        //Check if selectedType is not null before passing it to loadQuestionsTopic
        if (selectedType != null && !selectedType.isEmpty()){
            loadQuestionsTopic(selectedTopics, selectedType);
        } else {
            Toast.makeText(this, "You have not selected a type", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Check if selectedTopics is not null before passing it to loadQuestionsTopic
        if (selectedTopics != null && !selectedTopics.isEmpty()){
            loadQuestionsTopic(selectedTopics, selectedType);
        } else {
            Toast.makeText(this, "There are no questions available for this topic", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Loads all questions based on selected topic and type
        loadQuestionsTopic(selectedTopics, selectedType);

        //Shuffles questions
        Collections.shuffle(magicTroubleQuestionItems);

        //Load first question
        setQuestionScreen(currentQuestion);
        Context context = this;
        Context context1 = this;

        //Onclick listener
        magic_troubleendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedType.equals("romaji")) {
                    // game over for romaji
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndRPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                } else {
                    // game over for other types
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndKPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //Onclick listener
        magic_troubleinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.magic_trouble_info_popup, null);

                // Create the popup window
                int width;
                int height;
                boolean focusable = true; // Allows the user to interact with elements behind the popup window
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, focusable);

                // Set an onClickListener to the close button in the popup layout XML file
                Button closeButton = popupView.findViewById(R.id.infobackbtn);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });

                // Determine the size of the popup window based on the device orientation
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    width = getResources().getDisplayMetrics().widthPixels * 90 / 100; // Set the width to 90% of the screen width
                    height = getResources().getDisplayMetrics().heightPixels * 95 / 100; // Set the height to 80% of the screen height
                } else {
                    width = getResources().getDisplayMetrics().widthPixels * 80 / 100; // Set the width to 80% of the screen width
                    height = getResources().getDisplayMetrics().heightPixels * 90 / 100; // Set the height to 90% of the screen height
                }

                popupWindow.setWidth(width);
                popupWindow.setHeight(height);

                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            }
        });

        b_answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer1().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer1.setBackgroundColor(getResources().getColor(R.color.green));

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct!"+ "You guessed" + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(soundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 10000);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    //wrong
                    wrong +=1;
                    //background red
                    b_answer1.setBackgroundColor(getResources().getColor(R.color.red));

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Wrong! Correct Answer: " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                }

                currentQuestion++;
                Log.d(TAG, "Current question index: " + currentQuestion);

                //Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    //Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    b_answer1.setBackgroundColor(getResources().getColor(R.color.white));
                                }
                            }, 500); // Delay for 100 milliseconds
                        }
                    }, 1000); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndRPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndKPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        b_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer2().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer2.setBackgroundColor(getResources().getColor(R.color.green));

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct!");

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(soundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 10000);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    //wrong
                    wrong +=1;
                    //background red
                    b_answer2.setBackgroundColor(getResources().getColor(R.color.red));

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Wrong! Correct Answer: " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                }

                currentQuestion++;
                Log.d(TAG, "Current question index: " + currentQuestion);

                //Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    //Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    b_answer2.setBackgroundColor(getResources().getColor(R.color.white));
                                }
                            }, 500); // Delay for 100 milliseconds
                        }
                    }, 1000); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndRPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndKPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        b_answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer3().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer3.setBackgroundColor(getResources().getColor(R.color.green));

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct!");

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(soundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 10000);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    //wrong
                    wrong +=1;
                    //background red
                    b_answer3.setBackgroundColor(getResources().getColor(R.color.red));

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Wrong! Correct Answer: " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                }

                currentQuestion++;
                Log.d(TAG, "Current question index: " + currentQuestion);

                //Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    //Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    b_answer3.setBackgroundColor(getResources().getColor(R.color.white));
                                }
                            }, 500); // Delay for 100 milliseconds
                        }
                    }, 1000); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndRPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndKPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        b_answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer4().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer4.setBackgroundColor(getResources().getColor(R.color.green));

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct!");

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    MediaPlayer mediaPlayer = loadAudio(soundName);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();

                        // Release the MediaPlayer object after 2 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.release();
                            }
                        }, 10000);
                    } else {
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    //wrong
                    wrong +=1;
                    //background red
                    b_answer4.setBackgroundColor(getResources().getColor(R.color.red));

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Wrong! Correct Answer: " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();
                }

                currentQuestion++;
                Log.d(TAG, "Current question index: " + currentQuestion);

                //Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    //Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    b_answer4.setBackgroundColor(getResources().getColor(R.color.white));
                                }
                            }, 500); // Delay for 100 milliseconds
                        }
                    }, 1000); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndRPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndKPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }

    //set question to the screen
    private void setQuestionScreen(int index){
        // Get the question item for the given index
        MagicTroubleQuestionItem magicTroubleQuestionItem = magicTroubleQuestionItems.get(index);

        String type = magicTroubleQuestionItem.getType();

        // Set up the question text view
        TextView magictroubleQuestion = findViewById(R.id.text_view_magic_trouble_question);
        if (type.equals("kana")) {
            magictroubleQuestion.setTypeface(Typeface.createFromAsset(getAssets(), "kyo.ttc"));
        } else {
            magictroubleQuestion.setTypeface(Typeface.createFromAsset(getAssets(),"comicsans.ttf"));
        }

        magictroubleQuestion.setText(magicTroubleQuestionItem.getQuestion());

        // Set up the answer buttons
        b_answer1 = findViewById(R.id.answer1);
        b_answer2 = findViewById(R.id.answer2);
        b_answer3 = findViewById(R.id.answer3);
        b_answer4 = findViewById(R.id.answer4);

        // Log the text of each answer button for debugging
        Log.d(TAG, "Answer 1: " + magicTroubleQuestionItem.getAnswer1());
        Log.d(TAG, "Answer 2: " + magicTroubleQuestionItem.getAnswer2());
        Log.d(TAG, "Answer 3: " + magicTroubleQuestionItem.getAnswer3());
        Log.d(TAG, "Answer 4: " + magicTroubleQuestionItem.getAnswer4());

        // Set the text of each answer button
        b_answer1.setText(magicTroubleQuestionItem.getAnswer1());
        b_answer2.setText(magicTroubleQuestionItem.getAnswer2());
        b_answer3.setText(magicTroubleQuestionItem.getAnswer3());
        b_answer4.setText(magicTroubleQuestionItem.getAnswer4());
    }

    //make list with all the questions
    private void loadQuestionsTopic(ArrayList<String> selectedTopics, String selectedType) {
        magicTroubleQuestionItems = new ArrayList<>();

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
                String typeString = question.getString("type");
                String soundString = question.getString("sound");

                // Check if the question topic is one of the selected topics
                if (selectedTopics.contains(topicString)&& selectedType.equals(typeString)) {
                    magicTroubleQuestionItems.add(new MagicTroubleQuestionItem(
                            questionString,
                            answer1String,
                            answer2String,
                            answer3String,
                            answer4String,
                            correctString,
                            topicString,
                            typeString,
                            soundString
                    ));
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //
    private MediaPlayer loadAudio(String soundName) {
        int resID = getResources().getIdentifier(soundName, "raw", getPackageName());

        if (resID != 0) {
            return MediaPlayer.create(this, resID);
        } else {
            // Handle error getting resource ID
            Log.e(TAG, "Error getting resource ID");
            return null;
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















