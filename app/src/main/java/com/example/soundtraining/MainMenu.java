package com.example.soundtraining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;

public class MainMenu extends Fragment {
    public MainMenu() {
        super(R.layout.main_menu);
    }

    View mainView;
    NavigationView navigationView;
    static enum ProgramMode{
        TEXT, IMAGES, INPUT, SPEECH_SYNTHESIZER, GUESS_FREQUENCY, FREQUENCY_GENERATOR, SIMILAR_WORDS
    }
    static enum ExcerciseMode{
        SOUND, SPEECH, FREQUENCY
    }
    static ProgramMode mode;
    static ExcerciseMode emode;
    static boolean similarWordsModeOn;
    static int whichDatabaseToUseInSimilarWordsModule; // 1 or 2

    public void loadAndSetBackgoundButtonsImages() {
        AssetManager manager = mainView.getContext().getAssets();
        try {
            InputStream stream = manager.open("background/bg_sound.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            ((Button) mainView.findViewById(R.id.button1)).setBackground(new BitmapDrawable(getResources(), bitmap));
            ((Button) mainView.findViewById(R.id.button1)).setText("Dźwięk");

            stream = manager.open("background/bg_frequency.jpg");
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            ((Button) mainView.findViewById(R.id.button3)).setBackground(new BitmapDrawable(getResources(), bitmap));
            ((Button) mainView.findViewById(R.id.button3)).setText("Częstotliwość");

            stream = manager.open("background/bg_speech.jpg");
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            ((Button) mainView.findViewById(R.id.button2)).setBackground(new BitmapDrawable(getResources(), bitmap));
            ((Button) mainView.findViewById(R.id.button2)).setText("Mowa");

            stream = manager.open("background/bg_similar.jpg");
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            ((Button) mainView.findViewById(R.id.button4)).setBackground(new BitmapDrawable(getResources(), bitmap));
            ((Button) mainView.findViewById(R.id.button4)).setText("Podobne słowa");

            stream = manager.open("background/bg_stories.jpg");
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            ((Button) mainView.findViewById(R.id.button6)).setBackground(new BitmapDrawable(getResources(), bitmap));
            ((Button) mainView.findViewById(R.id.button6)).setText("Opowiadania");

            stream = manager.open("background/bg_other.jpg");
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            ((Button) mainView.findViewById(R.id.button5)).setBackground(new BitmapDrawable(getResources(), bitmap));
            ((Button) mainView.findViewById(R.id.button5)).setText("Inne");
        } catch (IOException e) {
            Log.e("AssetError", "Error with opening the background image file");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.main_menu, container, false);
        loadAndSetBackgoundButtonsImages();
        setBackPanel();
        setRightPanelButton();
        similarWordsModeOn = false;
        mainView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarWordsModeOn = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wybierz ćwiczenie z listy");
                builder.setItems(R.array.dzwiek, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Sound module;
                        FragmentManager fm;
                        FragmentTransaction ft;
                        switch (which) {
                            case 0:
                                module = new Sound(2,1);
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 1:
                                module = new Sound(2,2);
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 2:
                                module = new Sound(3,3);
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 3:
                                module = new Sound(2,1);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 4:
                                module = new Sound(2,2);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 5:
                                module = new Sound(3,3);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 6:
                                module = new Sound(-1,-1);
                                mode = ProgramMode.INPUT;
                                emode = ExcerciseMode.SOUND;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        mainView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarWordsModeOn = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wybierz ćwiczenie z listy");
                builder.setItems(R.array.mowa, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Speech moduleSpeech;
                        FragmentManager fm;
                        FragmentTransaction ft;
                        switch (which) {
                            case 0:
                                moduleSpeech = new Speech(2,1,0);
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 1:
                                moduleSpeech = new Speech(2,2,0);
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 2:
                                moduleSpeech = new Speech(3,3,0);
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 3:
                                moduleSpeech = new Speech(2,1,0);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 4:
                                moduleSpeech = new Speech(2,2,0);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 5:
                                moduleSpeech = new Speech(3,3,0);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 6:
                                moduleSpeech = new Speech(3,3,0);
                                mode = ProgramMode.INPUT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 7:
                                moduleSpeech = new Speech(-1,-1,0);
                                mode = ProgramMode.SPEECH_SYNTHESIZER;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        mainView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarWordsModeOn = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wybierz ćwiczenie z listy");
                builder.setItems(R.array.czestotliwosc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Sound module;
                        FragmentManager fm;
                        FragmentTransaction ft;
                        switch (which) {
                            case 0:
                                module = new Sound();
                                mode = ProgramMode.GUESS_FREQUENCY;
                                emode = ExcerciseMode.FREQUENCY;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 1:
                                module = new Sound();
                                mode = ProgramMode.FREQUENCY_GENERATOR  ;
                                emode = ExcerciseMode.FREQUENCY;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        mainView.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarWordsModeOn = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wybierz ćwiczenie z listy");
                builder.setItems(R.array.podobne_slowa, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Speech module;
                        FragmentManager fm;
                        FragmentTransaction ft;
                        switch (which) {
                            case 0:
                                module = new Speech(2,1,3);
                                whichDatabaseToUseInSimilarWordsModule = 1;
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 1:
                                module = new Speech(2,2,3);
                                whichDatabaseToUseInSimilarWordsModule = 1;
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 2:
                                module = new Speech(2,1,4);
                                whichDatabaseToUseInSimilarWordsModule = 2;
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 3:
                                module = new Speech(2,2,4);
                                whichDatabaseToUseInSimilarWordsModule = 2;
                                mode = ProgramMode.TEXT;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        mainView.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarWordsModeOn = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wybierz ćwiczenie z listy");
                builder.setItems(R.array.opowiadania, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Speech moduleSpeech;
                        FragmentManager fm;
                        FragmentTransaction ft;
                        switch (which) {
                            case 0:
                                moduleSpeech = new Speech(2,1,2);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 1:
                                moduleSpeech = new Speech(2,2,2);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 2:
                                moduleSpeech = new Speech(3,3,2);
                                mode = ProgramMode.IMAGES;
                                emode = ExcerciseMode.SPEECH;
                                fm = getActivity().getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, moduleSpeech);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                        }
                    }
                });
                builder.create().show();

            }
        });
        return mainView;
    }
    public void setBackPanel() {
        navigationView = mainView.findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.drawer_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                FragmentManager fragmentManager = ((AppCompatActivity) mainView.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (itemID) {
                    case 2131296593:
                        Fragment settingsFragment = new SettingsFragment();
                        ((SettingsFragment)settingsFragment).setMainView(mainView);
                        fragmentTransaction.replace(R.id.fragmentContainerView, settingsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case 2131296594:
                        Fragment textFragmentStats = new Stats();
                        fragmentTransaction.replace(R.id.fragmentContainerView, textFragmentStats);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case 2131296595:
                        Fragment textFragmentInfo = new Info();
                        fragmentTransaction.replace(R.id.fragmentContainerView, textFragmentInfo);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
    }
    void setRightPanelButton(){
        Button openNavButton = mainView.findViewById(R.id.button5);
        openNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = mainView.findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }


}
