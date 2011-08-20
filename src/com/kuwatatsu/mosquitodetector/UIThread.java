package com.kuwatatsu.mosquitodetector;

import android.os.Handler;
import android.widget.TextView;

public class UIThread extends Thread {
	public boolean isRunning = true;

	private MyData myData = null;
	
	public UIThread(MyData myData) {
		this.myData = myData;
	}
	
	@Override
	public void run() {
		Handler handler = myData.getHandler();
		while(isRunning) {
			try {
				Thread.sleep(MyData.INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			handler.post(
				new Runnable() {
					@Override
					public void run() {
						double[] array = null;
						AudioRecordService audioRecordService = myData.getService();
						if (audioRecordService == null) return;
						array = audioRecordService.get();
						if (array == null) return;

						array = toStrength(array);
						TextView[] textViewFrequency = myData.getTextViewFrequency();
						TextView[] textViewStrength = myData.getTextViewStrength();
						double threshold = MyPreference.getThreshold(myData.getContext());
						int lowerLimit = myData.getLowerLimit();
						int upperLimit = myData.getUpperLimit();
						for (int i = 0, j= 0; i < array.length; i++) {
							double f1, f2, f3;
							try {
								f1 = (i == 0)?(double)0:(double)(i-1) * MyData.STEP;
							} catch (Exception e) {
								f1 = 0;
							}
							f2 = (double)i * MyData.STEP;
							try {
								f3 = (i == array.length - 1)?20000:(double)(i+1) * MyData.STEP;
							} catch (Exception e) {
								f3 = 20000;
							}
							if (isOutOfRange(lowerLimit, upperLimit, f1, f2, f3)) {
								continue;
							}
							if (isCloseToMosquitoWingbeat(f2)) {
								textViewFrequency[j].setBackgroundColor(myData.getColor(R.color.f_attention));
							} else {
								textViewFrequency[j].setBackgroundColor(myData.getColor(R.color.f));
							}
							textViewFrequency[j].setText(String.format("%3d", Math.round(f2)));
							if (array[i] > threshold) {
								textViewStrength[j].setBackgroundColor(myData.getColor(R.color.power_warning));
							} else {
								textViewStrength[j].setBackgroundColor(myData.getColor(R.color.power));
							}
							textViewStrength[j].setText(String.format("%5.3f", array[i]));
							j++;
						}
					}
				}
			);
		}
	}
	
	private boolean isOutOfRange(int lowerLimit, int upperLimit, double f1, double f2, double f3) {
		boolean b1, b2;
		
		if (f2 >= lowerLimit) {
			b1 = true;
		} else {
			if (f3 > lowerLimit) {
				b1 = true;
			} else {
				b1 = false;
			}
		}
		
		if (f2 <= upperLimit) {
			b2 = true;
		} else {
			if (f1 < upperLimit) {
				b2 = true;
			} else {
				b2 = false;
			}
		}
		
		return !(b1 & b2);
		
	}

	private boolean isCloseToMosquitoWingbeat(double f) {
		int[] wingBeatFrequency = myData.getWingbeatFrequency();
		int individual_difference = MyData.INDIVIDUAL_DIFFERENCE;
		for (int i = 0; i < wingBeatFrequency.length; i++) {
			if (wingBeatFrequency[i] - individual_difference < f && f < wingBeatFrequency[i] + individual_difference) {
				return true;
			}
		}
		return false;
	}
	
	private double[] toStrength(double[] in) {
		double[] result = new double[1 + (in.length-1) / 2];
		result[0] = in[0];
		for (int i = 1, j = 1; i < in.length - 1; i += 2, j++) {
			double d1 = in[i];
			double d2 = in[i + 1];
			result[j] = Math.sqrt(d1 * d1 + d2 * d2);
		}
		return result;
	}
}
