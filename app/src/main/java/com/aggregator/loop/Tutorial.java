package com.aggregator.loop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aggregator.Configuration.Util;
import com.aggregator.Constant.Constant;

import java.util.Locale;

public class Tutorial extends AppCompatActivity {

    ViewPager mViewPager;

    SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mViewPager= (ViewPager) findViewById(R.id.pager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        String appRun= Util.getSharedPrefrenceValue(Tutorial.this, Constant.SHARED_PREFERENCE_CHECK_APP_RUN);

        if(appRun==null){
            Util.setSharedPrefrenceValue(Tutorial.this,Constant.PREFS_NAME,Constant.SHARED_PREFERENCE_CHECK_APP_RUN,"not_first");
        }


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


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            try {
                switch (position) {
                    case 0:
                        // Games fragment activity
                        return new TutorialFragmentTwo();
                    case 1:
                        // Movies fragment activity
                        return new TutorialFragmentThree();
                    case 2:
                        // Movies fragment activity
                        return new TutorialFragmentFour();
                    case 3:
                        // Movies fragment activity
                        return new TutorialFragmentFive();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

}
