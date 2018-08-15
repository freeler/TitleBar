package com.freeler.titlebar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * 类描述：标题栏
 * 创建人：xuzeyang
 * 创建时间：2018/08/08
 */
@SuppressWarnings("all")
public class TitleBar extends ViewGroup implements View.OnClickListener {

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private View view;

    public interface TitleBarAction {
        void execute();
    }

    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;//默认中间字体大小
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;//默认副标题文字大小
    private static final int DEFAULT_ACTION_TEXT_SIZE = 16;//默认左右两边字体大小
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 44;//默认高度

    private int mHeight;//TitleBar高度
    private int mStatusBarHeight;//StatusBar高度
    private int mScreenWidth;//屏幕宽度
    private int mPaddingBottom;//与底部边距

    private int mActionPaddingLeft;//右边内容padding值
    private int mActionPaddingRight;//右边内容padding值
    private int mActionTextColor;//右边文字颜色

    private int mOutPaddingLeft;//左右文字padding值
    private int mOutPaddingRight;//左右文字padding值

    private TextView mLeftText;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mDividerView;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mActionPaddingLeft = dip2px(16);
        mActionPaddingRight = dip2px(8);
        mOutPaddingLeft = dip2px(16);
        mOutPaddingRight = dip2px(16);
        mHeight = dip2px(DEFAULT_TITLE_BAR_HEIGHT);
        mPaddingBottom = dip2px(10);
//        // 默认titleBar为白色
//        setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        initView(context);
    }

    private void initView(Context context) {
        mLeftText = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mCenterLayout.setPadding(0, 0, 0, mPaddingBottom * 2 / 3);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);
//        mDividerView.setBackgroundColor(ContextCompat.getColor(context, R.color.diverColor));

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        mLeftText.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        mLeftText.setSingleLine();
        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mOutPaddingLeft, 0, mOutPaddingRight, 0);

        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);

        mCenterLayout.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);

        mSubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mRightLayout.setPadding(mOutPaddingLeft, 0, mOutPaddingRight, 0);

        addView(mLeftText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    }

    public TitleBar.Action defaultWhiteStyle(Context context, String left, String title, String right, OnClickListener onLeftClick, TitleBarAction rightAction) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        setLeftText(left);
        setLeftTextSize(px2dip(context, getResources().getDimension(R.dimen.text_size_small)));
        setLeftTextColor(ContextCompat.getColor(context, R.color.themeColor));
        if (null != onLeftClick) {
            setLeftClickListener(onLeftClick);
        }
        setTitle(title);
        setTitleColor(ContextCompat.getColor(context, R.color.black));
        setTitleSize(DEFAULT_MAIN_TEXT_SIZE);
        TitleBar.Action action = null;
        if (right != null && right.length() != 0) {
            action = new TitleBar.TextAction(right) {
                @Override
                public void performAction(View view) {
                    if (null != rightAction) {
                        rightAction.execute();
                    }
                }
            };
            addAction(action);
            TextView txtRight = (TextView) getViewByAction(action);
            txtRight.setTextColor(ContextCompat.getColor(context, R.color.themeColor));
            txtRight.setTextSize(px2dip(context, getResources().getDimension(R.dimen.text_size_small)));
        }
        return action;
    }

    public TitleBar.Action defaultGrayStyle(Context context, String left, String title, String right, TitleBarAction rightAction) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.actionBarColor));
        setTitle(title);
        setLeftText(left);
        setTitleColor(ContextCompat.getColor(context, R.color.white));
        setTitleSize(DEFAULT_MAIN_TEXT_SIZE);
        TitleBar.Action action = null;
        if (right != null && right.length() != 0) {
            action = new TitleBar.TextAction(right) {
                @Override
                public void performAction(View view) {
                    if (null != rightAction) {
                        rightAction.execute();
                    }
                }
            };
            addAction(action);
            TextView txtRight = (TextView) getViewByAction(action);
            txtRight.setTextColor(ContextCompat.getColor(context, R.color.white));
            txtRight.setTextSize(px2dip(context, getResources().getDimension(R.dimen.text_size_small)));
        }

        return action;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public TitleBar hideStateBar(boolean b) {
        if (b) {
            mStatusBarHeight = 0;
        } else {
            mStatusBarHeight = getStatusBarHeight();
        }
        return this;
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public TitleBar setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
        return this;
    }

    /**
     * 设置左边文字
     *
     * @param title
     */
    public TitleBar setLeftText(CharSequence title) {
        mLeftText.setText(title);
        return this;
    }

    /**
     * 设置左边文字
     *
     * @param resId string资源id
     */
    public TitleBar setLeftText(int resId) {
        mLeftText.setText(resId);
        return this;
    }

    /**
     * 获取左边文字
     */
    public TextView getLeftText() {
        return mLeftText;
    }

    /**
     * 设置左侧文字大小
     *
     * @param size sp
     */
    public TitleBar setLeftTextSize(float size) {
        mLeftText.setTextSize(size);
        return this;
    }

    /**
     * 设置左侧文字颜色
     *
     * @param color 颜色资源id
     */
    public TitleBar setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
        return this;
    }

    /**
     * 设置左侧文字是否可见
     *
     * @param visible true 可见，false 隐藏
     */
    public TitleBar setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }


    /**
     * 设置左边文字左侧图片
     *
     * @param resId 图片资源id
     */
    public TitleBar setLeftImageResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    /**
     * 设置左边文字左侧图片
     *
     * @param resId   图片资源id
     * @param padding 距离
     */
    public TitleBar setLeftImageResource(int resId, int padding) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        mLeftText.setCompoundDrawablePadding(padding);
        return this;
    }

    /**
     * 设置左边文字左侧图片
     *
     * @param resId 图片资源id
     */
    public TitleBar setLeftTextImageRightResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        return this;
    }

    /**
     * 设置左边点击
     *
     * @param l 接口
     */
    public TitleBar setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
        return this;
    }

    /**
     * 设置中间文字
     *
     * @param title 标题
     */
    public TitleBar setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), " " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
        return this;
    }

    /**
     * 设置中间文字
     *
     * @param title       标题
     * @param subTitle    副标题
     * @param orientation 对其方式
     */
    private TitleBar setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置中间文字
     *
     * @param resId string资源id
     */
    public TitleBar setTitle(int resId) {
        setTitle(getResources().getString(resId));
        return this;
    }

    /**
     * 获取中间文字
     */
    public TextView getTitleText() {
        return mCenterText;
    }


    /**
     * 设置中间点击
     *
     * @param listener 接口
     */
    public TitleBar setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置中间文字颜色
     *
     * @param resId color资源id
     */
    public TitleBar setTitleColor(int resId) {
        mCenterText.setTextColor(resId);
        return this;
    }

    /**
     * 设置中间文字大小
     *
     * @param size sp
     */
    public TitleBar setTitleSize(float size) {
        mCenterText.setTextSize(size);
        return this;
    }

    /**
     * 设置中间文字背景
     *
     * @param resId 图片资源id
     */
    public TitleBar setTitleBackground(int resId) {
        mCenterText.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置副标题颜色
     *
     * @param resId 颜色资源id
     */
    public TitleBar setSubTitleColor(int resId) {
        mSubTitleText.setTextColor(resId);
        return this;
    }

    /**
     * 设置自定义标题
     *
     * @param titleView view
     */
    public TitleBar setCustomTitle(View titleView) {
        mCenterText.setVisibility(View.GONE);
        addView(titleView);
        return this;
    }

    /**
     * 设置自定义标题
     *
     * @param titleView view
     */
    public TitleBar setCustomCenterTitle(View titleView) {
        mCenterText.setVisibility(View.GONE);
        mCenterLayout.addView(titleView);
        return this;
    }

    /**
     * 设置分割线
     *
     * @param drawable drawable
     */
    public TitleBar setDivider(Drawable drawable) {
        mDividerView.setBackground(drawable);
        return this;
    }

    /**
     * 设置分割线背景色
     *
     * @param color color
     */
    public TitleBar setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置分割线高度
     *
     * @param dividerHeight
     */
    public TitleBar setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
        return this;
    }

    /**
     * 设置中间文字右侧图片
     *
     * @param resId 图片资源id
     */
    public TitleBar setCenterRightImageResource(int resId) {
        mCenterText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        return this;
    }

    /**
     * 设置右侧文字
     *
     * @param s s
     */
    public void setRightText(CharSequence s) {
        if (view instanceof TextView) {
            ((TextView) view).setText(s);
        }
    }

    /**
     * 设置右侧文字
     *
     * @param resId string资源id
     */
    public void setRightText(int resId) {
        if (view instanceof TextView) {
            ((TextView) view).setText(resId);
        }
    }

    /**
     * 设置右侧文字颜色
     *
     * @param colorResId 颜色资源id
     */
    public TitleBar setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
        return this;
    }


    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public TitleBar addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
        return this;
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public TitleBar addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        addAction(action, index);
        return this;
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        view = null;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }

        view.setPadding(mActionPaddingLeft, 0, mActionPaddingRight, 0);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int oldHeightMeasureSpec = heightMeasureSpec;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftText, widthMeasureSpec, oldHeightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, oldHeightMeasureSpec);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftText.layout(0, mHeight - mPaddingBottom - mLeftText.getMeasuredHeight() + mStatusBarHeight, mLeftText.getMeasuredWidth(), mHeight - mPaddingBottom + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mHeight - mPaddingBottom - mRightLayout.getMeasuredHeight() + mStatusBarHeight,
                mScreenWidth, mHeight - mPaddingBottom + mStatusBarHeight);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftText.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     *
     * @return 高度
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {

        String getText();

        int getDrawable();

        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;

        protected ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        protected TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }

}
