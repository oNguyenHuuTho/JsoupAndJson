package com.edu.gvn.jsoupdemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edu.gvn.jsoupdemo.R;
import com.edu.gvn.jsoupdemo.fragment.PlayerFragment;
import com.edu.gvn.jsoupdemo.fragment.QueueFragment;
import com.edu.gvn.jsoupdemo.fragment.online.NavigationDrawerOnlineFragment;
import com.edu.gvn.jsoupdemo.fragment.online.album.AlbumCategoryFragment;
import com.edu.gvn.jsoupdemo.fragment.online.artist.ArtistFragment;
import com.edu.gvn.jsoupdemo.fragment.online.hot.HotMusicFragment;
import com.edu.gvn.jsoupdemo.fragment.online.rank.RankFragment;
import com.edu.gvn.jsoupdemo.fragment.online.search.SearchFragment;
import com.edu.gvn.jsoupdemo.fragment.online.top100.Top100Fragment;


public class HomeActivity extends BaseActivity implements NavigationDrawerOnlineFragment.GetNavItemClickListener {
    private boolean isFirstLoad = true;
    private NavigationDrawerOnlineFragment mNavDrawerOnlFragment;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setNavOnlFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addFragment(new QueueFragment());
    }

    private void setNavOnlFragment() {
        mNavDrawerOnlFragment = new NavigationDrawerOnlineFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.rl_nav, mNavDrawerOnlFragment);
        mNavDrawerOnlFragment.setUpNavDrawer((DrawerLayout) findViewById(R.id.drawer_layout), mToolbar, this);
        ft.commit();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void addFragment(Fragment toFragment) {
        String backStackName = toFragment.getClass().getName();
        String tag = backStackName;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped && mFragmentManager.findFragmentByTag(tag) == null) {
            transaction.add(R.id.rl_parent, toFragment, tag);
            transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    public void replaceFragment(Fragment fragment) {
        String backStackName = fragment.getClass().getName();
        String tag = backStackName;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        boolean isExistFragment = manager.popBackStackImmediate(backStackName, 0);
        if (!isExistFragment && manager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.rl_parent, fragment, tag);
            transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    public void replaceFragmentWithToolbar(Fragment fragment) {
        String backStackName = fragment.getClass().getName();
        String tag = backStackName;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        boolean isExistFragment = manager.popBackStackImmediate(backStackName, 0);
        if (!isExistFragment) {
            transaction.replace(R.id.main_with_toolbar, fragment);
            transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    public void hideToolbar() {
        if (mToolbar.getVisibility() == View.VISIBLE)
            mToolbar.setVisibility(View.GONE);
    }

    public void showToolbar() {
        if (mToolbar.getVisibility() == View.GONE)
            mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_fav:
                Toast.makeText(this, "fav", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_menu_setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment mFragment = fm.findFragmentById(R.id.main_with_toolbar);

        if (mFragment instanceof PlayerFragment) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        int countFragmentInBackStack = fm.getBackStackEntryCount();

        switch (countFragmentInBackStack) {
            case 0:
                break;
            case 1:
                HomeActivity.this.replaceFragment(new QueueFragment());
                break;
            default:
                fm.popBackStackImmediate();
        }
    }

    @Override
    public void onItemClick(View v) {

        mNavDrawerOnlFragment.closeNavDrawer();

        switch (v.getId()) {
            case R.id.option_voice_serch:
                break;
            case R.id.option_search:
                //   if (!isFirstLoad)
                replaceFragment(new SearchFragment());
//                else {
//                    addFragment(new SearchFragment());
//                    isFirstLoad = false;
//                }
                break;
            case R.id.option_hot_music:
                //    if (!isFirstLoad)
                replaceFragment(new HotMusicFragment());
//                else {
//                    addFragment(new HotMusicFragment());
//                    isFirstLoad = false;
//                }
                break;
            case R.id.option_rank:
//                if (!isFirstLoad)
                replaceFragment(new RankFragment());
//                else {
//                    addFragment(new RankFragment());
//                    isFirstLoad = false;
//                }
                break;
            case R.id.option_artists:
//                if (!isFirstLoad)
                replaceFragment(new ArtistFragment());
//                else {
//                    addFragment(new ArtistFragment());
//                    isFirstLoad = false;
//                }
                break;
            case R.id.option_albums:
//                if (!isFirstLoad)
                replaceFragment(new AlbumCategoryFragment());
//                else {
//                    addFragment(new AlbumCategoryFragment());
//                    isFirstLoad = false;
//                }
                break;
            case R.id.option_top_ten:
//                if (!isFirstLoad)
                replaceFragment(new Top100Fragment());
//                else {
//                    addFragment(new Top100Fragment());
//                    isFirstLoad = false;
//                }
                break;
            case R.id.option_lyric_screen:
                break;
            case R.id.option_suggested_apps:
                break;
            case R.id.option_settings:
                break;
            case R.id.option_exit:
                break;
        }
    }

    // biến variable
    // contructor
    // lifecicler
    // func
    // even
    // innerclass
}
