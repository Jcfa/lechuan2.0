package com.poso2o.lechuan.activity.publish;

import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.fragment.publish.ImageTextFragment;
import com.poso2o.lechuan.fragment.publish.SelectArticleFragment;
import com.poso2o.lechuan.fragment.publish.ShowImageFragment;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 发布
 * Created by Jaydon on 2017/11/30.
 */
public class PublishActivity extends BaseActivity implements OnClickListener {

    /**
     * 发布类型
     */
    public static final String PUBLISH_TYPE = "publish_type";

    public static final int SHOW_IMAGE = 0;// 晒图
    public static final int SELECT_ARTICLE = 1;// 选文
    public static final int IMAGE_TEXT = 2;// 图文

    /**
     * 标题
     */
    private TextView tv_title;

    /**
     * 半透明遮罩
     */
    private View publish_shade;

    /**
     * 发布按钮
     */
    private TextView publish_publish;

    /**
     * 搜索按钮
     */
    private ImageView publish_search;

    /**
     * 搜索编辑框
     */
    private EditText publish_search_edit;

    /**
     * 搜索清空按钮
     */
    private View publish_search_delete;

    /**
     * 晒图
     */
    private ShowImageFragment showImageFragment;

    /**
     * 选文发布
     */
    private SelectArticleFragment selectArticleFragment;

    /**
     * 图文编辑
     */
    private ImageTextFragment imageTextFragment;

    /**
     * 下拉菜单
     */
    private PopupWindow pwMenu;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initView() {
        tv_title = findView(R.id.tv_title);
        tv_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_arrow_down, 0);
        publish_shade = findView(R.id.publish_shade);
        publish_publish = findView(R.id.publish_publish);
        publish_search = findView(R.id.publish_search);
        publish_search_edit = findView(R.id.publish_search_edit);
        publish_search_delete = findView(R.id.publish_search_delete);
    }

    @Override
    protected void initData() {
        showImageFragment = new ShowImageFragment();
        selectArticleFragment = new SelectArticleFragment();
        imageTextFragment = new ImageTextFragment();
        // 显示上次发布类型
        int type = SharedPreferencesUtils.getInt(PUBLISH_TYPE, SHOW_IMAGE);
        switch (type) {
            case SHOW_IMAGE:
                setTitle(R.string.show_image);
                addFragment(R.id.publish_group, showImageFragment);
                break;

            case SELECT_ARTICLE:
                setTitle(R.string.select_publish);
                addFragment(R.id.publish_group, selectArticleFragment);
                break;

            case IMAGE_TEXT:
                setTitle(R.string.image_text);
                addFragment(R.id.publish_group, imageTextFragment);
                break;
        }
    }

    @Override
    protected void initListener() {
        findView(R.id.iv_back).setOnClickListener(this);
        tv_title.setOnClickListener(this);
        publish_publish.setOnClickListener(this);
        publish_search.setOnClickListener(this);
        publish_search_delete.setOnClickListener(this);

        publish_search_edit.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                publish_search_delete.setVisibility(s.length() > 0 ? VISIBLE : GONE);
            }
        });

        publish_search_edit.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    selectArticleFragment.search(publish_search_edit.getText().toString());
                    publish_search_edit.clearFocus();
                    KeyboardUtils.hideSoftInput(activity);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (showImageFragment.isVisible()) {
            if (showImageFragment.isRedbagManagerShow()) {
                showImageFragment.hideRedbagManager();
                return;
            }
            if (showImageFragment.isContentNotNull()) {
                showAbandonEditDialog();
                return;
            }
        } else if (imageTextFragment.isVisible()) {
            if (imageTextFragment.isRedbagManagerShow()) {
                imageTextFragment.hideRedbagManager();
                return;
            }
            if (imageTextFragment.isContentNotNull()) {
                showAbandonEditDialog();
                return;
            }
        } else if (selectArticleFragment.isVisible()) {
            if (publish_search_edit.getVisibility() == VISIBLE) {
                clearSearchContent();
                return;
            }
        }

        super.onBackPressed();
    }

    /**
     * 显示放弃编辑对话框
     */
    private void showAbandonEditDialog() {
        CommonDialog dialog = new CommonDialog(this, R.string.dialog_abandon_edit_content);
        dialog.show(new CommonDialog.OnConfirmListener() {

            @Override
            public void onConfirm() {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_title:
                showPopupWindow();
                showImageFragment.hideRedbagManager();
                break;

            case R.id.publish_publish:
                if (showImageFragment.isVisible()) {
                    showImageFragment.publish();
                } else if (imageTextFragment.isVisible()) {
                    imageTextFragment.publish();
                }
                break;

            case R.id.publish_search:
                if (publish_search_edit.getVisibility() == VISIBLE) {
                    selectArticleFragment.search(publish_search_edit.getText().toString());
                    publish_search_edit.clearFocus();
                    KeyboardUtils.hideSoftInput(activity);
                } else {
                    showSearch();
                }
                break;

            case R.id.publish_search_delete:
                publish_search_edit.setText("");
                selectArticleFragment.search("");
                break;
        }
    }

    private void showSearch() {
        publish_search_edit.setVisibility(VISIBLE);
        publish_search_edit.requestFocus();
        if (publish_search_edit.getText().length() > 0) {
            publish_search_delete.setVisibility(VISIBLE);
        }
        KeyboardUtils.showSoftInput(publish_search_edit);
    }

    private void hideSearch() {
        selectArticleFragment.search("");
        publish_search_edit.setVisibility(GONE);
        publish_search_delete.setVisibility(GONE);
        publish_search_edit.clearFocus();
        KeyboardUtils.hideSoftInput(activity);
    }

    /**
     * 显示下拉菜单
     */
    private void showPopupWindow() {
        if (pwMenu == null) {
            pwMenu = new PopupWindow(this);
            View popup_publish_menu = View.inflate(this, R.layout.popup_publish_menu, null);
            pwMenu.setContentView(popup_publish_menu);
            int width = (int) (ScreenInfo.WIDTH * 0.4);
            pwMenu.setWidth(width);
            pwMenu.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pwMenu.setFocusable(true);
            pwMenu.setBackgroundDrawable(new ColorDrawable(0xb0000000));
            pwMenu.setOutsideTouchable(true);
            pwMenu.showAsDropDown(tv_title, -((width - tv_title.getWidth()) / 2), 0);
            publish_shade.setVisibility(VISIBLE);
            // 设置撤销监听
            pwMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    publish_shade.setVisibility(GONE);
                }
            });
            final TextView publish_menu_show_image = (TextView) popup_publish_menu.findViewById(R.id.publish_menu_show_image);
            final TextView publish_menu_select_publish = (TextView) popup_publish_menu.findViewById(R.id.publish_menu_select_publish);
            final TextView publish_menu_image_text = (TextView) popup_publish_menu.findViewById(R.id.publish_menu_image_text);
            // 设置默认选中项
            if (showImageFragment.isVisible()) {
                publish_menu_show_image.setTextColor(getColorValue(R.color.colorAccent));
            } else if (selectArticleFragment.isVisible()) {
                publish_menu_select_publish.setTextColor(getColorValue(R.color.colorAccent));
            } else if (imageTextFragment.isVisible()) {
                publish_menu_image_text.setTextColor(getColorValue(R.color.colorAccent));
            }
            // 设置点击事件
            OnClickListener onClickListener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    publish_menu_show_image.setTextColor(getColorValue(R.color.textBlack));
                    publish_menu_select_publish.setTextColor(getColorValue(R.color.textBlack));
                    publish_menu_image_text.setTextColor(getColorValue(R.color.textBlack));
                    tv.setTextColor(getColorValue(R.color.colorAccent));
                    switch (v.getId()) {
                        case R.id.publish_menu_show_image:
                            tv_title.setText(publish_menu_show_image.getText());
                            publish_publish.setVisibility(VISIBLE);
                            publish_search.setVisibility(GONE);
                            replaceFragment(R.id.publish_group, showImageFragment);
                            break;

                        case R.id.publish_menu_select_publish:
                            publish_publish.setVisibility(GONE);
                            publish_search.setVisibility(VISIBLE);
                            tv_title.setText(publish_menu_select_publish.getText());
                            replaceFragment(R.id.publish_group, selectArticleFragment);
                            break;

                        case R.id.publish_menu_image_text:
                            publish_publish.setVisibility(VISIBLE);
                            publish_search.setVisibility(GONE);
                            tv_title.setText(publish_menu_image_text.getText());
                            replaceFragment(R.id.publish_group, imageTextFragment);
                            break;
                    }
                    pwMenu.dismiss();
                }
            };
            publish_menu_show_image.setOnClickListener(onClickListener);
            publish_menu_select_publish.setOnClickListener(onClickListener);
            publish_menu_image_text.setOnClickListener(onClickListener);
        } else {
            publish_shade.setVisibility(VISIBLE);
            pwMenu.showAsDropDown(tv_title, -((pwMenu.getWidth() - tv_title.getWidth()) / 2), 0);
        }
    }

    public void clearSearchContent() {
        hideSearch();
        publish_search_edit.setText("");
    }
}