package com.poso2o.lechuan.manager.rshopmanager;

import android.os.AsyncTask;

import com.poso2o.lechuan.util.ImageLoaderUtils;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.List;

/** 
 * 压缩图片异步任务
 * 
 * @author 郑洁东 
 * @date 创建时间：2016-11-7 下午5:02:12 
 */
public class CompressImageAsyncTask extends AsyncTask<Void, Integer, Integer> {
	
	/**
	 * 选中的图片路径集合 
	 */
	private List<String> mSelectedPaths;
	
	/**
	 * 路径集合 
	 */
	private List<String> mPaths;
	
	/**
	 * 回调函数
	 */
	private AsyncTaskCallback mCallback;
	
	public CompressImageAsyncTask(List<String> selectedPaths, List<String> paths, AsyncTaskCallback callback) {
		mSelectedPaths = selectedPaths;
		mPaths = paths;
		mCallback = callback;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		if (ListUtils.isNotEmpty(mPaths)) {
			for (int i = 0; i < mSelectedPaths.size(); i++) {
				publishProgress(i + 1);
				String sPath = mSelectedPaths.get(i);
				if(sPath.toLowerCase().contains(".gif")){//如果gif图就不压缩
					continue;
				}
				String path = mPaths.get(i);
				if (TextUtil.isNotEmpty(path)) {
					ImageLoaderUtils.saveImageBackUrl(sPath, path);
				}
			}
			return -1;
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (values != null && values.length > 0) {
			mCallback.action(values[0]);
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (mCallback != null) {
			mCallback.action(result);
		}
	}

	public interface AsyncTaskCallback {

		/**
		 * 回调方法
		 *
		 * @param result 异步任务结果
		 */
		void action(Integer result);

	}
}
