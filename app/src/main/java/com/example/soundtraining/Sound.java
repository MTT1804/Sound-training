package com.example.soundtraining;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class Sound extends Fragment {
    private View mainView;
    private String [] soundNames;
    private Integer [] choices;
    private int m, n;
    public Sound(int m, int n) {
        super(R.layout.sound);
        this.m = m;
        this.n = n;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.sound, container, false);
        searchForSoundsAndGetSoundsNames();
        insertAParticularNumberOfButtonsIntoGridLayout(m,n);
        return mainView;
    }

    public void generateAndInsertRandomlyChosenSounds(int fieldAmount) {
        choices = generateRandomChoices(fieldAmount);
        for (int buttonCounter = 0; buttonCounter < fieldAmount; buttonCounter++) {
            int buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
            ((Button)mainView.findViewById(buttonId)).setText(soundNames[choices[buttonCounter]]);
        }
    }

    public Integer [] generateRandomChoices(int fieldAmount){
        Random r = new Random();
        HashSet<Integer>numbers = new HashSet<>();

        while (fieldAmount != numbers.size()) {
            for (int i = 0; i < soundNames.length; i++){
                numbers.add(r.nextInt(fieldAmount));
            }
        }
        return numbers.toArray(new Integer[numbers.size()]);
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

    public void insertAParticularNumberOfButtonsIntoGridLayout(int m, int n) {
        final GridLayout buttonsMatrix = mainView.findViewById(R.id.buttonsMatrix);
        buttonsMatrix.setColumnCount(n);
        buttonsMatrix.setRowCount(m);
        buttonsMatrix.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int buttonWidth = buttonsMatrix.getWidth() / n;
                int buttonHeight = buttonsMatrix.getHeight() / m;
                int buttonCounter = 0;
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        Button button = new Button(mainView.getContext());
                        int buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
                        button.setId(buttonId);
                        button.setText("Przycisk " + ((i * n) + j + 1));
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = buttonWidth;
                        params.height = buttonHeight;
                        params.setMargins(2, 2, 2, 2);
                        params.setGravity(Gravity.FILL);
                        button.setLayoutParams(params);
                        buttonsMatrix.addView(button);
                        buttonCounter++;
                    }
                }
                generateAndInsertRandomlyChosenSounds(m*n);
                buttonsMatrix.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


}
