package com.example.miraiappv2;

public class VocabularyConfusionQuestionItem {
    private String question, answer1, answer2, answer3, answer4, correct, sound;

    public VocabularyConfusionQuestionItem(String question, String answer1, String answer2, String answer3, String answer4, String correct, String sound) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correct = correct;
        this.sound = sound;
    }

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

    public String getSound() {
        return sound;
    }
}
