package com.example.lifetagparamedic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


	private NfcAdapter mNfcAdapter;
	private String DELIMITER = "*";
	private String name;
	private String NRIC;
	private String address;
	private String bloodType;
	private String age;
	private String allergy;
	private String preCondition;
	private String emergencyContact;
	private TextView nameText;
	private TextView sexText;
	private TextView ageText;
	private TextView bloodTypeText;
	private TextView preconditionText;
	private TextView allergyText;
	private TextView nricText;
	Button emergencyButton;
	double latitude;
	double longitude;
	String content;
	LocationManager locationmanager;
	LocationListener locationlistener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nameText = (TextView) findViewById(R.id.nameText);
		bloodTypeText = (TextView) findViewById(R.id.bloodTypeText);
		allergyText = (TextView) findViewById(R.id.allergyText);
		preconditionText = (TextView) findViewById(R.id.preconditionText);
		nricText = (TextView) findViewById(R.id.NRICText);
		ageText = (TextView) findViewById(R.id.ageText);
		
		locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		locationlistener = new LocationListener() {
			
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
				
			}
		};
		
		locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistener);
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
		
		Location lastKnownLocation = locationmanager.getLastKnownLocation(locationmanager.NETWORK_PROVIDER);
		Log.d("Inian", lastKnownLocation.toString());
		
		latitude = lastKnownLocation.getLatitude();
		longitude = lastKnownLocation.getLongitude();
		
 		if (mNfcAdapter == null) {
			Toast.makeText(this, "Sorry, NFC is not available on this device",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		emergencyButton = (Button) findViewById(R.id.emergencyButton);
		emergencyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String sms = "I'm in an emergency situation. My current coordinates are " + latitude + " , " + longitude;

				try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(emergencyContact, null, sms, null, null);
					Toast.makeText(getApplicationContext(), "SMS Sent!",
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"SMS faild, please try again later!",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		// set the intent here to update mIntent
		setIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent()
						.getAction())) {
			processReadIntent(getIntent());
		}
	}

	public void processReadIntent(Intent intent) {
		Log.d("hahaha", "haha reading intent");
		List<NdefMessage> intentMessages = NfcUtils
				.getMessagesFromIntent(intent);
		List<String> payloadStrings = new ArrayList<String>(
				intentMessages.size());

		for (NdefMessage message : intentMessages) {
			for (NdefRecord record : message.getRecords()) {
				byte[] payload = record.getPayload();
				String payloadString = new String(payload);

				if (!TextUtils.isEmpty(payloadString))
					payloadStrings.add(payloadString);
			}
		}

		if (!payloadStrings.isEmpty()) {
			content = TextUtils.join(",", payloadStrings);
//			Toast.makeText(MainActivity.this, "Read from tag: " + content,
//					Toast.LENGTH_LONG).show();
			processContent();
		}
	}
	
	public void processContent() {
		Log.d("test", "haha " + "Read from tag: " + content);
		String[] components = content.split("\\*");
		Log.d("tmep", "hahacount " + components.length);
		Log.d("test", "haha " + components.length);
		String name = components[0];
		Log.d("test", "haha " + name);
		
		String dob = "" + (2013 - Integer.parseInt(components[1].trim().substring(0, 4))) ; 
		Log.d("test", "haha "+ dob);
		
		String NRIC = components[2];
		Log.d("test", "haha " + NRIC);
		
		emergencyContact = components[3];
		Log.d("test", "haha " + emergencyContact);
		
		bloodType = components[4];
		Log.d("test", "hahablood " + bloodType);
		String allergy = new String();
		String temp = components[5];
		
		if(!temp.equals("/")) {
			allergy += temp;
			allergy += " ";
		}
		temp = components[6];
		Log.d("temp", "haha " + allergy);
		if(!temp.equals("/")) {
			allergy += temp;
			allergy += " ";
		}
		Log.d("temp", "haha " + allergy);
		temp = components[7];
		if(!temp.equals("/")) {
			allergy += temp;
			allergy += " ";
		}
		Log.d("temp", "haha " + allergy);
		temp = components[8];
		if(!temp.equals("/")) {
			allergy += temp;
			allergy += " ";
		}
		Log.d("temp", "haha " + allergy);
		String precondition = new String();
		temp = components[9];
		if(!temp.equals("/")) {
			precondition += temp;
			precondition += " ";
		}
		temp = components[10];
		if(!temp.equals("/")) {
			precondition += temp;
			precondition += " ";
		}
		Log.d("ini", "hahatemp " + temp);
		temp = components[11];
		if(!temp.equals("/")) {
			precondition += temp;
			precondition += " ";
		}
		
		Log.d("test", "haha " + emergencyContact);
		nameText.setText(name);
		
		nricText.setText(NRIC);
		bloodTypeText.setText(bloodType);
		allergyText.setText(allergy);
		preconditionText.setText(precondition);
		ageText.setText(dob);
		bloodTypeText.setText(bloodType);
	}
}

