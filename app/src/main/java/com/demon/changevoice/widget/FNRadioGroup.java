package com.demon.changevoice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.demon.changevoice.R;

/**
 * @author DeMon
 * Created on 2021/5/7.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class FNRadioGroup extends ViewGroup {

    /** 没有ID */
    private final static int NO_ID = -1;

    /** 当前选中的子控件ID */
    private int mCheckedId = NO_ID;

    /** 子控件选择改变监听器 */
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;

    /** 为true时，不处理子控件选择事件 */
    private boolean mProtectFromCheckedChange = false;

    /** 选择改变监听器 */
    private OnCheckedChangeListener mOnCheckedChangeListener;

    /** 子控件添加移除监听器 */
    private PassThroughHierarchyChangeListener mPassThroughListener;

    /** 子控件左边距 */
    private int childMarginLeft = 0;

    /** 子控件右边距 */
    private int childMarginRight = 0;

    /** 子控件上边距 */
    private int childMarginTop = 0;

    /** 子控件下边距 */
    private int childMarginBottom = 0;

    /** 子空间高度 */
    private int childHeight;

    /**
     * 默认构造方法
     */
    public FNRadioGroup(Context context) {
        super(context);
        init();
    }

    /**
     * XML实例构造方法
     */
    public FNRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取自定义属性checkedButton
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FNRadioGroup) ;
        // 读取默认选中id
        int value = attributes.getResourceId(R.styleable.FNRadioGroup_checkedButtonId, NO_ID);
        if (value != NO_ID) {
            // 如果为设置checkButton属性，保持默认值NO_ID
            mCheckedId = value;
        }
        // 读取子控件左边距
        childMarginLeft = attributes.getLayoutDimension(R.styleable.FNRadioGroup_childMarginLeft, childMarginLeft);
        if (childMarginLeft < 0) {
            childMarginLeft = 0;
        }
        // 读取子控件右边距
        childMarginRight = attributes.getLayoutDimension(R.styleable.FNRadioGroup_childMarginRight, childMarginRight);
        if (childMarginRight < 0) {
            childMarginRight = 0;
        }
        // 读取子控件上边距
        childMarginTop = attributes.getLayoutDimension(R.styleable.FNRadioGroup_childMarginTop, childMarginTop);
        if (childMarginTop < 0) {
            childMarginTop = 0;
        }
        // 读取子控件下边距
        childMarginBottom = attributes.getLayoutDimension(R.styleable.FNRadioGroup_childMarginBottom, childMarginBottom);
        if (childMarginBottom < 0) {
            childMarginBottom = 0;
        }
        attributes.recycle();
        // 调用二级构造
        init();
    }

    /**
     * 设置子控件边距
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 下边距
     */
    public void setChildMargin(int l, int t, int r, int b) {
        childMarginTop = t;
        childMarginLeft = l;
        childMarginRight = r;
        childMarginBottom = b;
    }

    /**
     * 选中子控件为id的组件为选中项
     */
    public void check(int id) {
        if (id != -1 && (id == mCheckedId)) {
            return;
        }
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }
        if (id != -1) {
            setCheckedStateForView(id, true);
        }
        setCheckedId(id);
    }

    /**
     * 获取当前选中子控件的id
     * @return 当前选中子控件的id
     */
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * 清除当前选中项
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * 设置选中改变监听
     * @param listener 选中改变监听
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * 布局参数
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        /**
         * XML构造
         * @param c 页面引用
         * @param attrs XML属性集
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
        /**
         * 默认构造
         * @param w 宽度
         * @param h 高度
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }
        /**
         * 父传递构造
         * @param p ViewGroup.LayoutParams对象
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }
        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }
            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    /**
     * 项目选中改变监听器
     */
    public interface OnCheckedChangeListener {
        /**
         * 选中项目改变回调
         * @param group 组引用
         * @param checkedId 改变的ID
         */
        void onCheckedChanged(FNRadioGroup group, int checkedId);
    }

    /********************************************私有方法*******************************************/

    /**
     * 二级构造方法
     */
    private void init() {

        // 初始化子控件选择监听
        mChildOnCheckedChangeListener = new CheckedStateTracker();

        // 初始化子控件添加移除监听器
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        // 设置子控件添加移除监听器
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams params = getLayoutParams();
        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();
        // 获取视图宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 计算Tag最大高度(以此作为所有tag的高度)
        childHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            int cmh = getChildAt(i).getMeasuredHeight();
            if (cmh > childHeight) {
                childHeight = cmh;
            }
        }
        // 计算本视图
        if (params.height != LayoutParams.WRAP_CONTENT) {
            // 非内容匹配的情况下
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            // 计算视图高度
            int currentHeight = pt;
            int currentWidth = pl;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                // 本视图加入行中是否会超过视图宽度
                if (currentWidth + childWidth + childMarginLeft + childMarginRight > width - pl - pr) {
                    // 累加行高读
                    currentHeight += childMarginTop + childMarginBottom + childHeight;
                    currentWidth = pl;
                    currentWidth += childMarginLeft + childMarginRight + childWidth;
                } else {
                    // 累加行宽度
                    currentWidth += childMarginLeft + childMarginRight + childWidth;
                }
            }
            currentHeight += childMarginTop + childMarginBottom + childHeight + pb;
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(currentHeight, MeasureSpec.EXACTLY));
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();
        int width = r - l;
        // 布局Tag视图
        int currentHeight = pt;
        int currentWidth = pl;
        for (int i=0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            // 本视图加入行中是否会超过视图宽度
            if (currentWidth + childWidth + childMarginLeft + childMarginRight > width - pl - pr) {
                // 累加行高读
                currentHeight += childMarginTop + childMarginBottom + childHeight;
                currentWidth = pl;
                // 布局视图
                child.layout(currentWidth + childMarginLeft, currentHeight + childMarginTop,
                        currentWidth + childMarginLeft + childWidth, currentHeight + childMarginTop + childHeight);
                currentWidth += childMarginLeft + childMarginRight + childWidth;
            } else {
                // 布局视图
                child.layout(currentWidth + childMarginLeft, currentHeight + childMarginTop,
                        currentWidth + childMarginLeft + childWidth, currentHeight + childMarginTop + childHeight);
                // 累加行宽度
                currentWidth += childMarginLeft + childMarginRight + childWidth;
            }
        }
    }
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // 设置子空间添加移除监听
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mCheckedId != NO_ID) {
            // 如果读取到选中项,设置并存储选中项
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }

        super.addView(child, index, params);
    }
    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FNRadioGroup.LayoutParams(getContext(), attrs);
    }
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof RadioGroup.LayoutParams;
    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(RadioGroup.class.getName());
    }
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(RadioGroup.class.getName());
    }
    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;
            int id = buttonView.getId();
            setCheckedId(id);
        }
    }
    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
        public void onChildViewAdded(View parent, View child) {
            if (parent == FNRadioGroup.this && child instanceof RadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = generateViewId();
                    child.setId(id);
                }
                ((RadioButton) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }
        public void onChildViewRemoved(View parent, View child) {
            if (parent == FNRadioGroup.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeListener(null);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
