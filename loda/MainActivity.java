package com.locatedamini.loda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.locatedamini.loda.AlertProActivity;
import com.locatedamini.loda.ContactsActivity;
import com.locatedamini.loda.LocationActivity;
import com.locatedamini.loda.MainActivity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements LocationListener  {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected boolean gps_enabled,network_enabled;
    public LocationManager lm;
    DatabaseHandler dbHandler;
    double Lat, Long;
    //private TextView currentadd;

    LocationActivity locationActivity;
    Location location;
    Contact contact;
    Phone phone;
    ContactsActivity contactsActivity;
    DecimalFormat df = new DecimalFormat("#.######");




    /** Called when the user clicks the Send button */
    public void openHome(View view) {
        // Do something in response to button

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);


    }
	public void openAlertPro(View view) {
	    // Do something in response to button

		Intent i = new Intent(MainActivity.this, AlertProActivity.class);
        startActivity(i);


	}
	public void openLocationActivity(View view) {
		//Do something in response to the button
		Intent i = new Intent(MainActivity.this, LocationActivity.class);
		startActivity(i);
	}
	public void openContacts(View view) {
	    // Do something in response to button
		Intent i = new Intent(MainActivity.this, ContactsActivity.class);
        startActivity(i);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        final Button help = (Button) findViewById(R.id.help);
        final TextView currentadd = (TextView) findViewById(R.id.currentadd);
        dbHandler = new DatabaseHandler(this);
        //getting location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);



        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

try {
 //String currentloc = null;
    List<Address> addresses = null;
   /* try {
        addresses = geocoder.getFromLocation(Lat, Long, 1);
        Address returnedAddress = addresses.get(0);
        StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }*/
    String themnumbers = Arrays.toString(ContactsActivity.Phones.toArray()).replaceAll("\\[|\\]", "");//getting rid of the brackets too when printing the phone number list
    List<String> sendinglist = ContactsActivity.Phones;//getting string array of the preset phones from the ContactsAcitivity
    int count = sendinglist.toArray().length;//getting the count of the array
    for (int i = 0; i < count; i++) {//running the loop to send an sms to each of the recipients
        String phonenumber = ContactsActivity.Phones.get(i);
        SmsManager smsManager = SmsManager.getDefault();
        String sms = "Help! I'm in trouble! My current location is: " + String.valueOf(currentadd.getText());//sms value with GPS location
        smsManager.sendTextMessage(phonenumber, null, sms, null, null );//sending the text message with the gps location
        Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();//notification

    }

}catch(Exception e) {
    Toast.makeText(getApplicationContext(),
            "Message could not be sent! Check your contacts list!",//notification
            Toast.LENGTH_LONG).show();
    e.printStackTrace();
}}

        });
        /*
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
        */
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        final TextView currentadd = (TextView) findViewById(R.id.currentadd);


        Lat = location.getLatitude();
        Long = location.getLongitude();

        try {
            List<Address> addresses = geocoder.getFromLocation(Lat, Long, 1);
//getting the current address or any address remembered beforehand
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                currentadd.setText(strReturnedAddress.toString());
            } else {
                currentadd.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            currentadd.setText("Cannot get Address!");
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
