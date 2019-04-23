package com.softmed.htmr_chw_staging.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.softmed.htmr_chw_staging.R;

import org.ei.opensrp.repository.AllSharedPreferences;

import static org.ei.opensrp.util.Log.logInfo;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }


    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference cbhsNumberPreference = findPreference("CBHS_NUMBER");
            if (cbhsNumberPreference != null) {
                EditTextPreference baseUrlEditTextPreference = (EditTextPreference) cbhsNumberPreference;
                baseUrlEditTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (newValue != null) {
                            updateCBHSNumber(newValue.toString());
                        }
                        return true;
                    }
                });
            }
        }

        private void updateCBHSNumber(String cbhsNumber) {
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                AllSharedPreferences allSharedPreferences = new AllSharedPreferences(preferences);


                allSharedPreferences.saveCBHS(cbhsNumber);

                logInfo("Saved CBHS Number : " + allSharedPreferences.fetchCBHS());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}