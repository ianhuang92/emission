package monash.emission.account;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import monash.emission.R;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHistoryFragment extends Fragment {
    View vUH;
    private ExpandableListView expandableListView;
    private EmissionListViewAdapter infoLVAdapter;
    private List<String> expandableListTitle;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private LinkedHashMap<String, List<String>> expandableListDetail;
    private UserInfo currentUser;
    private UserDashboard u;
    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    public UserHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vUH = inflater.inflate(R.layout.fragment_user_history, container, false);
        u = (UserDashboard)getActivity();
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        currentUser = new Gson().fromJson(sharePreference.getString(currentUser.getUsername(),null),UserInfo.class);
        expandableListView = (ExpandableListView)vUH.findViewById(R.id.emissionlv);
        expandableListDetail = new LinkedHashMap<String, List<String>>();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        TextView intro = (TextView) vUH.findViewById(R.id.historytv);
        if (currentUser.getEmissionRecords().size()==0){
            intro.setText("No Emission Data");
        }else {
            intro.setText("Total: " + currentUser.getEmissionRecords().size() +" Records.");
        }
        initializeInfo();
        infoLVAdapter = new EmissionListViewAdapter(getActivity(), expandableListTitle,expandableListDetail);
        expandableListView.setAdapter(infoLVAdapter);
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                if(childPosition == 1){
//
//                }
//                return false;
//            }
//        });
        return vUH;
    }

    public void initializeInfo(){
        int i = 1 ;
        for (EmissionRecord er : currentUser.getEmissionRecords()){
            List<String> detail = new ArrayList<>();
            detail.add(er.getEmissionName() + " level: " + er.getLevel() + " kg");
            //detail.add("Click to DELETE this record");
            expandableListDetail.put(sdf.format(er.getStartDate()) + " to " + sdf.format(er.getEndDate()) + "    EM00" + i ,detail);
            i++;
        }
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
    }
}
