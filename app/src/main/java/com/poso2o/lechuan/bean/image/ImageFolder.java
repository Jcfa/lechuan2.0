package com.poso2o.lechuan.bean.image;

import com.poso2o.lechuan.util.ListUtils;

import java.util.ArrayList;

public class ImageFolder {

	/**
	 * 图片的文件夹路径
	 */
	private String dir;

	/**
	 * 文件夹的名称
	 */
	private String name;

	/**
	 * 文件路径集合
	 */
	private ArrayList<String> imagePaths = new ArrayList<>();

	public void setDir(String dir) {
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf).replace("/", "");
	}

	public String getFirstImagePath() {
		if (ListUtils.isNotEmpty(imagePaths)) {
			return imagePaths.get(0);
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return imagePaths.size();
	}

	public void setCount(int count) {
	}

	public void addPath(String path) {
		imagePaths.add(path);
	}

	public void addPaths(ArrayList<String> paths) {
		imagePaths.addAll(paths);
	}

	public ArrayList<String> getImagePaths() {
		return imagePaths;
	}
}
