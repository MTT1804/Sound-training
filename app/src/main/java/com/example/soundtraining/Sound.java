package com.example.soundtraining;

import android.os.Bundle;
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

public class Sound extends Fragment {
    View mainView;
    int m, n;
    public Sound(int m, int n) {
        super(R.layout.sound);
        this.m = m;
        this.n = n;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.sound, container, false);
        insertAParticularNumberOfButtonsIntoGridLayout(m,n);
        return mainView;
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

                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        Button button = new Button(mainView.getContext());
                        button.setText("Przycisk " + ((i * n) + j + 1));
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = buttonWidth;
                        params.height = buttonHeight;
                        params.setMargins(2, 2, 2, 2);
                        params.setGravity(Gravity.FILL);
                        button.setLayoutParams(params);
                        buttonsMatrix.addView(button);
                    }
                }
                buttonsMatrix.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


}
