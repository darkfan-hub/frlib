package com.frlib.basic.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pools;
import com.frlib.basic.R;

import java.util.ArrayList;

/**
 * Creator: Gu FanFan.
 * Date: 2018/5/15.
 * Description: 导航栏View.
 */
public class NavigationBar extends LinearLayout {

    private static final Pools.Pool<Tab> sTabPool = new Pools.SynchronizedPool<>(16);

    private OnTabSelectedListener mOnTabSelectedListener;
    public final ArrayList<Tab> mTabs = new ArrayList<>();
    private Tab mSelectedTab;
    private ColorStateList mTabTextColors;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.WHITE);
    }

    /**
     * 选中监听.
     */
    public void addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mOnTabSelectedListener = onTabSelectedListener;
    }

    public void setTabColors(int unselectColor, int selectedColor) {
        mTabTextColors = createColorStateList(ContextCompat.getColor(getContext(), unselectColor),
                ContextCompat.getColor(getContext(), selectedColor));
    }

    private ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;

        return new ColorStateList(states, colors);
    }

    public void addTab(Tab tab) {
        addTab(tab, mTabs.isEmpty());
    }

    public void addTab(Tab tab, boolean setSelected) {
        addTab(tab, mTabs.size(), setSelected);
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        configureTab(tab, position);
        addView(tab.mTabView);

        if (setSelected) {
            tab.select();
        }
    }

    public Tab newTab() {
        Tab tab = sTabPool.acquire();
        if (tab == null) {
            tab = new Tab();
        }
        tab.mParent = this;
        tab.mTabView = createTabView();
        return tab;
    }

    private View createTabView() {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.frlib_layout_navigation_bar, null);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);
        return view;
    }

    private void configureTab(Tab tab, int position) {
        tab.setPosition(position);
        mTabs.add(position, tab);

        final int count = mTabs.size();
        for (int i = position + 1; i < count; i++) {
            mTabs.get(i).setPosition(i);
        }
    }

    public void selectTab(Tab tab) {
        int newPosition = tab.getPosition();

        final Tab currentTab = mSelectedTab;
        if (currentTab == null || currentTab != tab) {
            tab.setSelect(true);
            if (currentTab != null) {
                currentTab.setSelect(false);
            }
            mSelectedTab = tab;
        }

        if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onTabSelected(newPosition);
        }
    }

    public Tab getTab(int position) {
        return mTabs.get(position);
    }

    public static final class Tab {

        private NavigationBar mParent;
        private int mPosition;
        private Drawable mIcon;
        private CharSequence mText;
        private boolean isSelect = false;
        private View mTabView;
        private ImageView mImageView;
        private TextView mTextView;

        boolean isSelect() {
            return isSelect;
        }

        void setSelect(boolean select) {
            isSelect = select;
            updateTabView();
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public Tab setTab(int textResId, int imageResId) {
            return setTab(mParent.getContext().getString(textResId),
                    ContextCompat.getDrawable(mParent.getContext(), imageResId));
        }

        Tab setTab(String text, Drawable icon) {
            mText = text;
            mIcon = icon;
            updateTabView();
            return this;
        }

        public Drawable getIcon() {
            return mIcon;
        }

        public Tab setIcon(Drawable icon) {
            mIcon = icon;
            updateTabView();
            return this;
        }

        public Tab setIcon(int resId) {
            return setIcon(ContextCompat.getDrawable(mParent.getContext(), resId));
        }

        public CharSequence getText() {
            return mText;
        }

        public Tab setText(CharSequence text) {
            mText = text;
            updateTabView();
            return this;
        }

        public Tab setText(int resId) {
            return setText(mParent.getContext().getString(resId));
        }

        void updateTabView() {
            if (mTabView != null) {
                mTabView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select();
                    }
                });
                if (mImageView == null) {
                    mImageView = mTabView.findViewById(R.id.imageView);
                }
                if (mTextView == null) {
                    mTextView = mTabView.findViewById(R.id.textView);
                }
                mTextView.setTextColor(mParent.mTabTextColors);
                if (getIcon() != null) {
                    mImageView.setBackground(getIcon());
                }
                if (getText() != null) {
                    mTextView.setText(getText());
                }
                mTextView.setSelected(isSelect());
                mImageView.setSelected(isSelect());
            }
        }

        void select() {
            if (mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            mParent.selectTab(this);
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(int tabPosition);
    }
}
