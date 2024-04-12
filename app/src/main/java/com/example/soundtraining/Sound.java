package com.example.soundtraining;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public class Sound extends Excercise {
    public Sound(int m, int n) {
        super(R.layout.sound);
        this.m = m;
        this.n = n;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.sound, container, false);
        this.assetManager = mainView.getContext().getAssets();
        currentInformations = mainView.findViewById(R.id.currentInformations);
        messageSystem = new UserMessages(currentInformations);
        searchForSoundsAndGetSoundsNames();
        if (MainMenu.mode != MainMenu.ProgramMode.INPUT) {
            insertAParticularNumberOfButtonsIntoGridLayout(m, n);
            setButtonsClickedBehaviour(m * n);
            setDefaultColorsOfButtons();
        } else {
            addTextViewAndEditTextToGridLayout();
            messageSystem.pressAButtonAndInputText();
            setButtonsClickedBehaviour(0);
            setAnswerChecker();
            generateACorrectAnswer(soundNames.length);
            Log.i("CORRECT_ANSWER",soundNames[correctAnswer]);
        }
        return mainView;
    }
    public void searchForSoundsAndGetSoundsNames(){
        AssetManager assetManager = mainView.getContext().getAssets();
        try {
            soundNames = assetManager.list("sounds");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < soundNames.length; i++){
            soundNames[i] = soundNames[i].replace(".ogg", "");
        }

    }
    @Override
    protected void playSound(String sound){
        Log.i("GRAM",sound);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor descriptor = assetManager.openFd(sound);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
