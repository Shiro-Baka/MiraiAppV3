package com.example.miraiappv2;

public class KCQuestionItem {

    private String question, answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8,answer9, answer10, correct;

    public KCQuestionItem(String question, String answer1, String answer2, String answer3, String answer4,String answer5, String answer6,String answer7, String answer8,String answer9, String answer10, String correct) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.answer6 = answer6;
        this.answer7 = answer7;
        this.answer8 = answer8;
        this.answer9 = answer9;
        this.answer10 = answer10;
        this.correct = correct;
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

    public String getAnswer5() {
        return answer5;
    }

    public String getAnswer6() {
        return answer6;
    }

    public String getAnswer7() {
        return answer7;
    }

    public String getAnswer8() {
        return answer8;
    }

    public String getAnswer9() {
        return answer9;
    }

    public String getAnswer10() {return answer10;}

    public String getCorrect() {
        return correct;
    }

}
