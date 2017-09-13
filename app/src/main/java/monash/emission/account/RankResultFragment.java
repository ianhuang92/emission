package monash.emission.account;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.BuildConfig;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import monash.emission.R;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.SortingEntity;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankResultFragment extends Fragment {

    View vRank;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private UserDashboard u;
    private Button but;
    private UserInfo currentUser;
    private ArrayList<SortingEntity> otherSO2;
    private ArrayList<SortingEntity> otherCO;
    private TwitterLoginButton loginButton;
    TextView ranktv;
    TextView otherRank;
    public RankResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vRank = inflater.inflate(R.layout.fragment_rank_result, container, false);
        u = (UserDashboard)getActivity();
        but = (Button) u.findViewById(R.id.button_share2Twitter);
        but.setEnabled(true);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                if (session != null) {
                    final Intent intent = new ComposerActivity.Builder(u)
                            .session(session)
                            .text("Love where you work")
                            .hashtags("#twitter")
                            .createIntent();
                    startActivity(intent);
                    Toast.makeText(getActivity(),"Post done", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(),"Post Failed", Toast.LENGTH_SHORT).show();
            }
        });


        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        final EmissionRecord er = currentUser.getEmissionRecords().get(u.userBundle.getInt("rankselection"));
        TextView infoTV = (TextView)vRank.findViewById(R.id.enterBeanTV);
        infoTV.setText("Enter the amount of coffee bean consumed from " + sdf.format(er.getStartDate()) + " to " + sdf.format(er.getEndDate()));
        final EditText etBean = (EditText)vRank.findViewById(R.id.etBean);
        initializeOtherInfo(er.getEmissionName());
        ranktv = (TextView)vRank.findViewById(R.id.textView30);
        otherRank = (TextView)vRank.findViewById(R.id.otherRankTV);
        Button rankBTN = (Button)vRank.findViewById(R.id.getrankBTN);
        rankBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etBean.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Bean amount can not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    SortingEntity current = new SortingEntity(currentUser.getUsername(),er.getLevel()/Double.parseDouble(etBean.getText().toString()));
                    if (er.getEmissionName().equals("SO2")){
                        otherSO2 = new ArrayList<SortingEntity>();
                        otherSO2.add(new SortingEntity("Axil Roasting",12.1));
                        otherSO2.add(new SortingEntity("Di Bella",6.5));
                        otherSO2.add(new SortingEntity("Ian Coffee",18.3));
                        otherSO2.add(new SortingEntity("James Coffee",15.7));
                        otherSO2.add(new SortingEntity("John Coffee",21.9));
                        otherSO2.add(new SortingEntity("Eliza Coffee",24.4));
                        otherSO2.add(new SortingEntity("Huang's Coffee",28.2));
                        otherSO2.add(new SortingEntity("Monash Roasting",30.6));
                        otherSO2.add(current);
                        Collections.sort(otherSO2, new Comparator<SortingEntity>() {
                            @Override
                            public int compare(SortingEntity z1, SortingEntity z2) {
                                if (z1.getValue() > z2.getValue())
                                    return 1;
                                if (z1.getValue() < z2.getValue())
                                    return -1;
                                return 0;
                            }
                        });
                        int rank = otherSO2.indexOf(current) + 1;
                        ranktv.setText(rank + "");
                        if (rank == otherSO2.size() && rank != 1){
                            otherRank.setText("You relative value is " + String.format("%.02f", current.getValue()) + "\nYou are in the bottom of the list. The user just above you is " + otherSO2.get(rank-2).getName() + ", relative value is " + otherSO2.get(rank-2).getValue()+ "\n\n The standard value is : 9.4");
                        }else if (rank == 1){
                            otherRank.setText("You relative value is " + String.format("%.02f", current.getValue()) + "\nThe user just below you is " + otherSO2.get(rank).getName() + ", relative value is " + otherSO2.get(rank).getValue()+ "\n\n The standard value is : 23.4");
                        }else {
                            otherRank.setText("You relative value is " + String.format("%.02f", current.getValue()) + "\nThe user just above you is " + otherSO2.get(rank-2).getName() + ", relative value is " + otherSO2.get(rank-2).getValue() + "\nThe user just below you is " + otherSO2.get(rank).getName() + ", relative value is " + otherSO2.get(rank).getValue()+ "\n\n The standard value is : 23.4");
                        }
                    }else{
                        otherCO = new ArrayList<SortingEntity>();
                        otherCO.add(new SortingEntity("Axil Roasting",70.2));
                        otherCO.add(new SortingEntity("Di Bella",29.9));
                        otherCO.add(new SortingEntity("Ian Coffee",35.7));
                        otherCO.add(new SortingEntity("James Coffee",66.8));
                        otherCO.add(new SortingEntity("John Coffee",60.5));
                        otherCO.add(new SortingEntity("Eliza Coffee",40.8));
                        otherCO.add(new SortingEntity("Huang's Coffee",45.2));
                        otherCO.add(new SortingEntity("Monash Roasting",51.2));
                        otherCO.add(current);
                        Collections.sort(otherCO, new Comparator<SortingEntity>() {
                            @Override
                            public int compare(SortingEntity z1, SortingEntity z2) {
                                if (z1.getValue() > z2.getValue())
                                    return 1;
                                if (z1.getValue() < z2.getValue())
                                    return -1;
                                return 0;
                            }
                        });
                        int rank = otherCO.indexOf(current) + 1;
                        ranktv.setText(rank + "");
                        if (rank == otherCO.size() && rank != 1){
                            otherRank.setText("You relative value is " + String.format("%.02f", current.getValue()) + "\nYou are in the bottom of the list. The user just above you is " + otherCO.get(rank-2).getName() + ", relative value is " + otherCO.get(rank-2).getValue() + "\n\n The standard value is : 54.6");
                        }else if (rank == 1){
                            otherRank.setText("You relative value is " + String.format("%.02f", current.getValue()) + "\nThe user just below you is " + otherCO.get(rank).getName() + ", relative value is " + otherCO.get(rank).getValue()  + "\n\n The standard value is : 54.6");
                        }else {
                            otherRank.setText("You relative value is " + String.format("%.02f", current.getValue()) + "\nThe user just above you is " + otherCO.get(rank-2).getName() + ", relative value is " + otherCO.get(rank-2).getValue() + "\nThe user just below you is " + otherCO.get(rank).getName() + ", relative value is " + otherCO.get(rank).getValue()+ "\n\n The standard value is : 54.6");
                        }
                    }
                }
            }
        });
        return vRank;
    }

    private void twitterAct()
    {
        Twitter.initialize(u);
        Uri imageUri = FileProvider.getUriForFile(u,
                BuildConfig.APPLICATION_ID + ".file_provider",
                new File("t.png"));
        TweetComposer.Builder builder = new TweetComposer.Builder(u)
                .text("just setting up my Twitter Kit.")
                .image(imageUri);
        builder.show();
    }

    public void initializeOtherInfo(String substance){
        if (substance.equals("SO2")){
            otherSO2 = new ArrayList<SortingEntity>();
            otherSO2.add(new SortingEntity("Axil Roasting",12.1));
            otherSO2.add(new SortingEntity("Di Bella",6.5));
            otherSO2.add(new SortingEntity("Ian Coffee",18.3));
            otherSO2.add(new SortingEntity("James Coffee",15.7));
            otherSO2.add(new SortingEntity("John Coffee",21.9));
            otherSO2.add(new SortingEntity("Eliza Coffee",24.4));
            otherSO2.add(new SortingEntity("Huang's Coffee",28.2));
            otherSO2.add(new SortingEntity("Monash Roasting",30.6));

        }else {
            otherCO = new ArrayList<SortingEntity>();
            otherCO.add(new SortingEntity("Axil Roasting",70.2));
            otherCO.add(new SortingEntity("Di Bella",29.9));
            otherCO.add(new SortingEntity("Ian Coffee",35.7));
            otherCO.add(new SortingEntity("James Coffee",66.8));
            otherCO.add(new SortingEntity("John Coffee",60.5));
            otherCO.add(new SortingEntity("Eliza Coffee",40.8));
            otherCO.add(new SortingEntity("Huang's Coffee",45.2));
            otherCO.add(new SortingEntity("Monash Roasting",51.2));
        }
    }
}
