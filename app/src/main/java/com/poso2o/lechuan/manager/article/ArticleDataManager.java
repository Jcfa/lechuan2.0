package com.poso2o.lechuan.manager.article;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.article.ArticleBean;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.poso2o.lechuan.http.HttpAPI.OA_GOODS_PIC_URL;
import static com.poso2o.lechuan.http.HttpAPI.OA_SEND_ARTICLE_URL;

/**
 * 文章管理
 * Created by Administrator on 2017-11-25.
 */
public class ArticleDataManager extends BaseManager {

    /**
     * 文章类型
     */
    public static final int COLLECT = 0;// 收藏
    public static final int FASHION = 1;// 时尚
    public static final int CATE = 2;// 美食
    public static final int HEALTH = 3;// 健康
    public static final int FASHIAN = 4;// 健康

    private static ArticleDataManager articleDataManager;

    private final int TAG_ARTICLE_LIST_ID = 121;

    private final int TAG_COLLECT_ID = 122;

    private final int PUBLISH_ARTICLE_ID = 123;
    //根据商品id获取商品推广图片
    private static final int OA_GOODS_PIC_ID = 124;

    //已添加到发布列表的选文篇数
    private int art_num = 0;

    //记录已收藏文章的id
    private String collect_id = "";

    /**
     * 公众号助手选中的数据
     */
    private ArrayList<Article> oaSelectData = new ArrayList<>();

    public static ArticleDataManager getInstance() {
        if (articleDataManager == null) {
            synchronized (ArticleDataManager.class) {
                if (articleDataManager == null) {
                    articleDataManager = new ArticleDataManager();
                }
            }
        }
        return articleDataManager;
    }

    /**
     * 加载文章列表
     */
    public void loadListData(final BaseActivity baseActivity, final int articles_type, String keywords, final int pageType, final OnLoadListDataCallback callback) {
        if (pageType == FIRST) {
            currPage = 0;
        }
        Request<String> request;
        if (articles_type == COLLECT) {
            request = getStringRequest(HttpAPI.ARTICLES_COLLECT_LIST_API);
        } else {
            request = getStringRequest(HttpAPI.ARTICLES_LIST_API);
            request.add("articles_type", articles_type);
        }
        String type = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_TYPES);
        String labels = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_LAYBELS);
        Print.println("参数：" + type + " : " + labels);
        request.add("articles_types", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_TYPES));
        request.add("articles_labels", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_OA_LAYBELS));
        request.add("keywords", keywords);
        request.add("currPage", ++currPage);
        defaultParam(request);
        baseActivity.request(TAG_ARTICLE_LIST_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                ArticleBean articleBean = gson.fromJson(response, ArticleBean.class);
                if (articleBean != null && articleBean.list != null) {
                    if (pageType == NEXT && articleBean.list.size() == 0) {
                        currPage--;
                        callback.onFail(baseActivity.getString(R.string.toast_no_more_data));
                    } else {
                        if (articles_type == COLLECT){
                            collect_id = "";
                            for (int i = 0; i<articleBean.list.size(); i++){
                                collect_id = collect_id + articleBean.list.get(i).articles_id + ",";
                            }
                        }
                        callback.onSucceed(articleBean.list);
                    }
                } else {
                    callback.onFail(baseActivity.getString(R.string.toast_load_articles_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFail(response);
            }
        }, true, false);
    }

    /**
     * 收藏
     * @param baseActivity
     * @param articles_id
     * @param has_collect
     */
    public void collect(BaseActivity baseActivity,final String articles_id, final int has_collect, final OnCollectCallback onCollectCallback) {
        Request<String> request;
        if (has_collect == 1) {
            request = getStringRequest(HttpAPI.ARTICLES_UNCOLLECT_API);
        } else {
            request = getStringRequest(HttpAPI.ARTICLES_COLLECT_API);
        }
        request.add("articles_id", articles_id);
        defaultParam(request);
        baseActivity.request(TAG_COLLECT_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Log.i(this.getClass().getSimpleName(), response);
                if (has_collect == 1){
                    String[] str = collect_id.split(articles_id);
                    collect_id = "";
                    for (int i=0; i<str.length; i++){
                        collect_id = collect_id + str[i];
                    }
                }else {
                    collect_id = collect_id + articles_id + ",";
                }
                onCollectCallback.onSucceed(null);
            }

            @Override
            public void onFailed(int what, String response) {
                onCollectCallback.onFail(response);
            }
        }, true, false);
    }

    //获取收藏文章的id
    public String getCollect_id(){
        return collect_id;
    }

    /**
     * 获取选择的文章
     * @return
     */
    public ArrayList<Article> getSelectData(){
        return oaSelectData;
    }

    /**
     * 发送成功后删除所有选择的文章
     */
    private void removeSelectData(){
        oaSelectData.clear();
        art_num = 0;
        EventBean bean = new EventBean(EventBean.CODE_ART_NUM_CHANGE);
        EventBus.getDefault().post(bean);
    }

    /**
     * 添加选择数据
     */
    public boolean addSelectData(Context context,Article article) {
        boolean add = addAble(context,article);
        if (add){
            oaSelectData.add(article);
            if (article.articles_type != Constant.SELF_ARTICLE){
                art_num++;
                EventBean bean = new EventBean(EventBean.CODE_ART_NUM_CHANGE);
                EventBus.getDefault().post(bean);
            }
        }
        return add;
    }

    /**
     * 是否能继续添加文章
     * @param article
     * @return
     */
    public boolean addAble(Context context,Article article){
        boolean add = true;
        if (oaSelectData.size() == 8){
            add = false;
            Toast.show(context,"最多只能发布8篇文章");
        }else {
            if (article.articles_type != Constant.SELF_ARTICLE){
                if (art_num == 3){
                    add = false;
                    Toast.show(context,"最多只能选3篇文章");
                }else {
                    add = true;
                }
            }else {
                add = true;
            }
        }
        return add;
    }

    /**
     * 删除选择数据
     */
    public void removeSelectData(Article article) {
        article = findSelectData(article);
        if (article != null) {
            oaSelectData.remove(article);
            if (article.articles_type != Constant.SELF_ARTICLE){
                art_num--;
                EventBean bean = new EventBean(EventBean.CODE_ART_NUM_CHANGE);
                EventBus.getDefault().post(bean);
            }
        }
    }

    /**
     * 修改已选择的文章
     * @param article
     */
    public void updateSelectData(Article article){
        Article item = findSelectData(article);
        item.content = article.content;
        item.pic = article.pic;
        item.title = article.title;
    }

    /**
     * 查找选择数据
     */
    public Article findSelectData(Article article) {
        for (Article item : oaSelectData) {
            if (item.articles_id.equals(article.articles_id)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 文章上移
     */
    public ArrayList<Article> upArticle(Article article){
        if (oaSelectData.size() > 1){
            int index = 0;
            for (int i = 0 ; i<oaSelectData.size(); i++){
                if (oaSelectData.get(i).articles_id == article.articles_id){
                    index = i;
                    break;
                }
            }
            if (index != 0){
                //该文章没有排在第一位
                oaSelectData.remove(index);
                oaSelectData.add(index-1,article);
            }
        }
        return oaSelectData;
    }

    /**
     * 文章下移
     */
    public ArrayList<Article> downArticle(Article article){
        if (oaSelectData.size() > 1){
            int index = 0;
            for (int i = 0 ; i<oaSelectData.size(); i++){
                if (oaSelectData.get(i).articles_id == article.articles_id){
                    index = i;
                    break;
                }
            }
            if (index != oaSelectData.size()-1){
                //该文章没有排在最后一位
                oaSelectData.remove(index);
                oaSelectData.add(index+1,article);
            }
        }
        return oaSelectData;
    }

    /**
     * 文章设为封面
     */
    public ArrayList<Article> topArticle(Article article){
        if (oaSelectData.size() > 1){
            oaSelectData.remove(article);
            oaSelectData.add(0,article);
        }
        return oaSelectData;
    }

    /**
     * 加载数据列表回调
     */
    public interface OnLoadListDataCallback {

        void onSucceed(ArrayList<Article> articles);

        void onFail(String failMsg);
    }

    /**
     * 收藏回调
     */
    public interface OnCollectCallback {

        void onSucceed(Article article);

        void onFail(String failMsg);
    }

    /**
     * 发布文章
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void sendArticle(final BaseActivity baseActivity,final IRequestCallBack iRequestCallBack){

        Request<String> request = getStringRequest(OA_SEND_ARTICLE_URL);
        defaultParam(request);
        for (Article article : getSelectData()){
            article.cover_pic_url = article.pic;
        }
        request.add("articles",new Gson().toJson(getSelectData()));

        baseActivity.request(PUBLISH_ARTICLE_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                removeSelectData();
                iRequestCallBack.onResult(PUBLISH_ARTICLE_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(PUBLISH_ARTICLE_ID,response);
            }
        },true,true);
    }

    /**
     * 获取商品的推广图
     * @param baseActivity
     * @param goods_id
     * @param iRequestCallBack
     */
    public void getGoodsPic(BaseActivity baseActivity,String goods_id,final IRequestCallBack iRequestCallBack){
        Request<String> request = getStringRequest(OA_GOODS_PIC_URL);
        defaultParamNoShop(request);
        request.add("goods_id",goods_id);

        baseActivity.request(OA_GOODS_PIC_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(OA_GOODS_PIC_ID,response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(OA_GOODS_PIC_ID,response);
            }
        },true,true);
    }

}
