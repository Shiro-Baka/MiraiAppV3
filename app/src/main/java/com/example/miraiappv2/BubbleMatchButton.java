package com.example.miraiappv2;

public class BubbleMatchButton {
        //Declare string to store the question text, answer options, correct answer, and topic.
        String id, eword, jword, topic, type, sound;

        //Constructor that sets all the fields of a MagicTroubleQuestionItem object.
        public BubbleMatchButton(String id, String eword, String jword, String topic, String type, String sound){
            this.id = id;
            this.eword = eword;
            this.jword = jword;
            this.topic = topic;
            this.type = type;
            this.sound = sound;
        }

        //Get methods for accessing the fields of a MagicTroubleQuestionItem.

        public String getId() {
            return id;
        }

        public String getEword() {
            return eword;
        }

        public String getJword() {
            return jword;
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
