package com.poso2o.lechuan.activity.wopenaccount;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.base.BaseActivity;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class AuthorizationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title,tv_wopen_auth_w,tv_wopen_auth_s;
    //点击公众号授权
    private Button bt_wopen_auth_qian;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wopen_authorization;
    }

    @Override
    protected void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_wopen_auth_w=(TextView)findViewById(R.id.tv_wopen_auth_w);
        tv_wopen_auth_s=(TextView)findViewById(R.id.tv_wopen_auth_s);
        bt_wopen_auth_qian=(Button)findViewById(R.id.bt_wopen_auth_qian);
    }

    @Override
    protected void initData() {
//        tv_title.setText(getResources().getString(R.string.authorization_statement));
        tv_title.setTextColor(getResources().getColor(R.color.text_type));
        String w="日进斗金老板管理app <font color='#FF0000'>不会</font> 将您的公众号登陆账号和密码上传到服务器。";
        String s="<font color='#B2000000'>-删除app后，日进斗金服务端</font><font color='#FF0000'>不会保留您的隐私信息。</font>";
        tv_wopen_auth_w.setText(Html.fromHtml(w));
        tv_wopen_auth_s.setText(Html.fromHtml(s));
    }

    @Override
    protected void initListener() {
        bt_wopen_auth_qian.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_wopen_auth_qian:
                //进入授权页面
                Intent intent = new Intent();
                intent.setClass(AuthorizationActivity.this, WCAuthorityActivity.class);
                intent.putExtra(WCAuthorityActivity.BIND_TYPE,1);
                startActivityForResult(intent,10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){

        }
    }
}
