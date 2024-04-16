package com.example.soundtraining;

import android.content.Context;
import android.widget.TextView;

public class UserMessages {
    public static int points;
    TextView currentInformations;
    Context context;

    public void setPoints(int points) {
        this.points = points;
    }

    public UserMessages(TextView currentInformations, Context context) {
        this.currentInformations = currentInformations;
        this.context = context;
        this.points = FileUtils.readPointsFromFile(context, "points");
    }

    public void setText(String newText) {
        currentInformations.setText(newText);
    }

    public void correctAnswer() {
        points += 10;
        FileUtils.savePointsToFile(context, points, "points");
        currentInformations.setText("Brawo! Poprawna odpowiedź. \nPunkty:" + points);
    }

    public void incorrectAnswer(String correctAnswer) {
        points -= 10;
        FileUtils.savePointsToFile(context, points, "points");
        currentInformations.setText("Niepoprawna odpowiedź!. Poprawna opowiedź: " + correctAnswer + "\nPunkty: " + points);
    }

    public void pressAButtonToPlayASound() {
        currentInformations.setText("Naciśnij zielony przycisk aby odtworzyć dźwięk. \nPunkty: " + points);
    }

    public void soundPaused() {
        currentInformations.setText("Dźwięk został zatrzymany. Naciśnij zielony przycisk aby odtworzyć dźwięk. \nPunkty: " + points);
    }

    public void pressAButtonAndInputText() {
        currentInformations.setText("Naciśnij zielony przycisk a następnie wpisz w pole tekstowe Twoją odpowiedź. \nPunkty: " + points);
    }

    public void stillWrong() {
        currentInformations.setText("Wpisany tekst nadal nie pasuje! Próbuj dalej. \nPunkty: " + points);
    }

    public void pointsHaveBeenAlreadyAssigned() {
        currentInformations.setText("Punkty za poprawną odpowiedź zostały już przypisane. Naciśnij niebieski przycisk aby przejść do kolejnego dźwięku. \nPunkty: " + points);
    }
    public void pressAButtonAndProgramWillReadYourInput(){
        currentInformations.setText("Wpisz w pole tekstowe twój tekst który zostanie odczytany przez syntezator mowy po wciśnięciu zielonego przycisku.");
    }
}
