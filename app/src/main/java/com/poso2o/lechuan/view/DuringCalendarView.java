package com.poso2o.lechuan.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.customcalendar.CalendarCard;
import com.poso2o.lechuan.view.customcalendar.CalendarViewAdapter;
import com.poso2o.lechuan.view.customcalendar.CustomDate;

/**
 * Created by mr zhang on 2017/9/21.
 * 自定义时间段选择窗
 */

public class DuringCalendarView extends BaseView implements View.OnClickListener,CalendarCard.OnCellClickListener{

    private View view;
    private Context context;

    //开始时间
    private TextView during_begin;
    //结束时间
    private TextView during_end;
    //上一个月
    private ImageButton btn_pre_month;
    //下一个月
    private ImageButton btn_next_month;
    //当前月
    private TextView tv_current_month;
    //日历
    private ViewPager during_calendar;
    //确定
    private TextView during_finish;

    //是开始时间还是结束时间
    private boolean isBeginTime = true;
    //开始时间
    private String mBegin;
    //结束时间
    private String mEnd;
    //初始化开始时间
    private String beginTime;
    //初始化结束时间
    private String endTime;

    private int mCurrentIndex = 498;
    private CalendarCard[] mShowViews;
    private CalendarViewAdapter<CalendarCard> adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public DuringCalendarView(Context context,String beginTime,String endTime) {
        super(context);
        this.context = context;
        this.beginTime = beginTime;
        this.endTime = endTime;
        mBegin = beginTime;
        mEnd = endTime;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_during_calendar,null);
        return view;
    }

    @Override
    public void initView() {

        during_begin = (TextView) view.findViewById(R.id.during_begin);

        during_end = (TextView) view.findViewById(R.id.during_end);

        btn_pre_month = (ImageButton) view.findViewById(R.id.btn_pre_month);

        btn_next_month = (ImageButton) view.findViewById(R.id.btn_next_month);

        tv_current_month = (TextView) view.findViewById(R.id.tv_current_month);

        during_calendar = (ViewPager) view.findViewById(R.id.during_calendar);

        during_finish = (TextView) view.findViewById(R.id.during_finish);

        during_begin.setTextColor(0xFFFF6573);
    }

    @Override
    public void initData() {
        CalendarCard[] views = new CalendarCard[3];
        for (int i = 0; i < 3; i++) {
            views[i] = new CalendarCard(context, this);
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();

        during_begin.setText(beginTime);
        during_end.setText(endTime);
    }

    @Override
    public void initListenner() {
        during_begin.setOnClickListener(this);
        during_end.setOnClickListener(this);
        btn_pre_month.setOnClickListener(this);
        btn_next_month.setOnClickListener(this);
        during_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pre_month:
                during_calendar.setCurrentItem(during_calendar.getCurrentItem() - 1);
                break;
            case R.id.btn_next_month:
                during_calendar.setCurrentItem(during_calendar.getCurrentItem() + 1);
                break;
            case R.id.during_finish:
                if (onDuringListener != null){
                    onDuringListener.onDuringClick(mBegin,mEnd);
                }
                break;
            case R.id.during_begin:
                isBeginTime = true;
                during_begin.setTextColor(0xFFFF6573);
                during_end.setTextColor(0xFF959595);
                break;
            case R.id.during_end:
                isBeginTime = false;
                during_begin.setTextColor(0xFF959595);
                during_end.setTextColor(0xFFFF6573);
                break;
        }
    }

    @Override
    public void clickDate(CustomDate date) {
        String dateT = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        if (isBeginTime){
            if (CalendarUtil.TimeCompare(dateT, during_end.getText().toString())) {
                during_begin.setText(dateT);
//                mBegin = CalendarUtil.timeStamp(dateT + " 00:00:00");
                mBegin = dateT;
                Toast.show(context,mBegin);
            } else {
                Toast.show(context,"选择的时间范围不正确");
            }
        }else {
            if (CalendarUtil.TimeCompare(during_begin.getText().toString(), dateT)) {
//                mEnd = CalendarUtil.timeStamp(dateT + " 59:59:59");
                mEnd = dateT;
                during_end.setText(dateT);
            } else {
                Toast.show(context,"选择的时间范围不正确");
            }
        }
    }

    @Override
    public void changeDate(CustomDate date) {
        tv_current_month.setText(date.year + "年" + date.month + "月");
    }

    private void setViewPager() {
        during_calendar.setAdapter(adapter);
        during_calendar.setCurrentItem(498);
        during_calendar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
    }

    private OnDuringListener onDuringListener;
    public interface OnDuringListener{
        void onDuringClick(String begin, String end);
    }
    public void setOnDuringListener(OnDuringListener onDuringListener){
        this.onDuringListener = onDuringListener;
    }
}
