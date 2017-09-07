package monash.emission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.account.AccountActivity;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView weatherText;
    private String currentLat;
    private String currentLng;
    static final Integer LOCATION = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button manageBtn = (Button)findViewById(R.id.managebtn);

        manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(i);
            }
        });
        Button estimateBtn = (Button) findViewById(R.id.estimatebtn); //for iteration1 , the manage button wont reply
        estimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CoffeeRoastActivity.class);
                i.putExtra("IndustryName","Coffee Roasting");
                i.putExtra("entrance","fuel");
                startActivity(i);
            }
        });
        weatherText = (TextView)findViewById(R.id.weatherInfo);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        getCurrentLocation(); //get user location, if location service is not avaliable using the default location.
        getWeatherInformation();
    }


    //using backgourd task to get the weather information based on user location and display it to user.
    private void getWeatherInformation() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return RestClient.getWeatherInfo(params[0],params[1]);
            }
            @Override
            protected void onPostExecute(String result){
                if(result.length() == 0){
                    weatherText.setText("Can not get the weather information");
                }else{
                    String[] weatherInfo = result.split(",");

                    weatherText.setText("Air polution info PLACEHOLDER");
                /*    weatherText.setText("Location: " + weatherInfo[0] + ", Weather: " + weatherInfo[1] + "\nTemperature: "
                            + weatherInfo[2] + ", Humidity: " + weatherInfo[3] + "\n"  + "Wind speed: " + weatherInfo[4]);
                */
                }
            }
        }.execute(currentLat,currentLng);
    }


    /**
     * Ask user for specific permission. in this case it will be the location service
     * @param permission permissionname
     * @param requestCode permission code
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }
    private void getCurrentLocation(){

        try{
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            Criteria crta = new Criteria();
//            crta.setAccuracy(Criteria.ACCURACY_FINE);
//            crta.setAltitudeRequired(true);
//            crta.setBearingRequired(true);
//            crta.setCostAllowed(true);
//            crta.setPowerRequirement(Criteria.POWER_LOW);
            // String provider = locationManager.getBestProvider(crta, true);
            // Log.d("","provider : "+provider);
            String provider = LocationManager.GPS_PROVIDER;
            locationManager.requestLocationUpdates(provider, 1, 0, this);
            Location mLastLocation = locationManager.getLastKnownLocation(provider);
            if (mLastLocation != null) {
                currentLat = (String.valueOf(mLastLocation.getLatitude()));
                currentLng = (String.valueOf(mLastLocation.getLongitude()));
            }else{    //default location information when using android simulator(sometimes the location service not avaliable)
                currentLat = "-37.8770";
                currentLng = "145.0443";
            }
        }catch (SecurityException e){
            e.printStackTrace();
            Toast.makeText(this, "Error, please enable location service", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
