package monash.emission.account;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import monash.emission.R;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    View vDb;
    private UserInfo currentUser;
    UserDashboard u;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vDb = inflater.inflate(R.layout.fragment_dashboard, container, false);
        u = (UserDashboard)getActivity();
        System.out.print(u.userBundle.getString("userdata"));
        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        TextView welcomeTv = (TextView)vDb.findViewById(R.id.welcomeText);
        welcomeTv.setText("Welcome " + currentUser.getUsername());
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
        return vDb;

    }

}
