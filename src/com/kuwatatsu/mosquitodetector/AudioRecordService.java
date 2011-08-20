package com.kuwatatsu.mosquitodetector;

import java.util.Arrays;

import ca.uol.aig.fftpack.RealDoubleFFT;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioRecordService extends Service {
	private boolean isRecording = true;

	// Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    
    private double[] doubleArray = null;

    private static final int SAMPLE_RATE = 44100;

    private AudioRecord  mAudioRecord   = null;

    private short[] shortArray = new short[8192];
    
	private AudioRecordThread mAudioRecordThread = null;
	
	private RealDoubleFFT myfft = null;

	private double[] windowFunctin = null;

	class AudioRecordThread extends Thread {
		private void initialization() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO); 
	    	
	    	mAudioRecord = new AudioRecord(
	        		MediaRecorder.AudioSource.MIC,
	        		SAMPLE_RATE,
	        		AudioFormat.CHANNEL_CONFIGURATION_MONO,
	        		AudioFormat.ENCODING_PCM_16BIT,
	        		16384
	        		);

	    	
	    	Arrays.fill(shortArray, (short)0);
	    	
			myfft = new RealDoubleFFT(MyData.NUMBER_OF_SAMPLES);
			
			windowFunctin = new double[MyData.NUMBER_OF_SAMPLES];
			double d = 2 * Math.PI / MyData.NUMBER_OF_SAMPLES;
			for (int i = 0; i < MyData.NUMBER_OF_SAMPLES; i++) {
				windowFunctin[i] = 0.5 - 0.5 * Math.cos(d * i);
			}

			mAudioRecord.startRecording();
			int size = mAudioRecord.read(shortArray, 0, shortArray.length);
			Log.d("Mosquito_Detector", "" + size);
		}
		
		@Override
		public void run() {
			initialization();
			while(isRecording) {
			    double[] mDoubleArray = new double[8192];
		    	Arrays.fill(mDoubleArray, (double)0);

				int size = mAudioRecord.read(shortArray, 0, shortArray.length);
				Log.d("Mosquito_Detector", "" + size);
				
				for (int i = 0; i < 8192; i++) {
					mDoubleArray[i] = shortArray[i] / (double)Short.MAX_VALUE;
				}
				for (int i = 0; i < MyData.NUMBER_OF_SAMPLES; i++) {
					mDoubleArray[i] *= windowFunctin[i];
				}
				
				double fftedArray[] = fft(mDoubleArray);
				
				set(fftedArray);
			}
			finalization();
		}

		private void finalization() {
			mAudioRecord.stop();
			mAudioRecord.release();
			mAudioRecord = null;
		}

		private double[] fft(double[] doubleArray_rawAudio) {
	    	myfft.ft(doubleArray_rawAudio);
	    	doubleArray_rawAudio[0] /= 8192;
	    	double double_half = 8192 / 2.0;
	    	for (int i = 1; i< 8192; i++) {
	    		doubleArray_rawAudio[i] /= double_half;
	    	}
	    	return doubleArray_rawAudio;
		}
	}

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
    	AudioRecordService getService() {
            return AudioRecordService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
	@Override
	public void onCreate() {
		super.onCreate();
	}

    /** method for clients */
    public void start() {
    	mAudioRecordThread = new AudioRecordThread();
    	mAudioRecordThread.start();
    }
    
    /** method for clients */
    public void stop() {
		isRecording = false;
    	stopSelf();
    }
    
    /** method for clients */
	synchronized public double[] get() {
		double[] result = doubleArray;
		doubleArray = null;
		return result;
	}

	synchronized public void set(double[] array) {
		this.doubleArray = array;
	}
}
