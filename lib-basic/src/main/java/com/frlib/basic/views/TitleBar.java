package com.frlib.basic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.frlib.basic.R;
import com.frlib.basic.config.AppConstants;
import com.frlib.basic.ext.ViewExtKt;
import com.frlib.utils.StringUtil;
import com.frlib.utils.UIUtil;
import com.frlib.utils.ext.ResourcesExtKt;

import timber.log.Timber;

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 04/12/2020 16:44
 */
public class TitleBar extends RelativeLayout {

    /**
     * 状态栏填充视图
     */
    private View statusBarFillView;
    /**
     * 状态栏颜色
     */
    private int statusBarColor;

    /**
     * 是否显示底部分割线
     */
    private boolean showBottomLine;
    /**
     * 分割线颜色
     */
    private int bottomLineColor;
    /**
     * 底部阴影高度
     */
    private float bottomShadowHeight;

    /**
     * 主视图
     */
    private RelativeLayout rlMain;

    /**
     * 是否撑起状态栏, true时,标题栏浸入状态栏
     */
    private boolean fillStatusBar;
    /**
     * 标题栏背景颜色
     */
    private int titleBarColor;
    /**
     * 标题栏高度
     */
    private int titleBarHeight;

    /**
     * 左边视图类型
     */
    private int leftType;
    /**
     * 左边自定义View
     */
    private View leftView;
    /**
     * 左边TextView文字
     */
    private String leftText;
    /**
     * 左边TextView颜色
     */
    private int leftTextColor;
    /**
     * 左边TextView文字大小
     */
    private float leftTextSize;
    /**
     * 左边TextView drawableLeft资源
     */
    private int leftDrawable;
    /**
     * 左边TextView drawablePadding
     */
    private float leftDrawablePadding;
    /**
     * 左边图片资源
     */
    private int leftImageResource;
    /**
     * 左边自定义View id
     */
    private int leftCustomViewId;

    /**
     * 右边视图类型
     */
    private int rightType;
    /**
     * 右边自定义View
     */
    private View rightView;
    /**
     * 右边TextView文字
     */
    private String rightText;
    /**
     * 右边TextView颜色
     */
    private int rightTextColor;
    /**
     * 右边TextView文字大小
     */
    private float rightTextSize;
    /**
     * 右边图片资源
     */
    private int rightImageResource;
    /**
     * 右边自定义View id
     */
    private int rightCustomViewId;

    /**
     * 中间视图类型
     */
    private int centerType;
    /**
     * 中间视图View
     */
    private View centerView;
    /**
     * 中间视图View
     */
    private int centerViewId;
    /**
     * 中间TextView文字
     */
    private String centerText;
    /**
     * 中间TextView字体颜色
     */
    private int centerTextColor;
    /**
     * 中间TextView字体大小
     */
    private float centerTextSize;
    /**
     * 中间TextView字体是否显示跑马灯效果
     */
    private boolean centerTextMarquee;
    /**
     * 中间subTextView文字
     */
    private String centerSubText;
    /**
     * 中间subTextView字体颜色
     */
    private int centerSubTextColor;
    /**
     * 中间subTextView字体大小
     */
    private float centerSubTextSize;
    /**
     * 中间搜索输入框
     */
    private EditText searchEditText;
    /**
     * 中间搜索输入框提示文字
     */
    private String searchHintText;
    /**
     * 搜索框是否可输入
     */
    private boolean centerSearchEditable;
    /**
     * 搜索框背景图片
     */
    private int centerSearchBgResource;
    /**
     * 搜索框左边按钮资源
     */
    private int centerSearchLeftResource;
    /**
     * 搜索框右边按钮类型
     */
    private int centerSearchRightType;

    private int size5;
    private int size7;
    private int size15;

    private LayoutInflater layoutInflater;
    private OnTitleBarListener titleBarListener;

    private TextView centerTextView;
    private ProgressBar centerProgress;
    private TextView centerSubTextView;

    enum BarType {
        // 无
        NONE,
        // 文字
        TEXT,
        // 图片
        IMAGE,
        // 自定义
        CUSTOM,
        // 搜索
        SEARCH,
        // 语音
        VOICE,
        // 删除
        DELETE
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(context, attrs);
        initTitleBarViews(context);
        initMainViews(context);
    }

    public void addTitleBarListener(OnTitleBarListener onTitleBarListener) {
        this.titleBarListener = onTitleBarListener;
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        size5 = UIUtil.dp2px(context, 5);
        size7 = UIUtil.dp2px(context, 7);
        size15 = UIUtil.dp2px(context, 15);
        int color33 = ResourcesExtKt.color(context, R.color.color_33);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // notice 未引入沉浸式标题栏之前,隐藏标题栏撑起布局
            fillStatusBar = array.getBoolean(R.styleable.TitleBar_fillStatusBar, true);
        }

        statusBarColor = array.getColor(R.styleable.TitleBar_statusBarColor, Color.WHITE);

        titleBarColor = array.getColor(R.styleable.TitleBar_titleBarColor, Color.WHITE);
        titleBarHeight = (int) array.getDimension(R.styleable.TitleBar_titleBarHeight, UIUtil.dp2px(context, 44));

        showBottomLine = array.getBoolean(R.styleable.TitleBar_showBottomLine, true);
        bottomLineColor = array.getColor(R.styleable.TitleBar_bottomLineColor, Color.parseColor("#dddddd"));
        bottomShadowHeight = array.getDimension(R.styleable.TitleBar_bottomShadowHeight, UIUtil.dp2px(context, 0));

        leftType = array.getInt(R.styleable.TitleBar_leftType, BarType.NONE.ordinal());
        if (leftType == BarType.TEXT.ordinal()) {
            leftText = array.getString(R.styleable.TitleBar_leftText);
            leftTextColor = array.getColor(R.styleable.TitleBar_leftTextColor, color33);
            leftTextSize = array.getDimension(R.styleable.TitleBar_leftTextSize, UIUtil.dp2px(context, 16));
            leftDrawable = array.getResourceId(R.styleable.TitleBar_leftDrawable, 0);
            leftDrawablePadding = array.getDimension(R.styleable.TitleBar_leftDrawablePadding, 5);
        } else if (leftType == BarType.IMAGE.ordinal()) {
            leftImageResource = array.getResourceId(R.styleable.TitleBar_leftImageResource, R.drawable.frlib_icon_back_white);
        } else if (leftType == BarType.CUSTOM.ordinal()) {
            leftCustomViewId = array.getResourceId(R.styleable.TitleBar_leftCustomView, 0);
        }

        rightType = array.getInt(R.styleable.TitleBar_rightType, BarType.NONE.ordinal());
        if (rightType == BarType.TEXT.ordinal()) {
            rightText = array.getString(R.styleable.TitleBar_rightText);
            rightTextColor = array.getColor(R.styleable.TitleBar_rightTextColor, color33);
            rightTextSize = array.getDimension(R.styleable.TitleBar_rightTextSize, UIUtil.dp2px(context, 16));
        } else if (rightType == BarType.IMAGE.ordinal()) {
            rightImageResource = array.getResourceId(R.styleable.TitleBar_rightImageResource, 0);
        } else if (rightType == BarType.CUSTOM.ordinal()) {
            rightCustomViewId = array.getResourceId(R.styleable.TitleBar_rightCustomView, 0);
        }

        centerType = array.getInt(R.styleable.TitleBar_centerType, BarType.NONE.ordinal());
        if (centerType == BarType.TEXT.ordinal()) {
            centerText = array.getString(R.styleable.TitleBar_centerText);
            centerTextColor = array.getColor(R.styleable.TitleBar_centerTextColor, ContextCompat.getColor(context, R.color.color_33));
            centerTextSize = array.getDimension(R.styleable.TitleBar_centerTextSize, UIUtil.dp2px(context, 17));
            centerTextMarquee = array.getBoolean(R.styleable.TitleBar_centerTextMarquee, true);
            centerSubText = array.getString(R.styleable.TitleBar_centerSubText);
            centerSubTextColor = array.getColor(R.styleable.TitleBar_centerSubTextColor, ContextCompat.getColor(context, R.color.color_66));
            centerSubTextSize = array.getDimension(R.styleable.TitleBar_centerSubTextSize, UIUtil.dp2px(context, 11));
        } else if (centerType == BarType.SEARCH.ordinal()) {
            searchHintText = array.getString(R.styleable.TitleBar_centerSearchHint);
            centerSearchEditable = array.getBoolean(R.styleable.TitleBar_centerSearchEditable, true);
            centerSearchBgResource = array.getResourceId(R.styleable.TitleBar_centerSearchBg, R.drawable.frlib_bg_ovl_solide_f6_radius_3);
            centerSearchLeftResource = array.getResourceId(R.styleable.TitleBar_centerSearchLeftIcon, R.drawable.frlib_icon_search);
            centerSearchRightType = array.getInt(R.styleable.TitleBar_centerSearchRightType, BarType.NONE.ordinal());
        } else if (centerType == BarType.CUSTOM.ordinal()) {
            centerViewId = array.getResourceId(R.styleable.TitleBar_centerCustomView, 0);
        }

        array.recycle();
    }

    /**
     * 初始化标题栏
     *
     * @param context 上下文
     */
    private void initTitleBarViews(Context context) {
        ViewGroup.LayoutParams titleBarParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(titleBarParams);

        if (fillStatusBar) {
            int statusBarHeight = UIUtil.statusBarHeight(context);
            statusBarFillView = new View(context);
            statusBarFillView.setId(View.generateViewId());
            statusBarFillView.setBackgroundColor(statusBarColor);
            LayoutParams fillViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, statusBarHeight);
            fillViewParams.addRule(RelativeLayout.ALIGN_TOP);
            addView(statusBarFillView, fillViewParams);
        }

        rlMain = new RelativeLayout(context);
        rlMain.setId(View.generateViewId());
        rlMain.setBackgroundColor(titleBarColor);
        int bottomLineHeight = Math.max(1, UIUtil.dp2px(context, 1f));
        titleBarHeight = showBottomLine
                ? titleBarHeight - bottomLineHeight
                : titleBarHeight;
        LayoutParams rlMainParams = new LayoutParams(LayoutParams.MATCH_PARENT, titleBarHeight);
        if (fillStatusBar) {
            rlMainParams.addRule(RelativeLayout.BELOW, statusBarFillView.getId());
        } else {
            rlMainParams.addRule(RelativeLayout.ALIGN_TOP);
        }

        if (centerType != BarType.SEARCH.ordinal()) {
            rlMain.setOnClickListener(v -> {
                long currentClickMillis = System.currentTimeMillis();
                if (currentClickMillis - lastClickMillis < AppConstants.double_click_time) {
                    Timber.d("双击标题栏");
                    if (titleBarListener != null) {
                        titleBarListener.doubleClick(rlMain);
                    }
                }
                lastClickMillis = currentClickMillis;
            });
        }

        addView(rlMain, rlMainParams);

        if (showBottomLine) {
            View bottomLineView = new View(context);
            bottomLineView.setBackgroundColor(bottomLineColor);
            LayoutParams bottomLineParams = new LayoutParams(LayoutParams.MATCH_PARENT, bottomLineHeight);
            bottomLineParams.addRule(RelativeLayout.BELOW, rlMain.getId());
            bottomLineView.setLayoutParams(bottomLineParams);
            addView(bottomLineView, bottomLineParams);
        }

        if (bottomShadowHeight != 0) {
            View bottomShadowView = new View(context);
            bottomShadowView.setBackgroundResource(R.drawable.frlib_titlebar_bottom_shadow);
            LayoutParams bottomShadowParams = new LayoutParams(LayoutParams.MATCH_PARENT, UIUtil.dp2px(context, bottomShadowHeight));
            bottomShadowParams.addRule(RelativeLayout.BELOW, rlMain.getId());
            addView(bottomShadowView, bottomShadowParams);
        }
    }

    private void initMainViews(Context context) {
        if (leftType != BarType.NONE.ordinal()) {
            initLeftViews(context);
        }

        if (rightType != BarType.NONE.ordinal()) {
            initRightViews(context);
        }

        if (centerType != BarType.NONE.ordinal()) {
            initCenterViews(context);
        }
    }

    private void initLeftViews(Context context) {
        LayoutParams leftParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (leftType == BarType.TEXT.ordinal()) {
            leftView = titleBarTextView(context, leftText, leftTextColor, leftTextSize,
                    (Gravity.START | Gravity.CENTER_VERTICAL), (int) leftDrawablePadding, leftDrawable);
        }

        if (leftType == BarType.IMAGE.ordinal()) {
            leftParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            leftView = titleBarImageView(context, leftImageResource);
        }

        if (leftType == BarType.CUSTOM.ordinal()) {
            leftView = titleBarCustomView(context, rlMain, leftCustomViewId);
        }

        addViewToMain(leftView, leftParams);
        ViewExtKt.click(leftView, () -> {
            if (titleBarListener != null) {
                titleBarListener.leftClick(leftView);
            }
            return null;
        });
    }

    /**
     * 双击事件中，上次被点击时间
     */
    private long lastClickMillis = 0;

    private void initCenterViews(Context context) {
        LayoutParams centerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        centerParams.setMarginStart(size15);
        centerParams.setMarginEnd(size15);

        if (centerType == BarType.TEXT.ordinal()) {
            centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            titleBarCenterTextView(context);
        }

        if (centerType == BarType.SEARCH.ordinal()) {
            centerParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            centerParams.topMargin = size7;
            centerParams.bottomMargin = size7;
            if (leftView != null) {
                if (leftType != BarType.NONE.ordinal()) {
                    centerParams.addRule(RelativeLayout.END_OF, leftView.getId());
                }
            } else {
                centerParams.setMarginStart(size15);
            }

            if (null != rightView) {
                if (rightType != BarType.NONE.ordinal()) {
                    centerParams.addRule(RelativeLayout.START_OF, rightView.getId());
                }
            }

            titleBarCenterSearchView(context);
        }

        if (centerType == BarType.CUSTOM.ordinal()) {
            centerView = titleBarCustomView(context, rlMain, centerViewId);

            centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        }

        addViewToMain(centerView, centerParams);
    }

    /**
     * title bar 中间文本view
     *
     * @param context 上下文对象
     */
    private void titleBarCenterTextView(Context context) {
        final LinearLayout centerMain = new LinearLayout(context);
        centerMain.setId(View.generateViewId());
        centerMain.setGravity(Gravity.CENTER);
        centerMain.setOrientation(LinearLayout.VERTICAL);
        centerView = centerMain;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        centerTextView = new TextView(context);
        centerTextView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        centerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
        centerTextView.setText(centerText);
        centerTextView.setTextColor(centerTextColor);
        centerTextView.setGravity(Gravity.CENTER);
        centerTextView.setSingleLine();
        centerTextView.setMaxWidth((int) (UIUtil.getScreenWidth(context) * 3 / 5.0));
        if (centerTextMarquee) {
            centerTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            centerTextView.setMarqueeRepeatLimit(-1);
            centerTextView.requestFocus();
            centerTextView.setSelected(true);
        }

        centerMain.addView(centerTextView, textParams);

        centerSubTextView = new TextView(context);
        centerSubTextView.setSingleLine();
        centerSubTextView.setVisibility(View.GONE);
        centerSubTextView.setText(centerSubText);
        centerSubTextView.setTextColor(centerSubTextColor);
        centerSubTextView.setTextSize(centerSubTextSize);
        if (!StringUtil.isSpace(centerSubText)) {
            centerSubTextView.setVisibility(View.VISIBLE);
        }
        centerMain.addView(centerSubTextView, textParams);

        titleBarCenterProgressView(context, centerMain);
    }

    /**
     * 标题栏加载loading view
     *
     * @param context    上下文对象
     * @param centerMain 中间view
     */
    private void titleBarCenterProgressView(Context context, LinearLayout centerMain) {
        centerProgress = new ProgressBar(context);
        centerProgress.setIndeterminateDrawable(AppCompatResources.getDrawable(context, R.drawable.frlib_titlebar_loading_progress));
        centerProgress.setVisibility(View.GONE);
        int progressSize = UIUtil.dp2px(context, 18);
        LayoutParams progressParams = new LayoutParams(progressSize, progressSize);
        progressParams.addRule(RelativeLayout.CENTER_VERTICAL);
        progressParams.addRule(RelativeLayout.START_OF, centerMain.getId());
        rlMain.addView(centerProgress, progressParams);
    }

    /**
     * 标题栏中间搜索视图
     *
     * @param context 上下文
     */
    private void titleBarCenterSearchView(Context context) {
        final RelativeLayout rlCenterSearch = new RelativeLayout(context);
        rlCenterSearch.setBackgroundResource(centerSearchBgResource);

        final ImageView searchLeftIcon = new ImageView(context);
        searchLeftIcon.setId(View.generateViewId());
        searchLeftIcon.setImageResource(centerSearchLeftResource);
        LayoutParams searchIconLeftParams = new LayoutParams(size15, size15);
        searchIconLeftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        searchIconLeftParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        searchIconLeftParams.setMarginStart(UIUtil.dp2px(context, 10));
        rlCenterSearch.addView(searchLeftIcon, searchIconLeftParams);

        int searchRightIconId = centerSearchImageView(context, rlCenterSearch);
        centerEditTextView(context, rlCenterSearch, searchLeftIcon.getId(), searchRightIconId);

        centerView = rlCenterSearch;
    }

    /**
     * 标题栏中间搜索右边图片视图
     *
     * @param context      上下文
     * @param centerSearch 搜索视图
     * @return 搜索右边图片视图id
     */
    private int centerSearchImageView(Context context, RelativeLayout centerSearch) {
        if (centerSearchRightType == BarType.NONE.ordinal()) {
            return -1;
        } else {
            ImageView searchRightIcon = new ImageView(context);
            searchRightIcon.setId(View.generateViewId());
            searchRightIcon.setImageResource(centerSearchRightType == BarType.VOICE.ordinal()
                    ? R.drawable.frlib_icon_titlebar_voice
                    : R.drawable.frlib_icon_search_delete);
            LayoutParams searchIconRightParams = new LayoutParams(size15, size15);
            searchIconRightParams.addRule(RelativeLayout.CENTER_VERTICAL);
            searchIconRightParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            searchIconRightParams.setMarginEnd(size15);
            ViewExtKt.click(searchRightIcon, () -> {
                if (titleBarListener != null) {
                    if (centerSearchRightType == BarType.VOICE.ordinal()) {
                        titleBarListener.voiceIconClick(searchRightIcon);
                    }
                    if (centerSearchRightType == BarType.DELETE.ordinal()) {
                        titleBarListener.deleteIconClick(searchRightIcon);
                    }
                }
                return null;
            });
            centerSearch.addView(searchRightIcon, searchIconRightParams);
            return searchRightIcon.getId();
        }
    }

    /**
     * 搜索文本框
     *
     * @param context           上下文
     * @param centerSearch      搜索布局view
     * @param searchLeftIconId  搜索左边视图id
     * @param searchRightIconId 搜索右边视图id
     * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
     */
    private void centerEditTextView(Context context, RelativeLayout centerSearch, int searchLeftIconId, int searchRightIconId) {
        searchEditText = new EditText(context);
        searchEditText.setBackgroundColor(Color.TRANSPARENT);
        searchEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        searchEditText.setTextColor(ContextCompat.getColor(context, R.color.color_66));
        searchEditText.setHintTextColor(ContextCompat.getColor(context, R.color.color_99));
        searchEditText.setText(searchHintText);
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, UIUtil.dp2px(context, 14));
        searchEditText.setPadding(size5, 0, size5, 0);
        if (!centerSearchEditable) {
            searchEditText.setCursorVisible(false);
            searchEditText.clearFocus();
            searchEditText.setFocusable(false);
        } else {
            ViewExtKt.click(searchEditText, () -> {
                searchEditText.setCursorVisible(true);
                return null;
            });
        }

        searchEditText.setCursorVisible(false);
        searchEditText.setSingleLine(true);
        searchEditText.setEllipsize(TextUtils.TruncateAt.END);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (EditorInfo.IME_ACTION_SEARCH == actionId && null != titleBarListener) {
                titleBarListener.search(searchEditText.getText().toString());
            }
            return false;
        });

        LayoutParams searchParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (searchLeftIconId != -1) {
            searchParams.addRule(RelativeLayout.END_OF, searchLeftIconId);
        }
        if (searchRightIconId != -1) {
            searchParams.addRule(RelativeLayout.START_OF, searchRightIconId);
        }
        searchParams.addRule(RelativeLayout.CENTER_VERTICAL);
        searchParams.setMarginStart(size5);
        searchParams.setMarginEnd(size5);
        centerSearch.addView(searchEditText, searchParams);
    }

    /**
     * 初始化右边的view
     *
     * @param context 上下文
     */
    private void initRightViews(Context context) {
        LayoutParams rightParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (rightType == BarType.TEXT.ordinal()) {
            rightView = titleBarTextView(context, rightText, rightTextColor, rightTextSize,
                    (Gravity.END | Gravity.CENTER_VERTICAL), 0, 0);
        }

        if (rightType == BarType.IMAGE.ordinal()) {
            rightParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            rightView = titleBarImageView(context, rightImageResource);
        }

        if (rightType == BarType.CUSTOM.ordinal()) {
            rightView = titleBarCustomView(context, rlMain, rightCustomViewId);
        }

        addViewToMain(rightView, rightParams);

        ViewExtKt.click(rightView, () -> {
            if (titleBarListener != null) {
                titleBarListener.rightClick(rightView);
            }
            return null;
        });
    }

    /**
     * 标题栏文本view
     *
     * @param context         上下文对象
     * @param text            文本
     * @param textColor       文本颜色
     * @param textSize        文本大小
     * @param gravity         位置
     * @param drawablePadding 图片与文字间距
     * @param drawable        图片资源id
     * @return 文本view
     */
    private TextView titleBarTextView(
            Context context,
            String text,
            int textColor,
            float textSize,
            int gravity,
            int drawablePadding,
            int drawable
    ) {
        final TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setGravity(gravity);
        textView.setSingleLine();
        textView.setPadding(size15, 0, size15, 0);
        if (drawable != 0) {
            textView.setCompoundDrawablePadding(drawablePadding);
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0);
        }
        return textView;
    }

    /**
     * 标题栏图片view
     *
     * @param context       上下文
     * @param imageResource 图片资源id
     * @return 图片view
     */
    private ImageButton titleBarImageView(Context context, int imageResource) {
        final ImageButton imageButton = new ImageButton(context);
        imageButton.setId(View.generateViewId());
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setImageResource(imageResource);
        imageButton.setPadding(size15, 0, size15, 0);
        return imageButton;
    }

    /**
     * 标题栏自定义view
     *
     * @param context 上下文
     * @param root    parent view
     * @param viewId  自定义view id
     * @return 自定义 view
     */
    private View titleBarCustomView(Context context, ViewGroup root, int viewId) {
        final View customView =
                getLayoutInflater(context).inflate(viewId, root, false);
        if (customView.getId() == View.NO_ID) {
            customView.setId(View.generateViewId());
        }
        return customView;
    }

    /**
     * 添加view到主view
     *
     * @param view   需要添加的view
     * @param params 需要添加view的布局参数
     */
    private void addViewToMain(View view, LayoutParams params) {
        if (null != view && null != params) {
            rlMain.addView(view, params);
        }
    }

    private LayoutInflater getLayoutInflater(Context context) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        return layoutInflater;
    }

    public View getLeftView() {
        return leftView;
    }

    public View getRightView() {
        return rightView;
    }

    public View getCenterView() {
        return centerView;
    }

    /**
     * 动态设置左视图
     *
     * @param type
     */
    public void setLeftViewType(int type) {
        this.leftType = type;
        initLeftViews(getContext());
    }

    public void setLeftTextView(String text) {
        leftType = BarType.TEXT.ordinal();
        leftText = text;

        initLeftViews(getContext());
    }

    public void setLeftTextView(String text, int textColor, int textSize) {
        rightType = BarType.TEXT.ordinal();
        rightText = text;
        rightTextColor = textColor;
        rightTextSize = textSize;

        initRightViews(getContext());
    }

    public void setTitleText(String text) {
        this.centerText = text;

        if (centerTextView != null && !StringUtil.isSpace(this.centerText)) {
            centerTextView.setText(text);
        }
    }

    public void setRightTextView(String text, int textColor, int textSize) {
        rightType = BarType.TEXT.ordinal();
        rightText = text;
        rightTextColor = textColor;
        rightTextSize = textSize;

        initRightViews(getContext());
    }

    public void hideRightView() {
        rightType = BarType.NONE.ordinal();

        if (null != rlMain && null != rightView) {
            rlMain.removeView(rightView);
        }
    }

    public void setSearchHintText(String hint) {
        if (null != searchEditText) {
            searchEditText.setHint(hint);
        }
    }

    public void setSubTitleText(String text) {
        this.centerSubText = text;

        if (centerSubTextView != null) {
            centerSubTextView.setText(text);
        }
    }

    public void showProgress() {
        centerProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        centerProgress.setVisibility(View.GONE);
    }

    public interface OnTitleBarListener {
        /**
         * 左边点击事件
         *
         * @param view 左边视图
         */
        void leftClick(View view);

        /**
         * 双击点击事件
         *
         * @param view 主视图
         */
        void doubleClick(View view);

        /**
         * 搜索
         *
         * @param searchWorld 搜索关键字
         */
        void search(String searchWorld);

        /**
         * 搜索-语音点击事件
         *
         * @param view 搜索视图语音按钮
         */
        void voiceIconClick(View view);

        /**
         * 搜索-删除点击事件
         *
         * @param view 搜索视图删除按钮
         */
        void deleteIconClick(View view);

        /**
         * 右边点击事件
         *
         * @param view 右边视图
         */
        void rightClick(View view);
    }
}
