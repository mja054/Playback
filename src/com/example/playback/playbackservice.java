package com.example.playback;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class playbackservice extends Service implements SensorEventListener{
	private static String tag = "playbackservice";
	private SensorManager msensormanager;
	private LocationManager locationManager;
	BufferedWriter locationWriter;
	private Sensor macc;
	private Sensor mlight;
	private static int k = 0;
	private static float i = 10, j = 0,  l = 10;
	private static double m = 0.5d, n = 5.784d, o = 10.1234d, p = 0;
	private static float sq[][] = {
		{0f,9f}, 
			{0f,7f}, {0f,5f},{0f,3f},{0f,0f},{0f,-1f}, {0f,-3f},{0f,-5f},
		{2f, -9f}, 
			{1.5f, -9f}, {1f, -9f}, {0.5f, -9f},      
		{0f, -9f}, 
		 	{0f, -7f},  {0f, -5f},  {0f, -3f}, {-1f, -1f},  {-1f, 0f},  {-1f, 2f},  {-1f, 5f},  {-1f, 7f},
		{-1f, 8f},
		 	 {0f, 7f}, {0f, 6f} };
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			Log.i(tag, i + ":" + event.values[0] + "," + event.values[1]+ "," + event.values[2]);

			event.values[0] = sq[k][0];
			event.values[1] = sq[k][1];
			msensormanager.sendEvents(event, event.sensor);
			if (i >= 5) {
				k = (k + 1) % 24;
				i = 0;
			}
			i++;
		} else  if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			Log.i(tag,  "light:" + event.values[0] + "," + event.values[1]+ "," + event.values[2]);
			event.values[0] = l;
			msensormanager.sendEvents(event, event.sensor);
			if (j == 5) {
				l++;
				j = 0;
			}
			j = j + 1;

		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate()
	{
		
	}
	
	private void registerLocationManager()
	{
		
		LocationListener locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				Log.i(tag, "onLocationChanged:" + location.toString());
				try {
					locationWriter.append(location.toString()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Location l = new Location("gps");
				l.setAccuracy(5);
				l.setAltitude(m);
				l.setLatitude(n);
				l.setLongitude(o);
				locationManager.setLocation(l);
				if (p >= 100) {
					m = 0.5d;
					n = 1.7d;
					o = 6.8d;
				}
				m = m + 1;
				n = n + 1;
				o = o + 1;
				p = p + 1;
			}
		};
		try {
			locationWriter = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().toString() + "/location.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		msensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		macc   = msensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		mlight = msensormanager.getDefaultSensor(Sensor.TYPE_LIGHT);
//		msensormanager.registerListener(this, macc, SensorManager.SENSOR_DELAY_NORMAL);
//		msensormanager.registerListener(this, mlight, SensorManager.SENSOR_DELAY_FASTEST);
//		i = 0; k = 1;
//		SensorEvent event = new SensorEvent();
 		registerLocationManager();
		return 0;
	}
}
