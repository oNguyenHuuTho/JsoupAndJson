package com.edu.gvn.jsoupdemo.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.edu.gvn.jsoupdemo.R;
import com.edu.gvn.jsoupdemo.activity.BaseActivity;
import com.edu.gvn.jsoupdemo.common.Player;
import com.squareup.picasso.Picasso;

import info.abdolahi.CircularMusicProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends BaseFragment implements View.OnClickListener, Player.MediaPlayerOnComplete {

    private static final int TIME_DELAY_UPDATE = 1000;
    private static final int REPEAT_OFF = 0;
    private static final int REPEAT_ON = 1;
    private static final int REPEAT_ONE = 2;

    private Toolbar toolbar;
    private CircularMusicProgressBar mMusicProgress;
    private ImageView mPlayState, mNext, mForward;
    private TextView mNameSong, mArtistSong;
    private ImageView mShuffle, mRepeat;
    private SeekBar mVolume;

    private Handler mUpdateProgressBarHandler;

    public static PlayerFragment newInstance() {
        Bundle args = new Bundle();
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        mMusicProgress = (CircularMusicProgressBar) v.findViewById(R.id.fragment_player_progress_bar);
        mVolume = (SeekBar) v.findViewById(R.id.fragment_player_seekbar_volume);

        mPlayState = (ImageView) v.findViewById(R.id.fragment_player_imageview_play_state);
        mNext = (ImageView) v.findViewById(R.id.fragment_play_imageview_nexts);
        mForward = (ImageView) v.findViewById(R.id.fragment_play_imageview_forward);
        mShuffle = (ImageView) v.findViewById(R.id.fragment_player_imageview_shuffle);
        mRepeat = (ImageView) v.findViewById(R.id.fragment_player_imageview_repeat);

        mNameSong = (TextView) v.findViewById(R.id.fragment_player_song_title);
        mArtistSong = (TextView) v.findViewById(R.id.fragment_player_song_artist);

        mUpdateProgressBarHandler = new Handler();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mPlayState.setActivated(BaseActivity.mPlayService.isPlaying());
        mShuffle.setActivated(BaseActivity.mPlayService.getShuffle());
        setStateRepeat(BaseActivity.mPlayService.getRepeat());

        mNameSong.setText(BaseActivity.mPlayService.getNameSong());
        mArtistSong.setText(BaseActivity.mPlayService.getArtistSong());

        mMusicProgress.setValue(percentMusicProgress(BaseActivity.mPlayService.getCurrentPosition(),
                BaseActivity.mPlayService.getMaxDuration()));
        mVolume.setProgress(BaseActivity.mPlayService.getVolume());

        mVolume.setOnSeekBarChangeListener(volumeChangeListener);
        mPlayState.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mForward.setOnClickListener(this);
        mShuffle.setOnClickListener(this);
        mRepeat.setOnClickListener(this);

        BaseActivity.mPlayService.setOnComplete(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        ImageTask imageTask = new ImageTask();
        imageTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {

        Log.i("huutho", "onClick: ");

        switch (v.getId()) {
            case R.id.fragment_player_imageview_play_state:
                boolean isPlaying = BaseActivity.mPlayService.isPlaying();
                if (isPlaying) {
                    BaseActivity.mPlayService.pause();
                } else {
                    BaseActivity.mPlayService.start();
                }
                mPlayState.setActivated(BaseActivity.mPlayService.isPlaying());
                break;

            case R.id.fragment_play_imageview_nexts:
                BaseActivity.mPlayService.next();
                break;

            case R.id.fragment_play_imageview_forward:
                BaseActivity.mPlayService.forward();
                break;

            case R.id.fragment_player_imageview_shuffle:
                BaseActivity.mPlayService.setShuffle();
                mShuffle.setActivated(BaseActivity.mPlayService.getShuffle());
                break;

            case R.id.fragment_player_imageview_repeat:
                BaseActivity.mPlayService.setRepeat();
                setStateRepeat(BaseActivity.mPlayService.getRepeat());
                break;
        }
    }


    @Override
    public void onComplete() {

    }

    @Override
    public void notifiDataSetChange(String name, String artist, String image) {
        updateUI(name,artist,image);
    }


    // Call when Next, Forward, onComplete


    private void setStateRepeat(int repeat) {
        switch (repeat) {
            case REPEAT_OFF:
                mRepeat.setSelected(false);
                mRepeat.setActivated(false);
                break;
            case REPEAT_ON:
                mRepeat.setSelected(true);
                mRepeat.setActivated(false);
                break;
            case REPEAT_ONE:
                mRepeat.setSelected(true);
                mRepeat.setActivated(true);
                break;
        }
    }

    private void updateProgressMusicBar(final CircularMusicProgressBar mMusicProgress) {
        mUpdateProgressBarHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMusicProgress.setValue(percentMusicProgress(BaseActivity.mPlayService.getCurrentPosition(), BaseActivity.mPlayService.getMaxDuration()));
                mUpdateProgressBarHandler.postDelayed(this, TIME_DELAY_UPDATE);
            }
        }, TIME_DELAY_UPDATE);
    }

    private long percentMusicProgress(int currentPosition, int maxDuration) {
        if (maxDuration == 0) maxDuration = 1;
        return (int) (((float) currentPosition / maxDuration) * 100);
    }

    private void updateUI(String name, String artist, String image) {
        Picasso.with(getActivity())
                .load(image)
                .placeholder(R.drawable.background_nav)
                .error(R.drawable.background_nav)
                .into(mMusicProgress);
        updateProgressMusicBar(mMusicProgress);

        mNameSong.setText(name);
        mArtistSong.setText(artist);
        mPlayState.setActivated(BaseActivity.mPlayService.isPlaying());
    }

    private SeekBar.OnSeekBarChangeListener volumeChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                BaseActivity.mPlayService.setVolume(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public class ImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Picasso.with(getActivity())
                    .load(BaseActivity.mPlayService.getCover())
                    .placeholder(R.drawable.background_nav)
                    .error(R.drawable.background_nav)
                    .into(mMusicProgress);
            updateProgressMusicBar(mMusicProgress);

            mNameSong.setText(BaseActivity.mPlayService.getNameSong());
            mArtistSong.setText(BaseActivity.mPlayService.getArtistSong());
            mPlayState.setActivated(BaseActivity.mPlayService.isPlaying());
        }
    }


}
