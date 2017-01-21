package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        public static Preference mPreference;
        public static String mPreferenceString;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            /* we still need to update the preference summary when the settings activity is launched.
               Given the key of a preference, we can use PreferenceFragment's findPreference() method to
               get the Preference object, and setup the preference using a helper method called bindPreferenceSummaryToValue()
            */
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        //The code in this method takes care of updating the preferences
        //and updating the displayed preference summary after any of the 2 settings is updated
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            //<string name="settings_order_by_magnitude_value"> OR <string name="settings_order_by_most_recent_value">

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /* bindPreferenceSummaryToValue() helper method to set the current EarhtquakePreferenceFragment
           instance as the listener on each preference. We also read the current value of the preference
           stored in the SharedPreferences on the device, and display that in the preference summary
           (so that the user can see the current value of the preference).
        */
        private void bindPreferenceSummaryToValue(Preference preference) {

            mPreference = preference;
            mPreference.setOnPreferenceChangeListener(this);//whenever a new value is set in settings screen, onPreferenceChange will be called

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mPreference.getContext());
            String preferenceString = preferences.getString(mPreference.getKey(), "");

            mPreferenceString = preferenceString;
            onPreferenceChange(mPreference, mPreferenceString);
        }
    }
}

/*
           INFORMATION ABOUT STATIC CLASSES
Java has static nested classes but it sounds like you're looking for a top-level static class. Java has no way of making
 a top-level class static

Declare your class final - Prevents extension of the class since extending a static class makes no sense

Make the constructor private - Prevents instantiation by client code as it makes no sense to instantiate a static class

Make all the members and functions of the class static - Since the class cannot be instantiated no
instance methods can be called or instance fields accessed

Note that the compiler will not prevent you from declaring an instance (non-static) member.
The issue will only show up if you attempt to call the instance member
 */