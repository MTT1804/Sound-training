package com.example.soundtraining;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;
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

    View mainView;
    String[] soundNames;
    Integer[] choices;
    int m, n;
    GridLayout buttonsMatrix;
    TextView currentInformations;
    UserMessages messageSystem;

    enum programMode {
        BEFORE_PLAY, PLAYING, STOPPED, ANSWERED
    }

    programMode mode;
    int correctAnswer;
    int userAnswer;
    int buttonCounter, buttonCounter2;
    int buttonId;
    MediaPlayer mediaPlayer;
    AssetManager assetManager;
    boolean deactivateAllButtons = false;

    abstract void playSound(String sound);

    abstract void stopSound();

    void setDefaultColorsOfButtons() {
        buttonsMatrix.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < buttonsMatrix.getChildCount(); i++) {
                    Button buttonChosen = (Button) buttonsMatrix.getChildAt(i);
                    buttonChosen.setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        });
    }

    void generateAndInsertRandomlyChosenSounds(int fieldAmount) {
        choices = generateRandomChoices(fieldAmount);
        generateACorrectAnswer(fieldAmount);
        for (int buttonCounter = 0; buttonCounter < fieldAmount; buttonCounter++) {
            int buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
            ((Button) mainView.findViewById(buttonId)).setText(soundNames[choices[buttonCounter]]);
        }
    }

    Integer[] generateRandomChoices(int fieldAmount) {
        Random r = new Random();
        HashSet<Integer> numbers = new HashSet<>();

        while (fieldAmount != numbers.size()) {
            for (int i = 0; i < fieldAmount; i++) {
                numbers.add(r.nextInt(soundNames.length));
            }
        }
        return numbers.toArray(new Integer[numbers.size()]);
    }

    void insertAParticularNumberOfButtonsIntoGridLayout(int m, int n) {
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

    void setButtonsClickedBehaviour(int fieldAmount) {
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
        ((ImageButton) mainView.findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.button_pressed_animation);
                ((ImageButton) mainView.findViewById(R.id.next)).startAnimation(animation);
                stopSound();
                buttonsMatrix.post(new Runnable() {
                    @Override
                    public void run() {
                        setDefaultColorsOfButtons();
                        generateAndInsertRandomlyChosenSounds(m * n);
                        deactivateAllButtons = false;
                        messageSystem.pressAButtonToPlayASound();
                    }
                });


            }
        });
        buttonsMatrix.post(new Runnable() {

            @Override
            public void run() {
                for (buttonCounter = 0; buttonCounter < buttonsMatrix.getChildCount(); buttonCounter++) {
                    final int currentButtonIndex = buttonCounter;
                    Button buttonChosen = (Button) buttonsMatrix.getChildAt(buttonCounter);
                    buttonChosen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!deactivateAllButtons) {
                                deactivateAllButtons = true;
                                userAnswer = currentButtonIndex;
                                Log.i("USERANSWER", userAnswer +"");
                                Log.i("CORRECTANSWER", correctAnswer +"");
                                mode = programMode.ANSWERED;
                                if (checkUserAnswer()) {
                                    buttonChosen.setBackgroundResource(R.color.green);
                                    messageSystem.correctAnswer();
                                } else {
                                    buttonChosen.setBackgroundResource(R.color.red);
                                    Button buttonCorrect = (Button) buttonsMatrix.getChildAt(correctAnswer);
                                    buttonCorrect.setBackgroundResource(R.color.green);
                                    messageSystem.incorrectAnswer(soundNames[choices[correctAnswer]]);
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    void lockButtons() {

    }

    void unlockButtons() {

    }

    boolean checkUserAnswer() {
        return userAnswer == correctAnswer;
    }

    void generateACorrectAnswer(int fieldAmount) {
        Random r = new Random();
        correctAnswer = r.nextInt(fieldAmount);
    }
}
