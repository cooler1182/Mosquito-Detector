package com.kuwatatsu.mosquitodetector;

import com.kuwatatsu.mosquitodetector.AudioRecordService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MosquitoDetectorActivity extends Activity {
	UIThread uiThread = null;
	
    boolean mBound = false;

    private MyData myData = new MyData();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        myData.setContext(this);
        
        myData.setResources(getResources());
        myData.setHandler(new Handler());

        setTextView();
        
        setWingbeatFrequency();

	}

	private void setTextView() {
		TextView[] temp_frequency = new TextView[MyData.NUMBER_OF_ITEMS_DISPLAYED];
		TextView[] temp_strength = new TextView[MyData.NUMBER_OF_ITEMS_DISPLAYED];
		for (int i = 0; i < MyData.NUMBER_OF_ITEMS_DISPLAYED; i++) {
			TextView textView = (TextView)findViewById(myData.getFrequency()[i]);
			temp_frequency[i] = textView;
		}
		myData.setTextViewFrequency(temp_frequency);
		
		for (int i = 0; i < MyData.NUMBER_OF_ITEMS_DISPLAYED; i++) {
			TextView textView = (TextView)findViewById(myData.getStrength()[i]);
			temp_strength[i] = textView;
		}
		myData.setTextViewStrength(temp_strength);
	}

	private void setWingbeatFrequency() {
		TextView textView = null;
		textView = (TextView)findViewById(R.id.species1_wingbeat_frequency);
		textView.setText("" + myData.getWingbeatFrequency()[0]);
		textView = (TextView)findViewById(R.id.species2_wingbeat_frequency);
		textView.setText("" + myData.getWingbeatFrequency()[1]);
		textView = (TextView)findViewById(R.id.species3_wingbeat_frequency);
		textView.setText("" + myData.getWingbeatFrequency()[2]);
		textView = (TextView)findViewById(R.id.species4_wingbeat_frequency);
		textView.setText("" + myData.getWingbeatFrequency()[3]);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
        uiThread = new UIThread(myData);
        uiThread.start();

        Intent intent = new Intent(this, AudioRecordService.class);
        startService(intent);
        
        intent = new Intent(this, AudioRecordService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		uiThread.isRunning = false;
		
        myData.getService().stop();

        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu){  
	    super.onCreateOptionsMenu(menu);  
	    MenuInflater inflater = getMenuInflater();  
	    inflater.inflate(R.menu.menu, menu);  
	    return true;  
	}  
	
	@Override  
	public boolean onOptionsItemSelected(MenuItem item){  
	    switch(item.getItemId()){  
	    case R.id.exit:
	    	finish();
	        return true;  
	    case R.id.setting:  
	        Intent intent = new Intent();  
	        intent.setClassName(  
	            "com.kuwatatsu.mosquitodetector",  
	            "com.kuwatatsu.mosquitodetector.MyPreference");  
	        startActivity(intent);  
	        return true;  
	    }  
	    return false;  
	}

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            myData.setService(binder.getService());
            mBound = true;
            myData.getService().start();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}