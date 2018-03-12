package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.addressdata.AddressCacheData;
import com.poso2o.lechuan.bean.addressdata.AreaData;
import com.poso2o.lechuan.bean.addressdata.CityData;
import com.poso2o.lechuan.bean.addressdata.ProvinceData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.address.AddressDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择地址对话框
 * Created by Luo on 2016/12/19.
 */
public class SelectAddressedDialog extends BaseDialog implements NumberPicker.Formatter {

    private Context context;

    private View view;

    private OnSelectAddressListener onSelectAddressListener;

    private Button dialog_confirm, dialog_cancel;
    private ImageView iv_close_dialog;

    private Spinner dialog_select_address_province_spinner, dialog_select_address_city_spinner, dialog_select_address_area_spinner;

    /**
     * 地址
     */
    private String mProvinceid = "", mProvincename = "", mCityId = "", mCityname = "", mAreaId = "", mAreaname = "";

    /**
     * 省
     */
    private ArrayList<ProvinceData> mProvinceDatas;
    private int mCurrentProvincePosition = 0;

    /**
     * 市
     */
    private ArrayList<CityData> mCityDatas;
    private int mCurrentCityPosition = 0;

    /**
     * 区
     */
    private ArrayList<AreaData> mAreaDatas;
    private int mCurrentAreaPosition = 0;

    public SelectAddressedDialog(Context context, OnSelectAddressListener onSelectAddressListener) {
        super(context);
        this.context = context;
        this.onSelectAddressListener = onSelectAddressListener;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_select_address, null);
    }

    public void show(String provinceId, String provincename, String cityId, String cityname, String areaId, String areaname) {
        super.show();
        loadAddressData(provinceId, provincename, cityId, cityname, areaId, areaname);
    }

    @Override
    public void initView() {
        setWindowDisplay(0.7, OUT_TO);
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);

        // 初始化控件
        dialog_confirm = (Button) view.findViewById(R.id.dialog_confirm);
        dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
        iv_close_dialog = (ImageView) view.findViewById(R.id.iv_close_dialog);
        dialog_select_address_province_spinner = (Spinner) view.findViewById(R.id.dialog_select_address_province_spinner);
        dialog_select_address_city_spinner = (Spinner) view.findViewById(R.id.dialog_select_address_city_spinner);
        dialog_select_address_area_spinner = (Spinner) view.findViewById(R.id.dialog_select_address_area_spinner);
    }

    public void initData() {

    }

    /**
     * 获取全部国家地理地址
     */
    private void loadAddressData(final String provinceid, final String provincename, final String cityid, final String cityname, final String areaid, final String areaname) {
        ((BaseActivity) context).showLoading();
        AddressDataManager.getInstance().loadAllData((BaseActivity) context, new IRequestCallBack<AddressCacheData>() {

            @Override
            public void onResult(int tag, AddressCacheData result) {
                mProvinceDatas = result.provinceDatas;
                mProvinceid = provinceid;
                mProvincename = provincename;
                mCityId = cityid;
                mCityname = cityname;
                mAreaId = areaid;
                mAreaname = areaname;

                setAddressSelected();
                ((BaseActivity) context).dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
                ((BaseActivity) context).dismissLoading();
            }
        });
    }

    @Override
    public void initListener() {
        dialog_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        iv_close_dialog.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //确认
        dialog_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onSelectAddressListener != null) {

                    String provinceid = mProvinceDatas.get(mCurrentProvincePosition).provinceid;
                    String provincename = mProvinceDatas.get(mCurrentProvincePosition).provincename;

                    mCityDatas = mProvinceDatas.get(mCurrentProvincePosition).city;
                    String cityid = mCityDatas.get(mCurrentCityPosition).cityid;
                    String cityname = mCityDatas.get(mCurrentCityPosition).cityname;

                    mAreaDatas = mCityDatas.get(mCurrentCityPosition).areas;
                    String areaid = mAreaDatas.get(mCurrentAreaPosition).areaid;
                    String areaname = mAreaDatas.get(mCurrentAreaPosition).areaname;

                    onSelectAddressListener.confirm(provinceid, provincename, cityid, cityname, areaid, areaname);
                }
                dismiss();
            }
        });

        // 选择省
        dialog_select_address_province_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dialog_select_address_province_spinner.setTag(arg2);
                // new ToastView(context, "province:"+arg2).show();
                arg0.setVisibility(View.VISIBLE);
                mCurrentProvincePosition = arg2;
                mCurrentCityPosition = 0;
                mCurrentAreaPosition = 0;

                // 市
                mCityDatas = mProvinceDatas.get(mCurrentProvincePosition).city;
                List<String> cityList = new ArrayList<>();
                for (CityData city : mCityDatas) {
                    cityList.add(city.cityname);
                }
                ArrayAdapter<String> city_adapter = new ArrayAdapter<>(context, R.layout.set_spinner_item, cityList);
                dialog_select_address_city_spinner.setAdapter(city_adapter);

                // 区
                mAreaDatas = mCityDatas.get(mCurrentAreaPosition).areas;
                List<String> areaList = new ArrayList<>();
                for (AreaData area : mAreaDatas) {
                    areaList.add(area.areaname);
                }
                ArrayAdapter<String> area_adapter = new ArrayAdapter<>(context, R.layout.set_spinner_item, areaList);
                dialog_select_address_area_spinner.setAdapter(area_adapter);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        // 选择市
        dialog_select_address_city_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dialog_select_address_city_spinner.setTag(arg2);
                arg0.setVisibility(View.VISIBLE);
                mCurrentCityPosition = arg2;
                // new ToastView(context, "city:"+arg2).show();
                mCurrentAreaPosition = 0;

                // 市
                mCityDatas = mProvinceDatas.get(mCurrentProvincePosition).city;

                //区
                List<String> areaList = new ArrayList<>();
                for (AreaData area : mCityDatas.get(mCurrentCityPosition).areas) {
                    areaList.add(area.areaname);
                }
                ArrayAdapter<String> area_adapter = new ArrayAdapter<>(context, R.layout.set_spinner_item, areaList);
                dialog_select_address_area_spinner.setAdapter(area_adapter);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });

        // 选择区
        dialog_select_address_area_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dialog_select_address_area_spinner.setTag(arg2);
                mCurrentAreaPosition = arg2;
                // new ToastView(context, "area:"+arg2).show();
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
    }

    // 设置选中
    private void setAddressSelected() {

        List<String> provinceList = new ArrayList<>();
        for (ProvinceData province : mProvinceDatas) {
            provinceList.add(province.provincename);
        }
        ArrayAdapter<String> province_adapter = new ArrayAdapter<>(context, R.layout.set_spinner_item, provinceList);
        dialog_select_address_province_spinner.setAdapter(province_adapter);

        // 设置选中省
        for (int a = 0; a < mProvinceDatas.size(); a++) {
            if (mProvinceDatas.get(a).provinceid.equals(mProvinceid)) {
                dialog_select_address_province_spinner.setSelection(a, true);
                mCurrentProvincePosition = a;
                break;
            }
        }

        // 设置选中市
        mCityDatas = mProvinceDatas.get(mCurrentProvincePosition).city;
        for (int b = 0; b < mCityDatas.size(); b++) {
            if (mCityDatas.get(b).cityid.equals(mCityId)) {

                mCurrentCityPosition = b;

                final Handler handler = new Handler();
                final int finalB = b;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        dialog_select_address_city_spinner.setSelection(finalB, true);
                    }
                };
                handler.postDelayed(runnable, 200);// 打开定时器，执行操作

                break;
            }
        }


        // 设置选中区
        mAreaDatas = mCityDatas.get(mCurrentCityPosition).areas;
        for (int c = 0; c < mAreaDatas.size(); c++) {
            if (mAreaDatas.get(c).areaid.equals(mAreaId)) {

                mCurrentAreaPosition = c;

                final Handler handler = new Handler();
                final int finalC = c;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        dialog_select_address_area_spinner.setSelection(finalC, true);
                    }
                };
                handler.postDelayed(runnable, 400);// 打开定时器，执行操作

            }
        }

    }

    @Override
    public String format(int i) {
        return null;
    }

    public interface OnSelectAddressListener {

        void confirm(String provinceId, String provincename, String cityId, String cityname, String areaId, String areaname);

    }
}
