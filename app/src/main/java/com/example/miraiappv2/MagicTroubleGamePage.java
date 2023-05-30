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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicTroubleGamePage extends AppCompatActivity {
    //private static final String TAG = "MagicTroubleGamePage";

    //Create media player for sounds
    MediaPlayer MagicTroubleMediaPlayer;

    //Set textview names
    TextView magictroubleQuestion, answer1_text, answer2_text, answer3_text, answer4_text;

    //Set button names
    ImageButton b_answer1, b_answer2, b_answer3, b_answer4, b_answer1s, b_answer2s, b_answer3s, b_answer4s;

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
        //getSupportActionBar().hide();

        //Set content view to show the xml file
        setContentView(R.layout.activity_magic_trouble_game_page);

        //Get the reference to the elements from the XML layout file
        magictroubleQuestion = findViewById(R.id.text_view_magic_trouble_question);
        b_answer1 = findViewById(R.id.answer1);
        b_answer2 = findViewById(R.id.answer2);
        b_answer3 = findViewById(R.id.answer3);
        b_answer4 = findViewById(R.id.answer4);
        b_answer1s = findViewById(R.id.answer1s);
        b_answer2s = findViewById(R.id.answer2s);
        b_answer3s = findViewById(R.id.answer3s);
        b_answer4s = findViewById(R.id.answer4s);
        answer1_text = findViewById(R.id.answer1_text);
        answer2_text = findViewById(R.id.answer2_text);
        answer3_text = findViewById(R.id.answer3_text);
        answer4_text = findViewById(R.id.answer4_text);
        magic_troubleendbtn = findViewById(R.id.magic_trouble_endbtn);
        magic_troubleinfobtn = findViewById(R.id.magic_trouble_infobtn);

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
                    // game over for romaji, pass the score and results
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    intent.putExtra("background", R.drawable.background_magic_trouble_end_romaji_phone);
                    startActivity(intent);
                    finish();
                } else {
                    // game over for other type, pass the score and results
                    Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    intent.putExtra("background", R.drawable.background_magic_trouble_end_kana_phone);
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

                // Create the magic_popup window
                int width;
                int height;
                boolean focusable = true; // Allows the user to interact with elements behind the magic_popup window
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, focusable);

                // Set an onClickListener to the close button in the magic_popup layout XML file
                Button closeButton = popupView.findViewById(R.id.infobackbtn);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the magic_popup window
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

        //Onclick listener
        b_answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the parent view of the button to be removed
                ViewGroup parentView = (ViewGroup) b_answer1s.getParent();
                if (parentView != null) {
                    // Remove the button to be removed from its parent view
                    parentView.removeView(b_answer1s);
                }

                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer1().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer1.setBackgroundResource(R.drawable.button_magic_trouble_answer1_green);

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct! "+ "You guessed " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    // Load media player and play sound based on JSON file, if it doesnt load it will display a error log with the file that it failed on
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
                        // For Testing
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    // Set background red
                    b_answer1.setBackgroundResource(R.drawable.button_magic_trouble_answer1_red);

                    // Add 1+ to wrong
                    wrong +=1;

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

                // Move to next question
                currentQuestion++;

                // Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    // Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Put question on the screen
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Set background back to default
                                    b_answer1.setBackgroundResource(R.drawable.button_magic_trouble_answer1);
                                }
                            }, 100); // Delay for 100 milliseconds
                        }
                    }, 1100); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_romaji_phone);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_kana_phone);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        //Onclick listener
        b_answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the parent view of the button to be removed
                ViewGroup parentView = (ViewGroup) b_answer2s.getParent();
                if (parentView != null) {
                    // Remove the button to be removed from its parent view
                    parentView.removeView(b_answer2s);
                }

                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer2().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer2.setBackgroundResource(R.drawable.button_magic_trouble_answer2_green);

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct! "+ "You guessed " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    // Load media player and play sound based on JSON file, if it doesnt load it will display a error log with the file that it failed on
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
                        // For Testing
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    // Set background red
                    b_answer2.setBackgroundResource(R.drawable.button_magic_trouble_answer2_red);

                    // Add 1+ to wrong
                    wrong +=1;

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

                // Move to next question
                currentQuestion++;

                // Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    // Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Put question on the screen
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Set background back to default
                                    b_answer2.setBackgroundResource(R.drawable.button_magic_trouble_answer2);
                                }
                            }, 100); // Delay for 100 milliseconds
                        }
                    }, 1100); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_kana_phone);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_kana_phone);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        //Onclick listener
        b_answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the parent view of the button to be removed
                ViewGroup parentView = (ViewGroup) b_answer3s.getParent();
                if (parentView != null) {
                    // Remove the button to be removed from its parent view
                    parentView.removeView(b_answer3s);
                }

                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer3().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer3.setBackgroundResource(R.drawable.button_magic_trouble_answer3_green);

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct! "+ "You guessed " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    // Load media player and play sound based on JSON file, if it doesnt load it will display a error log with the file that it failed on
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
                        // For Testing
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    // Set background red
                    b_answer3.setBackgroundResource(R.drawable.button_magic_trouble_answer3_red);

                    // Add 1+ to wrong
                    wrong +=1;

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

                // Move to next question
                currentQuestion++;

                // Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    // Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Put question on the screen
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Set background back to default
                                    b_answer3.setBackgroundResource(R.drawable.button_magic_trouble_answer3);
                                }
                            }, 100); // Delay for 100 milliseconds
                        }
                    }, 1100); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_romaji_phone);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_kana_phone);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        //Onclick listener
        b_answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the parent view of the button to be removed
                ViewGroup parentView = (ViewGroup) b_answer4s.getParent();
                if (parentView != null) {
                    // Remove the button to be removed from its parent view
                    parentView.removeView(b_answer4s);
                }

                //check if the answer is correct
                if(magicTroubleQuestionItems.get(currentQuestion).getAnswer4().equals(magicTroubleQuestionItems.get(currentQuestion).getCorrect())){
                    //background green
                    b_answer4.setBackgroundResource(R.drawable.button_magic_trouble_answer4_green);

                    //correct
                    correct +=1;
                    score +=100;
                    TextView magictroubleScore = findViewById(R.id.text_view_magic_trouble_score);
                    magictroubleScore.setText("" + score);

                    // Inflate the custom toast layout
                    View toastLayout = getLayoutInflater().inflate(R.layout.magic_trouble_popup, (ViewGroup) findViewById(R.id.toast_layout_root));

                    // Set the toast message
                    TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
                    toastTextView.setText("Correct! "+ "You guessed " + magicTroubleQuestionItems.get(currentQuestion).getCorrect());

                    // Create and show the toast
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(toastLayout);
                    toast.show();

                    // Get the sound string from the current question
                    String soundName = magicTroubleQuestionItems.get(currentQuestion).getSound();

                    // Load media player and play sound based on JSON file, if it doesnt load it will display a error log with the file that it failed on
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
                        // For Testing
                        // Handle error creating MediaPlayer object
                        Log.e(TAG, "Error creating MediaPlayer object");
                        Log.d(TAG, "Sound name: " + soundName);
                    }

                }else {
                    // Set background red
                    b_answer4.setBackgroundResource(R.drawable.button_magic_trouble_answer4_red);

                    // Add 1+ to wrong
                    wrong +=1;

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

                // Move to next question
                currentQuestion++;

                // Load next set of questions if any
                if(currentQuestion < magicTroubleQuestionItems.size()-1){
                    // Delay for 1 second (1000 milliseconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Put question on the screen
                            setQuestionScreen(currentQuestion);

                            // Reset the background color of the button after a slight delay
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Set background back to default
                                    b_answer4.setBackgroundResource(R.drawable.button_magic_trouble_answer4);
                                }
                            }, 100); // Delay for 100 milliseconds
                        }
                    }, 1100); // Delay for 1 second (1000 milliseconds)
                } else {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_romaji_phone);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other types, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), MagicTroubleEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_magic_trouble_end_kana_phone);
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

        // Set the text of each answer button
        answer1_text.setText(magicTroubleQuestionItem.getAnswer1());
        answer2_text.setText(magicTroubleQuestionItem.getAnswer2());
        answer3_text.setText(magicTroubleQuestionItem.getAnswer3());
        answer4_text.setText(magicTroubleQuestionItem.getAnswer4());
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

            // for each question get all elements from json and set as variables
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















