package com.example.miraiappv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BubbleMatchGamePage extends AppCompatActivity {
    //Create media player for sounds
    MediaPlayer BubbleMatchMediaPlayer;

    private CountDownTimer countDownTimer;
    private TextView countdownTextView;
    //Declare a list of MagicTroubleQuestionItem objects to store the questions and their answers
    List<BubbleMatchButton> bubbleMatchButton;

    //Set image button names
    ImageButton bubble_matchendbtn, bubble_matchinfobtn;

    private int matchesFound = 0;
    private int matchesIncorrect = 0;
    private String getJSONValueForButton(BubbleMatchButton button) {
        // Create a JSON object for the button data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", button.getId());
            jsonObject.put("eword", button.getEword());
            jsonObject.put("jword", button.getJword());
            jsonObject.put("topic", button.getTopic());
            jsonObject.put("type", button.getType());
            jsonObject.put("sound", button.getSound());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the JSON object as a string
        return jsonObject.toString();
    }
    // Declare variables to store the JSON values and buttons for each category
    String eJsonValue = null;
    String jJsonValue = null;
    BubbleMatchButton eButton1 = null;
    BubbleMatchButton eButton2 = null;
    BubbleMatchButton jButton1 = null;
    BubbleMatchButton jButton2 = null;
    boolean isFirstSelection = false;
    boolean isSecondSelection = false;
    private int firstSelectedButtonId = -1;
    private List<ImageButton> generatedButtons = new ArrayList<>();
    //Set correct, wrong and score counters to zero
    int correct = 0, wrong = 0, score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();


        setContentView(R.layout.activity_bubble_match_game_page);

        GridLayout gridLayout = findViewById(R.id.grid_layout_bubble_match);
        bubbleMatchButton = new ArrayList<BubbleMatchButton>();

        // Retrieve the selectedTopics and selectedType values from the intent
        Intent intent = getIntent();
        ArrayList<String> selectedTopics = intent.getStringArrayListExtra("selected_topics");
        int numberOfSelectedTopics = selectedTopics.size();
        String selectedType = intent.getStringExtra("selected_type");

        countdownTextView = findViewById(R.id.countdown_textview);
        startCountdown(selectedType, numberOfSelectedTopics);
        int generatedWordCount = 0;

        // Check if selectedTopics is null before accessing it
        if (selectedTopics != null) {
            //load questions into json string
            String jsonStr = loadJSONFromAsset("bubble.json");

            //load all data into the list
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray questions = jsonObj.getJSONArray("bubble");

                for (int i = 0; i < questions.length(); i++) {
                    JSONObject question = questions.getJSONObject(i);

                    String idString = question.getString("id");
                    String ewordString = question.getString("eword");
                    String jwordString = question.getString("jword");
                    String topicString = question.getString("topic");
                    String typeString = question.getString("type");
                    String soundString = question.getString("sound");

                    // Check if the question topic is one of the selected topics
                    if (selectedTopics.contains(topicString) && selectedType.equals(typeString)) {
                        bubbleMatchButton.add(new BubbleMatchButton(
                                idString,
                                ewordString,
                                jwordString,
                                topicString,
                                typeString,
                                soundString
                        ));
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            // Shuffle the game elements
            Collections.shuffle(bubbleMatchButton);
            bubble_matchendbtn = findViewById(R.id.bubble_match_endbtn);
            bubble_matchinfobtn = findViewById(R.id.bubble_match_infobtn);
            //Onclick listener
            bubble_matchendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedType.equals("romaji")) {
                        // game over for romaji, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                        startActivity(intent);
                        finish();
                    } else {
                        // game over for other type, pass the score and results
                        Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        intent.putExtra("score", score);
                        intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            //Onclick listener
            bubble_matchinfobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.bubble_match_info_popup, null);

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

            // Iterate over the shuffled game elements and create buttons for eword and jword
            for (BubbleMatchButton buttonData : bubbleMatchButton) {
                // Modify the button size
                int buttonWidth = getResources().getDimensionPixelSize(R.dimen.button_width);
                int buttonHeight = getResources().getDimensionPixelSize(R.dimen.button_height);
                int marginInPixels = getResources().getDimensionPixelSize(R.dimen.button_margin);

                // Check if the generated word count has reached the desired limit
                if (generatedWordCount >= 12 * numberOfSelectedTopics ) {
                    break; // Exit the loop if the limit is reached
                }

                RelativeLayout ebuttonContainer = new RelativeLayout(this);

                ImageButton ewordButton = new ImageButton(this);
                ewordButton.setBackgroundResource(R.drawable.button_bubble_match_default);
                generatedButtons.add(ewordButton);
                RelativeLayout.LayoutParams ebuttonLayoutParams = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);

                ebuttonLayoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                ewordButton.setLayoutParams(ebuttonLayoutParams);

                TextView ewordText = new TextView(this);
                ewordText.setText(buttonData.getEword());
                ewordText.setTextSize(25);
                ewordText.setTextColor(Color.WHITE);
                ewordText.setShadowLayer(10, 0, 0, Color.BLUE);

                if (selectedType.equals("romaji")) {
                    // Load the desired font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "comicsans.ttf");
                    // Apply the font to the TextView
                    ewordText.setTypeface(typeface);
                } else {
                    // Load the desired font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "kyo.ttc");
                    // Apply the font to the TextView
                    ewordText.setTypeface(typeface);
                }

                RelativeLayout.LayoutParams etextLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                etextLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                ewordText.setLayoutParams(etextLayoutParams);

                ebuttonContainer.addView(ewordButton);
                ebuttonContainer.addView(ewordText);
                gridLayout.addView(ebuttonContainer);
                generatedWordCount++;

                RelativeLayout jbuttonContainer = new RelativeLayout(this);

                ImageButton jwordButton = new ImageButton(this);
                jwordButton.setBackgroundResource(R.drawable.button_bubble_match_default);
                generatedButtons.add(jwordButton);
                RelativeLayout.LayoutParams jbuttonLayoutParams = new RelativeLayout.LayoutParams(buttonWidth, buttonHeight);
                jbuttonLayoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
                jwordButton.setLayoutParams(jbuttonLayoutParams);

                TextView jwordText = new TextView(this);
                jwordText.setText(buttonData.getJword());
                jwordText.setTextSize(25); // Set the text size to 16 (change the value as needed)
                jwordText.setTextColor(Color.WHITE);
                jwordText.setShadowLayer(10, 0, 0, Color.BLUE);// Set the outline

                if (selectedType.equals("romaji")) {
                    // Load the desired font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "comicsans.ttf");
                    // Apply the font to the TextView
                    jwordText.setTypeface(typeface);
                } else {
                    // Load the desired font from assets
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "kyo.ttc");
                    // Apply the font to the TextView
                    jwordText.setTypeface(typeface);
                }
                RelativeLayout.LayoutParams jtextLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                jtextLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                jwordText.setLayoutParams(jtextLayoutParams);

                jbuttonContainer.addView(jwordButton);
                jbuttonContainer.addView(jwordText);

                gridLayout.addView(jbuttonContainer);
                generatedWordCount++;

                String jsonValue = getJSONValueForButton(buttonData);
                Log.d("Button Clicked", jsonValue);

                // Retrieve the sound name from the button data
                String soundName = buttonData.getSound();

                // Set an OnClickListener for eButton
                ewordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Highlight the button
                        ewordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);

                        // Log the JSON value of the button
                        String jsonValue = getJSONValueForButton(buttonData);
                        Log.d("Button Clicked", jsonValue);

                        if (isFirstSelection == false) {
                            // First button selection
                            ewordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                            eJsonValue = jsonValue;
                            eButton1 = buttonData;
                            isFirstSelection = true;
                            firstSelectedButtonId = view.getId();
                            Log.d("Button Clicked", "First Selection");
                        } else {
                            // Second button selection
                            ewordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                            eButton2 = buttonData;
                            isSecondSelection = true;
                            // Print some debug logs to check the values
                            Log.d("Button Clicked", "Second selection");
                            Log.d("Button Clicked", "eButton2: " + eButton2);
                            Log.d("Button Clicked", "jButton1: " + jButton1);
                            Log.d("Button Clicked", "eButton1: " + eButton1);
                            Log.d("Button Clicked", "jButton2: " + jButton2);
                            Log.d("Button Clicked", "eJsonValue: " + eJsonValue);
                            Log.d("Button Clicked", "jJsonValue: " + jJsonValue);
                            // Perform the matching logic and removal
                            if(eButton1 != null && eButton2 !=null){
                                Log.d("Button Clicked", "They cant match!");
                                wrong +=1;
                                resetButtonBackgrounds();
                                // Reset the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                            if (eButton2 != null && jButton1 != null) {
                                // Compare the JSON values
                                if (getJSONValueForButton(eButton2).equals(getJSONValueForButton(jButton1))) {
                                    Log.d("Button Clicked", "They match!");
                                    jwordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);
                                    ewordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);
                                    //correct
                                    correct +=1;
                                    score +=100;

                                    // Load media player and play sound based on JSON file, if it doesnt load it will display a error log with the file that it failed on
                                    MediaPlayer mediaPlayer = loadAudio(soundName);
                                    if (mediaPlayer != null) {
                                        mediaPlayer.start();
                                        // Release the MediaPlayer object after 10 seconds
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

                                    // Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Remove the matched buttons from the screen
                                            gridLayout.removeView(ebuttonContainer);
                                            gridLayout.removeView(jbuttonContainer);

                                            // Increment the matchesFound count
                                            matchesFound++;

                                            // Check if all matches have been found
                                            if (matchesFound == 6 * numberOfSelectedTopics) {
                                                if (selectedType.equals("romaji")) {
                                                    // game over for romaji, pass the score and results
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // game over for other type, pass the score and results
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }, 2000); // Delay for 2 seconds (2000 milliseconds)
                                } else {
                                    Log.d("Button Clicked", "They don't match!");
                                    ewordButton.setBackgroundResource(R.drawable.button_bubble_match_incorrect);

                                    wrong +=1;

                                    // Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Reset the backgrounds of all buttons
                                            resetButtonBackgrounds();
                                            // Increment the matchesFound count
                                            matchesIncorrect++;
                                            if (firstSelectedButtonId != -1) {
                                                Button firstSelectedButton = findViewById(firstSelectedButtonId);
                                                if (firstSelectedButton != null) {
                                                    firstSelectedButton.setBackgroundResource(R.drawable.button_bubble_match_default);
                                                }
                                            }
                                        }
                                    }, 2000); // Delay for 2 seconds (2000 milliseconds)

                                }

                                // Reset the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                        }
                    }
                });

                // Set an OnClickListener for jButton
                jwordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jwordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                        // Log the JSON value of the button
                        String jsonValue = getJSONValueForButton(buttonData);
                        Log.d("Button Clicked", jsonValue);

                        if (isFirstSelection == false) {
                            // First button selection
                            jwordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                            jJsonValue = jsonValue;
                            jButton1 = buttonData;
                            isFirstSelection = true;
                            firstSelectedButtonId = view.getId();
                            Log.d("Button Clicked", "First Selection");
                        } else {
                            // Second button selection
                            jwordButton.setBackgroundResource(R.drawable.button_bubble_match_selected);
                            jButton2 = buttonData;
                            isSecondSelection = true;
                            // Print some debug logs to check the values
                            Log.d("Button Clicked", "Second selection");
                            Log.d("Button Clicked", "eButton2: " + eButton2);
                            Log.d("Button Clicked", "jButton1: " + jButton1);
                            Log.d("Button Clicked", "eButton1: " + eButton1);
                            Log.d("Button Clicked", "jButton2: " + jButton2);
                            Log.d("Button Clicked", "eJsonValue: " + eJsonValue);
                            Log.d("Button Clicked", "jJsonValue: " + jJsonValue);
                            // Perform the matching logic and removal
                            if(jButton1 != null && jButton2 !=null){
                                Log.d("Button Clicked", "They cant match!");
                                wrong +=1;
                                resetButtonBackgrounds();
                                // Reset the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                            if (eButton1 != null && jButton2 != null) {
                                // Compare the JSON values
                                if (getJSONValueForButton(eButton1).equals(getJSONValueForButton(jButton2))) {
                                    Log.d("Button Clicked", "They match!");
                                    jwordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);
                                    ewordButton.setBackgroundResource(R.drawable.button_bubble_match_correct);
                                    //correct
                                    correct +=1;
                                    score +=100;
                                    // Load media player and play sound based on the sound name
                                    MediaPlayer mediaPlayer = loadAudio(soundName);
                                    if (mediaPlayer != null) {
                                        mediaPlayer.start();
                                        // Release the MediaPlayer object after 10 seconds
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


                                    // Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Remove the matched buttons from the screen
                                            gridLayout.removeView(ebuttonContainer);
                                            gridLayout.removeView(jbuttonContainer);

                                            // Increment the matchesFound count
                                            matchesFound++;

                                            // Check if all matches have been found
                                            if (matchesFound == 6 * numberOfSelectedTopics) {
                                                if (selectedType.equals("romaji")) {
                                                    // game over for romaji, pass the score and results
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // game over for other type, pass the score and results
                                                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                                                    intent.putExtra("correct", correct);
                                                    intent.putExtra("wrong", wrong);
                                                    intent.putExtra("score", score);
                                                    intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }, 1000); // Delay for 2 seconds (2000 milliseconds)
                                } else {
                                    Log.d("Button Clicked", "They don't match!");
                                    jwordButton.setBackgroundResource(R.drawable.button_bubble_match_incorrect);

                                    wrong +=1;
                                    // Delay the removal of the matched buttons
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Reset the backgrounds of all buttons
                                            resetButtonBackgrounds();
                                            // Increment the matchesFound count
                                            matchesIncorrect++;
                                            if (firstSelectedButtonId != -1) {
                                                Button firstSelectedButton = findViewById(firstSelectedButtonId);
                                                if (firstSelectedButton != null) {
                                                    firstSelectedButton.setBackgroundResource(R.drawable.button_bubble_match_default);
                                                }
                                            }
                                        }
                                    }, 2000); // Delay for 2 seconds (2000 milliseconds)
                                }

                                // Reset the stored values
                                eJsonValue = null;
                                jJsonValue = null;
                                eButton1 = null;
                                jButton1 = null;
                                eButton2 = null;
                                jButton2 = null;
                                isFirstSelection = false;
                                isSecondSelection = false;
                                firstSelectedButtonId = -1;
                            }
                        }
                    }
                });
            }
        }

        // Shuffle the buttons within the grid layout
        List<View> allButtons = new ArrayList<>();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            allButtons.add(child);
        }

        Collections.shuffle(allButtons);
        gridLayout.removeAllViews();
        for (View button : allButtons) {
            gridLayout.addView(button);
        }
    }
    // Method to reset the backgrounds of all buttons
    private void resetButtonBackgrounds() {
        Log.d("ResetButtonBackgrounds", "Running!");
        // Iterate through all buttons in the generatedButtons list and set their backgrounds to default
        for (ImageButton button : generatedButtons) {
            button.setBackgroundResource(R.drawable.button_bubble_match_default);
        }
    }

    private void startCountdown(String selectedType, int numberOfSelectedTopics) {
        countDownTimer = new CountDownTimer(60000 * numberOfSelectedTopics, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                countdownTextView.setTextColor(Color.WHITE);
                countdownTextView.setText("" + secondsRemaining);
            }

            public void onFinish() {

                if (selectedType.equals("romaji")) {
                    // game over for romaji, pass the score and results
                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    intent.putExtra("background", R.drawable.background_bubble_match_end_romaji_phone);
                    startActivity(intent);
                    finish();
                } else {
                    // game over for other type, pass the score and results
                    Intent intent = new Intent(getApplicationContext(), BubbleMatchEndPage.class);
                    intent.putExtra("correct", correct);
                    intent.putExtra("wrong", wrong);
                    intent.putExtra("score", score);
                    intent.putExtra("background", R.drawable.background_bubble_match_end_kana_phone);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountdown();
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