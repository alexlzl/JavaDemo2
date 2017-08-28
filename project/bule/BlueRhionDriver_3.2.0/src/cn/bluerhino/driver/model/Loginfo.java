package cn.bluerhino.driver.model;

import android.content.ContentValues;
import android.os.Parcel;

import com.bluerhino.library.model.BRModel;

public class Loginfo extends BRModel {

	private String id;
	private String userName;
	private String passWord;
	private boolean isMemorize;
	private String sessionID;
	/**
	 * 表示用户是否通过常规密码验证方式登陆
	 */
	private boolean isLogIn;

	public Loginfo() {
		id = "0";
		userName = "";
		passWord = "";
		isMemorize = false;
		sessionID = "";
		isLogIn = false;
	}

	public Loginfo(Parcel source) {
		super(source);
		id = source.readString();
		userName = source.readString();
		passWord = source.readString();
		isMemorize = source.readInt() == 0 ? false : true;
		sessionID = source.readString();
		isLogIn = source.readInt() == 0 ? false : true;
	}

	public final String getId() {
		return id;
	}

	public final String getUserName() {
		return userName;
	}

	public final String getPassWord() {
		return passWord;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public final boolean getMemorize() {
		return isMemorize;
	}

	public final void setMemorize(boolean isMemorize) {
		this.isMemorize = isMemorize;
	}
	
	public final void setSessionID(String session){
		this.sessionID = session;
	}
	
	public final String getSessionID(){
		return this.sessionID;
	}
	
	public final void setIsLogin(boolean flag){
		isLogIn = flag;
	}
	
	public final boolean getIsLogin(){
		return isLogIn;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(userName);
		dest.writeString(passWord);
		dest.writeInt(isMemorize ? 1 : 0);
		dest.writeString(sessionID);
		dest.writeInt(isLogIn? 1 : 0);
	}

	@Override
    public String toString() {
	    return "Loginfo [id=" + id + ", userName=" + userName + ", passWord=" + passWord
	            + ", isMemorize=" + isMemorize +  ",sessionID=" + sessionID + ",isLogin=" + isLogIn + "]";
    }

	@Override
	public ContentValues createContentValues() {
		return null;
	}

}
