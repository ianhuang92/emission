package monash.emission.account;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.R;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {

    View vRank;
    private ListView lv;
    private UserInfo currentUser;
    private UserDashboard u;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        u = (UserDashboard)getActivity();
        vRank = inflater.inflate(R.layout.fragment_rank, container, false);
        lv = (ListView)vRank.findViewById(R.id.recordlv);
        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        final ArrayList<String> stringArrayList = new ArrayList<>();
        for (EmissionRecord er:currentUser.getEmissionRecords()){
            stringArrayList.add(sdf.format(er.getStartdate()) + " to " + sdf.format(er.getEnddate()) + ", " + er.getName() + " : " + er.getLevel() + "kg");
        }

        if(currentUser.getEmissionRecords().size()==0){
            new AlertDialog.Builder(getActivity())
                    .setTitle(Html.fromHtml("<font color='#0000ff'>No record yet</font>"))
                    .setMessage("You don't have any emission record, go to estimation and then you can compare it to other users")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Estimate now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), CoffeeRoastActivity.class);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.normal_list_item,stringArrayList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                u.userBundle.putInt("rankselection",position);
                FragmentTransaction ft = u.fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, new RankResultFragment());
                ft.addToBackStack("CoffeeSlt2Duration");
                ft.commit();
            }
        });
       /* Button next = (Button)vRank.findViewById(R.id.rankNextBTN);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        return vRank;
    }

}
