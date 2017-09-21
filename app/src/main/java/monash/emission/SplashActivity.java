package monash.emission;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import monash.emission.account.UserDashboard;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * Created by 80576 on 19/09/2017.
 */
public class SplashActivity extends AppCompatActivity {

    protected Bundle userBundle;

    UserInfo currentUser;
    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userBundle = new Bundle();
        sharePreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        if (sharePreference.getString("CurrentUser",null) != null){
            if (sharePreference.getString("CurrentUser",null).equals("guest")){
                currentUser = new Gson().fromJson(sharePreference.getString("guest",null), UserInfo.class);
                Intent i = new Intent(getApplicationContext(), UserDashboard.class);
                Bundle userBundle = new Bundle();
                userBundle.putString("userdata",new Gson().toJson(currentUser));
                userBundle.putString("userType","guest");
                //Intent i = new Intent(getActivity(), UserDashboard.class);
                i.putExtra("bundle",userBundle);
                startActivity(i);
            }else {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        return RestClient.getUser(params[0]);
                    }

                    @Override
                    protected void onPostExecute(String result)
                    {
                        if (result.length()!=0){
                            currentUser = new Gson().fromJson(result,UserInfo.class);
                            new AsyncTask<String, Void, String>() {
                                @Override
                                protected String doInBackground(String... params) {
                                    return RestClient.getEmissionRecords(params[0]);
                                }

                                @Override
                                protected void onPostExecute(String result){
                                    if (result.length()!=0){
                                        ArrayList<EmissionRecord> er = new Gson().fromJson(result,new TypeToken<ArrayList<EmissionRecord>>(){}.getType());
                                        currentUser.setEmissionRecords(er);
                                        Intent i = new Intent(getApplicationContext(), UserDashboard.class);
                                        Bundle userBundle = new Bundle();
                                        userBundle.putString("userdata",new Gson().toJson(currentUser));
                                        userBundle.putString("userType","login");
                                        //Intent i = new Intent(getActivity(), UserDashboard.class);
                                        i.putExtra("bundle",userBundle);
                                        startActivity(i);
                                        //welcomeTv.setText("Welcome " + currentUser.getUsername());
                                    }
                                }
                            }.execute(currentUser.getUsername());
                        }
                    }
                }.execute(sharePreference.getString("CurrentUser",null));}
        }else {
          UserInfo  guest = new UserInfo("guest","guest","Coffee Roasting",new ArrayList<EmissionRecord>());
            Intent i = new Intent(getApplicationContext(), UserDashboard.class);
            Bundle userBundle = new Bundle();
            SharedPreferences.Editor editor = sharePreference.edit();
            //editor.remove(guest.getUsername());
            //editor.remove("useResult");
            editor.putString("CurrentUser","guest");
            editor.putString(guest.getUsername(),new Gson().toJson(guest));
            editor.commit();
            userBundle.putString("userdata",new Gson().toJson(guest));
            userBundle.putString("userType","guest");
            //Intent i = new Intent(getActivity(), UserDashboard.class);
            i.putExtra("bundle",userBundle);
            startActivity(i);
        }
    }
}