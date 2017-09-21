package monash.emission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import monash.emission.entity.UserInfo;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView weatherText;
    private String currentLat;
    UserInfo currentUser;
    private String currentLng;

    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";
    static final Integer LOCATION = 0x1;
    ImageView recom;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        recom = (ImageView)findViewById(R.id.imageView2);
        //Button manageBtn = (Button)findViewById(R.id.managebtn);
        TwitterConfig config = new TwitterConfig.Builder(MainActivity.this).debug(true).twitterAuthConfig(new TwitterAuthConfig("F838MexJj5YcBul4ERMCncQu9", "3O1kC3U3rvqpNmHvtwIssFlmHLCjnpOn5HFZFkENBNTJV0XYkk")).build();
        Twitter.initialize(config);
//        manageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sharePreference.getString("CurrentUser",null) == null){
//                    Intent i = new Intent(getApplicationContext(), AccountActivity.class);
//                    startActivity(i);
//                }else{
//
//
//                    //i.putExtra("cu",sharePreference.getString("CurrentUser",null));
//                    new AsyncTask<String, Void, String>() {
//                        @Override
//                        protected String doInBackground(String... params) {
//                            return RestClient.getUser(params[0]);
//                        }
//
//                        @Override
//                        protected void onPostExecute(String result)
//                        {
//                            if (result.length()!=0){
//                                currentUser = new Gson().fromJson(result,UserInfo.class);
//                                new AsyncTask<String, Void, String>() {
//                                    @Override
//                                    protected String doInBackground(String... params) {
//                                        return RestClient.getEmissionRecords(params[0]);
//                                    }
//
//                                    @Override
//                                    protected void onPostExecute(String result){
//                                        if (result.length()!=0){
//                                            ArrayList<EmissionRecord> er = new Gson().fromJson(result,new TypeToken<ArrayList<EmissionRecord>>(){}.getType());
//                                            currentUser.setEmissionRecords(er);
//                                            Intent i = new Intent(getApplicationContext(), UserDashboard.class);
//                                            Bundle userBundle = new Bundle();
//                                            userBundle.putString("userdata",new Gson().toJson(currentUser));
//                                            userBundle.putString("userType","login");
//                                            //Intent i = new Intent(getActivity(), UserDashboard.class);
//                                            i.putExtra("bundle",userBundle);
//                                            startActivity(i);
//                                            //welcomeTv.setText("Welcome " + currentUser.getUsername());
//                                        }
//                                    }
//                                }.execute(currentUser.getUsername());
//                            }
//                        }
//                    }.execute(sharePreference.getString("CurrentUser",null));
//
//
//                }
//
//            }
//        });
//        Button estimateBtn = (Button) findViewById(R.id.estimatebtn); //for iteration1 , the manage button wont reply
//        estimateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),CoffeeRoastActivity.class);
//                i.putExtra("IndustryName","Coffee Roasting");
//                i.putExtra("entrance","fuel");
//                startActivity(i);
//            }
//        });
        weatherText = (TextView)findViewById(R.id.weatherInfo);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        getCurrentLocation(); //get user location, if location service is not avaliable using the default location.
        getWeatherInformation();
        Button zipbtn = (Button)findViewById(R.id.zipBTN);
        zipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.custom_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        weatherText.setText("Loading weather information....");
                                        new AsyncTask<String,Void,String>(){

                                            @Override
                                            protected String doInBackground(String... params) {
                                                return RestClient.zip2Location(params[0]);
                                            }
                                            @Override
                                            protected void onPostExecute(String result){
                                                if (result.length()==0){
                                                    weatherText.setText("Zip code not valid");
                                                }else if (result.equalsIgnoreCase("ZERO_RESULTS"))
                                                {
                                                    weatherText.setText("No result for that zip code");
                                                }
                                                else{
                                                    new AsyncTask<String,Void,String>(){

                                                        @Override
                                                        protected String doInBackground(String... params) {
                                                            return RestClient.getAirQuality(params[0],params[1]);
                                                        }

                                                        @Override
                                                        protected void onPostExecute(String result){
                                                            final String[] weatherInfo = result.split("@");

                                                            //weatherText.setText("Air polution info PLACEHOLDER");
                                                            weatherText.setText(weatherInfo[0] + "\n" + weatherInfo[1] + "\n"
                                                                    + weatherInfo[2] + "\n" + weatherInfo[3] + "\n");
                                                            recom.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    new AlertDialog.Builder(MainActivity.this)
                                                                            .setTitle(Html.fromHtml("<font color='#0000ff'>Recommendations</font>"))
                                                                            .setMessage(weatherInfo[4])
                                                                            .setIcon(android.R.drawable.btn_star_big_on)
                                                                            .setPositiveButton("Ok",null).show();
                                                                }
                                                            });
                                                        }

                                                    }.execute(result.split(","));
                                                }
                                            }
                                        }.execute(userInput.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    //using backgourd task to get the weather information based on user location and display it to user.
    private void getWeatherInformation() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return RestClient.getAirQuality(params[0],params[1]);
            }
            @Override
            protected void onPostExecute(String result){
                if(result.length() == 0){
                    weatherText.setText("Can not get the weather information");
                }else{
                    final String[] weatherInfo = result.split("@");

                    //weatherText.setText("Air polution info PLACEHOLDER");
                   weatherText.setText(weatherInfo[0] + "\n" + weatherInfo[1] + "\n"
                            + weatherInfo[2] + "\n" + weatherInfo[3] + "\n");
                   recom.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           new AlertDialog.Builder(MainActivity.this)
                                   .setTitle(Html.fromHtml("<font color='#0000ff'>Recommendations</font>"))
                                   .setMessage(weatherInfo[4])
                                   .setIcon(android.R.drawable.btn_star_big_on)
                                   .setPositiveButton("Ok",null).show();
                       }
                   });

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
