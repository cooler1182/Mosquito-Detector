package com.kuwatatsu.mosquitodetector;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyPreference extends PreferenceActivity {
	Preference.OnPreferenceChangeListener numberCheckListener = new OnPreferenceChangeListener() {
	    @Override
	    public boolean onPreferenceChange(Preference preference, Object newValue) {
	        //Check that the string is an integer.
	        return numberCheck(newValue);
	    }
	};

	private boolean numberCheck(Object newValue) {
	    if( !newValue.toString().equals("")  &&  newValue.toString().matches("[0-9]+[.][0-9]+") ) {
	        return true;
	    }
	    else {
	        Toast.makeText(MyPreference.this, newValue+" "+getResources().getString(R.string.is_an_invalid_number), Toast.LENGTH_SHORT).show();
	        return false;
	    }
	}
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        
        Preference delayPreference = getPreferenceScreen().findPreference("threshold");

        delayPreference.setOnPreferenceChangeListener(numberCheckListener);
    }
  
    public static double getThreshold (Context context){  
        String stringThreshold = PreferenceManager.getDefaultSharedPreferences(context).getString("threshold", "0.01");
        double doubleThreshold = 0;
        try {
        	doubleThreshold = Double.parseDouble(stringThreshold);
        } catch (Exception e) {
        	doubleThreshold = 0.01;
		}
        
        return doubleThreshold;
    } 
}
