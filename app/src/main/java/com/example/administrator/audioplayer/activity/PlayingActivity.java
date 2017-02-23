package com.example.administrator.audioplayer.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.audioplayer.MyApplication;
import com.example.administrator.audioplayer.R;
import com.example.administrator.audioplayer.adapter.RoundFragmentPagerAdapter;
import com.example.administrator.audioplayer.fragment.PlayQueueFragment;
import com.example.administrator.audioplayer.fragment.RoundFragment;
import com.example.administrator.audioplayer.lrc.DefaultLrcParser;
import com.example.administrator.audioplayer.lrc.LrcRow;
import com.example.administrator.audioplayer.lrc.LrcView;
import com.example.administrator.audioplayer.service.MediaService;
import com.example.administrator.audioplayer.service.MusicPlayer;
import com.example.administrator.audioplayer.utils.CommonUtils;
import com.example.administrator.audioplayer.utils.GlobalHandler;
import com.example.administrator.audioplayer.widget.AlbumViewPager;
import com.example.administrator.audioplayer.widget.PlayingSeekBar;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

public class PlayingActivity extends BaseActivity {

    private ActionBar ab;

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

    //需要旋转的视图，这里指RoundFragment的根视图
    private View mRotateView;

    private WeakReference<View> mViewWeakReference = new WeakReference<View>(null);

    //唱针动画，唱片旋转动画
    private ObjectAnimator mNeedleAnim, mRotateAnim;

    //动画集合，用来指定上面两个动画的先后顺序
    private AnimatorSet mAnimatorSet;

    //判断viewpager由手动滑动 还是setcruuentitem换页
    private boolean isNextOrPreSetPage = false;

    //判断是不是点了上一首和下一首，还是直接从LocalMusicFragment进来
    //直接进来的话唱针直接就在下面，点了上一首和下一首的话唱针有动画
    private boolean isFromOutSide = true;


    private RoundFragmentPagerAdapter mAdapter;


    //TODO 在正在播放页面，如果当前是播放最后一首歌，在播放列表中删除最后一首歌，会出现错乱！！


    /**
     * 刷新播放进度条
     * mProgress.postDelayed(mUpdateProgress, 50)  开启
     * mProgress.removeCallbacks(mUpdateProgress)  暂停
     */
    public Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {

            long position = MusicPlayer.position();
            long duration = MusicPlayer.duration();
            if (duration > 0 && duration < 627080716) {
                mProgress.setProgress((int) ((1000 * position / duration) - 0.5F));
                mTimePlayed.setText(CommonUtils.makeTimeString(position));
            }

            if (MusicPlayer.isPlaying()) {
                //循环SeekBar运动
                mProgress.postDelayed(mUpdateProgress, 50);
            } else {
                //暂停SeekBar
                mProgress.removeCallbacks(mUpdateProgress);
                //要重启或重置SeekBar调用mProgress.postDelayed(mUpdateProgress, 50);
            }

        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PlayingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 重写该方法为空方法，不加载底部播放控制栏
     * @param show 显示或关闭底部播放控制栏
     */
    @Override
    protected void showQuickControl(boolean show) {}


    /**
     * 重写该方法，当歌曲信息变更的时候进行刷新
     */
    @Override
    public void onMetaChange() {
        Logger.d("MusicPlayer.getQueueSize():" + MusicPlayer.getQueueSize());
        if (MusicPlayer.getQueueSize() == 0) {
            this.finish();
            return;
        }

        //歌曲更新时候刷新歌曲名和歌手名
        ab.setTitle(MusicPlayer.getTrackName());
        ab.setSubtitle(MusicPlayer.getArtistName());

        //歌曲更新时候刷新歌曲时间
        mDuration.setText(CommonUtils.makeTimeString(MusicPlayer.duration()));

        //ControlTool控制的上一首，下一首是先发送PRE或NEXT命令给BaseActivity中接受到在执行刷行界面的
        //用来刷新通过ControlTool控制的上一首，下一首的页面变化
        isNextOrPreSetPage = false;
        if (MusicPlayer.getQueuePosition() + 1 != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(MusicPlayer.getQueuePosition() + 1);
            isNextOrPreSetPage = true;
        }

        //mAdapter = new RoundFragmentPagerAdapter(getSupportFragmentManager());
        //mViewPager.setAdapter(mAdapter);
        //onPlayStateChange();
    }


    /**
     * 重写该方法，当歌曲播放状态变更的时候调用
     * 唱针动画和唱片动画
     */
    @Override
    public void onPlayStateChange() {
        Fragment fragment = (RoundFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());


        if (fragment != null) {
            View v = fragment.getView();
            if (mViewWeakReference.get() != v && v != null) {
                ((ViewGroup) v).setAnimationCacheEnabled(false);
                if (mViewWeakReference != null)
                    mViewWeakReference.clear();
                mViewWeakReference = new WeakReference<View>(v);
                mRotateView = mViewWeakReference.get();
            }
        }

        //获取fragment的动画
        if(mRotateView != null)
            mRotateAnim = (ObjectAnimator) mRotateView.getTag(R.id.tag_animator);

        mAnimatorSet = new AnimatorSet();
        if (MusicPlayer.isPlaying()) {
            //启动PlaySeekBar
            mProgress.postDelayed(mUpdateProgress, 200);
            //播放按钮设置为暂停图片
            mPlayorPause.setImageResource(R.drawable.play_rdi_btn_pause);

            if (mAnimatorSet != null && mRotateAnim != null && !mRotateAnim.isRunning()) {
                //正在播放，针下去，开始旋转

                //修复从playactivity回到Main界面null
                if (mNeedleAnim == null) {
                    mNeedleAnim = ObjectAnimator.ofFloat(mNeedle, "rotation", -30, 0);
                    mNeedleAnim.setDuration(200);
                    mNeedleAnim.setInterpolator(new LinearInterpolator());
                }

                mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
                mAnimatorSet.start();
            }

        } else {
            //播放按钮设置为播放图片
            mPlayorPause.setImageResource(R.drawable.play_rdi_btn_play);
            //暂停，针回去
            if (mNeedleAnim != null) {
                mNeedleAnim.reverse();
                mNeedleAnim.end();
            }

            //停止旋转，停在旋转位置
            if (mRotateAnim != null && mRotateAnim.isRunning()) {
                mRotateAnim.cancel();
                float valueAvatar = (float) mRotateAnim.getAnimatedValue();
                mRotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);
            }
        }
    }

    /**
     * 重写该方法进场页面的更新，在baseActivity中调用
     */
    @Override
    public void onQueueChange() {
        if (MusicPlayer.getQueueSize() == 0) {
            MusicPlayer.stop();
            finish();
            return;
        }
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(MusicPlayer.getQueuePosition() + 1, false);
    }


    /**
     * 重写该方法进行歌词的刷新，在baseActivity中调用
     */
    @Override
    public void updateLrc() {
        List<LrcRow> list = getLrcRows();
        if (list != null && list.size() > 0) {
            //若成功获取歌词，隐藏获取歌词按钮，显示歌词
            mTryGetLrc.setVisibility(View.INVISIBLE);
            mLrcView.setLrcRows(list);
        } else {
            //或无法获取歌词，显示获取歌词按钮
            mTryGetLrc.setVisibility(View.VISIBLE);
            //重置歌词视图
            mLrcView.reset();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


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


        mNeedleAnim = ObjectAnimator.ofFloat(mNeedle, "rotation", -25, 0);
        mNeedleAnim.setDuration(200);
        mNeedleAnim.setInterpolator(new LinearInterpolator());


        initVolumeBar();
        initLrcView();
        setViewPager();
        setMusicToolListener();
        setControlToolListener();
        setPlayingSeekBarListener();


        //开启SeekBar
        mProgress.setMax(1000);
        mProgress.postDelayed(mUpdateProgress, 10);
        //设置歌曲时长
        mDuration.setText(CommonUtils.makeTimeString(MusicPlayer.duration()));


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
        //updateNowplayingCard();

    }

    @Override
    public void onStop() {
        super.onStop();
        mProgress.removeCallbacks(mUpdateProgress);
        stopAnim();
    }

    private void stopAnim() {
        mRotateView = null;

        if (mRotateAnim != null) {
            mRotateAnim.end();
            mRotateAnim = null;
        }
        if (mNeedleAnim != null) {
            mNeedleAnim.end();
            mNeedleAnim = null;
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
            mAnimatorSet = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //设置activity退出动画
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    /**
     * 初始化toolbar，这里toolbar为透明的，但是会占有位置，把其他内容往下移一点
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("一首歌名");
        toolbar.setSubtitle("歌手名");
        setSupportActionBar(toolbar);

        ab = getSupportActionBar();
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
            //设置activity退出动画
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化音量控制条
     */
    private void initVolumeBar() {
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mMaxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //设置最大音量
        mVolumeSeek.setMax(mMaxVol);
        //设置当前音量
        mVolumeSeek.setProgress(v);
        //设置控制条监听事件
        mVolumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.ADJUST_SAME);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * 初始化唱片ViewPager
     */
    private void setViewPager() {
        //两边提前读取1个
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new RoundFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        //设置页面切换时候的回调，用于显示唱针的动画
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View view, float position) {

                        //Logger.d("transformPage");
                        //position为位置[-Infinity,-1)  左边看不到的
                        //(1,+Infinity] 右边看不到了
                        //( 0, -1]左边滑出
                        //[ 1 , 0 ]右边划入

                        //当看不见的时候，把恢复原样，把旋转动画取消并设置为null
                        if (position <= -1 || position >= 1) {
                            mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
                            if (mRotateAnim != null) {
                                mRotateAnim.setFloatValues(0);
                                mRotateAnim.end();
                                mRotateAnim = null;
                            }
                        } else if (position == 0) {
                            if (MusicPlayer.isPlaying()) {
                                mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);

                                if (mRotateAnim != null && !mRotateAnim.isRunning() && mNeedleAnim != null) {
                                    mAnimatorSet = new AnimatorSet();
                                    if(isFromOutSide) {
                                        //外面进来的唱针直接在下面，直接旋转
                                        mNeedleAnim.end();
                                        mRotateAnim.start();
                                    } else {
                                        //不是外面进来则先把唱针放下去然后开始旋转
                                        mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
                                        mAnimatorSet.start();
                                    }
                                }
                            }
                        } else {
                            mRotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
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
                        }

                    }
                });

        //设置页面切换的监听事件，即切换item项，切换播放列表的位置，重新播放
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int pPosition) {

                Logger.d("OnPageSelected:" + pPosition);
                Logger.d("MusicPlayer.getQueue().length:" + MusicPlayer.getQueue().size());
                Logger.d("MusicPlayer.getQueuePosition():" + MusicPlayer.getQueuePosition());


                if (pPosition < 1) { //首位之前，跳转到末尾（N）
                    MusicPlayer.setQueuePosition(MusicPlayer.getQueue().size() - 1);
                    mViewPager.setCurrentItem(MusicPlayer.getQueue().size());
                    isNextOrPreSetPage = false;
                    Logger.d("Message:tiaodaowei");
                    return;

                } else if (pPosition > MusicPlayer.getQueue().size()) { //末位之后，跳转到首位（1）
                    MusicPlayer.setQueuePosition(0);
                    //mViewPager.setCurrentItem(1, false); //false:不显示跳转过程的动画
                    mViewPager.setCurrentItem(1);
                    isNextOrPreSetPage = false;
                    Logger.d("Message:tiaodaotou");
                    return;
                } else {
                    if (!isNextOrPreSetPage) {
                        if (pPosition < MusicPlayer.getQueuePosition() + 1) {
                            //前一首，发送消息到Handler中处理
                            MusicPlayer.previous();
                            Logger.d("Message:PRE");

                        } else if (pPosition > MusicPlayer.getQueuePosition() + 1) {
                            //下一首，发送消息到Handler中处理
                            MusicPlayer.next();
                            Logger.d("Message:Next");
                        }
                    }

                }
                isNextOrPreSetPage = false;



            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int pState) {
            }
        });

        //单击隐藏，显示歌词框和musicTool框
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
    }


    private void initLrcView() {

        //设置歌词拉动监听
        mLrcView.setOnSeekToListener(new LrcView.OnSeekToListener() {
            @Override
            public void onSeekTo(int progress) {
                MusicPlayer.seek(progress);
            }
        });

        /*
        mLrcView.setOnLrcClickListener(new LrcView.OnLrcClickListener() {
            @Override
            public void onClick() {
                if (mLrcViewContainer.getVisibility() == View.VISIBLE) {
                    mLrcViewContainer.setVisibility(View.INVISIBLE);
                    mAlbumLayout.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.VISIBLE);
                }
            }
        });*/

        //单击隐藏歌词框和MusicTool框，显示ViewPager唱片
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

        //设置获取歌词点击事件
        mTryGetLrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaService.TRY_GET_TRACKINFO);
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "正在获取信息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置MusicTool，喜欢，下载，评论，更多
     */
    private void setMusicToolListener() {

        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

    }

    /**
     * 设置ControlTool，播放模式，播放或暂停，下一首，上一首，播放列表
     */
    private void setControlToolListener() {
        //播放模式
        mPlayingmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变模式
                MusicPlayer.changeMode();

                //更新图片
                if (MusicPlayer.getShuffleMode() == MediaService.SHUFFLE_NORMAL) {
                    mPlayingmode.setImageResource(R.drawable.play_icn_shuffle);
                    Toast.makeText(MyApplication.getContext(), "随机播放", Toast.LENGTH_SHORT).show();
                } else {
                    switch (MusicPlayer.getRepeatMode()) {
                        case MediaService.REPEAT_ALL:
                            mPlayingmode.setImageResource(R.drawable.play_icn_loop);
                            Toast.makeText(MyApplication.getContext(), "列表循环", Toast.LENGTH_SHORT).show();
                            break;
                        case MediaService.REPEAT_CURRENT:
                            mPlayingmode.setImageResource(R.drawable.play_icn_one);
                            Toast.makeText(MyApplication.getContext(), "单曲播放", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });

        //上一首
        mPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer.previous();
            }
        });

        //播放或暂停
        mPlayorPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MusicPlayer.isPlaying()) {
                    mPlayorPause.setImageResource(R.drawable.play_rdi_btn_pause);
                } else {
                    mPlayorPause.setImageResource(R.drawable.play_rdi_btn_play);
                }
                isFromOutSide = false;
                if (MusicPlayer.getQueueSize() != 0) {
                    GlobalHandler.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MusicPlayer.playOrPause();
                        }
                    }, 200);

                }

            }
        });

        //下一首
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer.next();
            }
        });

        //播放列表
        mPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayQueueFragment playQueueFragment = new PlayQueueFragment();
                playQueueFragment.show(getSupportFragmentManager(), "playlistframent");
            }
        });
    }


    private void setPlayingSeekBarListener() {
        //设置音乐播放条的监听
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //计算当前时间点，因为总长是1000
                i = (int) (i * MusicPlayer.duration() / 1000);
                mLrcView.seekTo(i, true, b);
                if (b) {
                    MusicPlayer.seek((long) i);
                    mTimePlayed.setText(CommonUtils.makeTimeString(i));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }


    /**
     * 获取歌词
     * @return
     */
    private List<LrcRow> getLrcRows() {
        Logger.e("start to get LrcRows");


        List<LrcRow> rows = null;
        InputStream is = null;
        try {
            is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/audioplayer/lrc/" + MusicPlayer.getCurrentAudioId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is == null) {
                return null;
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Logger.e("return rows:" + rows.size());
        return rows;

    }




}
