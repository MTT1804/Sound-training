package com.example.soundtraining;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    int correctAnswer;
    int userAnswer;
    int buttonCounter;
    MediaPlayer mediaPlayer;
    AssetManager assetManager;
    boolean deactivateAllButtons = false;
    EditText editText;
    boolean lockPoints = false;
    List<Integer> numbers;
    SeekBar frequencyBar;
    TextView frequencyText;
    int correctFrequency;
    int userFrequency;
    int extra;

    abstract void playSound(String sound);

    abstract void stopSound();

    abstract void playFrequencySound(int frequency);

    abstract void stopFrequencySound();

    void addTextViewAndEditTextToGridLayout() {
        buttonsMatrix = mainView.findViewById(R.id.buttonsMatrix);
        buttonsMatrix.post(new Runnable() {
            @Override
            public void run() {
                GridLayout gridLayout = mainView.findViewById(R.id.buttonsMatrix);

                TextView textView = new TextView(mainView.getContext());
                textView.setText("Wpisz swoją odpowiedź poniżej. Komunikat na dole ekranu poinformuje cię o poprawności odpowiedzi.");
                textView.setTextSize(24);
                textView.setGravity(Gravity.CENTER);

                editText = new EditText(mainView.getContext());
                editText.setTextSize(24);
                editText.setHint("Wpisz tekst tutaj");
                editText.setGravity(Gravity.CENTER);
                editText.setPadding(0, 20, 0, 0);

                GridLayout.LayoutParams textParams = new GridLayout.LayoutParams();
                textParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
                textParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                textParams.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textParams.rowSpec = GridLayout.spec(0, 1f);
                textParams.columnSpec = GridLayout.spec(0, 1f);

                GridLayout.LayoutParams editParams = new GridLayout.LayoutParams();
                editParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
                editParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                editParams.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                editParams.rowSpec = GridLayout.spec(1, 1f);
                editParams.columnSpec = GridLayout.spec(0, 1f);

                gridLayout.addView(textView, textParams);
                gridLayout.addView(editText, editParams);
            }
        });

    }

    void setAnswerChecker() {
        buttonsMatrix.post(new Runnable() {
            @Override
            public void run() {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (checkUserAnswerString(s.toString())) {
                            if (!lockPoints) {
                                messageSystem.correctAnswer();
                                lockPoints = true;
                            } else {
                                messageSystem.pointsHaveBeenAlreadyAssigned();
                            }
                        } else {
                            messageSystem.stillWrong();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

    }

    void setDefaultColorsOfButtons() {
        if (MainMenu.mode == MainMenu.ProgramMode.TEXT) {
            buttonsMatrix.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < buttonsMatrix.getChildCount(); i++) {
                        Button buttonChosen = (Button) buttonsMatrix.getChildAt(i);
                        buttonChosen.setBackgroundResource(android.R.drawable.btn_default);
                    }
                }
            });
        } else if (MainMenu.mode == MainMenu.ProgramMode.IMAGES) {
            buttonsMatrix.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < buttonsMatrix.getChildCount(); i++) {
                        ImageButton buttonChosen = (ImageButton) buttonsMatrix.getChildAt(i);
                        buttonChosen.setBackgroundColor(ContextCompat.getColor(mainView.getContext(), android.R.color.transparent));
                    }
                }
            });
        }
    }

    void generateAndInsertRandomlyChosenSounds(int fieldAmount) {
        if (!MainMenu.similarWordsModeOn) choices = generateRandomChoices(fieldAmount);
        else choices = generateRandomChoicesSimilarWords(fieldAmount);
        generateACorrectAnswer(fieldAmount);
        for (int buttonCounter = 0; buttonCounter < fieldAmount; buttonCounter++) {
            int buttonId = getResources().getIdentifier("button_" + buttonCounter, "id", mainView.getContext().getPackageName());
            if (MainMenu.mode == MainMenu.ProgramMode.TEXT) {
                ((Button) mainView.findViewById(buttonId)).setText(soundNames[choices[buttonCounter]]);
            } else if (MainMenu.mode == MainMenu.ProgramMode.IMAGES) {
                ImageButton imageButton = ((ImageButton) mainView.findViewById(buttonId));
                try {
                    InputStream inputStream = null;
                    if (MainMenu.emode == MainMenu.ExcerciseMode.SOUND)
                        inputStream = mainView.getContext().getAssets().open("images_sounds/" + soundNames[choices[buttonCounter]] + ".jpg");
                    else if (MainMenu.emode == MainMenu.ExcerciseMode.SPEECH)
                        if(extra != 2) inputStream = mainView.getContext().getAssets().open("images_speech/" + soundNames[choices[buttonCounter]] + ".jpg");
                        else inputStream = mainView.getContext().getAssets().open("images_stories/" + soundNames[choices[buttonCounter]] + ".jpg");
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    int targetWidth = imageButton.getWidth();
                    int targetHeight = imageButton.getHeight();

                    float scale = Math.min((float) targetWidth / bitmap.getWidth(), (float) targetHeight / bitmap.getHeight());

                    int scaledWidth = Math.round(scale * bitmap.getWidth());
                    int scaledHeight = Math.round(scale * bitmap.getHeight());

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
                    imageButton.setImageBitmap(scaledBitmap);
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Integer[] generateRandomChoices(int fieldAmount) {
        Collections.shuffle(numbers);

        return numbers.subList(0, fieldAmount).toArray(new Integer[fieldAmount]);
    }
    Integer[] generateRandomChoicesSimilarWords(int fieldAmount) {
        int howManyGroups = soundNames.length / fieldAmount;
        int whichGroupOfWordsWeUse = new Random().nextInt(howManyGroups);
        return numbers.subList(whichGroupOfWordsWeUse*fieldAmount, whichGroupOfWordsWeUse*fieldAmount + fieldAmount).toArray(new Integer[fieldAmount]);
    }

    void setListForRandomGenerator() {
        numbers = new ArrayList<>();
        for (int i = 0; i < soundNames.length; i++) {
            numbers.add(i);
        }
    }

    void insertAParticularNumberOfButtonsIntoGridLayout(int m, int n) {
        buttonsMatrix = mainView.findViewById(R.id.buttonsMatrix);
        buttonsMatrix.setColumnCount(n);
        buttonsMatrix.setRowCount(m);

        if (MainMenu.mode == MainMenu.ProgramMode.TEXT) {
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
        } else if (MainMenu.mode == MainMenu.ProgramMode.IMAGES) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    ImageButton button = new ImageButton(mainView.getContext());
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
                        ImageButton button = (ImageButton) buttonsMatrix.getChildAt(i);

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = buttonWidth;
                        params.height = buttonHeight;
                        params.setMargins(2, 2, 2, 2);
                        params.setGravity(Gravity.FILL);

                        button.setLayoutParams(params);
                    }
                    generateAndInsertRandomlyChosenSounds(m * n);
                    initialization();
                }
            });
        }

    }

    void initialization() {
        ((ImageButton) mainView.findViewById(R.id.next)).performClick();
    }

    void setButtonsClickedBehaviour(int fieldAmount) {
        ((ImageButton) mainView.findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.button_pressed_animation);
                ((ImageButton) mainView.findViewById(R.id.play)).startAnimation(animation);
                if (MainMenu.emode == MainMenu.ExcerciseMode.SOUND) {
                    if (MainMenu.mode != MainMenu.ProgramMode.INPUT)
                        playSound("sounds/" + soundNames[choices[correctAnswer]] + ".ogg");
                    else playSound("sounds/" + soundNames[correctAnswer] + ".ogg");
                } else if (MainMenu.emode == MainMenu.ExcerciseMode.SPEECH) {
                    if (MainMenu.mode == MainMenu.ProgramMode.SPEECH_SYNTHESIZER) {
                        EditText et = mainView.findViewById(R.id.inputTextFieldSynthesizer);
                        String sound = et.getText().toString();
                        playSound(sound);
                    } else {
                        if (MainMenu.mode != MainMenu.ProgramMode.INPUT)
                            playSound(soundNames[choices[correctAnswer]]);
                        else playSound(soundNames[correctAnswer]);
                    }
                } else if (MainMenu.mode == MainMenu.ProgramMode.GUESS_FREQUENCY) {
                    playFrequencySound(correctFrequency);
                } else if (MainMenu.mode == MainMenu.ProgramMode.FREQUENCY_GENERATOR){
                    stopFrequencySound();
                    playFrequencySound(userFrequency);
                }
            }
        });
        ((ImageButton) mainView.findViewById(R.id.stop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.button_pressed_animation);
                ((ImageButton) mainView.findViewById(R.id.stop)).startAnimation(animation);
                if (MainMenu.emode != MainMenu.ExcerciseMode.FREQUENCY) stopSound();
                else stopFrequencySound();
            }
        });
        ((ImageButton) mainView.findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.button_pressed_animation);
                ((ImageButton) mainView.findViewById(R.id.next)).startAnimation(animation);
                stopSound();
                if (MainMenu.emode != MainMenu.ExcerciseMode.FREQUENCY) {
                    buttonsMatrix.post(new Runnable() {
                        @Override
                        public void run() {
                            if (MainMenu.mode == MainMenu.ProgramMode.SPEECH_SYNTHESIZER) {
                                stopSound();
                                EditText et = mainView.findViewById(R.id.inputTextFieldSynthesizer);
                                et.setText("");
                            } else {
                                if (MainMenu.mode != MainMenu.ProgramMode.INPUT) {
                                    setDefaultColorsOfButtons();
                                    generateAndInsertRandomlyChosenSounds(m * n);
                                    deactivateAllButtons = false;
                                    messageSystem.pressAButtonToPlayASound();
                                } else {
                                    generateACorrectAnswer(soundNames.length);
                                    editText.setText("");
                                    messageSystem.pressAButtonAndInputText();
                                    lockPoints = false;
                                }
                            }

                        }
                    });
                } else {
                    messageSystem.pressAButtonToPlayASoundAndSelectAFrequency();
                    correctFrequency = Sound.generateCorrectFrequency();
                    deactivateAllButtons = false;
                    stopFrequencySound();
                }


            }
        });
        if (MainMenu.mode == MainMenu.ProgramMode.TEXT) {
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
        } else if (MainMenu.mode == MainMenu.ProgramMode.IMAGES) {
            buttonsMatrix.post(new Runnable() {
                @Override
                public void run() {
                    for (buttonCounter = 0; buttonCounter < buttonsMatrix.getChildCount(); buttonCounter++) {
                        final int currentButtonIndex = buttonCounter;
                        ImageButton buttonChosen = (ImageButton) buttonsMatrix.getChildAt(buttonCounter);
                        buttonChosen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!deactivateAllButtons) {
                                    deactivateAllButtons = true;
                                    userAnswer = currentButtonIndex;
                                    if (checkUserAnswer()) {
                                        buttonChosen.setBackgroundColor(ContextCompat.getColor(mainView.getContext(), R.color.green));
                                        messageSystem.correctAnswer();
                                    } else {
                                        buttonChosen.setBackgroundColor(ContextCompat.getColor(mainView.getContext(), R.color.red));
                                        ImageButton buttonCorrect = (ImageButton) buttonsMatrix.getChildAt(correctAnswer);
                                        buttonCorrect.setBackgroundColor(ContextCompat.getColor(mainView.getContext(), R.color.green));
                                        messageSystem.incorrectAnswer(soundNames[choices[correctAnswer]]);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        } else if (MainMenu.emode == MainMenu.ExcerciseMode.FREQUENCY) {
            ((Button) mainView.findViewById(R.id.checkSetFrequency)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!deactivateAllButtons) {
                        for (int i = 0; i < 10; i++) {
                            int difference = userFrequency - correctFrequency;
                            if (Math.abs(difference) <= Math.round(Sound.FREQUENCY_LIMIT_TO_EARN_10_POINTS / (10 - i))) {
                                messageSystem.youAreCloseToTheCorrectAnswer(10 - i, difference, correctFrequency);
                                deactivateAllButtons = true;
                                break;
                            } else {
                                messageSystem.youAreFarAwayFromCorrectAnswer(difference, correctFrequency);
                                deactivateAllButtons = true;
                            }
                        }
                    }
                }
            });
        }

    }

    boolean checkUserAnswer() {
        return userAnswer == correctAnswer;
    }

    boolean checkUserAnswerString(String s) {
        return s.equals(soundNames[correctAnswer]);
    }

    void generateACorrectAnswer(int fieldAmount) {
        Random r = new Random();
        correctAnswer = r.nextInt(fieldAmount);
    }

    void setAFrequencySlider() {
        frequencyBar = mainView.findViewById(R.id.frequencyBar);
        frequencyText = mainView.findViewById(R.id.frequencyText);
        frequencyBar.setMax(7900);
        frequencyBar.setProgress(0);
        frequencyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += Sound.LOWER_FREQ_LIMIT;
                userFrequency = progress;
                frequencyText.setText("Wybrana częstotliwość dźwięku: " + progress + "Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    void removeUnnecesaryButtonsFromFrequencyModules(){
        ((Button)mainView.findViewById(R.id.checkSetFrequency)).setVisibility(View.GONE);
        ((ImageButton)mainView.findViewById(R.id.next)).setVisibility(View.GONE);
    }
}
