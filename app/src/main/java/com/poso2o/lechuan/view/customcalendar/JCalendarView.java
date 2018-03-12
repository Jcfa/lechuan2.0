package com.poso2o.lechuan.view.customcalendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;

/**
 * Created by mr zhang on 2017/8/9.
 * 自定义日历控件
 */
public class JCalendarView extends BaseView implements View.OnClickListener,CalendarCard.OnCellClickListener{

    private View view;
    private Context context;

    private ViewPager mViewPager;
    private int mCurrentIndex = 498;
    private CalendarCard[] mShowViews;
    private CalendarViewAdapter<CalendarCard> adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    private String setYear = null;
    private String setMonth = null;
    private String setDay = null;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    private ImageButton preImgBtn, nextImgBtn;
    private TextView monthText;


    public JCalendarView(Context context) {
        super(context);
        this.context = context;
    }

    public JCalendarView(Context context,String setDate) {
        super(context);
        this.context = context;
        String[] date = setDate.split("-");
        setYear = date[0];
        setMonth = date[1];
        setDay = date[2];
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPreMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.btnNextMonth:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void clickDate(CustomDate date) {
        if (onCalendarDateListener != null){
            onCalendarDateListener.onCalendarDate(date);
        }
    }

    @Override
    public void changeDate(CustomDate date) {
        monthText.setText(date.year + "年" + date.month + "月");
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context,R.layout.view_calendar,null);
        return view;
    }

    @Override
    public void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.vp_calendar);
        preImgBtn = (ImageButton) view.findViewById(R.id.btnPreMonth);
        nextImgBtn = (ImageButton) view.findViewById(R.id.btnNextMonth);
        monthText = (TextView) view.findViewById(R.id.tvCurrentMonth);
    }

    @Override
    public void initData() {
        CalendarCard[] views = new CalendarCard[3];
        if (setYear != null){
            for (int i = 0; i < 3; i++) {
                views[i] = new CalendarCard(context, this,Integer.parseInt(setYear),Integer.parseInt(setMonth),Integer.parseInt(setDay));
            }
        }else {
            for (int i = 0; i < 3; i++) {
                views[i] = new CalendarCard(context, this);
            }
        }
        adapter = new CalendarViewAdapter<>(views);
        setViewPager();
    }

    @Override
    public void initListenner() {
        preImgBtn.setOnClickListener(this);
        nextImgBtn.setOnClickListener(this);
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(498);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    private OnCalendarDateListener onCalendarDateListener;
    public interface OnCalendarDateListener{
        void onCalendarDate(CustomDate date);
    }
    public void setOnCalendarDateListener(OnCalendarDateListener onCalendarDateListener){
        this.onCalendarDateListener = onCalendarDateListener;
    }
}
