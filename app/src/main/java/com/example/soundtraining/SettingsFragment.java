package com.example.soundtraining;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    View mainView;
    public void setMainView(View mainView){
        this.mainView = mainView;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        SwitchPreferenceCompat darkModeSwitch = findPreference("dark_mode");
        if (darkModeSwitch != null) {
            darkModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isDarkMode = (boolean) newValue;
                    if (isDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        setDrawerLayoutBackgroundColor(true);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        setDrawerLayoutBackgroundColor(false);
                    }
                    return true;
                }
            });
        }
    }

    private void setDrawerLayoutBackgroundColor(boolean isDarkMode) {
        DrawerLayout drawerLayout = mainView.findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            int backgroundColor;
            if (isDarkMode) {
                backgroundColor = getResources().getColor(R.color.white, requireContext().getTheme());
            } else {
                backgroundColor = getResources().getColor(R.color.black, requireContext().getTheme());
            }
            drawerLayout.setBackgroundColor(backgroundColor);
        }
    }

}
