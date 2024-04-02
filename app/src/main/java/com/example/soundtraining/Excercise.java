package com.example.soundtraining;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.util.HashSet;
import java.util.Random;

public abstract class Excercise extends Fragment {
    public Excercise(int layout) {
        super(layout);
    }

    protected View mainView;
    protected String[] soundNames;
    protected Integer[] choices;
    protected int m, n;
    protected GridLayout buttonsMatrix;
    protected TextView currentInformations;
    protected UserMessages messageSystem;

    protected enum programMode {
        BEFORE_PLAY, PLAYING, STOPPED, ANSWERED
    }

    protected programMode mode;
    protected int correctAnswer;
    protected int userAnswer;
    protected int buttonCounter;
    protected int buttonId;
    protected MediaPlayer mediaPlayer;
    protected AssetManager assetManager;

    protected abstract void playSound(String sound);

    protected abstract void stopSound();

    protected void generateAndInsertRandomlyChosenSounds(int fieldAmount) {
        choices = generateRandomChoices(fieldAmount);
        for (int buttonCounter = 0; buttonCounter < fieldAmount; buttonCounter++) {
            int buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
            ((Button) mainView.findViewById(buttonId)).setText(soundNames[choices[buttonCounter]]);
        }
    }

    protected Integer[] generateRandomChoices(int fieldAmount) {
        Random r = new Random();
        HashSet<Integer> numbers = new HashSet<>();

        while (fieldAmount != numbers.size()) {
            for (int i = 0; i < fieldAmount; i++) {
                numbers.add(r.nextInt(soundNames.length));
            }
        }
        return numbers.toArray(new Integer[numbers.size()]);
    }

    protected void insertAParticularNumberOfButtonsIntoGridLayout(int m, int n) {
        buttonsMatrix = mainView.findViewById(R.id.buttonsMatrix);
        buttonsMatrix.setColumnCount(n);
        buttonsMatrix.setRowCount(m);

        int buttonCounter = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Button button = new Button(mainView.getContext());
                int buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
                button.setId(buttonId);
                buttonsMatrix.addView(button);
                buttonCounter++;
            }
        }

        buttonsMatrix.post(new Runnable() {
            @Override
            public void run() {
                int buttonWidth = buttonsMatrix.getWidth() / n;
                int buttonHeight = buttonsMatrix.getHeight() / m;

                for (int i = 0; i < buttonsMatrix.getChildCount(); i++) {
                    Button button = (Button) buttonsMatrix.getChildAt(i);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = buttonWidth;
                    params.height = buttonHeight;
                    params.setMargins(2, 2, 2, 2);
                    params.setGravity(Gravity.FILL);

                    button.setLayoutParams(params);
                }

                generateAndInsertRandomlyChosenSounds(m * n);
            }
        });
    }

    protected void setButtonsClickedBehaviour(int fieldAmount) {
        ((ImageButton) mainView.findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.button_pressed_animation);
                ((ImageButton) mainView.findViewById(R.id.play)).startAnimation(animation);
                playSound("sounds/" + soundNames[choices[correctAnswer]] + ".ogg");
            }
        });
        ((ImageButton) mainView.findViewById(R.id.stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.button_pressed_animation);
                ((ImageButton) mainView.findViewById(R.id.stop)).startAnimation(animation);
                stopSound();
            }
        });
        buttonsMatrix.post(new Runnable() {

            @Override
            public void run() {
                for (buttonCounter = 0; buttonCounter < fieldAmount; buttonCounter++) {
                    buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
                    ((Button) mainView.findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userAnswer = buttonCounter;
                            mode = programMode.ANSWERED;
                            if (checkUserAnswer()) {
                                ((Button) mainView.findViewById(buttonId)).setBackgroundResource(R.color.green);
                                messageSystem.points += 10;
                                messageSystem.correctAnswer();
                            } else {
                                ((Button) mainView.findViewById(buttonId)).setBackgroundResource(R.color.red);
                                buttonId = getResources().getIdentifier("button_" + correctAnswer, "id", mainView.getContext().getPackageName());
                                ((Button) mainView.findViewById(buttonId)).setBackgroundResource(R.color.green);
                                messageSystem.incorrectAnswer(soundNames[choices[correctAnswer]]);
                            }
                        }
                    });
                }
            }
        });

    }

    protected void lockButtons() {

    }

    protected void unlockButtons() {

    }

    protected boolean checkUserAnswer() {
        return userAnswer == correctAnswer;
    }

    protected void generateACorrectAnswer(int fieldAmount) {
        Random r = new Random();
        correctAnswer = r.nextInt(fieldAmount);
    }
}
