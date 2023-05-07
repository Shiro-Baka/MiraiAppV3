package com.example.miraiappv2;

public class MagicTroubleQuestionItem {
    //Declare string to store the question text, answer options, correct answer, and topic.
    String question, answer1, answer2, answer3, answer4, correct, topic, type, sound;


    //Constructor that sets all the fields of a MagicTroubleQuestionItem object.
    public MagicTroubleQuestionItem(String question, String answer1, String answer2, String answer3, String answer4, String correct, String topic, String type, String sound){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correct = correct;
        this.topic = topic;
        this.type = type;
        this.sound = sound;
    }

    //Get methods for accessing the fields of a MagicTroubleQuestionItem.
    public String getQuestion() {
        return question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getCorrect() {
        return correct;
    }

    public String getTopic() {
        return topic;
    }

    public String getType() {
        return type;
    }

    public String getSound() {
        return sound;
    }
}
