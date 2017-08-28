
package com.minsheng.app.entity;

import java.io.Serializable;

/**
 *
 * @describe:
 *
 * @author:LiuZhouLiang
 *
 * @time:2014-12-11下午3:25:38
 * 
 */
public class JsonBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String auth ;
	private String jsonResult  ;
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "JsonBean [auth=" + auth + ", jsonResult=" + jsonResult + "]";
	}
}

