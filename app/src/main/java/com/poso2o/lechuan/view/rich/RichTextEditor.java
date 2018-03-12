package com.poso2o.lechuan.view.rich;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.util.FileUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 *
 * @author xmuSistone
 */
@SuppressLint("InflateParams")
public class RichTextEditor extends InterceptLinearLayout {

    /**
     * editText常规padding是10dp
     */
    private static final int EDIT_PADDING = 10;

    /**
     * 第一个EditText的paddingTop值
     */
    private static final int EDIT_FIRST_PADDING_TOP = 10;

    /**
     * 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
     */
    private int mViewTagIndex = 1;

    /**
     * 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
     */
    private LinearLayout mAllLayout;

    /**
     * View填充器
     */
    private LayoutInflater mInflater;

    /**
     * 所有EditText的软键盘监听器
     */
    private OnKeyListener mKeyListener;

    /**
     * 图片右上角红叉按钮监听器
     */
    private OnClickListener mBtnListener;

    /**
     * 控件数量监听器
     */
    private OnChildCountListener mOnChildCountListener;

    /**
     * 所有图片点击监听
     */
    private OnClickImageListener mOnClickImageListener;

    /**
     * 所有EditText的焦点监听listener
     */
    private OnFocusChangeListener mFocusListener;

    /**
     * 最近被聚焦的EditText
     */
    private EditText mLastFocusEdit;

    /**
     * 只在图片View添加或remove时，触发transition动画
     */
    private LayoutTransition mTransition;

    private int mEditNormalPadding = 0;

    private int mDisappearingImageIndex = 0;

    private Context mContext;

    public interface LayoutClickListener {
        void layoutClick();
    }

    /**
     * 布局被点击监听
     */
    private LayoutClickListener mLayoutClickListener;

    public void setLayoutClickListener(LayoutClickListener mLayoutClickListener) {
        this.mLayoutClickListener = mLayoutClickListener;
    }

    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void setIntercept(boolean b) {
        super.setIntercept(b);
    }

    private void init() {
        mInflater = LayoutInflater.from(mContext);

        // 1. 初始化allLayout
        mAllLayout = this;
        mAllLayout.setOrientation(LinearLayout.VERTICAL);
        mAllLayout.setBackgroundColor(Color.WHITE);
        setupLayoutTransitions();

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        mKeyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 3. 图片叉掉处理
        mBtnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
            }
        };

        mFocusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLastFocusEdit = (EditText) v;
                }
            }
        };

        LayoutParams firstEditParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mEditNormalPadding = dip2px(EDIT_PADDING);
        EditText firstEdit = createEditText("", dip2px(EDIT_FIRST_PADDING_TOP));
        mAllLayout.addView(firstEdit, firstEditParam);
        mLastFocusEdit = firstEdit;
        // 给第一个EditText设置输入监听
        firstEdit.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                // 如果只有一个控件，则根据控件的输入变化调用回调函数，多个控件时不调用
                if (mOnChildCountListener != null && mAllLayout.getChildCount() == 1) {
                    mOnChildCountListener.change(1);
                }
            }
        });
    }

    public void editTextRequestFocus() {
        View view = mAllLayout.getChildAt(getChildNum() - 1);
        if (view != null && view instanceof EditText) {
            view.requestFocus();
            KeyboardUtils.showSoftInput(view);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mLayoutClickListener != null)
                    mLayoutClickListener.layoutClick();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = mAllLayout.indexOfChild(editTxt);
            View preView = mAllLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    mAllLayout.setLayoutTransition(null);
                    mAllLayout.removeView(editTxt);
                    mAllLayout.setLayoutTransition(mTransition); // 恢复transition动画

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    mLastFocusEdit = preEdit;
                }
            }
        }
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(View view) {
        if (!mTransition.isRunning()) {
            mDisappearingImageIndex = mAllLayout.indexOfChild(view);
            mAllLayout.removeView(view);
        }
    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) mInflater.inflate(R.layout.richtextedit_textview, null);
        editText.setOnKeyListener(mKeyListener);
        editText.setTag(mViewTagIndex++);
        editText.setPadding(mEditNormalPadding, paddingTop, mEditNormalPadding, 0);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(mFocusListener);
        return editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.richtextedit_imageview, null);
        layout.setTag(mViewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(mBtnListener);
        return layout;
    }

    /**
     * 根据下标删除图片 TODO
     *
     * @author Zheng Jie Dong
     * @date 2016-11-19
     */
    public void removeImage(int index) {
        View view = mAllLayout.getChildAt(index);
        if (view instanceof RelativeLayout) {
            RelativeLayout parentView = (RelativeLayout) view;
            onImageCloseClick(parentView);
        }
    }

    /**
     * 根据下标修改图片
     *
     * @author Zheng Jie Dong
     * @date 2016-11-19
     */
    public void updateImage(int index, String imagePath, String imageUrl) {
        Bitmap bmp = getScaledBitmap(imagePath, getWidth());
        View view = mAllLayout.getChildAt(index);
        DataImageView imageView = (DataImageView) view.findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setBitmap(bmp);
        imageView.setImageUrl(imageUrl);
        imageView.setAbsolutePath(imagePath);
        imageView.setOnClickListener(mOnClickListener);

        // 调整imageView的高度
        int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
        imageView.setLayoutParams(lp);
    }

    /**
     * 根据绝对路径添加view
     *
     * @param imagePath
     */
    public void insertImage(String imagePath, String imageUrl, Callback callback) {
        Bitmap bmp = getScaledBitmap(imagePath, getWidth());
        insertImage(bmp, imagePath, imageUrl, callback);
    }

    /**
     * 根据绝对路径添加view
     *
     * @param imagePath
     */
    public void insertImage(String imagePath, String imageUrl) {
        Bitmap bmp = getScaledBitmap(imagePath, getWidth());
        insertImage(bmp, imagePath, imageUrl);
    }

    /**
     * 插入文字
     *
     * @param text
     */
    public void insertText(String text) {
        View itemView = mAllLayout.getChildAt(mAllLayout.getChildCount() - 1);
        if (itemView instanceof EditText) {
            EditText item = (EditText) itemView;
            if (item.getText() == null || item.getText().length() < 1)
                item.setText(text);
            else
                addEditTextAtIndex(-1, text);
        }
    }

    /**
     * 插入一张图片
     */
    private void insertImage(Bitmap bitmap, String imagePath, String imageUrl) {
        insertImage(bitmap, imagePath, imageUrl, null);
    }

    /**
     * 插入一张图片
     */
    private void insertImage(Bitmap bitmap, String imagePath, String imageUrl, Callback callback) {
        // 获取当前得到焦点的EditText文本
        String lastEditStr = mLastFocusEdit.getText().toString();
        // 获取当前光标位置
        int cursorIndex = mLastFocusEdit.getSelectionStart();
        // 获取光标前面的文本
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        // 获取最后一个获取焦点的EditText下标
        int lastEditIndex = mAllLayout.indexOfChild(mLastFocusEdit);
        // 将裁减后的文本设置到原来的EditText
        mLastFocusEdit.setText(editStr1);
        // 获取光标后的文本
        String editStr2 = lastEditStr.substring(cursorIndex).trim();
        // 添加一个EditText，将光标后的文本设置进去
        addEditTextAtIndex(lastEditIndex + 1, editStr2);
        // 添加一个ImageView
        addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath, imageUrl, callback);
        // 设置最后获取焦点的EditText获取焦点
        // lastFocusEdit.requestFocus();
        // lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
        hideKeyBoard();
    }

    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("", getResources().getDimensionPixelSize(R.dimen.richtextedit_padding_top));
        editText2.setText(editStr);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        mAllLayout.setLayoutTransition(null);
        mAllLayout.addView(editText2, index);
        mAllLayout.setLayoutTransition(mTransition); // remove之后恢复transition动画
        // 设置EditText外边距
        MarginLayoutParams marginParams = new MarginLayoutParams(editText2.getLayoutParams());
        marginParams.setMargins(0, dip2px(5), 0, dip2px(5));
        LayoutParams layoutParams = new LayoutParams(marginParams);
        editText2.setLayoutParams(layoutParams);
        // 设置EditText获取焦点
        editText2.requestFocus();
        mLastFocusEdit = editText2;
        if (mOnChildCountListener != null) {
            mOnChildCountListener.change(mAllLayout.getChildCount());
        }
    }

    /**
     * 在特定位置添加ImageView
     *
     * @param callback
     */
    private void addImageViewAtIndex(final int index, Bitmap bmp, String imagePath, String imageUrl, final Callback callback) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setBitmap(bmp);
        imageView.setImageUrl(imageUrl);
        imageView.setAbsolutePath(imagePath);
        imageView.setOnClickListener(mOnClickListener);

        // 调整imageView的高度
        int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
        imageView.setLayoutParams(lp);

        // onActivityResult无法触发动画，此处post处理
//        mAllLayout.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                mAllLayout.addView(imageLayout, index);
//                if (callback != null) {
//                    callback.complete();
//                }
//            }
//        }, 200);
        mAllLayout.addView(imageLayout, index);
        if (callback != null) {
            callback.complete();
        }
        if (mOnChildCountListener != null) {
            mOnChildCountListener.change(mAllLayout.getChildCount());
        }
    }

    /**
     * 所有图片的点击事件
     */
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 获取当前图片在布局中的位置
            int position = mAllLayout.indexOfChild((View) v.getParent());
            if (mOnClickImageListener != null) {
                mOnClickImageListener.onClickImage(position, (DataImageView) v);
            }
        }
    };

    /**
     * 插入网络图片
     *
     * @param url
     */
    public void insertImageByURL(String url) {
        if (url == null)
            return;
        final RelativeLayout imageLayout = createImageLayout();
        final DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.edit_imageView);
//		imageView.setImageResource(R.drawable.logo);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        mAllLayout.addView(imageLayout);
        addEditTextAtIndex(-1, "");
        ImageLoader.getInstance().displayImage(url, imageView, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                String path = FileUtils.savaRichTextImage(imageUri, loadedImage);
                imageView.setImageBitmap(loadedImage);
                imageView.setBitmap(loadedImage);
                imageView.setAbsolutePath(path);
                int imageHeight = getWidth() * loadedImage.getHeight() / loadedImage.getWidth();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        });
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    private Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransition = new LayoutTransition();
        mAllLayout.setLayoutTransition(mTransition);
        mTransition.addTransitionListener(new TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning() && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    // mergeEditText();
                }
            }
        });
        mTransition.setDuration(300);
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    @SuppressWarnings("unused")
    private void mergeEditText() {
        View preView = mAllLayout.getChildAt(mDisappearingImageIndex - 1);
        View nextView = mAllLayout.getChildAt(mDisappearingImageIndex);
        if (preView != null && preView instanceof EditText && null != nextView && nextView instanceof EditText) {
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();
            String mergeText = "";
            if (str2.length() > 0) {
                mergeText = str1 + "\n" + str2;
            } else {
                mergeText = str1;
            }

            mAllLayout.setLayoutTransition(null);
            mAllLayout.removeView(nextEdit);
            preEdit.setText(mergeText);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
            mAllLayout.setLayoutTransition(mTransition);
        }
    }

    /**
     * dp和pixel转换
     *
     * @param dipValue dp值
     * @return 像素值
     */
    public int dip2px(float dipValue) {
        float m = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 获取子控件个数
     */
    public int getChildNum() {
        return mAllLayout.getChildCount();
    }

    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> dataList = new ArrayList<EditData>();
        int num = mAllLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = mAllLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
                itemData.imageUrl = item.getImageUrl();
                itemData.bitmap = item.getBitmap();
            }
            dataList.add(itemData);
        }

        return dataList;
    }

    /**
     * 获取指定位置view距离顶部的高度 TODO 测试中
     *
     * @param index
     * @return
     * @author Zheng Jie Dong
     * @date 2016-11-19
     */
    public int getChildTop(int index) {
        int top = 0;
        if (mAllLayout.getChildCount() > index) {
            for (int i = 0; i < index; i++) {
                View view = mAllLayout.getChildAt(i);
                if (view != null) {
                    top += view.getHeight();
                }
            }
        }
        return top;
    }

    public HashMap<String, Object> getRichEditData() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        StringBuilder editTextSB = new StringBuilder();
        List<String> imgUrls = new ArrayList<String>();
        char separator = 26;
        int num = mAllLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = mAllLayout.getChildAt(index);
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                editTextSB.append(item.getText().toString());
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(R.id.edit_imageView);
                imgUrls.add(item.getAbsolutePath());
                editTextSB.append(separator);
            }
        }
        data.put("text", editTextSB);
        data.put("imgUrls", imgUrls);

        return data;
    }

    /**
     * 设置点击图片监听
     */
    public void setOnClickImageListener(OnClickImageListener onClickImageListener) {
        mOnClickImageListener = onClickImageListener;
    }

    /**
     * 设置子控件数量监听
     *
     * @param onChildCountListener
     */
    public void setOnChildCountListener(OnChildCountListener onChildCountListener) {
        mOnChildCountListener = onChildCountListener;
    }

    public class EditData {
        public String inputStr;
        public String imagePath;
        public String imageUrl;
        public Bitmap bitmap;
    }

    public interface Callback {
        void complete();
    }

    /**
     * 子控件数量变更回调
     *
     * @author Zheng Jie Dong
     * @date 2016-11-18
     */
    public interface OnChildCountListener {
        void change(int count);
    }

    /**
     * 点击图片回调
     *
     * @author Zheng Jie Dong
     * @date 2016-11-18
     */
    public interface OnClickImageListener {
        void onClickImage(int position, DataImageView view);
    }

    /**
     * 是否有内容
     *
     * @return
     * @author Zheng Jie Dong
     * @date 2016-11-28
     */
    public boolean isContent() {
        if (mAllLayout.getChildCount() > 1) {
            return true;
        }
        if (mLastFocusEdit != null) {
            String text = mLastFocusEdit.getText().toString();
            return TextUtil.isNotEmpty(text);
        }
        return false;
    }
}
