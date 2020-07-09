package com.xnx3.spider;

import com.xnx3.StringUtil;
import com.xnx3.ui.Log;

/**
 * 数据缓存
 * @author 管雷鸣
 */
public class Global {
	
	private static String localTemplatePath;

	public static String getLocalTemplatePath(){
		if(localTemplatePath == null){
			localTemplatePath = Global.class.getResource("/").getPath();
			if(localTemplatePath.contains("%")){
				//判断路径中是否有URL编码，若有，进行转码为正常汉字
				localTemplatePath = StringUtil.urlToString(localTemplatePath);
			}
		}
		return localTemplatePath+"kunyu"+"/";
	}

}
