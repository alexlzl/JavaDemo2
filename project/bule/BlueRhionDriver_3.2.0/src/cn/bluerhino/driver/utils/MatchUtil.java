package cn.bluerhino.driver.utils;

import java.util.regex.Pattern;

public class MatchUtil {
	
	private static int mStatus = -1;
	
	private static interface Statues{
		static final int STUS_101 = 101;
		static final int STUS_102 = 102;
		static final int STUS_103 = 103;
		static final int STUS_109 = 109;
	}
	
	public static class StatusManager{
		public static String getReason(){
			String reason = null;
			switch (mStatus) {
			case Statues.STUS_101:
				reason = "验证成功";
				break;
			case Statues.STUS_102:
				reason = "请填写手机号";
				break;
			case Statues.STUS_103:
				reason = "手机号长度小于11位,请重新填写";
				break;
			case Statues.STUS_109:
				reason = "手机号格式错误";
				break;
			default:
				reason = "验证失败";
				break;
			}
			return reason;
		}
	}
	

	public static boolean isMobileNum(String mobileNum) {

		Pattern p = Pattern.compile("^1[345678][0-9]{9}$");
		
		if(p.matcher(mobileNum).matches()){
			mStatus = Statues.STUS_101;
			return true;
		}else{
			if(isEmpty(mobileNum)){
				mStatus = Statues.STUS_102;
			}
			else if(mobileNum.length() < 11){
				mStatus =  Statues.STUS_103;
			}
			else {
				mStatus = Statues.STUS_109;
			}
			return false;
		}
	}
	
    private static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

}
