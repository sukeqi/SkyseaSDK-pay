package com.skysea.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skysea.sdk.R;
import com.skysea.utils.UtilTools;

import java.util.List;

/**
 * 带下划线的
 */

public class FragmentLayoutWithLine extends LinearLayout implements ViewPager.OnPageChangeListener {
    private List<Fragment> list;
    private Fragment_viewpager_Adapter fragmentAdapter;
    private FragmentActivity context;
    private ViewPager viewPager;
    private LinearLayout tabLayout;
    private int position = 0;
    private boolean isScorll = false;
    private boolean isScorllToNext = false;
    private int whereTab = 0;
    private ChangeFragmentListener changeListener;
    private int tabHeight = 6;
    private int tabWidth = 0;
    private int tabColor = Color.GREEN;
    private View v;
    ViewTreeObserver vto;

    public interface ChangeFragmentListener {
        /**
         * @param lastTabView    上一项的tab视图，用来改变没选中tab状态
         * @param currentTabView 当前想的tab视图,用来改变选中的tab样式
         */
        public void change(int lastPosition, int position, View lastTabView, View currentTabView);
    }

    public void setOnChangeFragmentListener(ChangeFragmentListener listener) {
        this.changeListener = listener;
    }

    public FragmentLayoutWithLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (FragmentActivity) context;
    }

    public FragmentLayoutWithLine(Context context) {
        super(context);
        this.context = (FragmentActivity) context;
    }

    /**
     * @param list        fragment数据
     * @param tabLayoutId tab布局 id
     * @param id          任意int型，不能重复
     */
    public void setAdapter(List<Fragment> list, int tabLayoutId, int id) {
        this.setOrientation(LinearLayout.VERTICAL);
        FrameLayout tabFrame = (FrameLayout) View.inflate(context, tabLayoutId, null);
        tabLayout = (LinearLayout) (tabFrame.findViewById(R.id.tabLayout));
        tabLayout.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.list = list;
        fragmentAdapter = new Fragment_viewpager_Adapter(context.getSupportFragmentManager());
        viewPager = new MyViewPager(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        viewPager.setLayoutParams(params);
        viewPager.setId(id);
        if (whereTab == 0) {
            this.addView(viewPager);
            this.addView(tabFrame);
        } else {
            this.addView(tabFrame);
            this.addView(viewPager);
        }
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            View view = tabLayout.getChildAt(i);
            view.setClickable(true);
            view.setOnClickListener(new tabClickLisener(i));
        }
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOnPageChangeListener(this);
        // 添加导航动画横线
        v = new View(context);
        FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(180, tabHeight, Gravity.BOTTOM);
        v.setLayoutParams(vParams);
        tabFrame.addView(v);
        setTabLine();
    }

    public void setTabLine() {
        vto = tabLayout.getChildAt(0).getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (!vto.isAlive()) {
                    vto = tabLayout.getChildAt(0).getViewTreeObserver();
                }
                if (vto.isAlive()) {
                    vto.removeOnPreDrawListener(this);
                }
                tabWidth = tabLayout.getChildAt(0).getWidth();
                FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(tabLayout.getChildAt(0).getWidth(), tabHeight,
                        Gravity.BOTTOM);
                v.setLayoutParams(vParams);
                v.setBackgroundColor(tabColor);
                return true;
            }
        });
    }

    private class Fragment_viewpager_Adapter extends FragmentStatePagerAdapter {

        public Fragment_viewpager_Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            ((ViewGroup) list.get(position).getView()).removeAllViews();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    class tabClickLisener implements OnClickListener {
        private int position;

        public tabClickLisener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (list.size() == tabLayout.getChildCount()) {
                viewPager.setCurrentItem(position, isScorll);
            } else
                Toast.makeText(context, "page项数量不等于tab项数量", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        FragmentLayoutWithLine.this.v.setX(arg0 * tabWidth + tabWidth * arg1);
    }

    @Override
    public void onPageSelected(int arg0) {
        if (changeListener != null) {
            changeListener.change(position, arg0, tabLayout.getChildAt(position), tabLayout.getChildAt(arg0));
        }
        position = arg0;
    }

    public int getCurrentPosition() {
        return position;
    }

    public boolean isScorll() {
        return isScorll;
    }

    /**
     * @param isScorll 设置点击tab时fragment切换是否带滑动，默认不带
     */
    public void setScorll(boolean isScorll) {
        this.isScorll = isScorll;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setCurrenItem(int position) {
        viewPager.setCurrentItem(position, isScorll);
        if (changeListener != null) {
            changeListener.change(0, position, tabLayout.getChildAt(0), tabLayout.getChildAt(position));
        }
        this.position = position;
        int width = UtilTools.getScreenWidth(getContext()) / tabLayout.getChildCount();
        v.setX(position * width);
    }

    public boolean isScorllToNext() {
        return isScorllToNext;
    }

    public int getWhereTab() {
        return whereTab;
    }

    /**
     * @param whereTab 设置tab位于viewpager上方还是下方，0代表下方，1代表上方
     */
    public void setWhereTab(int whereTab) {
        this.whereTab = whereTab;
    }

    public int getTabHeight() {
        return tabHeight;
    }

    /**
     * @param tabHeight 设置导航线宽度
     * @param tabColor  设置导航线颜色
     */
    public void setTabHeight(int tabHeight, int tabColor) {
        this.tabHeight = tabHeight;
        this.tabColor = tabColor;
    }

    public int getTabColor() {
        return tabColor;
    }

    public LinearLayout getTabLayout() {
        return tabLayout;
    }

    /**
     * @param isScorllToNext 是否可以滑动切换，默认为true
     */
    public void setScorllToNext(boolean isScorllToNext) {
        this.isScorllToNext = isScorllToNext;
    }

    class MyViewPager extends ViewPager {

        public MyViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent arg0) {
            if (isScorllToNext) {
                return super.onTouchEvent(arg0);
            }
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (isScorllToNext == false) {
                return false;
            } else {
                return super.onInterceptTouchEvent(ev);
            }
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
