package com.poso2o.lechuan.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.system.AddressBean;
import com.poso2o.lechuan.bean.system.AddressDTO;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.manager.system.SystemDataManager;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-14.
 */

public class AddressSelectActivity extends BaseActivity {
    //    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private static final int PROVINCE_ID = 1;//
    private static final int CITY_ID = 2;
    private static final int AREA_ID = 3;
    private int CURRENT_ID = 0;
    private BaseAdapter mAdapter;
    private AddressDTO mAddressDTO = new AddressDTO();
    private String mProvinceId, mCityId, mAreaId, mProvince, mCity, mArea;
    public static final String KEY_PROVINCE_ID = "provinceId", KEY_PROVINCE_NAME = "provinceName", KEY_CITY_ID = "cityId", KEY_CITY_NAME = "cityName", KEY_AREA_ID = "areaId", KEY_AREA_NAME = "areaName";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_province_list_view;
    }

    @Override
    protected void initView() {
        setTitle("省");
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findView(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = findView(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(
                activity, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        mAdapter = new BaseAdapter(activity, mAddressDTO.province) {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new BaseViewHolder(getItemView(R.layout.layout_province_item, viewGroup));
            }

            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                final AddressBean bean = (AddressBean) item;
                TextView tvName = holder.getView(R.id.tv_name);
                String name = "";
                switch (CURRENT_ID) {
                    case PROVINCE_ID:
                        name = bean.provincename;
                        break;
                    case CITY_ID:
                        name = bean.cityname;
                        break;
                    case AREA_ID:
                        name = bean.areaname;
                        break;
                }
                tvName.setText(name);
            }
        };
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<AddressBean>() {
            @Override
            public void onItemClick(AddressBean item) {
                switch (CURRENT_ID) {
                    case PROVINCE_ID:
                        setTitle(item.provincename);
                        CURRENT_ID = CITY_ID;
                        mProvince = item.provincename;
                        mProvinceId = item.provinceid;
                        mAdapter.notifyDataSetChanged(getCityList(item.provinceid));
                        break;
                    case CITY_ID:
                        setTitle(item.cityname);
                        CURRENT_ID = AREA_ID;
                        mCity = item.cityname;
                        mCityId = item.cityid;
                        mAdapter.notifyDataSetChanged(getAreaList(item.cityid));
                        break;
                    case AREA_ID:
                        mArea = item.areaname;
                        mAreaId = item.areaid;
                        Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_PROVINCE_ID, mProvinceId);
                        bundle.putString(KEY_PROVINCE_NAME, mProvince);
                        bundle.putString(KEY_CITY_ID, mCityId);
                        bundle.putString(KEY_CITY_NAME, mCity);
                        bundle.putString(KEY_AREA_ID, mAreaId);
                        bundle.putString(KEY_AREA_NAME, mArea);
                        resultIntent.putExtras(bundle);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getAddressList();
    }

    @Override
    protected void initListener() {

    }

    private void getAddressList() {
        WaitDialog.showLoaddingDialog(activity);
        SystemDataManager.getSystemDataManager().loadAddress(activity, new IRequestCallBack<AddressDTO>() {
            @Override
            public void onResult(int tag, AddressDTO addressDTO) {
                mAddressDTO = addressDTO;
                CURRENT_ID = PROVINCE_ID;
                mAdapter.notifyDataSetChanged(mAddressDTO.province);
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    /**
     * 获取所选省的城市列表
     *
     * @param provinceId
     * @return
     */
    private ArrayList<AddressBean> getCityList(String provinceId) {
        ArrayList<AddressBean> citys = new ArrayList<>();
        for (AddressBean bean : mAddressDTO.city) {
            if (bean.provinceid.equals(provinceId)) {
                citys.add(bean);
            }
        }
        return citys;
    }

    /**
     * 获取所选城市的区列表
     *
     * @param cityId
     * @return
     */
    private ArrayList<AddressBean> getAreaList(String cityId) {
        ArrayList<AddressBean> areas = new ArrayList<>();
        for (AddressBean bean : mAddressDTO.area) {
            if (bean.cityid.equals(cityId)) {
                areas.add(bean);
            }
        }
        return areas;
    }
}
