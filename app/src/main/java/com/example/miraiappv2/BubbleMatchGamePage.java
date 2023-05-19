package com.example.miraiappv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.GridLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BubbleMatchGamePage extends AppCompatActivity {
    //Create media player for sounds
    MediaPlayer BubbleMatchMediaPlayer;

    //Declare a list of MagicTroubleQuestionItem objects to store the questions and their answers
    List<BubbleMatchButton> bubbleMatchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bubble_match_game_page);

        GridLayout gridLayout = findViewById(R.id.grid_layout_bubble_match);

        bubbleMatchButton= new ArrayList<BubbleMatchButton>();

        // Retrieve the selectedTopics and selectedType values from the intent
        Intent intent = getIntent();
        ArrayList<String> selectedTopics = intent.getStringArrayListExtra("selected_topics");
        String selectedType = intent.getStringExtra("selected_type");

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
            final int[] firstSelectedButtonId = {-1};
            final int[] previousSelectedButtonId = {-1};

            // Iterate over the shuffled game elements and create buttons for eword and jword
            for (BubbleMatchButton buttonData : bubbleMatchButton) {
                // Modify the button size
                int buttonWidth = 200; // Replace 200 with your desired button width in pixels
                int buttonHeight = 100; // Replace 100 with your desired button height in pixels

                // Check if the generated word count has reached the desired limit
                if (generatedWordCount >= 16) {
                    break; // Exit the loop if the limit is reached
                }

                Button ewordButton = new Button(this);
                ewordButton.setText(buttonData.getEword()); // Display the eword on the button
                ewordButton.setId(Integer.parseInt(buttonData.getId())); // Set an ID for the eword button

                // Remove the button from its current parent (if any)
                ViewGroup parentEword = (ViewGroup) ewordButton.getParent();
                if (parentEword != null) {
                    parentEword.removeView(ewordButton);
                }

                // Set the layout parameters for the eword button
                GridLayout.LayoutParams ewordParams = new GridLayout.LayoutParams();
                ewordParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 0.33f); // Set column weight to 0.33
                ewordParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                ewordParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                ewordButton.setLayoutParams(ewordParams);

                gridLayout.addView(ewordButton); // Add the eword button to the grid layout
                generatedWordCount++;

                Button jwordButton = new Button(this);
                jwordButton.setText(buttonData.getJword()); // Display the jword on the button
                jwordButton.setId(-Integer.parseInt(buttonData.getId())); // Set a negative ID for the jword button

                // Remove the button from its current parent (if any)
                ViewGroup parentJword = (ViewGroup) jwordButton.getParent();
                if (parentJword != null) {
                    parentJword.removeView(jwordButton);
                }

                // Set the layout parameters for the jword button
                GridLayout.LayoutParams jwordParams = new GridLayout.LayoutParams();
                jwordParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 0.33f); // Set column weight to 0.33
                jwordParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                jwordParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                jwordButton.setLayoutParams(jwordParams);

                gridLayout.addView(jwordButton); // Add the jword button to the grid layout
                generatedWordCount++;

                // Set an OnClickListener for each button
                ewordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentButtonId = view.getId();
                        Button currentButton = (Button) view;

                        if (firstSelectedButtonId[0] == -1) {
                            // This is the first button selected
                            firstSelectedButtonId[0] = currentButtonId;

                            // Highlight the button (change background color, for example)
                            currentButton.setBackgroundColor(Color.YELLOW);
                        } else {
                            // This is the second button selected
                            if (Math.abs(currentButtonId) == Math.abs(previousSelectedButtonId[0])) {
                                // Button IDs match, perform desired action
                                for (BubbleMatchButton buttonData : bubbleMatchButton) {
                                    if (buttonData.getId().equals(String.valueOf(Math.abs(currentButtonId)))) {
                                        String selectedIdFromJson = buttonData.getId();
                                        // Use the selected ID from the JSON file as needed
                                        Log.d("Match", "Button IDs match!");

                                        // Hide the matched buttons
                                        currentButton.setVisibility(View.INVISIBLE);
                                        Button previousButton = findViewById(Math.abs(previousSelectedButtonId[0]));
                                        previousButton.setVisibility(View.INVISIBLE);



                                        // Reset the selected button IDs
                                        firstSelectedButtonId[0] = -1;
                                        previousSelectedButtonId[0] = -1;

                                        break;
                                    }
                                }
                            } else {
                                // Button IDs don't match, perform desired action
                                Log.d("Match", "Button IDs don't match!");

                                // Reset the selected button IDs
                                firstSelectedButtonId[0] = -1;
                                previousSelectedButtonId[0] = -1;

                                // Reset the visual state of the buttons (remove highlighting)
                                currentButton.setBackgroundColor(Color.TRANSPARENT);
                                Button previousButton = findViewById(Math.abs(previousSelectedButtonId[0]));
                                previousButton.setBackgroundColor(Color.TRANSPARENT);
                            }

                            // Reset the variables for the next selection
                            firstSelectedButtonId[0] = -1;
                            previousSelectedButtonId[0] = -1;
                        }

                        // Update the previous selected button ID
                        previousSelectedButtonId[0] = currentButtonId;
                    }
                });


                jwordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentButtonId = view.getId();
                        Button currentButton = (Button) view;

                        if (firstSelectedButtonId[0] == -1) {
                            // This is the first button selected
                            firstSelectedButtonId[0] = currentButtonId;

                            // Highlight the button (change background color, for example)
                            currentButton.setBackgroundColor(Color.YELLOW);
                        } else {
                            // This is the second button selected
                            if (Math.abs(currentButtonId) == Math.abs(previousSelectedButtonId[0])) {
                                // Button IDs match, perform desired action
                                for (BubbleMatchButton buttonData : bubbleMatchButton) {
                                    if (buttonData.getId().equals(String.valueOf(Math.abs(currentButtonId)))) {
                                        String selectedIdFromJson = buttonData.getId();
                                        // Use the selected ID from the JSON file as needed
                                        Log.d("Match", "Button IDs match!");

                                        // Hide the matched buttons
                                        currentButton.setVisibility(View.INVISIBLE);
                                        Button previousButton = findViewById(Math.abs(previousSelectedButtonId[0]));
                                        previousButton.setVisibility(View.INVISIBLE);


                                        // Reset the selected button IDs
                                        firstSelectedButtonId[0] = -1;
                                        previousSelectedButtonId[0] = -1;

                                        break;
                                    }
                                }
                            } else {
                                // Button IDs don't match, perform desired action
                                Log.d("Match", "Button IDs don't match!");

                                // Reset the selected button IDs
                                firstSelectedButtonId[0] = -1;
                                previousSelectedButtonId[0] = -1;

                                // Reset the visual state of the buttons (remove highlighting)
                                currentButton.setBackgroundColor(Color.TRANSPARENT);
                                Button previousButton = findViewById(Math.abs(previousSelectedButtonId[0]));
                                previousButton.setBackgroundColor(Color.TRANSPARENT);
                            }

                            // Reset the variables for the next selection
                            firstSelectedButtonId[0] = -1;
                            previousSelectedButtonId[0] = -1;
                        }

                        // Update the previous selected button ID
                        previousSelectedButtonId[0] = currentButtonId;
                    }
                });

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
