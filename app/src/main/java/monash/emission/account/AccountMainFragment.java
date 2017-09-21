package monash.emission.account;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.R;
import monash.emission.RestClient;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * Created by Ranger on 2017/9/1.
 */

public class AccountMainFragment extends Fragment {

    private View vThis;
    private AccountActivity c;
    private ToggleButton toggleButton;
    private EditText etUserName;
    private EditText etPasswd;

    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";
    private TextView tvRepeatPassword;
    private EditText etRepeatPw;
    private ToggleButton toggleButtonSwitch;
    private Button buttonNext;
    private int mode;
    UserInfo newuser = null;
    private boolean flag;
    UserInfo guest = null;
    private TextView statustv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vThis = inflater.inflate(R.layout.fragment_account_main, container, false);
        c = (AccountActivity) getActivity();
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        toggleButton = (ToggleButton)vThis.findViewById(R.id.fragment_account_toggleButton);
        etUserName = (EditText) vThis.findViewById(R.id.et_account_username);
        etPasswd = (EditText) vThis.findViewById(R.id.et_account_passwd) ;
        etRepeatPw = (EditText) vThis.findViewById(R.id.et_account_repw) ;
        statustv = (TextView)vThis.findViewById(R.id.statusTV);
        statustv.setText("Sign Up");
        tvRepeatPassword = (TextView)vThis.findViewById(R.id.tv_account_repw) ;
        toggleButtonSwitch = (ToggleButton)vThis.findViewById(R.id.toggleButton2);
        buttonNext = (Button)vThis.findViewById(R.id.but_account_next);
        mode = 0;
        buttonNext.setText("Sign Up!");
        flag = sharePreference.getBoolean("useResult",false);
        //0 -> sign up
        //1 -> login
        //2 -> guest mode
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton.setChecked(isChecked);
                etUserName.setEnabled(!isChecked);
                etPasswd.setEnabled(!isChecked);
                tvRepeatPassword.setVisibility(isChecked?View.INVISIBLE:View.VISIBLE);
                etRepeatPw.setVisibility(isChecked?View.INVISIBLE:View.VISIBLE);
                toggleButtonSwitch.setVisibility(isChecked?View.INVISIBLE:View.VISIBLE);

                if (isChecked)
                {
                    mode = 2;
                    statustv.setText("Guest mode");
                    buttonNext.setText("NEXT");

                }else{
                    mode = 0;
                    statustv.setText("Sign Up");
                    buttonNext.setText("Sign Up!");
                }

            }
        });
        toggleButtonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonNext.setText(isChecked?"Sign UP!":"Login!");
                mode = isChecked?0:1; //isChecked ==true --> Sign up mode
                tvRepeatPassword.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
                etRepeatPw.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
                if (mode == 0){
                    statustv.setText("Sign Up");
                }else if (mode == 1){
                    statustv.setText("Log In");
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Navigate to next page
                //TODO Use this.mode to identify user choice
                if(mode == 0){
                    if (etUserName.getText().equals("")||etPasswd.getText().equals("")){
                        Toast.makeText(getActivity(),"Username or password invalid",Toast.LENGTH_SHORT).show();
                    }else if(etPasswd.getText().toString().equals(etRepeatPw.getText().toString())){

                        final UserInfo newUser = new UserInfo(etUserName.getText().toString(),etPasswd.getText().toString(),"Coffee Roasting",null);
                        new AsyncTask<UserInfo, Void, String>() {
                            @Override
                            protected String doInBackground(UserInfo... params) {
                                return RestClient.createUser(params[0]);
                            }

                            @Override
                            protected void onPostExecute(String result){
                                if (result.length() == 0){
                                    Toast.makeText(getActivity(),"Create User Successful",Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sharePreference.edit();
                                    //editor.putString(etUserName.getText().toString(),new Gson().toJson(newuser));
                                    editor.putString("CurrentUser",newUser.getUsername()).commit();//持久化用户实例，并设置CurrentUser为当前用户名


                                    if (flag){
                                        final Dialog dialog = new Dialog(getActivity());
                                        dialog.setContentView(R.layout.dialog);
                                        dialog.setTitle("Data Saved");
                                        TextView detail = (TextView)dialog.findViewById(R.id.detailText);
                                        detail.setText(sharePreference.getString("prompt","ERROR in saving the data") + newUser.getUsername());
                                        Button ok = (Button) dialog.findViewById(R.id.okBTN);
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                newUser.setEmissionRecords((ArrayList<EmissionRecord>) new Gson().fromJson(sharePreference.getString("result",null),new TypeToken<ArrayList<EmissionRecord>>(){}.getType()));
                                                SharedPreferences.Editor editor = sharePreference.edit();
                                                editor.remove("useResult");
                                               // editor.putString(newUser.getUsername(),new Gson().toJson(newUser));
                                                editor.commit();
                                                ///
                                                ArrayList<EmissionRecord> ers = new Gson().fromJson(sharePreference.getString("result",null),new TypeToken<ArrayList<EmissionRecord>>(){}.getType());
                                                ers.get(0).setUsername(newUser.getUsername());
                                                ers.get(1).setUsername(newUser.getUsername());
                                                new AsyncTask<EmissionRecord, Void, String>() {
                                                    @Override
                                                    protected String doInBackground(EmissionRecord... params) {
                                                        return RestClient.createEmissionRecords(params[0],params[1]);
                                                    }

                                                    @Override
                                                    protected void onPostExecute(String result){
                                                        if (result.equals("")){
                                                            System.out.println("OKk");
                                                            SharedPreferences.Editor editor = sharePreference.edit();
                                                            editor.remove("useResult");
                                                            //editor.putString(user.getUsername(),new Gson().toJson(user));
                                                            //c.userBundle.putString("userdata",new Gson().toJson(user));
                                                            editor.putString("CurrentUser",newUser.getUsername()).commit();
                                                            dialog.dismiss();
                                                            Intent i = new Intent(getActivity(), CoffeeRoastActivity.class);
                                                            startActivity(i);flag=false;
                                                        }
                                                    }
                                                }.execute(ers.get(0),ers.get(1));
                                            }
                                        });

                                        dialog.show();
                                    }else{
                                        c.userBundle.putString("userType","login");
                                        newUser.setEmissionRecords(new ArrayList<EmissionRecord>());
                                        c.userBundle.putString("userdata",new Gson().toJson(newUser));
                                        Intent i = new Intent(getActivity(), UserDashboard.class);
                                        i.putExtra("bundle",c.userBundle);
                                        startActivity(i);
                                    }


                                }else if (result.equals("ERROR")){
                                    Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(),"User Already Exist",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute(newUser);
                    }else{
                        Toast.makeText(getActivity(),"Repeat password does not match password",Toast.LENGTH_SHORT).show();
                    }


                }else if (mode == 1) {
                    //validate user info
                    if (etUserName.getText().toString().length()==0 || etPasswd.getText().toString().length()==0){
                        Toast.makeText(getActivity(),"Username or password can not be empty",Toast.LENGTH_SHORT).show();
                    }else {
                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... params) {
                                return RestClient.getUser(params[0]);
                            }

                            @Override
                            protected void onPostExecute(String result){
                                if (result.equals("ERROR")){
                                    Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_SHORT).show();
                                }else if(result.equals("NOUSER")){
                                    Toast.makeText(getActivity(),"User does not exist",Toast.LENGTH_SHORT).show();
                                }else {
                                    final UserInfo user = new Gson().fromJson(result,UserInfo.class);
                                    if (user.getPassword().equals(etPasswd.getText().toString())){
                                        if (flag){
                                            final Dialog dialog = new Dialog(getActivity());
                                            dialog.setContentView(R.layout.dialog);
                                            dialog.setTitle("Data Saved");
                                            TextView detail = (TextView)dialog.findViewById(R.id.detailText);
                                            detail.setText(sharePreference.getString("prompt","ERROR in saving the data") + user.getUsername());
                                            Button ok = (Button) dialog.findViewById(R.id.okBTN);
                                            ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    ArrayList<EmissionRecord> ers = new Gson().fromJson(sharePreference.getString("result",null),new TypeToken<ArrayList<EmissionRecord>>(){}.getType());
                                                    ers.get(0).setUsername(user.getUsername());
                                                    ers.get(1).setUsername(user.getUsername());
                                                    new AsyncTask<EmissionRecord, Void, String>() {
                                                        @Override
                                                        protected String doInBackground(EmissionRecord... params) {
                                                            return RestClient.createEmissionRecords(params[0],params[1]);
                                                        }

                                                        @Override
                                                        protected void onPostExecute(String result){
                                                            if (result.equals("")){
                                                                System.out.println("OKk");
                                                                SharedPreferences.Editor editor = sharePreference.edit();
                                                                editor.remove("useResult");
                                                                //editor.putString(user.getUsername(),new Gson().toJson(user));
                                                                //c.userBundle.putString("userdata",new Gson().toJson(user));
                                                                editor.putString("CurrentUser",user.getUsername()).commit();
                                                                dialog.dismiss();flag=false;
                                                                Intent i = new Intent(getActivity(), CoffeeRoastActivity.class);
                                                                startActivity(i);
                                                            }
                                                        }
                                                    }.execute(ers.get(0),ers.get(1));

                                                    //user.addEmissionRecords((ArrayList<EmissionRecord>) new Gson().fromJson(sharePreference.getString("result",null),new TypeToken<ArrayList<EmissionRecord>>(){}.getType()));

                                                }
                                            });
                                            dialog.show();

                                        }else{
                                            c.userBundle.putString("userType","login");

                                            new AsyncTask<String, Void, String>() {
                                                @Override
                                                protected String doInBackground(String... params) {
                                                    return RestClient.getEmissionRecords(params[0]);
                                                }

                                                @Override
                                                protected void onPostExecute(String result){
                                                    if (result.length()!=0){
                                                        ArrayList<EmissionRecord> er = new Gson().fromJson(result,new TypeToken<ArrayList<EmissionRecord>>(){}.getType());
                                                        user.setEmissionRecords(er);
                                                        c.userBundle.putString("userdata",new Gson().toJson(user));
                                                        Intent i = new Intent(getActivity(), UserDashboard.class);
                                                        i.putExtra("bundle",c.userBundle);
                                                        SharedPreferences.Editor editor = sharePreference.edit();
                                                        editor.putString("CurrentUser",user.getUsername()).commit();
                                                        startActivity(i);
                                                    }
                                                }
                                            }.execute(user.getUsername());

                                        }


                                    }else {
                                        Toast.makeText(getActivity(),"Password not correct",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }.execute(etUserName.getText().toString());
                    }




                }else if (mode == 2){

                    c.userBundle.putString("userType","guest");
                    String info = sharePreference.getString("guest",null);

                    if (info == null){
                        guest = new UserInfo("guest","guest","Coffee Roasting",new ArrayList<EmissionRecord>());
                        if (flag){
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog);
                            dialog.setTitle("Data Saved");
                            TextView detail = (TextView)dialog.findViewById(R.id.detailText);
                            detail.setText(sharePreference.getString("prompt","ERROR in saving the data") + guest.getUsername());
                            Button ok = (Button) dialog.findViewById(R.id.okBTN);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    guest.addEmissionRecords((ArrayList<EmissionRecord>) new Gson().fromJson(sharePreference.getString("result",null),new TypeToken<ArrayList<EmissionRecord>>(){}.getType()));
                                    SharedPreferences.Editor editor = sharePreference.edit();
                                    editor.remove(guest.getUsername());
                                    editor.remove("useResult");
                                    editor.putString("CurrentUser","guest");
                                    editor.putString(guest.getUsername(),new Gson().toJson(guest));
                                    editor.commit();
                                    dialog.dismiss();
                                    c.userBundle.putString("userdata",new Gson().toJson(guest));
                                    Intent i = new Intent(getActivity(), CoffeeRoastActivity.class);
                                    startActivity(i);
                                }
                            });

                            dialog.show();
                        }else{

                            SharedPreferences.Editor editor = sharePreference.edit();
                            editor.putString("guest", new Gson().toJson(guest));
                            editor.putString("CurrentUser","guest");
                            editor.commit();
                            c.userBundle.putString("userdata",new Gson().toJson(guest));
                            Intent i = new Intent (getActivity(), UserDashboard.class);
                            i.putExtra("bundle",c.userBundle);
                            startActivity(i);
                        }

                    }else{
                        guest = new Gson().fromJson(info,UserInfo.class);
                        if (flag){
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog);
                            dialog.setTitle("Data Saved");
                            TextView detail = (TextView)dialog.findViewById(R.id.detailText);
                            detail.setText(sharePreference.getString("prompt","ERROR in saving the data") + "guest");
                            Button ok = (Button) dialog.findViewById(R.id.okBTN);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    guest.addEmissionRecords((ArrayList<EmissionRecord>) new Gson().fromJson(sharePreference.getString("result",null),new TypeToken<ArrayList<EmissionRecord>>(){}.getType()));
                                    SharedPreferences.Editor editor = sharePreference.edit();
                                    editor.remove(guest.getUsername());
                                    editor.remove("useResult");
                                    editor.putString(guest.getUsername(),new Gson().toJson(guest));
                                    editor.putString("CurrentUser","guest");
                                    editor.commit();
                                    dialog.dismiss();
                                    c.userBundle.putString("userdata",new Gson().toJson(guest));
                                    Intent i = new Intent(getActivity(), CoffeeRoastActivity.class);
                                    startActivity(i);
                                }
                            });

                            dialog.show();
                        }else{
                            SharedPreferences.Editor editor = sharePreference.edit();
                            editor.putString("guest", new Gson().toJson(guest));editor.putString("CurrentUser","guest");
                            editor.commit();
                            c.userBundle.putString("userdata",new Gson().toJson(guest));

                            Intent i = new Intent (getActivity(), UserDashboard.class);
                            i.putExtra("bundle",c.userBundle);
                            startActivity(i);
                        }
                    }
                }
            }
        });



        return vThis;
    }


}