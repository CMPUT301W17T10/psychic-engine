package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.FileManager;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.Follows;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Fragments.HistoryTabFragment;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;
import com.psychic_engine.cmput301w17t10.feelsappman.Fragments.RecentTabFragment;
import com.psychic_engine.cmput301w17t10.feelsappman.Fragments.SummaryTabFragment;

import java.util.ArrayList;
import java.util.List;

public class SelfNewsFeedActivity extends AppCompatActivity {
    private ParticipantSingleton instance;
    private Spinner spinner;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_news_feed_actvity);
        instance = ParticipantSingleton.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(instance.getSelfParticipant().getLogin());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSectionsPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupspinners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_self_news_feed_actvity, menu);
        return true;
    }

    /**
     * Listener for any clicked widget. But if the widget is of where you would like to add
     * a mood event, then the app will direct the participant to the CreateMoodActivity where they
     * will be able to create a mood event.
     * @see CreateMoodActivity
     * @param item
     * @return true if clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(SelfNewsFeedActivity.this,CreateMoodActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapter for the fragments to properly initialize them with a title and proper positioning.
     * @see RecentTabFragment
     * @see HistoryTabFragment
     * @see SummaryTabFragment
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    RecentTabFragment recentTabFragment = new RecentTabFragment();
                    return recentTabFragment;
                case 1:
                    HistoryTabFragment historyTabFragment = new HistoryTabFragment();
                    return historyTabFragment;
                case 2:
                    SummaryTabFragment summaryTabFragment = new SummaryTabFragment();
                    return summaryTabFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
            return "Recent";
            case 1:
            return "History";
            case 2:
            return "Summary";
        }
            return null;
        }
    }
    void setupspinners(){
        spinner = (Spinner) (findViewById(R.id.dropdown));

        List<String>managefollow = new ArrayList<String>();
        managefollow.add("");
        Follows[] followses = Follows.values();
        for (Follows follows : followses) {
            managefollow.add(follows.toString());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,managefollow);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        Intent intent = new Intent(SelfNewsFeedActivity.this,FollowersActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent following = new Intent(SelfNewsFeedActivity.this,FollowingActivity.class);
                        startActivity(following);
                        break;
                    case 3:
                        Intent followrequest = new Intent(SelfNewsFeedActivity.this,FollowRequestActivity.class);
                        startActivity(followrequest);
                        break;
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    /**
     * Attempt to save the instance when the activity pauses
     */
    @Override
    protected void onPause() {
        super.onPause();
        FileManager.saveInFile(this);
        spinner.setSelection(0);
    }

    /**
     * Attempt to save the instance when the activity stops running
     */
    @Override
    public void onStop() {
        super.onStop();
        FileManager.saveInFile(this);
    }

}
