package monash.emission.account;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.R;

/**
 * Created by Ranger on 2017/9/1.
 */

public class AccountMainFragment extends Fragment {

    private View vThis;
    private AccountActivity c;
    private ToggleButton toggleButton;
    private EditText etUserName;
    private EditText etPasswd;
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
            }
        });



        return vThis;
    }


}