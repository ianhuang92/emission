package monash.emission.account;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.MainActivity;
import monash.emission.R;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    View vDb;
    private UserInfo currentUser;
    UserDashboard u;


    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vDb = inflater.inflate(R.layout.fragment_dashboard, container, false);
        u = (UserDashboard)getActivity();
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        final TextView welcomeTv = (TextView)vDb.findViewById(R.id.welcomeText);
        //System.out.print(u.userBundle.getString("userdata"));
        if (u.userBundle.getString("userType").equals("guest")){
            currentUser = new Gson().fromJson(sharePreference.getString("guest",null),UserInfo.class);
            welcomeTv.setText("Welcome " + currentUser.getUsername());
        }else {
                currentUser = new Gson().fromJson(u.userBundle.getString("userdata"), UserInfo.class);
                welcomeTv.setText("Welcome " + currentUser.getUsername());

        }
        welcomeTv.setText("Managing your emissions, making the world a better place!");

        ImageButton reportBTN = (ImageButton) vDb.findViewById(R.id.reportBTN);
        ImageButton rankBTN = (ImageButton) vDb.findViewById(R.id.rankBTN);
        reportBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = u.fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, new UserHistoryFragment());
                ft.addToBackStack("CoffeeSlt2Duration");
                ft.commit();
            }
        });
        rankBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = u.fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, new RankFragment());
                ft.addToBackStack("CoffeeSlt2Duration");
                ft.commit();
            }
        });
        ImageButton est = (ImageButton)vDb.findViewById(R.id.button2);
        est.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CoffeeRoastActivity.class);
                i.putExtra("IndustryName","Coffee Roasting");
                i.putExtra("entrance","fuel");
                i.putExtra("u",new Gson().toJson(currentUser));
                startActivity(i);
            }
        });

        ImageButton air = (ImageButton) vDb.findViewById(R.id.button4);
        air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);

            }
        });
        return vDb;

    }

}
