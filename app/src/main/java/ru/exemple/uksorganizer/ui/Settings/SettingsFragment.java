package ru.exemple.uksorganizer.ui.Settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;


import ru.exemple.uksorganizer.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);
    }
}
