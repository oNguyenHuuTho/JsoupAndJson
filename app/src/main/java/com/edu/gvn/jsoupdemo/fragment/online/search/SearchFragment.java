package com.edu.gvn.jsoupdemo.fragment.online.search;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.edu.gvn.jsoupdemo.R;
import com.edu.gvn.jsoupdemo.activity.BaseActivity;
import com.edu.gvn.jsoupdemo.activity.HomeActivity;
import com.edu.gvn.jsoupdemo.adapter.SearchAdater;
import com.edu.gvn.jsoupdemo.fragment.BaseFragment;
import com.edu.gvn.jsoupdemo.fragment.PlayerFragment;
import com.edu.gvn.jsoupdemo.model.online.DetailAlbumModel;
import com.edu.gvn.jsoupdemo.model.online.SearchModel;
import com.edu.gvn.jsoupdemo.network.JsonParser.SearchParserAsync;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private Activity mActivity;
    private EditText mEditSearch;
    private ListView mListSearch;
    private ArrayList<SearchModel.Song> mDataSong;
    private SearchAdater mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mDataSong = new ArrayList<>();
        mAdapter = new SearchAdater(mActivity, android.R.layout.simple_list_item_1, mDataSong);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mListSearch = (ListView) v.findViewById(R.id.list_search);
        mEditSearch = (EditText) v.findViewById(R.id.edt_search);
        mListSearch.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRequestFocusEdittext();
        mEditSearch.addTextChangedListener(new TextWatcher() {

            private Timer timer = new Timer();
            private long DELAY = 750;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        new SearchParserAsync(new SearchParserAsync.SearchDataCallBack() {
                            @Override
                            public void callData(SearchModel model) {
                                mDataSong.clear();
                                mDataSong.addAll(model.data.get(SearchModel.INDEX_SONG).song);
                                mAdapter.notifyDataSetChanged();

                            }
                        }).execute(s.toString());


                    }
                }, DELAY);
            }
        });
        mListSearch.setAdapter(mAdapter);
    }

    public void setRequestFocusEdittext() {
        mEditSearch.requestFocus();
        if (mEditSearch.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseActivity.mPlayService.setListAlbum(convertData());
        BaseActivity.mPlayService.playIndex(position);
        ((HomeActivity) getActivity()).replaceFragment(new PlayerFragment());
    }

    private ArrayList<DetailAlbumModel> convertData() {
        ArrayList<DetailAlbumModel> datas = new ArrayList<>();
        for (int i = 0; i < mDataSong.size(); i++) {
            String order = String.valueOf(i + 1);
            String id = mDataSong.get(i).mId;
            String name = mDataSong.get(i).mName;
            datas.add(new DetailAlbumModel(order, id, name));
        }
        return datas;
    }
}
