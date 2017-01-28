package com.swetupuru.materialtest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.swetupuru.extras.SortListener;
import com.swetupuru.fragment.FragmentBoxOffice;
import com.swetupuru.fragment.FragmentSearch;
import com.swetupuru.fragment.FragmentUpcoming;
import com.swetupuru.fragment.MyFragment;
import com.swetupuru.fragment.NavigationDrawerFragment;
import com.swetupuru.tabs.SlidingTabLayout;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends ActionBarActivity implements MaterialTabListener,View.OnClickListener{

    Toolbar toolbar;
    private ViewPager viewPager;
    private MaterialTabHost tabHost;
    ViewPagerAdapter adapter;
    // private SlidingTabLayout mTabs;
    public static final int MOVIES_SEARCH_RESULTS=0;
    public static final int MOVIES_HITS=1;
    public static final int MOVIES_UPCOMING=2;

    //tag associated with the FAB menu button that sorts by name
    private static final String TAG_SORT_NAME = "sortName";
    //tag associated with the FAB menu button that sorts by date
    private static final String TAG_SORT_DATE = "sortDate";
    //tag associated with the FAB menu button that sorts by ratings
    private static final String TAG_SORT_RATINGS = "sortRatings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFAB();
        setUpDrawer();
        setUpTabs();

       /* mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs= (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabs.setViewPager(mPager);*/


    }

    private void setupFAB() {
        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.ic_action_new);

        FloatingActionButton mFAB = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        ImageView iconSortName= new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate= new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView iconSortRatings= new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.ic_action_important);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_gray));
        //build the sub buttons
        SubActionButton buttonSortName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortRatings = itemBuilder.setContentView(iconSortRatings).build();

        // setting the tag to each button
        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortDate.setTag(TAG_SORT_DATE);
        buttonSortRatings.setTag(TAG_SORT_RATINGS);

        //adding clicklistener to the floating action button
        buttonSortName.setOnClickListener(this);
        buttonSortDate.setOnClickListener(this);
        buttonSortRatings.setOnClickListener(this);

        //add the sub buttons to the main floating action button
        FloatingActionMenu mFABMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRatings)
                .attachTo(mFAB)
                .build();
    }

    private void setUpTabs() {
        viewPager=(ViewPager)findViewById(R.id.pager);
        tabHost=(MaterialTabHost)findViewById(R.id.mainMaterialTabHost);
        viewPager=(ViewPager)findViewById(R.id.pager);
        adapter= new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < adapter.getCount(); i++) {
            MaterialTab materialTab = tabHost.newTab();
            materialTab.setIcon(adapter.getIcon(i));
            materialTab.setTabListener(this);
            tabHost.addTab(materialTab);
        }
    }

    private void setUpDrawer() {
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment=(NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.action_settings)
            return true;
        if(id==R.id.navigate){
            startActivity(new Intent(MainActivity.this,ActivityUsingTabLibrary.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    @Override
    public void onClick(View v) {
        Fragment fragment= (Fragment) adapter.instantiateItem(viewPager,viewPager.getCurrentItem());
        if(fragment instanceof SortListener)
        {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                //call the sort by name method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                //call the sort by date method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                //call the sort by ratings method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByRating();
            }

        }

    }

   /* class MyPagerAdapter extends FragmentPagerAdapter
    {
        String[] tabText;
        int[] icons={R.drawable.ic_action_home,R.drawable.ic_action_articles,R.drawable.ic_action_personal};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText =getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment=MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable=getResources().getDrawable(icons[position]);
            drawable.setBounds(0,0,36,36);
            ImageSpan imageSpan=new ImageSpan(drawable);
            SpannableString spannableString= new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }*/

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int count = 1;
        FragmentManager fragmentManager;
        int[] icons={R.drawable.ic_action_home,R.drawable.ic_action_articles,R.drawable.ic_action_personal};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position)
            {
                case MOVIES_SEARCH_RESULTS:
                    fragment= FragmentSearch.newInstance("","");
                    break;
                case MOVIES_HITS:
                    fragment= FragmentBoxOffice.newInstance("","");
                    break;
                case MOVIES_UPCOMING:
                    fragment= FragmentUpcoming.newInstance("", "");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position){
            return getResources().getDrawable(icons[position]);
        }
    }
}
