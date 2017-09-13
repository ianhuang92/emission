package monash.emission;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.net.URL;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.account.AccountActivity;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView weatherText;
    private String currentLat;
    private String currentLng;
    static final Integer LOCATION = 0x1;
    private TwitterLoginButton loginButton;
    private TwitterSession session;
    private TwitterAuthToken authToken;

    /*
    read me
    TODO 第一步：设置onActivityResult，为专用登录按钮提供支持
    TODO 如果专用登录按钮放在某fragment上，将下面onActivityResult中注释掉的代码启用，注意确保使用了正确的fragment id
    TODO 第二步：配置专用登录按钮，参考twitterFunction方法内注释
    TODO 第三步：发表推文相关功能，依赖于前面实现的登录
    TODO 第四步：配置广播接收器entity包内MyResultReceiver
          首先，确保MyResultReceiver.java已放置就位
          然后，将下文添加到Manifest的application段落中
          <receiver
            android:name=".MyResultReceiver"
            android:exported="false">
            <intent-filter>
                <action android:name="com.twitter.sdk.android.tweetcomposer.UPLOAD_SUCCESS"/>
                <action android:name="com.twitter.sdk.android.tweetcomposer.UPLOAD_FAILURE"/>
                <action android:name="com.twitter.sdk.android.tweetcomposer.TWEET_COMPOSE_CANCEL"/>
            </intent-filter>
        </receiver>
     */

    //function for twitter（第一步） 照抄下面这个方法即可
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        //TODO Twitter专用登录按钮如果在fragment内，启用下方代码寻找对应fragment
        //Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        //if (fragment != null) {
            //fragment.onActivityResult(requestCode, resultCode, data);
        //}

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    //main function for twitter
    private void twitterFunction()
    {
        //TODO 第二步，配置专用登录按钮。本方法（twitterFunction）内除了标注部分全部为第二步必须内容
        //
        //初始化
        TwitterConfig config = new TwitterConfig.Builder(MainActivity.this).debug(true).twitterAuthConfig(new TwitterAuthConfig("F838MexJj5YcBul4ERMCncQu9", "3O1kC3U3rvqpNmHvtwIssFlmHLCjnpOn5HFZFkENBNTJV0XYkk")).build();
        Twitter.initialize(config);
        //初始化专用登录按钮
        loginButton = (TwitterLoginButton) this.findViewById(R.id.login_button);
        loginButton.setEnabled(true);
        //设置按钮的监听器
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = result.data;  //获取session，也可以通过TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();  来获取session

                //TODO 第三步，细节请参考tComposer详情
                tComposer();
                // Do something with result, which provides a TwitterSession for making API calls
            }
            @Override
            public void failure(TwitterException exception) {
               //Do something if Fail to receive callback
            }
        });

    }
    private void tComposer()
    {
        //TODO 第三步：发表推推。 需要获取TwitterSession，参考下文注释
        //TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();  //可以启用本行来获取下文中的session
        final Intent intent = new ComposerActivity.Builder(MainActivity.this)  //change context to a corresponding one
                .session(session)
                .text("Love where you work") //推文正文
                .hashtags("#twitter")   //推文标签
                .createIntent();
        startActivity(intent);
        //TODO 第三步结束
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button manageBtn = (Button)findViewById(R.id.managebtn);

        twitterFunction();


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
       // selfCheck();

    }
/*
    private void selfCheck()
    {
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... params) {
                return RestClient.zip2Location(params[0]);
            }
            protected void onPostExecute(String result){
                weatherText.setText(result);
            }
        }.execute("3as3");
    }
*/

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
