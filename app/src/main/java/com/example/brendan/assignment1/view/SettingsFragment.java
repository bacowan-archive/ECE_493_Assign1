package com.example.brendan.assignment1.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.brendan.assignment1.R;

/**
 * Created by Brendan on 1/15/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
