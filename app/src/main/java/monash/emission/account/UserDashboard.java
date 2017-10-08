package monash.emission.account;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import monash.emission.CoffeeRoastingFuncV2.CoffeeRoastActivity;
import monash.emission.MainActivity;
import monash.emission.R;

public class UserDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    protected Bundle userBundle;
    protected FragmentManager fragmentManager;
    //function for twitter（第一步） 照抄下面这个方法即可
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        //TODO Twitter专用登录按钮如果在fragment内，启用下方代码寻找对应fragment
        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        // loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TwitterConfig config = new TwitterConfig.Builder(UserDashboard.this).debug(true).twitterAuthConfig(new TwitterAuthConfig("F838MexJj5YcBul4ERMCncQu9", "3O1kC3U3rvqpNmHvtwIssFlmHLCjnpOn5HFZFkENBNTJV0XYkk")).build();
        Twitter.initialize(config);
        sharePreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        userBundle = getIntent().getBundleExtra("bundle");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(userBundle.getString("userdata") == null) {
            userBundle.putString("userdata", sharePreference.getString("userdata", null));
        }
        int flag = getIntent().getIntExtra("drawer",0);
        switch (flag){
            case 0:fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new DashboardFragment(),"db").commit();break;
            case 1:fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new UserHistoryFragment()).commit();break;
            case 2:fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new RankFragment()).commit();break;
            default:fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new DashboardFragment()).commit();break;
        }


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
         //   super.onBackPressed();
            DashboardFragment myFragment = null;
            try {
                myFragment = (DashboardFragment) getFragmentManager().findFragmentByTag("db");
                if (myFragment != null && myFragment.isVisible()) {
                    // add your code here
                }else{
                    super.onBackPressed();
                }
            } catch (ClassCastException e) {
                super.onBackPressed();
            }


//
//            int count = fragmentManager.getBackStackEntryCount();
//            if(count > 0){
//                super.onBackPressed();
//            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.user_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment nextFragment = null;
        if (id == R.id.nav_camera) {
            nextFragment = new DashboardFragment();
        } else if (id == R.id.nav_gallery) {
            nextFragment = new UserHistoryFragment();
        } else if (id == R.id.nav_slideshow) {
            nextFragment = new RankFragment();
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(getApplicationContext(),CoffeeRoastActivity.class);
            i.putExtra("IndustryName","Coffee Roasting");
            i.putExtra("entrance","fuel");
            startActivity(i);
            return true;
        } else if (id == R.id.nav_gallery2) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_share) {
            SharedPreferences.Editor editor = sharePreference.edit();
            editor.remove("CurrentUser");
            editor.commit();
            Intent i = new Intent(this,AccountActivity.class);
            startActivity(i);
            finish();
            return true;
        }else if (id == R.id.nav_about){
            nextFragment = new AboutFragment();
        }
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                nextFragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
