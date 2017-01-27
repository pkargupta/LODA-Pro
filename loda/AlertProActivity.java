package com.locatedamini.loda;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AlertProActivity extends ActionBarActivity implements LocationListener {
EditText message;
    protected LocationManager locationManager;
    Button send;
    DatabaseHandler dbHandler;
    Contact contact;
    Phone phone;
    ContactsActivity contactsActivity;
    double Lat, Long;
    //private TextView caddress;

    public void openHome(View view) {
        // Do something in response to button

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);


    }
    public void openAlertPro(View view) {
        // Do something in response to button

        Intent i = new Intent(this, AlertProActivity.class);
        startActivity(i);


    }
    public void openLocationActivity(View view) {
        //Do something in response to the button
        Intent i = new Intent(this, LocationActivity.class);
        startActivity(i);
    }
    public void openContacts(View view) {
        // Do something in response to button
        Intent i = new Intent(this, ContactsActivity.class);
        startActivity(i);

    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_pro);
        final TextView caddress = (TextView) findViewById(R.id.currentaddress);
        final EditText message = (EditText) findViewById(R.id.msgtxt);
        send = (Button) findViewById(R.id.sendbtn);
        //getting the current location and displaying it on the bottom of the screen
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String themnumbers = Arrays.toString(ContactsActivity.Phones.toArray()).replaceAll("\\[|\\]", "");//getting rid of the brackets too when printing the phone number list
                    List<String> sendinglist = ContactsActivity.Phones;//getting the phone numbers array from the Contacts Activity that has already converted them into strings
                    int count = sendinglist.toArray().length;//getting the count of the array
                    for (int i = 0; i < count; i++) {//running the loop to send an sms to each of the recipients

                        String phonenumber = ContactsActivity.Phones.get(i);//sending text to these numbers
                        SmsManager smsManager = SmsManager.getDefault();
                        String sms = String.valueOf(message.getText()) + " My current location is: " + String.valueOf(caddress.getText());//setting the sms value to the user-inputed message with the current gps location
                        smsManager.sendTextMessage(phonenumber, null, sms, null, null );
                        Toast.makeText(getApplicationContext(),
                            "Message sent!",
                            Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Message could not be sent! Check your contacts list!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }}

        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onLocationChanged(Location location) {
        DecimalFormat df = new DecimalFormat("#.######");
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        final TextView caddress = (TextView) findViewById(R.id.currentaddress);

//getting the current location when changed
        Lat = location.getLatitude();
        Long = location.getLongitude();

        try {
            List<Address> addresses = geocoder.getFromLocation(Lat, Long, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                caddress.setText(strReturnedAddress.toString());
            } else {
                caddress.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            caddress.setText("Cannot get Address!");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
    }

    /**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_alert,
					container, false);
			return rootView;
		}
	}

}
