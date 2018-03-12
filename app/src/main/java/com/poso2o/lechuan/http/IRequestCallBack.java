package com.poso2o.lechuan.http;

/**
 * Created by Administrator on 2017-11-27.
 */
public interface IRequestCallBack<T> {
    void onResult(int tag, T result);
    void onFailed(int tag,  String msg);
}
