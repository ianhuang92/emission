package monash.emission.account;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

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
    private UserInfo currentUser;
    private ArrayList<SortingEntity> otherSO2;
    private ArrayList<SortingEntity> otherCO;
    TextView ranktv;
    TextView otherRank;


    private TwitterLoginButton loginButton;
    private TwitterSession session;
    private TwitterAuthToken authToken;

    /*
    read me
    TODO 第一步：设置onActivityResult，为专用登录按钮提供支持
    TODO 如果专用登录按钮放在某fragment上，将下面onActivityResult中注释掉的代码启用，注意确保使用了正确的fragment id
    TODO 第二步：配置专用登录按钮，参考twitterFunction方法内注释
    TODO 第三步：发表推文相关功能，依赖于前面实现的登录
    TODO 第四步：配置广播接收器entity包内MyResultReceiver
          首先，确保MyResultReceiver.java已放置就位
          然后，将下文添加到Manifest的application段落中
          <receiver
            android:name=".MyResultReceiver"
            android:exported="false">
            <intent-filter>
                <action android:name="com.twitter.sdk.android.tweetcomposer.UPLOAD_SUCCESS"/>
                <action android:name="com.twitter.sdk.android.tweetcomposer.UPLOAD_FAILURE"/>
                <action android:name="com.twitter.sdk.android.tweetcomposer.TWEET_COMPOSE_CANCEL"/>
            </intent-filter>
        </receiver>
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }



    //main function for twitter
    private void twitterFunction()
    {
        //TODO 第二步，配置专用登录按钮。本方法（twitterFunction）内除了标注部分全部为第二步必须内容
        //
        //初始化
//        TwitterConfig config = new TwitterConfig.Builder(MainActivity.this).debug(true).twitterAuthConfig(new TwitterAuthConfig("F838MexJj5YcBul4ERMCncQu9", "3O1kC3U3rvqpNmHvtwIssFlmHLCjnpOn5HFZFkENBNTJV0XYkk")).build();
//        Twitter.initialize(config);
        //初始化专用登录按钮
        loginButton = (TwitterLoginButton) vRank.findViewById(R.id.login_button);
        loginButton.setEnabled(true);
        loginButton.setText("Post to Twitter!");
        //设置按钮的监听器
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = result.data;  //获取session，也可以通过TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();  来获取session

                //TODO 第三步，细节请参考tComposer详情
                tComposer();
                // Do something with result, which provides a TwitterSession for making API calls
            }
            @Override
            public void failure(TwitterException exception) {
                //Do something if Fail to receive callback
            }
        });

    }
    private void tComposer()
    {
        //TODO 第三步：发表推推。 需要获取TwitterSession，参考下文注释
        //TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();  //可以启用本行来获取下文中的session
        final Intent intent = new ComposerActivity.Builder(getActivity())  //change context to a corresponding one
                .session(session)
                .text("I am using Emission Tracker and my Emission Rank is " + ranktv.getText()) //推文正文
                .hashtags("#Emission Tracker")   //推文标签
                .createIntent();
        startActivity(intent);
        //TODO 第三步结束
    }

    public RankResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vRank = inflater.inflate(R.layout.fragment_rank_result, container, false);
        u = (UserDashboard)getActivity();
        twitterFunction();

        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        final EmissionRecord er = currentUser.getEmissionRecords().get(u.userBundle.getInt("rankselection"));
        TextView infoTV = (TextView)vRank.findViewById(R.id.enterBeanTV);
        infoTV.setText("Rank for relative emission per kg of beans from " + sdf.format(er.getStartdate()) + " to " + sdf.format(er.getEnddate()));
        //final EditText etBean = (EditText)vRank.findViewById(R.id.etBean);
        initializeOtherInfo(er.getName());
        ranktv = (TextView)vRank.findViewById(R.id.textView30);
        otherRank = (TextView)vRank.findViewById(R.id.otherRankTV);
        //Button rankBTN = (Button)vRank.findViewById(R.id.getrankBTN);
        SortingEntity current = new SortingEntity(currentUser.getUsername(),er.getLevel()/er.getBeans());
        if (er.getName().equals("SO2")){
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
//        rankBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (etBean.getText().toString().isEmpty()){
//                    Toast.makeText(getActivity(),"Bean amount can not be empty", Toast.LENGTH_SHORT).show();
//                }else{
//
//                }
//            }
//        });
        return vRank;
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
