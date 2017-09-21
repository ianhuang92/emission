package monash.emission.account;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import monash.emission.R;
import monash.emission.RestClient;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * Created by Ranger on 2017/9/1.
 */

public class AccountActivity extends Activity{
    protected FragmentManager fragmentManager;
    protected Bundle userBundle;

    UserInfo currentUser;
    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_calculationv2);
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
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AccountMainFragment()).commit();
        }

    }

    @Override
    public void onBackPressed(){

    }
}
