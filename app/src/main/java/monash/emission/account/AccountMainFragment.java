package monash.emission.account;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

import monash.emission.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vThis = inflater.inflate(R.layout.fragment_account_main, container, false);
        c = (AccountActivity) getActivity();
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        toggleButton = (ToggleButton)vThis.findViewById(R.id.fragment_account_toggleButton);
        etUserName = (EditText) vThis.findViewById(R.id.et_account_username);
        etPasswd = (EditText) vThis.findViewById(R.id.et_account_passwd) ;
        etRepeatPw = (EditText) vThis.findViewById(R.id.et_account_repw) ;
        tvRepeatPassword = (TextView)vThis.findViewById(R.id.tv_account_repw) ;
        toggleButtonSwitch = (ToggleButton)vThis.findViewById(R.id.toggleButton2);
        buttonNext = (Button)vThis.findViewById(R.id.but_account_next);
        mode = 0;
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
                    buttonNext.setText("NEXT");
                }
                mode = 2;
            }
        });
        toggleButtonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonNext.setText(isChecked?"Sign UP!":"Login!");
                mode = isChecked?0:1; //isChecked ==true --> Sign up mode
                tvRepeatPassword.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
                etRepeatPw.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Navigate to next page
                //TODO Use this.mode to identify user choice
                if(mode == 0){
                   // tvRepeatPassword.setVisibility(View.INVISIBLE);
                    //etRepeatPw.setVisibility(View.INVISIBLE);
                    //create the new user
                    if (etUserName.getText().equals("")||etPasswd.getText().equals("")){
                        Toast.makeText(getActivity(),"Username or password invalid",Toast.LENGTH_SHORT).show();
                    }else{
                        if(sharePreference.getString(etUserName.getText().toString(),null) == null){
                            UserInfo newuser = new UserInfo(etUserName.getText().toString(),etPasswd.getText().toString(),"Coffee Roasting",new ArrayList<EmissionRecord>());
                            SharedPreferences.Editor editor = sharePreference.edit();
                            editor.putString(etUserName.getText().toString(),new Gson().toJson(newuser));
                            editor.commit();
                            c.userBundle.putString("userType","login");
                            c.userBundle.putString("userdata",new Gson().toJson(newuser));
                            Intent i = new Intent(getActivity(), UserDashboard.class);
                            i.putExtra("bundle",c.userBundle);
                            startActivity(i);
                        }else{
                            Toast.makeText(getActivity(),"User already exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if (mode == 1) {
                    //validate user info
                    String info = sharePreference.getString(etUserName.getText().toString(),null);
                    if (info == null){
                        Toast.makeText(getActivity(),"User Not exist",Toast.LENGTH_SHORT).show();
                    }else {
                        UserInfo user = new Gson().fromJson(info,UserInfo.class);
                        if (user.getPassword().equals(etPasswd.getText().toString())){
                            c.userBundle.putString("userdata",info);
                            c.userBundle.putString("userType","login");
                            Intent i = new Intent(getActivity(), UserDashboard.class);
                            i.putExtra("bundle",c.userBundle);
                            startActivity(i);
                        }else {
                            Toast.makeText(getActivity(),"Password not correct",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else if (mode == 2){
                    Intent i = new Intent (getActivity(), UserDashboard.class);
                    c.userBundle.putString("userType","guest");
                    String info = sharePreference.getString("guest",null);
                    if (info == null){
                        UserInfo guest = new UserInfo("guest","guest","Coffee Roasting",new ArrayList<EmissionRecord>());
                        SharedPreferences.Editor editor = sharePreference.edit();
                        editor.putString("guest", new Gson().toJson(guest));
                        editor.commit();
                        c.userBundle.putString("userdata",new Gson().toJson(guest));
                    }else{
                        c.userBundle.putString("userdata",info);
                    }

                    i.putExtra("bundle",c.userBundle);
                    startActivity(i);
                }
            }
        });



        return vThis;
    }


}