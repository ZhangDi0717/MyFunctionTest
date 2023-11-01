package com.zhangdi.myfunctiontest.preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.zhangdi.myfunctiontest.R;

public class PreferencesActivity extends PreferenceActivity implements androidx.preference.Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        SwitchPreference switchPreference = new SwitchPreference(this);
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return false;
            }
        });
//        switchPreference.setSummary();
    }

    @Override
    public boolean onPreferenceChange(@NonNull androidx.preference.Preference preference, Object newValue) {
        return false;
    }
}