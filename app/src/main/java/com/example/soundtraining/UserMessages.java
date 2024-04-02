package com.example.soundtraining;

import android.widget.TextView;

public class UserMessages {
    int points;
    TextView currentInformations;
    public void setPoints(int points) {
        this.points = points;
    }
    public UserMessages(TextView currentInformations){
        this.currentInformations = currentInformations;
        this.points = 0;
    }
    public void setText(String newText){
        currentInformations.setText(newText);
    }
    public void correctAnswer(){
        points+=10;
        currentInformations.setText("Brawo! Poprawna odpowiedź. \nPunkty:" + points);
    }
    public void incorrectAnswer(String correctAnswer){
        points-=10;
        currentInformations.setText("Niepoprawna odpowiedź!. Poprawna opowiedź: " + correctAnswer + "\nPunkty: " + points);
    }
    public void pressAButtonToPlayASound(){
        currentInformations.setText("Naciśnij zielony przycisk aby odtworzyć dźwięk. \nPunkty: " + points);
    }
    public void soundPaused(){
        currentInformations.setText("Dźwięk został zatrzymany. Naciśnij zielony przycisk aby odtworzyć dźwięk. \nPunkty: " + points);
    }
}
