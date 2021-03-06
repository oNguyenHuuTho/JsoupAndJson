package com.edu.gvn.jsoupdemo.fragment.online.rank;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.gvn.jsoupdemo.R;
import com.edu.gvn.jsoupdemo.adapter.RankPagerAdapter;
import com.edu.gvn.jsoupdemo.fragment.BaseFragment;

public class RankFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    private ViewPager mRankPager;
    private TabLayout mTabRank;
    private RankPagerAdapter mRankAdapter;
    private Animation zoomInAnim, zoomOutAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRankAdapter = new RankPagerAdapter(getActivity(),getActivity().getSupportFragmentManager());
        zoomInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.tab_zoom_in);
        zoomOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.tab_zoom_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rank, container, false);
        mRankPager = (ViewPager) v.findViewById(R.id.fragment_rank_pager);
        mTabRank = (TabLayout) v.findViewById(R.id.fragment_panel_tab_rank);

        mTabRank.addOnTabSelectedListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabRank.setupWithViewPager(mRankPager);
        mRankPager.setAdapter(mRankAdapter);
        setIconTab();
    }

    private void setIconTab() {
        mTabRank.getTabAt(0).setIcon(R.drawable.bottom_tab_vietnam);
        mTabRank.getTabAt(1).setIcon(R.drawable.bottom_tab_us);
        mTabRank.getTabAt(2).setIcon(R.drawable.bottom_tab_korea);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        ImageView img = (ImageView) (((LinearLayout)((LinearLayout)mTabRank.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(0));
        TextView txt = (TextView) (((LinearLayout)((LinearLayout)mTabRank.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
        img.startAnimation(zoomInAnim);
        txt.startAnimation(zoomOutAnim);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        ImageView img = (ImageView) (((LinearLayout)((LinearLayout)mTabRank.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(0));
        TextView txt = (TextView) (((LinearLayout)((LinearLayout)mTabRank.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
        img.startAnimation(zoomOutAnim);
        txt.startAnimation(zoomInAnim);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
