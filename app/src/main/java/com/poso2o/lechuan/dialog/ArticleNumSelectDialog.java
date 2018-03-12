package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.OaService;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.ItemOaNumView;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/2.
 */

public class ArticleNumSelectDialog extends BaseDialog {

    private View view;
    private Context context;

    //内容
    private LinearLayout oa_num_list;
    private ArrayList<ItemOaNumView> numViews = new ArrayList<>();

    //初始化选中
    private int preSelect = 3;
    private int boughtNum;

    private OnArtNumListener onArtNumListener ;

    private ArrayList<OaService.Item> items;
    private ItemOaNumView.OnOaNumListener onOaNumListener = new ItemOaNumView.OnOaNumListener() {
        @Override
        public void onArtNumClick(OaService.Item item) {
            if (Integer.parseInt(item.num) < boughtNum){
                Toast.show(context,"文章数量不能降低，只能升级");
                return;
            }
            onArtNumListener.onArtNumClick(item);
            dismiss();
        }
    };

    public ArticleNumSelectDialog(Context context,ArrayList<OaService.Item> items,OnArtNumListener onArtNumListener) {
        super(context);
        this.context = context;
        this.items = items;
        this.onArtNumListener = onArtNumListener;
    }

    //选择文章续费用到的构造方法，传入当下购买的文章数，作只能升级不能减低的逻辑判断
    public ArticleNumSelectDialog(Context context,ArrayList<OaService.Item> items,OnArtNumListener onArtNumListener,int boughtNum) {
        super(context);
        this.context = context;
        this.items = items;
        this.onArtNumListener = onArtNumListener;
        this.boughtNum = boughtNum;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_oa_article_num,null);
        return view;
    }

    @Override
    public void initView() {
        oa_num_list = (LinearLayout) view.findViewById(R.id.oa_num_list);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.8,0.8);
        setListData();
    }

    @Override
    public void initListener() {
        view.findViewById(R.id.oa_article_num_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setListData(){
        for (int i=0;i<items.size();i++){
            ItemOaNumView numView = new ItemOaNumView(context,items.get(i),onOaNumListener);
            oa_num_list.addView(numView.getRootView());
            numViews.add(numView);
        }
    }

    public void setSelected(int num){
        numViews.get(preSelect - 1).setSelected(false);
        numViews.get(num -1).setSelected(true);
        preSelect = num;
    }

    public interface OnArtNumListener{
        void onArtNumClick(OaService.Item item);
    }
}
