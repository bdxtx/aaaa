package com.weiyu.sp.lsjy.course;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.PrepareView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.util.L;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.weiyu.sp.lsjy.R;
import com.weiyu.sp.lsjy.base.BaseApplication;
import com.weiyu.sp.lsjy.base.BaseMvpActivity;
import com.weiyu.sp.lsjy.base.EventBusTag;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseDetailBean;
import com.weiyu.sp.lsjy.bean.MessageEvent;
import com.weiyu.sp.lsjy.course.adapter.CourseAdapter;
import com.weiyu.sp.lsjy.course.adapter.CourseItemDecoration;
import com.weiyu.sp.lsjy.login.LoginActivity;
import com.weiyu.sp.lsjy.other.ShareActivity;
import com.weiyu.sp.lsjy.utils.ToastUtil;
import com.weiyu.sp.lsjy.view.dialog.StopVideoDialog;
import com.weiyu.sp.lsjy.vip.CourseVipActivity;
import com.weiyu.sp.lsjy.vip.VipCenterActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import com.dueeeke.videoplayer.player.VideoView;

public class CourseDetailActivity extends BaseMvpActivity<CourseDetailPresenter> implements CourseDetailContract.View ,BaseQuickAdapter.RequestLoadMoreListener,OnRefreshListener{

    @BindView(R.id.rv)
    RecyclerView rv;

    TextView subject;
    FrameLayout desc_title;
    TextView desc_content;
    @BindView(R.id.tv_tobe_vip)
    TextView tv_tobe_vip;
    @BindView(R.id.tv_pay)
    TextView tv_pay;
    @BindView(R.id.tv_read)
    TextView tv_read;
    @BindView(R.id.my_cover)
    FrameLayout my_cover;
    @BindView(R.id.fl_show)
    FrameLayout fl_show;
    ImageView img_like;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.smart)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.smart2)
    SmartRefreshLayout smartRefreshLayout2;
    @BindView(R.id.ll_no_network)
    LinearLayout ll_no_network;
    @BindView(R.id.player)
    VideoView mVideoView;
    TextView after_subject;



    SensorManager mSensorManager;
    private CourseAdapter courseAdapter;
    private CourseBean mCourseBean;

    long skTime=0L;
    String money="";
    boolean isVip=false;

    TextView subjectName;
    TextView schoolYear;
    TextView booksNumber;
    TextView curriculum;
    private String courseId;
    private String url="";
    private CourseDetailBean mCourseDetailBean;
    private TextView price_detail;
    private ImageView thumb;
    private TitleView titleView;
    private TextView after_school;
    private TextView after_book;

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_detail;
    }

    @Override
    public void initView() {
        Intent intent=getIntent();
        courseId = intent.getStringExtra("courseId");
        smartRefreshLayout.autoRefresh();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<CourseBean>list=new ArrayList<>();
        courseAdapter = new CourseAdapter(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new CourseItemDecoration());
        rv.setAdapter(courseAdapter);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout2.setOnRefreshListener(this);
        courseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mCourseBean = (CourseBean) adapter.getData().get(position);
                Map<String,String>map=new HashMap<>();
                map.put("id",mCourseBean.getId());
                map.put("collection",mCourseBean.isCollection()+"");
                mPresenter.courseCollection(map,position);
            }
        });
        courseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<CourseBean> list1=adapter.getData();
                CourseBean courseBean= list1.get(position);
                courseBean.setHasBeanClicked(true);
                for (CourseBean courseBean1:list1){
                    if (courseBean1!=courseBean){
                        courseBean1.setHasBeanClicked(false);
                    }
                }
                courseAdapter.notifyDataSetChanged();

                courseId=courseBean.getId();

                mCourseBean=courseBean;
                upDateUi(courseBean);
                my_cover.setVisibility(View.GONE);
                mVideoView.release();
                mVideoView.start();
                isVip=courseBean.isVip();
                if (!isVip){
                    startProgressTimer();
                }
                if (isVip){
                    tv_tobe_vip.setText("会员升级");
                    tv_tobe_vip.setVisibility(View.GONE);
                    tv_pay.setText("立即观看");
                    fl_show.setVisibility(View.GONE);
                }else {
                    fl_show.setVisibility(View.VISIBLE);
                    tv_tobe_vip.setText("开通VIP");
                    tv_tobe_vip.setVisibility(View.VISIBLE);
                    tv_pay.setText("单课付费");
                }
                tv_read.setText("已观看："+courseBean.getReadNumber()+"次");
            }
        });
        addHead();
        initVd();
    }
    private void initVd(){
        StandardVideoController controller = new StandardVideoController(this);
        //根据屏幕方向自动进入/退出全屏
//        controller.setEnableOrientation(true);

        PrepareView prepareView = new PrepareView(this);//准备播放界面
        //封面图
        thumb = prepareView.findViewById(R.id.thumb);
//        Glide.with(this).load(THUMB).into(thumb);
        controller.addControlComponent(prepareView);

        controller.addControlComponent(new CompleteView(this));//自动完成播放界面

        controller.addControlComponent(new ErrorView(this));//错误界面

        //标题栏
        titleView = new TitleView(this);
        controller.addControlComponent(titleView);

        VodControlView vodControlView = new VodControlView(this);//点播控制条
        //是否显示底部进度条。默认显示
//                vodControlView.showBottomProgress(false);
        controller.addControlComponent(vodControlView);

        GestureView gestureControlView = new GestureView(this);//滑动控制视图
        controller.addControlComponent(gestureControlView);
        //根据是否为直播决定是否需要滑动调节进度
        controller.setCanChangePosition(true);

        //设置标题
//        String title = intent.getStringExtra(IntentKeys.TITLE);

        //如果你不想要UI，不要设置控制器即可
        mVideoView.setVideoController(controller);

        //保存播放进度
//            mVideoView.setProgressManager(new ProgressManagerImpl());
        //播放状态监听
        mVideoView.addOnStateChangeListener(mOnStateChangeListener);

        //临时切换播放核心，如需全局请通过VideoConfig配置，详见MyApplication
        //使用IjkPlayer解码
//            mVideoView.setPlayerFactory(IjkPlayerFactory.create());
        //使用ExoPlayer解码
//            mVideoView.setPlayerFactory(ExoMediaPlayerFactory.create());
        //使用MediaPlayer解码
//            mVideoView.setPlayerFactory(AndroidMediaPlayerFactory.create());

//            mVideoView.start();

        ImageView mStartPlay = mVideoView.findViewById(R.id.start_play);
        mStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.start();
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        courseId = intent.getStringExtra("courseId");
        if (TextUtils.isEmpty(courseId)){
            finish();
            return;
        }
        mPresenter.courseDetail(courseId);
    }

    private void addHead(){
        View view=getLayoutInflater().inflate(R.layout.course_detail_header,null);
        desc_content=view.findViewById(R.id.desc_content);
        desc_title=view.findViewById(R.id.desc_title);
        subject=view.findViewById(R.id.subject);
        subjectName=view.findViewById(R.id.subjectName);
        after_subject=view.findViewById(R.id.after_subject);
        schoolYear=view.findViewById(R.id.schoolYear);
        after_school = view.findViewById(R.id.after_school);
        booksNumber=view.findViewById(R.id.booksNumber);
        after_book = view.findViewById(R.id.after_book);
        curriculum=view.findViewById(R.id.curriculum);
        img_like=view.findViewById(R.id.img_like);
        price_detail = view.findViewById(R.id.price_detail);

        view.findViewById(R.id.ll_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map=new HashMap<>();
                map.put("id",mCourseBean.getId());
                map.put("collection",mCourseBean.isCollection()+"");
                mPresenter.courseCollection(map,-1);
            }
        });

        courseAdapter.addHeaderView(view);
    }

    @Override
    protected CourseDetailPresenter setMPresenter() {
        CourseDetailPresenter courseDetailPresenter=new CourseDetailPresenter();
        courseDetailPresenter.attachView(this);
        return courseDetailPresenter;
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void onError(Throwable throwable, int flag) {
        if (mCourseBean==null){
            ll_no_network.setVisibility(View.VISIBLE);
            smartRefreshLayout2.setEnableRefresh(false);
            smartRefreshLayout.setEnableRefresh(true);
        }else {
            ll_no_network.setVisibility(View.GONE);
            smartRefreshLayout.setEnableRefresh(false);
            smartRefreshLayout2.setEnableRefresh(true);
            ToastUtil.show("当前网络不可用，请稍候再试");
        }

        smartRefreshLayout.finishRefresh();
        smartRefreshLayout2.finishRefresh();

    }
    @Override
    public void getDetail(CourseDetailBean courseDetailBean) {
        ll_no_network.setVisibility(View.GONE);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout2.setEnableRefresh(true);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout2.finishRefresh();
        content.setVisibility(View.VISIBLE);
        mCourseDetailBean = courseDetailBean;
        CourseBean courseBean=courseDetailBean.getShiPingListResponse();
        mCourseBean=courseBean;
        upDateUi(courseBean);
        List<CourseBean>list=courseDetailBean.getShiPingCollectionResponses();
        courseAdapter.setNewData(list);
        skTime=courseDetailBean.getSecond();
        isVip=courseDetailBean.isVip();
        if (!isVip){
            startProgressTimer();
        }
        if (courseDetailBean.isVip()){
            tv_tobe_vip.setText("会员升级");
            tv_tobe_vip.setVisibility(View.GONE);
            tv_pay.setText("立即观看");
            fl_show.setVisibility(View.GONE);
        }else {
            tv_tobe_vip.setText("开通VIP");
            tv_tobe_vip.setVisibility(View.VISIBLE);
            tv_pay.setText("单课付费");
            fl_show.setVisibility(View.VISIBLE);
        }
        tv_read.setText("已观看："+courseBean.getReadNumber()+"次");

    }
    private void upDateUi(CourseBean courseBean){
        if (courseBean.isCollection()){
            Glide.with(this).load(R.drawable.like).into(img_like);
        }else {
            Glide.with(this).load(R.drawable.like_un).into(img_like);
        }
        price_detail.setText("￥"+courseBean.getMinPrice());
        String title=courseBean.getSubject();
        if (title.length()>=14){
            title=title.substring(0,14)+"\n\t"+title.substring(14);
        }
        subject.setText(title);
        if (TextUtils.isEmpty(courseBean.getSubjectName())){
            subjectName.setVisibility(View.GONE);
            after_subject.setVisibility(View.GONE);
        }else {
            subjectName.setVisibility(View.VISIBLE);
            after_subject.setVisibility(View.VISIBLE);
        }
        subjectName.setText(courseBean.getSubjectName());
        if (TextUtils.isEmpty(courseBean.getSchoolYear())){
            schoolYear.setVisibility(View.GONE);
            after_school.setVisibility(View.GONE);
        }else {
            schoolYear.setVisibility(View.VISIBLE);
            after_school.setVisibility(View.VISIBLE);
        }
        schoolYear.setText(courseBean.getSchoolYear());
        if (TextUtils.isEmpty(courseBean.getBooksNumber())){
            booksNumber.setVisibility(View.GONE);
            after_book.setVisibility(View.GONE);
        }else {
            booksNumber.setVisibility(View.VISIBLE);
            after_book.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(courseBean.getCurriculum())){
            if (after_book.getVisibility()==View.VISIBLE){
                after_book.setVisibility(View.GONE);
            }else if (after_school.getVisibility()==View.VISIBLE){
                after_school.setVisibility(View.GONE);
            }else if (after_subject.getVisibility()==View.VISIBLE){
                after_subject.setVisibility(View.GONE);
            }
        }
        booksNumber.setText(courseBean.getBooksNumber());
        curriculum.setText(courseBean.getCurriculum());
        if (!TextUtils.isEmpty(courseBean.getCurriculumBrief())){
            desc_content.setText(courseBean.getCurriculumBrief());
            desc_title.setVisibility(View.VISIBLE);
            desc_content.setVisibility(View.VISIBLE);
        }else {
            desc_title.setVisibility(View.GONE);
            desc_content.setVisibility(View.GONE);
        }
        url=courseBean.getVideoUrl();
        mVideoView.setUrl(url);
        Glide.with(getBaseContext())
                .load(courseBean.getUrl())
                .apply(new RequestOptions().fitCenter())
                .into(thumb);
        titleView.setTitle("《"+courseBean.getSubject()+"》");
        money=courseBean.getMinPrice();
    }

    @Override
    public void onClickCollection(int position) {
        if (position==-1){
            mCourseBean.setCollection(!mCourseBean.isCollection());
            if (mCourseBean.isCollection()){
                ToastUtil.show("已收藏");
                Glide.with(this).load(R.drawable.like).into(img_like);
            }else {
                ToastUtil.show("已取消");
                Glide.with(this).load(R.drawable.like_un).into(img_like);
            }
        }else {
            if (mCourseBean.isCollection()){
                ToastUtil.show("已取消");
            }else {
                ToastUtil.show("已收藏");
            }
            mCourseBean.setCollection(!mCourseBean.isCollection());
            courseAdapter.notifyItemChanged(position+1);
        }
    }

    protected Timer UPDATE_PROGRESS_TIMER;
    protected ProgressTimerTask mProgressTimerTask;
    public void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
    }

    public void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }
    }

    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.courseDetail(courseId);
        smartRefreshLayout.finishRefresh(1500);
        smartRefreshLayout2.finishRefresh(1500);
    }

    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            long position = mVideoView.getCurrentPosition();
            long second=position/1000;
            if (second>=skTime&&skTime>0&&!isVip){
                StopVideoDialog stopVideoDialog = StopVideoDialog.newInstance(skTime,money,courseId);
                stopVideoDialog.show(getSupportFragmentManager(),"video");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.startStudy(courseId);
                        mVideoView.pause();
                        mVideoView.release();
                        my_cover.setVisibility(View.VISIBLE);
                    }
                });
                cancelProgressTimer();
            }
        }
    }


    @OnClick({R.id.back,R.id.tv_tobe_vip,R.id.tv_pay,R.id.my_play,R.id.tv_share,R.id.tv_refresh})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                EventBus.getDefault().post(new MessageEvent("", EventBusTag.MainRefreshCourseList));
                finish();
                break;
            case R.id.tv_tobe_vip:
                if (isVip){

                }else {
                    Intent intent=new Intent(this, VipCenterActivity.class);
                    intent.putExtra("courseId",courseId);
                    startActivity(intent);
                }
                break;
            case R.id.tv_pay:
                if (isVip){
                    mVideoView.start();
                }else {
                    if (TextUtils.isEmpty(BaseApplication.getInstance().getToken())){
                        LoginActivity.launchTask(BaseApplication.getInstance(),"");
                        return;
                    }
                    Intent intent=new Intent(CourseDetailActivity.this, CourseVipActivity.class);
                    intent.putExtra("courseId",courseId);
                    intent.putExtra("from","detail");
                    startActivity(intent);
                }
                break;
            case R.id.my_play:
                my_cover.setVisibility(View.GONE);
                mVideoView.start();
                if (!isVip){
                    startProgressTimer();
                }
                break;
            case R.id.tv_share:
                startActivity(new Intent(CourseDetailActivity.this, ShareActivity.class));
                break;
            case R.id.tv_refresh:
                smartRefreshLayout.autoRefresh();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasToken()){
            mPresenter.courseDetail(courseId);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (exoPlayerManager!=null){
//            Log.i("csc","onResume()");
//            exoPlayerManager.onResume();
//        }
        mVideoView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (exoPlayerManager!=null){
//            Log.i("csc","onPause()");
//            if (exoPlayerManager.isPlaying()){
//                mPresenter.startStudy(courseId);
//            }
//            exoPlayerManager.onPause();
//        }
        mVideoView.pause();
    }


    @Override
    protected void onDestroy() {
//        if (exoPlayerManager!=null){
//            exoPlayerManager.onDestroy();
//        }
        if (mVideoView!=null){
            mVideoView.release();
        }
        cancelProgressTimer();
        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            return;
        }
        EventBus.getDefault().post(new MessageEvent("", EventBusTag.MainRefreshCourseList));
        super.onBackPressed();

    }

    private VideoView.OnStateChangeListener mOnStateChangeListener = new VideoView.SimpleOnStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://小屏
                    break;
                case VideoView.PLAYER_FULL_SCREEN://全屏
                    break;
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    //在STATE_PREPARING时设置setMute(true)可实现静音播放
//                    mVideoView.setMute(true);
                    break;
                case VideoView.STATE_PREPARED:
                    break;
                case VideoView.STATE_PLAYING:
                    //需在此时获取视频宽高
//                    int[] videoSize = mVideoView.getVideoSize();
//                    L.d("视频宽：" + videoSize[0]);
//                    L.d("视频高：" + videoSize[1]);
                    mPresenter.startStudy(courseId);
                    if (my_cover!=null){
                        my_cover.setVisibility(View.GONE);
                    }
                    break;
                case VideoView.STATE_PAUSED:
                    mPresenter.startStudy(courseId);
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    mPresenter.startStudy(courseId);
                    break;
                case VideoView.STATE_ERROR:
                    break;
            }
        }
    };
}
