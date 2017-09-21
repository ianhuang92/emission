package monash.emission.CoffeeRoastingFuncV2;

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
import android.view.MenuItem;

import com.google.gson.Gson;

import monash.emission.MainActivity;
import monash.emission.R;
import monash.emission.account.AccountActivity;
import monash.emission.account.UserDashboard;
import monash.emission.entity.UserInfo;

/**
 * Created by Ranger on 2017/8/25.
 */

public class CoffeeRoastActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //protected ArrayList<Bundle> sharedBundles;
    protected Bundle sharedBundle;
    protected FragmentManager fragmentManager;
    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";
    private DrawerLayout drawer;
    private UserInfo currentUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_user_dashboard);
        //sharedBundles = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentUser = new Gson().fromJson(getIntent().getStringExtra("u"),UserInfo.class);
        sharedBundle = new Bundle();
        //About this bundle
        //Used Key -- Type -- Meaning
        //CheckedName -- String -- Name of methodology selected
        //CheckedID  -- int -- ID of radio button selected in group, starting from 0
        //StartDate -- String -- start date selected, dd-MM-yyyy
        //EndDate -- String -- end date selected, dd-MM-yyyy
        //Duration -- int --number of days between end date and start date.
        //AvgHour -- Double -- average running hour per day
        //Mass  -- Double -- Mass of beans
        Bundle bundle = new Bundle();
        bundle.putString("CheckedName","Fuel Analysis");
        bundle.putInt("CheckedID",0);
        //c.sharedBundles.add(bundle);
        sharedBundle = bundle;

        //fragment manager
        fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeDurationFragment()).commit();


    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment nextFragment = null;
        if (id == R.id.nav_camera) {
            Intent i = new Intent(getApplicationContext(), UserDashboard.class);
            i.putExtra("drawer",0);
            Bundle userBundle = new Bundle();
            userBundle.putString("userdata",new Gson().toJson(currentUser));
            userBundle.putString("userType","guest");
            //Intent i = new Intent(getActivity(), UserDashboard.class);
            i.putExtra("bundle",userBundle);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(getApplicationContext(), UserDashboard.class);
            i.putExtra("drawer",1);
            Bundle userBundle = new Bundle();
            userBundle.putString("userdata",new Gson().toJson(currentUser));
            userBundle.putString("userType","guest");
            //Intent i = new Intent(getActivity(), UserDashboard.class);
            i.putExtra("bundle",userBundle);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(getApplicationContext(), UserDashboard.class);
            i.putExtra("drawer",2);
            Bundle userBundle = new Bundle();
            userBundle.putString("userdata",new Gson().toJson(currentUser));
            userBundle.putString("userType","guest");
            //Intent i = new Intent(getActivity(), UserDashboard.class);
            i.putExtra("bundle",userBundle);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(getApplicationContext(),CoffeeRoastActivity.class);
            i.putExtra("IndustryName","Coffee Roasting");
            i.putExtra("entrance","fuel");
            drawer.closeDrawer(GravityCompat.START);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_gallery2) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_share) {
            SharedPreferences.Editor editor = sharePreference.edit();
            editor.remove("CurrentUser");
            editor.commit();
            Intent i = new Intent(this,AccountActivity.class);
            startActivity(i);
            finish();

            return true;
        }
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                nextFragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
