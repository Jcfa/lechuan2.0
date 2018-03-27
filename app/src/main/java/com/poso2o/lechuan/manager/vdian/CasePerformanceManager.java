package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.vdian.CasePerformanceDTO;
import com.poso2o.lechuan.http.CallServer;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * 授权管理
 * <p>
 * Created by Administrator on 2018/3/15 0015.
 */
public class CasePerformanceManager extends BaseManager {

    private int CASE_PERFORMANCE = 1;

    public String SERVICE_CASE_PERFORMANCE_URL = HttpAPI.SERVER_MAIN_API + "ShopSampleShowManage.htm?";

    private static CasePerformanceManager instance;

    private CasePerformanceManager() {

    }

    public static CasePerformanceManager getInstance() {
        if (instance == null) {
            synchronized (VdianCatalogManager.class) {
                if (instance == null) {
                    instance = new CasePerformanceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 案例列表
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void casePerformanceList(final BaseActivity baseActivity, final IRequestCallBack<CasePerformanceDTO> iRequestCallBack) {
        final Request<String> request = getStringRequest(SERVICE_CASE_PERFORMANCE_URL);
        defaultParam(request);
        baseActivity.request(CASE_PERFORMANCE, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                CasePerformanceDTO casePerformanceDTO = new Gson().fromJson(response, CasePerformanceDTO.class);
                iRequestCallBack.onResult(CASE_PERFORMANCE, casePerformanceDTO);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(CASE_PERFORMANCE, response);
            }
        }, true, true);
    }
}
