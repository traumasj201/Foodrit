package app.cap.foodreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import app.cap.foodreet.Adapter.ViewPagerAdapter;
import app.cap.foodreet.Fragment.MapFragment;
import app.cap.foodreet.Fragment.ProfileFragment;
import app.cap.foodreet.Fragment.StoreFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new ProfileFragment(),getString(R.string.profile));
        mViewPagerAdapter.addFragment(new MapFragment(), getString(R.string.map));
        mViewPagerAdapter.addFragment(new StoreFragment(), getString(R.string.store));
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}
