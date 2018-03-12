package com.poso2o.lechuan.manager.address;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.addressdata.AddressBean;
import com.poso2o.lechuan.bean.addressdata.AddressCacheData;
import com.poso2o.lechuan.bean.addressdata.AreaData;
import com.poso2o.lechuan.bean.addressdata.CityData;
import com.poso2o.lechuan.bean.addressdata.ProvinceData;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.util.ListUtils;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jaydon on 2017/12/15.
 */
public class AddressDataManager extends BaseManager {

    private static AddressDataManager addressDataManager;

    private AddressCacheData addressCacheData;

    public static AddressDataManager getInstance() {
        if (addressDataManager == null) {
            synchronized (ArticleDataManager.class) {
                if (addressDataManager == null) {
                    addressDataManager = new AddressDataManager();
                }
            }
        }
        return addressDataManager;
    }

    public void loadAllData(final BaseActivity baseActivity, final IRequestCallBack<AddressCacheData> callback) {
        if (addressCacheData != null && ListUtils.isNotEmpty(addressCacheData.provinceDatas)) {
            callback.onResult(0, addressCacheData);
            return;
        }
        Request<String> request = getStringRequest(HttpAPI.ADDRESS_API);

        defaultParam(request);
        baseActivity.request(0, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                AddressBean addressBean = new Gson().fromJson(response, AddressBean.class);
                addressCacheData = new AddressCacheData();
                packetAddress(addressBean, addressCacheData);
                callback.onResult(what, addressCacheData);
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFailed(what, response);
            }
        }, true, false);
    }

    public void packetAddress(AddressBean addressData, AddressCacheData addressCacheData) {
        // 分组数据
        // 分组省
        addressCacheData.version = "1.0";
        addressCacheData.provinceDatas = addressData.province;

        if (addressCacheData.cityMapDatas == null) {
            addressCacheData.cityMapDatas = new ConcurrentHashMap<String, ArrayList<CityData>>();
        }
        // 分组城市
        for (ProvinceData province : addressData.province) {
            ArrayList<CityData> citys = new ArrayList<CityData>();
            for (CityData city : addressData.city) {
                if (province.provinceid.equals(city.provinceid)) {
                    citys.add(city);
                }
            }
            province.city = citys;
            addressCacheData.cityMapDatas.put(province.provinceid, citys);
        }

        if (addressCacheData.areaMapDatas == null) {
            addressCacheData.areaMapDatas = new ConcurrentHashMap<String, ArrayList<AreaData>>();
        }
        // 分组区域
        for (CityData city : addressData.city) {
            ArrayList<AreaData> areas = new ArrayList<AreaData>();
            for (AreaData area : addressData.area) {
                if (city.cityid.equals(area.cityid)) {
                    areas.add(area);
                }
            }
            city.areas = areas;
            addressCacheData.areaMapDatas.put(city.cityid, areas);
        }
    }
}
