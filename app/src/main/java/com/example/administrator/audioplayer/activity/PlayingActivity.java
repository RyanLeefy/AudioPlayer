package com.example.administrator.audioplayer.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.fragment.RoundFragment;
import com.example.administrator.audioplayer.lrc.LrcView;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.widget.AlbumViewPager;
import com.example.administrator.audioplayer.widget.PlayingSeekBar;

import java.lang.reflect.Field;

public class PlayingActivity extends BaseActivity {

    //整个视图的背景图片
    private ImageView mBackAlbum;

    //音量控制条
    private SeekBar mVolumeSeek;

    //用来切换歌曲，切换封面的ViewPager
    private AlbumViewPager mViewPager;

    //封面整个框
    private FrameLayout mAlbumLayout;

    //歌词整个框，盖在封面框上面，但一开始为invisiable
    private RelativeLayout mLrcViewContainer;

    //歌词视图
    private LrcView mLrcView;

    //没有歌词时获取歌词的TextView
    private TextView mTryGetLrc;

    //唱针图片
    private ImageView mNeedle;

    //musicTool框
    private LinearLayout mMusicTool;

    //musicTool框里面四个按钮的图片，喜欢，下载，评论，更多
    private ImageView  mFav, mDown, mCmt, mMore;

    //当前播放时间点和歌曲时常
    private TextView mTimePlayed, mDuration;

    //播放控制条
    private PlayingSeekBar mProgress;

    //下面播放控制的五个按钮图片，播放模式，播放或暂停，下一首，上一首，播放列表
    private ImageView mPlayingmode, mPlayorPause, mNext, mPre, mPlaylist;


    private static final int VIEWPAGER_SCROLL_TIME = 390;
    private static final int TIME_DELAY = 500;
    private static final int NEXT_MUSIC = 0;
    private static final int PRE_MUSIC = 1;

    private boolean isNextOrPreSetPage = false; //判断viewpager由手动滑动 还是setcruuentitem换页
    private HandlerThread mHandlerThread;
    private MusicChangeHandler mHandler;

    private FragmentAdapter mAdapter;


    /**
     * 刷新播放进度条
     */
    public Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {

            long position = MusicPlayer.position();
            long duration = MusicPlayer.duration();
            if (duration > 0 && duration < 627080716) {
                mProgress.setProgress((int) (1000 * position / duration));
                mTimePlayed.setText(CommonUtils.makeTimeString(position));
            }

            if (MusicPlayer.isPlaying()) {
                mProgress.postDelayed(mUpdateProgress, 50);
            } else {
                mProgress.removeCallbacks(mUpdateProgress);
            }

        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PlayingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void showQuickControl(boolean show) {
       //重写该方法为空方法，不加载底部播放控制栏
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        mHandlerThread = new HandlerThread("MusicChangeHandler");
        mHandlerThread.start();
        mHandler = new MusicChangeHandler(mHandlerThread.getLooper());

        mAlbumLayout = (FrameLayout) findViewById(R.id.headerView);
        mLrcViewContainer = (RelativeLayout) findViewById(R.id.lrcviewContainer);
        mLrcView = (LrcView) findViewById(R.id.lrcview);
        mTryGetLrc = (TextView) findViewById(R.id.tragetlrc);
        mMusicTool = (LinearLayout) findViewById(R.id.music_tool);


        setToolbar();


        mVolumeSeek = (SeekBar) findViewById(R.id.volume_seek);
        mBackAlbum = (ImageView) findViewById(R.id.albumArt);
        mPlayingmode = (ImageView) findViewById(R.id.playing_mode);
        mPlayorPause = (ImageView) findViewById(R.id.playing_play);
        mNext = (ImageView) findViewById(R.id.playing_next);
        mPre = (ImageView) findViewById(R.id.playing_pre);
        mPlaylist = (ImageView) findViewById(R.id.playing_playlist);
        mMore = (ImageView) findViewById(R.id.playing_more);
        mCmt = (ImageView) findViewById(R.id.playing_cmt);
        mFav = (ImageView) findViewById(R.id.playing_fav);
        mDown = (ImageView) findViewById(R.id.playing_down);
        mTimePlayed = (TextView) findViewById(R.id.music_duration_played);
        mDuration = (TextView) findViewById(R.id.music_duration);
        mProgress = (PlayingSeekBar) findViewById(R.id.play_seek);
        mNeedle = (ImageView) findViewById(R.id.needle);
        mViewPager = (AlbumViewPager) findViewById(R.id.view_pager);


        initLrcView();
        setViewPager();
        //mDuration

    }


    @Override
    protected void onStart() {
        super.onStart();
        //设置ViewPager的默认项
        //因为左右两边各加了一个，所以全部推后一个
        mViewPager.setCurrentItem(MusicPlayer.getQueuePosition() + 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mProgress.setMax(1000);
        mProgress.removeCallbacks(mUpdateProgress);
        mProgress.postDelayed(mUpdateProgress, 10);
        //updateNowplayingCard();

    }

    @Override
    public void onStop() {
        super.onStop();
        mProgress.removeCallbacks(mUpdateProgress);
    }




    //初始化toolbar，这里toolbar为透明的，但是会占有位置，把其他内容往下移一点
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("一首歌名");
        toolbar.setSubtitle("歌手名");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 创建菜单，toolbar上的分享按钮
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.playing_menu, menu);
        return true;

    }

    /**
     * 分享按钮的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        /*
        if (item.getItemId() == R.id.menu_share) {
            MusicInfo musicInfo = MusicUtils.getMusicInfo(PlayingActivity.this, MusicPlayer.getCurrentAudioId());
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + musicInfo.data));
            shareIntent.setType("audio/*");
            this.startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.shared_to)));

        }*/
        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        PlaybarPagerTransformer transformer = new PlaybarPagerTransformer();
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, transformer);

        // 改变viewpager动画时间
        /*
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            MyScroller mScroller = new MyScroller(mViewPager.getContext().getApplicationContext(), new LinearInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int pPosition) {
                if (pPosition < 1) { //首位之前，跳转到末尾（N）
                    MusicPlayer.setQueuePosition(MusicPlayer.getQueue().length - 1);
                    mViewPager.setCurrentItem(MusicPlayer.getQueue().length, false);
                    isNextOrPreSetPage = false;
                    return;

                } else if (pPosition > MusicPlayer.getQueue().length) { //末位之后，跳转到首位（1）
                    MusicPlayer.setQueuePosition(0);
                    mViewPager.setCurrentItem(1, false); //false:不显示跳转过程的动画
                    isNextOrPreSetPage = false;
                    return;
                } else {

                    if (!isNextOrPreSetPage) {
                        if (pPosition < MusicPlayer.getQueuePosition() + 1) {
//                            HandlerUtil.getInstance(PlayingActivity.this).postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                  //  MusicPlayer.previous(PlayingActivity.this, true);
//                                    Message msg = new Message();
//                                    msg.what = 0;
//                                    mPlayHandler.sendMessage(msg);
//                                }
//                            }, 500);

                            Message msg = new Message();
                            msg.what = PRE_MUSIC;
                            mHandler.sendMessageDelayed(msg, TIME_DELAY);


                        } else if (pPosition > MusicPlayer.getQueuePosition() + 1) {
//                            HandlerUtil.getInstance(PlayingActivity.this).postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                  //  MusicPlayer.mNext();
//
//
//                                }
//                            }, 500);

                            Message msg = new Message();
                            msg.what = NEXT_MUSIC;
                            mHandler.sendMessageDelayed(msg, TIME_DELAY);

                        }
                    }

                }
                //MusicPlayer.setQueuePosition(pPosition - 1);
                isNextOrPreSetPage = false;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int pState) {
            }
        });
    }


    private void initLrcView() {
        mViewPager.setOnSingleTouchListener(new AlbumViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View v) {
                if (mAlbumLayout.getVisibility() == View.VISIBLE) {
                    mAlbumLayout.setVisibility(View.INVISIBLE);
                    mLrcViewContainer.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.INVISIBLE);
                }
            }
        });
        mLrcViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLrcViewContainer.getVisibility() == View.VISIBLE) {
                    mLrcViewContainer.setVisibility(View.INVISIBLE);
                    mAlbumLayout.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     *
     */
    public class PlaybarPagerTransformer implements ViewPager.PageTransformer {


        @Override
        public void transformPage(View view, float position) {

            /*
            if (position == 0) {
                if (MusicPlayer.isPlaying()) {
                    mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                    if (mRotateAnim != null && !mRotateAnim.isRunning() && mNeedleAnim != null) {
                        mAnimatorSet = new AnimatorSet();
                        mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
                        mAnimatorSet.start();
                    }
                }

            } else if (position == -1 || position == -2 || position == 1) {

                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                if (mRotateAnim != null) {
                    mRotateAnim.setFloatValues(0);
                    mRotateAnim.end();
                    mRotateAnim = null;
                }
            } else {

                if (mNeedleAnim != null) {
                    mNeedleAnim.reverse();
                    mNeedleAnim.end();
                }

                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                if (mRotateAnim != null) {
                    mRotateAnim.cancel();
                    float valueAvatar = (float) mRotateAnim.getAnimatedValue();
                    mRotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);

                }
            }*/
        }

    }


    /**
     *
     */
    private static class MusicChangeHandler extends Handler {

        public MusicChangeHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRE_MUSIC:
                    //MusicPlayer.previous(PlayingActivity.this, true);
                    break;
                case NEXT_MUSIC:
                    MusicPlayer.next();
                    break;
                //case 3:
                    //MusicPlayer.setQueuePosition(msg.arg1);
                    //break;
            }
        }
    }


    class FragmentAdapter extends FragmentStatePagerAdapter {

        private int mChildCount = 0;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //把最左边的多添加的item初始化为最后一个唱片的图片
            if ( position == 0) {
                return RoundFragment.newInstance(MusicPlayer.getAlbumPathAll()[MusicPlayer.getQueueSize() - 1]);
            }
            //把最右边的多添加爱的item初始化为第一个唱片的图片
            if (position == MusicPlayer.getQueue().length + 1 ) {
                return RoundFragment.newInstance(MusicPlayer.getAlbumPathAll()[0]);
            }

             //return RoundFragment.newInstance(MusicPlayer.getQueue()[position - 1]);
            return RoundFragment.newInstance(MusicPlayer.getAlbumPathAll()[position - 1]);

        }

        @Override
        public int getCount() {
            //左右各加一个
            return MusicPlayer.getQueue().length + 2;
        }


        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

    }





}
