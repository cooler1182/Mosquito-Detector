package com.kuwatatsu.mosquitodetector;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.widget.TextView;

public class MyData {
	public static final int NUMBER_OF_ITEMS_DISPLAYED = 52;

	public static final int SAMPLING_FREQUENCY = 44100;

	public static final int NUMBER_OF_SAMPLES = 8192;

	public static final int INTERVAL = 1000;

	public static final int INDIVIDUAL_DIFFERENCE = 5;

	public static final double STEP = (double)SAMPLING_FREQUENCY / (double)NUMBER_OF_SAMPLES;

	private int threshold = 1;
	
	private int[] wingbeatFrequency = null;
	
	private int[] frequency = null;

	private int[] strength = null;

	private Resources resources = null;
	
	private AudioRecordService service = null;
	
	private Context context = null;

	public AudioRecordService getService() {
		return service;
	}

	public void setService(AudioRecordService service) {
		this.service = service;
	}

	private Handler handler = null;
	
	private ArrayList<double[]> arrayList = new ArrayList<double[]>();

	private TextView[] textViewFrequency = null;

	private TextView[] textViewStrength = null;
	
	private int lowerLimit = 350;
	
	private int upperLimit = 600;
	
	public MyData() {
		int[] temp_frequency = {
				R.id.freq_1, R.id.freq_2, R.id.freq_3, R.id.freq_4, R.id.freq_5,
				R.id.freq_6, R.id.freq_7, R.id.freq_8, R.id.freq_9, R.id.freq_10,
				R.id.freq_11, R.id.freq_12, R.id.freq_13, R.id.freq_14, R.id.freq_15,
				R.id.freq_16, R.id.freq_17, R.id.freq_18, R.id.freq_19, R.id.freq_20,
				R.id.freq_21, R.id.freq_22, R.id.freq_23, R.id.freq_24, R.id.freq_25,
				R.id.freq_26, R.id.freq_27, R.id.freq_28, R.id.freq_29, R.id.freq_30,
				R.id.freq_31, R.id.freq_32, R.id.freq_33, R.id.freq_34, R.id.freq_35,
				R.id.freq_36, R.id.freq_37, R.id.freq_38, R.id.freq_39, R.id.freq_40,
				R.id.freq_41, R.id.freq_42, R.id.freq_43, R.id.freq_44, R.id.freq_45,
				R.id.freq_46, R.id.freq_47, R.id.freq_48, R.id.freq_49, R.id.freq_50,
				R.id.freq_51, R.id.freq_52
		};
		frequency = temp_frequency;
		int[] temp_strength = {
				R.id.strength_1, R.id.strength_2, R.id.strength_3, R.id.strength_4, R.id.strength_5,
				R.id.strength_6, R.id.strength_7, R.id.strength_8, R.id.strength_9, R.id.strength_10,
				R.id.strength_11, R.id.strength_12, R.id.strength_13, R.id.strength_14, R.id.strength_15,
				R.id.strength_16, R.id.strength_17, R.id.strength_18, R.id.strength_19, R.id.strength_20,
				R.id.strength_21, R.id.strength_22, R.id.strength_23, R.id.strength_24, R.id.strength_25,
				R.id.strength_26, R.id.strength_27, R.id.strength_28, R.id.strength_29, R.id.strength_30,
				R.id.strength_31, R.id.strength_32, R.id.strength_33, R.id.strength_34, R.id.strength_35,
				R.id.strength_36, R.id.strength_37, R.id.strength_38, R.id.strength_39, R.id.strength_40,
				R.id.strength_41, R.id.strength_42, R.id.strength_43, R.id.strength_44, R.id.strength_45,
				R.id.strength_46, R.id.strength_47, R.id.strength_48, R.id.strength_49, R.id.strength_50,
				R.id.strength_51, R.id.strength_52
		};
		strength = temp_strength;

	}
	
	public int[] getWingbeatFrequency() {
		return wingbeatFrequency;
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
		
        wingbeatFrequency = resources.getIntArray(R.array.wingbeat_frequency);
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public int[] getFrequency() {
		return frequency;
	}

	public int[] getStrength() {
		return strength;
	}

	public ArrayList<double[]> getArrayList() {
		return arrayList;
	}

	public TextView[] getTextViewFrequency() {
		return textViewFrequency;
	}

	public void setTextViewFrequency(TextView[] textViewFrequency) {
		this.textViewFrequency = textViewFrequency;
	}

	public TextView[] getTextViewStrength() {
		return textViewStrength;
	}

	public void setTextViewStrength(TextView[] textViewStrength) {
		this.textViewStrength = textViewStrength;
	}

	public int getColor(int color) {
		return resources.getColor(color);
	}

	public int getThreshold() {
		return threshold;
	}

	public int getLowerLimit() {
		return lowerLimit;
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
