package com.example.soundtraining;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Speech extends Excercise {
    // extra info: 2 - use stories database, 0 - use default settings, 3 - similar words mode & use 1st database, 4 - similar words mode & use 2nd database
    public Speech(int m, int n, int extra) {
        super(R.layout.sound);
        this.m = m;
        this.n = n;
        this.extra = extra;
    }
    TextToSpeech textToSpeech;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.sound, container, false);
        this.assetManager = mainView.getContext().getAssets();
        currentInformations = mainView.findViewById(R.id.currentInformations);
        messageSystem = new UserMessages(currentInformations, mainView.getContext());
        initializeTextToSpeech();
        if (MainMenu.mode == MainMenu.ProgramMode.TEXT || MainMenu.mode == MainMenu.ProgramMode.INPUT) {
            searchForSoundsAndGetSoundsNames();
        } else if (MainMenu.mode == MainMenu.ProgramMode.IMAGES)  {
            searchForImagesAndGetImagesNames();
        }
        if (MainMenu.mode != MainMenu.ProgramMode.SPEECH_SYNTHESIZER) setListForRandomGenerator();
        if (MainMenu.mode == MainMenu.ProgramMode.TEXT || MainMenu.mode == MainMenu.ProgramMode.IMAGES) {
            insertAParticularNumberOfButtonsIntoGridLayout(m, n);
            setButtonsClickedBehaviour(m * n);
            setDefaultColorsOfButtons();
        } else if (MainMenu.mode == MainMenu.ProgramMode.INPUT) {
            addTextViewAndEditTextToGridLayout();
            messageSystem.pressAButtonAndInputText();
            setButtonsClickedBehaviour(0);
            setAnswerChecker();
            generateACorrectAnswer(soundNames.length);
            Log.i("CORRECT_ANSWER",soundNames[correctAnswer]);
        } else if (MainMenu.mode == MainMenu.ProgramMode.SPEECH_SYNTHESIZER){
            messageSystem.pressAButtonAndProgramWillReadYourInput();
            addATextFieldForSpeechSynthesizer();
            setButtonsClickedBehaviour(0);
        }
        return mainView;
    }
    void addATextFieldForSpeechSynthesizer(){
        buttonsMatrix = mainView.findViewById(R.id.buttonsMatrix);
        buttonsMatrix.post(new Runnable() {
            @Override
            public void run() {
                ScrollView scrollView = new ScrollView(mainView.getContext());
                LinearLayout linearLayout = new LinearLayout(mainView.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(16, 16, 16, 16);
                linearLayout.setLayoutParams(layoutParams);
                EditText editText = new EditText(mainView.getContext());
                editText.setId(R.id.inputTextFieldSynthesizer);
                editText.setTextSize(24);
                editText.setHint("Wpisz tekst tutaj");
                editText.setGravity(Gravity.CENTER);
                editText.setBackground(ContextCompat.getDrawable(mainView.getContext(), R.drawable.edittext_background));
                linearLayout.addView(editText);
                scrollView.addView(linearLayout);
                GridLayout gridLayout = mainView.findViewById(R.id.buttonsMatrix);
                gridLayout.addView(scrollView);
            }
        });
    }

    public void searchForSoundsAndGetSoundsNames(){
        AssetManager assetManager = mainView.getContext().getAssets();
        ArrayList<String>wordsDatabase = new ArrayList<>();
        int i = 0;
        try {
            InputStreamReader inputStreamReader = null;
            if (!MainMenu.similarWordsModeOn) inputStreamReader = new InputStreamReader(assetManager.open("text/words"));
            else {
                if (MainMenu.whichDatabaseToUseInSimilarWordsModule == 1) inputStreamReader = new InputStreamReader(assetManager.open("text/similar_words1"));
                else inputStreamReader = new InputStreamReader(assetManager.open("text/similar_words2"));
            }
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                wordsDatabase.add(line);
            }

            bufferedReader.close();
            soundNames = new String[wordsDatabase.size()];
            soundNames = wordsDatabase.toArray(soundNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void searchForImagesAndGetImagesNames(){
        AssetManager assetManager = mainView.getContext().getAssets();
        try {
            if(extra != 2) soundNames = assetManager.list("images_speech");
            else soundNames = assetManager.list("images_stories");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < soundNames.length; i++){
            soundNames[i] = soundNames[i].replace(".jpg", "");
        }

    }

    void initializeTextToSpeech(){
        textToSpeech = new TextToSpeech(mainView.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {

                }
            }
        });
    }
    @Override
    protected void playSound(String sound){
        if (textToSpeech != null) {
            textToSpeech.speak(sound, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    @Override
    protected void stopSound() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
    @Override
    protected void playFrequencySound(int frequency) {
    }

    @Override
    protected void stopFrequencySound() {
    }

}
