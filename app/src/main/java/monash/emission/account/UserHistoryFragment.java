package monash.emission.account;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import monash.emission.R;
import monash.emission.RestClient;
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
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private LinkedHashMap<String, List<String>> expandableListDetail;
    private UserInfo currentUser;
    private ArrayList<EmissionRecord> ers;
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
        //currentUser = new Gson().fromJson(sharePreference.getString(currentUser.getUsername(),null),UserInfo.class);
        expandableListView = (ExpandableListView)vUH.findViewById(R.id.emissionlv);
        expandableListDetail = new LinkedHashMap<String, List<String>>();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        final TextView intro = (TextView) vUH.findViewById(R.id.historytv);

        Button chartBTN = (Button) vUH.findViewById(R.id.button3);
        chartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = u.fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, new ChartFragment());
                ft.addToBackStack("CoffeeSlt2Duration");
                ft.commit();
            }
        });
        if (currentUser.getEmissionRecords()==null){
            intro.setText("No Emission Data");
        }else {
            intro.setText("Total: " + currentUser.getEmissionRecords().size() +" Records.");
            initializeInfo();
        }

        infoLVAdapter = new EmissionListViewAdapter(getActivity(), expandableListTitle,expandableListDetail);
        expandableListView.setAdapter(infoLVAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, int childPosition, long id) {
                if (childPosition == 1){
                    final EmissionRecord er = ers.get(groupPosition);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Confirmation")
                            .setMessage("Do you want to delete this record ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String ID = er.getId();
                                    final EmissionRecord er2;
                                    if (!currentUser.getUsername().equals("guest")) {
                                        if (groupPosition == 0){
                                            er2 = ers.get(1);
                                        }else if (groupPosition == ers.size() -1 ){
                                            er2 = ers.get(ers.size()-2);
                                        }else{
                                            if (ers.get(groupPosition-1).getName().equals(er.getName())){
                                                er2 = ers.get(groupPosition+1);
                                            }else{
                                                er2 = ers.get(groupPosition-1);
                                            }
                                        }
                                        new AsyncTask<String, Void, String>() {
                                            @Override
                                            protected String doInBackground(String... params) {
                                                return RestClient.deleteEmissionRecords(params[0]);
                                            }

                                            @Override
                                            protected void onPostExecute(String result) {
                                                if (result != null) {
                                                    Toast.makeText(getActivity(), "Delete Emission Record Successful", Toast.LENGTH_SHORT).show();
                                                    ers.remove(er);
                                                    expandableListDetail.clear();
                                                    int i = 0;
                                                    if (ers.size() != 0) {
                                                        for (EmissionRecord er : ers) {
                                                            List<String> detail = new ArrayList<>();
                                                            detail.add(er.getName() + " level: " + er.getLevel() + " kg");
                                                            detail.add("Click to DELETE this record");
                                                            expandableListDetail.put(sdf.format(er.getStartdate()) + " to " + sdf.format(er.getEnddate()) + "    EM00" + i, detail);
                                                            i++;
                                                        }
                                                        //expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                                        intro.setText("Total: " + ers.size() + " Records.");
                                                    } else {
                                                        intro.setText("No Emission Data");
                                                    }
                                                    currentUser.setEmissionRecords(ers); u.userBundle.putString("userdata",new Gson().toJson(currentUser));
                                                    //totalFriends.setText("Total Friends : " + friendList.size());
                                                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                                    // friendListsViewAdapter.notifyDataSetChanged();
                                                    infoLVAdapter.setNewItems(expandableListTitle, expandableListDetail);
                                                } else {
                                                    Toast.makeText(getActivity(), "Delete Record fail", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }.execute(ID);
                                        new AsyncTask<String, Void, String>() {
                                            @Override
                                            protected String doInBackground(String... params) {
                                                return RestClient.deleteEmissionRecords(params[0]);
                                            }

                                            @Override
                                            protected void onPostExecute(String result) {
                                                if (result != null) {
                                                    Toast.makeText(getActivity(), "Delete Emission Record Successful", Toast.LENGTH_SHORT).show();
                                                    ers.remove(er2);
                                                    expandableListDetail.clear();
                                                    int i = 0;
                                                    if (ers.size() != 0) {
                                                        for (EmissionRecord er : ers) {
                                                            List<String> detail = new ArrayList<>();
                                                            detail.add(er.getName() + " level: " + er.getLevel() + " kg");
                                                            detail.add("Click to DELETE this record");
                                                            expandableListDetail.put(sdf.format(er.getStartdate()) + " to " + sdf.format(er.getEnddate()) + "    EM00" + i, detail);
                                                            i++;
                                                        }
                                                        //expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                                        intro.setText("Total: " + ers.size() + " Records.");
                                                    } else {
                                                        intro.setText("No Emission Data");
                                                    }
                                                    currentUser.setEmissionRecords(ers); u.userBundle.putString("userdata",new Gson().toJson(currentUser));
                                                    //totalFriends.setText("Total Friends : " + friendList.size());
                                                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                                    // friendListsViewAdapter.notifyDataSetChanged();
                                                    infoLVAdapter.setNewItems(expandableListTitle, expandableListDetail);
                                                } else {
                                                    Toast.makeText(getActivity(), "Delete Record fail", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }.execute(er2.getId());
                                    }else {
                                        if (groupPosition == 0){
                                            ers.remove(ers.get(1));
                                            ers.remove(er);

                                            SharedPreferences.Editor editor = sharePreference.edit();
                                            currentUser.setEmissionRecords(ers);
                                            editor.putString("guest",new Gson().toJson(currentUser));
                                            editor.commit();
                                            Toast.makeText(getActivity(), "Delete Record Successful", Toast.LENGTH_SHORT).show();
                                            infoLVAdapter.notifyDataSetChanged();
                                        }else if (groupPosition == ers.size() -1 ){
                                            ers.remove(ers.get(ers.size()-2));
                                            ers.remove(er);
                                            SharedPreferences.Editor editor = sharePreference.edit();
                                            currentUser.setEmissionRecords(ers);
                                            editor.putString("guest",new Gson().toJson(currentUser));
                                            editor.commit();
                                            Toast.makeText(getActivity(), "Delete Record Successful", Toast.LENGTH_SHORT).show();
                                            infoLVAdapter.notifyDataSetChanged();
                                        }else{
                                            if (ers.get(groupPosition-1).getName().equals(er.getName())){
                                                ers.remove(groupPosition+1);
                                                ers.remove(er); SharedPreferences.Editor editor = sharePreference.edit();
                                                currentUser.setEmissionRecords(ers);
                                                editor.putString("guest",new Gson().toJson(currentUser));
                                                editor.commit();
                                                Toast.makeText(getActivity(), "Delete Record Successful", Toast.LENGTH_SHORT).show();
                                                infoLVAdapter.notifyDataSetChanged();
                                            }else{
                                                ers.remove(groupPosition-1);
                                                ers.remove(er); SharedPreferences.Editor editor = sharePreference.edit();
                                                currentUser.setEmissionRecords(ers);
                                                editor.putString("guest",new Gson().toJson(currentUser));
                                                editor.commit();
                                                Toast.makeText(getActivity(), "Delete Record Successful", Toast.LENGTH_SHORT).show();
                                                infoLVAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        expandableListDetail.clear();
                                        int i = 0;
                                        if (ers.size() != 0) {
                                            for (EmissionRecord er : ers) {
                                                List<String> detail = new ArrayList<>();
                                                detail.add(er.getName() + " level: " + er.getLevel() + " kg");
                                                detail.add("Click to DELETE this record");
                                                expandableListDetail.put(sdf.format(er.getStartdate()) + " to " + sdf.format(er.getEnddate()) + "    EM00" + i, detail);
                                                i++;
                                            }
                                            //expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                            intro.setText("Total: " + ers.size() + " Records.");
                                        } else {
                                            intro.setText("No Emission Data");
                                        }
                                        //totalFriends.setText("Total Friends : " + friendList.size());
                                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                                        // friendListsViewAdapter.notifyDataSetChanged();
                                        infoLVAdapter.setNewItems(expandableListTitle, expandableListDetail);

                                    }
                                    u.userBundle.putString("userdata",new Gson().toJson(currentUser));
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }


                return false;
            }
        });
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
        ers = currentUser.getEmissionRecords();
        for (EmissionRecord er : ers){
            List<String> detail = new ArrayList<>();
            detail.add(er.getName() + " level: " + er.getLevel() + " kg. Coffee bean used: " + er.getBeans() + " kg");
            detail.add("Click to DELETE this record");
            expandableListDetail.put(sdf.format(er.getStartdate()) + " to " + sdf.format(er.getEnddate()) + "    EM00" + i ,detail);
            i++;
        }
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
    }
}
