package com.example.soundtraining;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Random;

public class Sound extends Excercise {
    private static final int SAMPLE_RATE = 44100;
    private static int DURATION_MS = 4000;
    private static int DURATION_GENERATOR_MS = 20000;
    public static int LOWER_FREQ_LIMIT = 100;
    public static int UPPER_FREQ_LIMIT = 8000;
    public static int FREQUENCY_LIMIT_TO_EARN_10_POINTS = 2000;
    private AudioTrack audioTrack;

    public Sound(int m, int n) {
        super(R.layout.sound);
        this.m = m;
        this.n = n;
    }

    public Sound() {
        super(R.layout.frequency);
        this.m = m;
        this.n = n;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (MainMenu.emode == MainMenu.ExcerciseMode.FREQUENCY) mainView = inflater.inflate(R.layout.frequency, container, false);
        else mainView = inflater.inflate(R.layout.sound, container, false);
        this.assetManager = mainView.getContext().getAssets();
        currentInformations = mainView.findViewById(R.id.currentInformations);
        messageSystem = new UserMessages(currentInformations, mainView.getContext());
        searchForSoundsAndGetSoundsNames();
        setListForRandomGenerator();
        if (MainMenu.mode == MainMenu.ProgramMode.GUESS_FREQUENCY) {
            setAFrequencySlider();
            correctFrequency = generateCorrectFrequency();
            setButtonsClickedBehaviour(0);
            messageSystem.pressAButtonToPlayASoundAndSelectAFrequency();
            userFrequency = LOWER_FREQ_LIMIT;
        } else if (MainMenu.mode == MainMenu.ProgramMode.FREQUENCY_GENERATOR){
            setAFrequencySlider();
            setButtonsClickedBehaviour(0);
            messageSystem.pressAButtonToPlayASoundAndSetAFrequencyUsingSlider();
            userFrequency = LOWER_FREQ_LIMIT;
            removeUnnecesaryButtonsFromFrequencyModules();
            ((TextView)mainView.findViewById(R.id.moduleTitleFreq)).setText("Generator częstotliwości");
        } else if (MainMenu.mode != MainMenu.ProgramMode.INPUT) {
            insertAParticularNumberOfButtonsIntoGridLayout(m, n);
            setButtonsClickedBehaviour(m * n);
            setDefaultColorsOfButtons();
        } else {
            addTextViewAndEditTextToGridLayout();
            messageSystem.pressAButtonAndInputText();
            setButtonsClickedBehaviour(0);
            setAnswerChecker();
            generateACorrectAnswer(soundNames.length);
            Log.i("CORRECT_ANSWER", soundNames[correctAnswer]);
        }
        return mainView;
    }

    public void searchForSoundsAndGetSoundsNames() {
        AssetManager assetManager = mainView.getContext().getAssets();
        try {
            soundNames = assetManager.list("sounds");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < soundNames.length; i++) {
            soundNames[i] = soundNames[i].replace(".ogg", "");
        }

    }

    @Override
    protected void playSound(String sound) {
        Log.i("GRAM", sound);
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
    @Override
    protected void playFrequencySound(int frequency) {
        int numSamples = 0;
        if (MainMenu.mode == MainMenu.ProgramMode.GUESS_FREQUENCY) numSamples = (int) (DURATION_MS * (SAMPLE_RATE / 1000.0));
        else numSamples = (int) (DURATION_GENERATOR_MS * (SAMPLE_RATE / 1000.0));
        double[] sample = new double[numSamples];
        byte[] generatedSnd = new byte[2 * numSamples];

        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (SAMPLE_RATE / frequency));
        }

        int idx = 0;
        for (double dVal : sample) {
            short val = (short) ((dVal * 32767));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        audioTrack = null;
        audioTrack = new AudioTrack.Builder()
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(generatedSnd.length)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build();

        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }

    @Override
    protected void stopFrequencySound() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }
    public static int generateCorrectFrequency(){
        return new Random().nextInt(UPPER_FREQ_LIMIT - LOWER_FREQ_LIMIT + 1) + LOWER_FREQ_LIMIT;
    }
}
