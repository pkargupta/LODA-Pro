package com.locatedamini.loda;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;
import android.provider.Settings;
import com.locatedamini.loda.R;
import com.uber.sdk.android.rides.RequestButton;
import com.uber.sdk.android.rides.RideParameters;

import android.util.Log;



public class LocationActivity extends ActionBarActivity implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected boolean gps_enabled,network_enabled;
    public LocationManager lm;
    LocationStorage locstore;
    public SharedPreferences rememberedinfo;


    TextView Latitude, Longitude, RemLatitude, RemLongitude, RemAddress,  address;
    double Lat, Long;
	    Button button1;
    Button directionsbtn;



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
		setContentView(R.layout.activity_location);
		Latitude=(TextView)findViewById(R.id.Latitude);// text to show latitude
        Longitude=(TextView)findViewById(R.id.Longitude);// text to show longitude
        address=(TextView)findViewById(R.id.address);// text to show addresses
        button1=(Button)findViewById(R.id.button1); // button
        directionsbtn = (Button) findViewById(R.id.getdirectionsbtn);
        RemAddress = (TextView) findViewById(R.id.RemAddress); //remembered location
        RemLatitude = (TextView) findViewById(R.id.RemLatitude); //remembered latitude
        RemLongitude = (TextView) findViewById(R.id.RemLongitude); //remembered longitude
        final SharedPreferences rememberedinfo = getSharedPreferences("myPrefs", MODE_PRIVATE);
        RequestButton requestButton = new RequestButton(context);
        requestButton.setClientId("your_client_id");
        RideParameters rideParams = new RideParameters.Builder()
                .setProductId("abc123-productID")
                .setPickupLocation(Float.parseFloat(Latitude.getText().toString()), Float.parseFloat(Longitude.getText().toString()), "Uber HQ", address.getText().toString())
                .setDropoffLocation(Float.parseFloat(rememberedinfo.getString("latitudeKey", "Unknown")), Float.parseFloat(rememberedinfo.getString("longitudeKey", "Unknown")), "Embarcadero", rememberedinfo.getString("addressKey", "Unknown"))
                .build();
        requestButton.setRideParameters(rideParams);
//getting the current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
//using shared preferences, this is checking to see if there is already a location that the user has previously chosen to remember.
// If there is, it is displayed on the screen
            if (rememberedinfo.contains("addressKey"))
            {
                RemAddress.setText(rememberedinfo.getString("addressKey", "Unknown"));

            }
            if (rememberedinfo.contains("latitudeKey"))
            {
                RemLatitude.setText(rememberedinfo.getString("latitudeKey", "Unknown"));

            }
            if (rememberedinfo.contains("longitudeKey"))
            {
                RemLongitude.setText(rememberedinfo.getString("longitudeKey", "Unknown"));

            }

      button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting the current location as the remembered location and replacing/updating the remlocation text. Then, the location is saved
               //so that the remembered location will come up the next time the app is opened (with SharedPreferences)
                String lat = Latitude.getText().toString();
                String lng = Longitude.getText().toString();
                String add = address.getText().toString();
                // Do something in response to button
                // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // if (!lat.equals("unknown") || !lng.equals("unknown")  ) {
                RemAddress.setText(" " + add);
                RemLatitude.setText(" " + lat);
                RemLongitude.setText(" " + lng);


              SharedPreferences.Editor editor = rememberedinfo.edit();
                editor.putString("addressKey", String.valueOf(RemAddress.getText()));
                editor.putString("latitudeKey", String.valueOf(RemLatitude.getText()));
                editor.putString("longitudeKey", String.valueOf(RemLongitude.getText()));
                editor.commit();

           }


        });

        directionsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting the directions from the user's current location to their remembered location.
                // This then calls a Maps application on the user's phone or pulls up the url from a browser on the phone.
                String saddr = String.valueOf(Latitude.getText() + "," + Longitude.getText());
                String daddr = String.valueOf(RemLatitude.getText() + "," + RemLongitude.getText());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + saddr + "&daddr=" + daddr));
                startActivity(intent);
            }


        });




    }



    @Override
    public void onLocationChanged(Location location) {
//updating the location when changed
        DecimalFormat df = new DecimalFormat("#.######");
        Latitude = (TextView) findViewById(R.id.Latitude);
        Latitude.setText(" " + df.format(location.getLatitude()));
        Longitude = (TextView) findViewById(R.id.Longitude);
        Longitude.setText(" " + df.format(location.getLongitude()));
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

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
                address.setText(strReturnedAddress.toString());
            } else {
                address.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            address.setText("Cannot get Address!");
        }
    }


    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_location,
                    container, false);
            return rootView;
        }
    }

}
