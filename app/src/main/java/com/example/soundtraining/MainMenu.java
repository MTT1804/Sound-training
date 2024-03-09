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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;

public class MainMenu extends Fragment {
    public MainMenu() {
        super(R.layout.main_menu);
    }

    View mainView;

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
        } catch (IOException e) {
            Log.e("AssetError", "Error with opening the background image file");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.main_menu, container, false);
        loadAndSetBackgoundButtonsImages();
        mainView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wybierz ćwiczenie z listy");
                builder.setItems(R.array.dzwiek, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                sound module = new sound();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentContainerView, module);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;
                            case 1:
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        return mainView;
    }


}
