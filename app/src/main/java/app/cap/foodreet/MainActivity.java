package app.cap.foodreet;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import app.cap.foodreet.Adapter.ViewPagerAdapter;
import app.cap.foodreet.Fragment.MapFragment;
import app.cap.foodreet.Fragment.ProfileFragment;
import app.cap.foodreet.Fragment.StoreFragment;

//일반 사용자용 MAIN

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new ProfileFragment(),getString(R.string.profile), ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_person_outline_black_18dp));
        mViewPagerAdapter.addFragment(new MapFragment(), getString(R.string.map),ContextCompat.getDrawable(getApplication(), R.drawable.ic_place_black_18dp));
        mViewPagerAdapter.addFragment(new StoreFragment(), getString(R.string.store),ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_menu_black_18dp));
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}
