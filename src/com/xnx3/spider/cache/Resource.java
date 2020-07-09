package com.xnx3.spider.cache;

import com.xnx3.Lang;
import com.xnx3.UrlUtil;
import com.xnx3.file.FileUtil;
import com.xnx3.spider.Global;

/**
 * 某个资源对象
 * @author 管雷鸣
 */
public class Resource {
	private String netUrl;		//资源在网络上的绝对路径，绝对地址Url
	private String originalUrl;	//原始url，资源在当前html中被引用的url
	private String localUrl;	//缓存到本地磁盘的Url，绝对URL

	private String localFile;		//变成缓存到本地文件，格式如 a.jpg 
	private String localRelativePath;	//存储在本地的相对路径，如 images/   而html的，则为空字符串

	
	/**
	 * 本地缓存导入时，会通过此创建
	 */
	public Resource() {
	}
	
	/**
	 * 传入绝对路径
	 * @param url 网络资源的绝对路径
	 * @param referrerUrl 来源的URL，即上级url，使用此资源的url
	 */
	public Resource(String url, String referrerUrl) {
		if(url.length() > 500){
			return;
		}
		if(url.length() < 3){
			return;
		}
		if(url.indexOf("//") == 0){
			//如果这个url是 //www.baidu.com这样的网址，是以//开头的，那么补全协议
			//Global.log("补齐协议。原路径："+url);
			url = UrlUtil.getProtocols(referrerUrl)+":"+url;
		}
		//如果url中包含空格，要将其变为 url 编码
		if(url.indexOf(" ") > 0){
			url = url.replaceAll(" ", "%20");
		}
		
		this.netUrl = url;
		this.originalUrl = url;
		jisuanLocalFile();
	}

	/**
	 * 根据this.netUrl 网上的绝对路径，来计算存到本地的路径
	 */
	public void jisuanLocalFile(){
		if(this.netUrl.lastIndexOf("/") < 5){
			//Global.log("debug netUrl.lastIndexOf < 5, netUrl:"+this.netUrl);
			return;
		}else{
			//保存到本地的文件
			this.localFile = UrlUtil.getFileName(this.netUrl);
		}

		//判断资源类型，是html、css、js、image
		//获取资源的后缀名＋后面？的一堆
		String suffix = Lang.subString(this.localFile, ".", null, 4);
		//图片文件后缀
		String[] imgs = {"jpg","jpeg","gif","png","bmp","ico","svg"};
		String type = null;
		for (int i = 0; i < imgs.length; i++) {
			if(suffix.equalsIgnoreCase(imgs[i])){
				type = "image";
				break;
			}
		}
		if(type == null){
			//判断是否是js后缀
			if(suffix.equalsIgnoreCase("js")){
				type = "js";
			}
		}
		if(type == null){
			//判断是否是css后缀
			String[] csss = {"css","woff2","woff","eot","ttf","otf"};
			for (int i = 0; i < csss.length; i++) {
				if(suffix.equalsIgnoreCase(csss[i])){
					type = "css";
					break;
				}
			}
		}
		if(type == null){
			//判断是否是html文件
			String[] htmls = {"htm","html","shtml","jsp","action","do","asp","aspx","php"};
			for (int i = 0; i < htmls.length; i++) {
				if(suffix.equalsIgnoreCase(htmls[i])){
					type = "html";
					suffix = "html";	//后缀名变为html
					this.localFile.replace("."+htmls[i], "html");	//将缓存的后缀改为html
					break;
				}
			}
		}
		if(type == null){
			System.out.println("未发现什么类型。type:"+type+",--"+originalUrl);
			this.localUrl = Global.getLocalTemplatePath();
			this.localRelativePath = "/";
		}else{
			switch (type) {
			case "image":
				this.localUrl = Global.getLocalTemplatePath()+"images/";
				this.localRelativePath = "images/";
				break;
			case "js":
				this.localUrl = Global.getLocalTemplatePath()+"js/";
				this.localRelativePath = "js/";
				break;
			case "css":
				this.localUrl = Global.getLocalTemplatePath()+"css/";
				this.localRelativePath = "css/";
				break;
				default:
				this.localUrl = Global.getLocalTemplatePath();
				this.localRelativePath = "/";
				break;
			}
		}

		
		//判断磁盘上这个文件是否存在
		if(FileUtil.exists(this.localUrl + localFile)){
			//已经存在了，那么要重新命名，不能覆盖
			this.localFile = Lang.subString(this.localFile, null, ".")+"_"+Lang.uuid()+"."+suffix;
		}
		
		//赋值，当前存储再磁盘的绝对路径
		this.localUrl = this.localUrl + this.localFile;
	}
	
	public String getNetUrl() {
		return netUrl;
	}
	public void setNetUrl(String netUrl) {
		this.netUrl = netUrl;
	}
	public String getLocalUrl() {
		return localUrl;
	}
	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public String getLocalFile() {
		return localFile;
	}

	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}

	public String getLocalRelativePath() {
		return localRelativePath;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	@Override
	public String toString() {
		return "Resource [netUrl=" + netUrl + ", originalUrl=" + originalUrl
				+ ", localUrl=" + localUrl + ", localFile=" + localFile
				+ ", localRelativePath=" + localRelativePath + "]";
	}

	

	
}
