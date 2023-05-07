package com.example.miraiappv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.os.Handler;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class BubbleMatchGamePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Removes the Title bar from the top of the application for all screens.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bubble_match_game_page);


    }
    //make list with all the questions
    private void loadBubbleTopic(ArrayList<String> selectedTopics, String selectedType) {
        bubbleMatchQuestionItems = new ArrayList<>();

        //load questions into json string
        String jsonStr = loadJSONFromAsset("bubble.json");

        //load all data into the list
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray questions = jsonObj.getJSONArray("questions");

            for (int i = 0; i < questions.length(); i++) {
                JSONObject question = questions.getJSONObject(i);

                String idString = question.getString("id");
                String ewordString = question.getString("eword");
                String jwordString = question.getString("jword");
                String topicString = question.getString("topic");
                String typeString = question.getString("type");
                String soundString = question.getString("sound");

                // Check if the question topic is one of the selected topics
                if (selectedTopics.contains(topicString)&& selectedType.equals(typeString)) {
                    bubbleMatchQuestionItems.add(new bubbleMatchQuestionItem(
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
