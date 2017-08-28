package com.minsheng.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class DateUtils {
	/**
	 * 日历实例
	 */
	private static Calendar c = Calendar.getInstance();
	
	/**
	 * 
	 * @return
	 */
	public static Integer getDateTime4PHP() {
		return (int) (System.currentTimeMillis() / 1000);
	}
	/**
	 * 获取指定数字的天的秒数
	 * @return
	 */
	public static Integer getDateTime4PHPByNum(int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, num);//+1明天  +2后天  -1昨天  -2前天 等等
//		System.out.println("昨天是："+c.getTime());
		return (int) (c.getTimeInMillis() / 1000);
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static long getDateTime4PHP(Date date) {
		return date.getTime() / 1000;
	}
	
	/**
	 * 
	 * @param num
	 * @return
	 */
	public static String getdatatime4PHP(int num) {
		return getDate(new Date(num * 1000L));
	}

	/**
	 * 
	 * @return
	 */
	public static String getOrderSnByTime() {
		Date now = new Date();
		String ymd = getDateFormat(now, "yyMMdd");
		String time = String.valueOf(now.getTime());
		time = time.substring(time.length() - 5);
		String microtime = String.valueOf(now.getTime()).substring(11, 13);
		return ymd + time + microtime + (new Random().nextInt(900) + 100);
	}
	
	/**
	 * 获取若干分钟之后的时间对象
	 * 
	 * @param min
	 * @return
	 */
	public static Date someMinAfter(int min) {
		c.setTime(new Date());
		c.add(Calendar.MINUTE, min);
		return c.getTime();
	}
	
	/**
	 * 若干分钟之后的时间
	 */
    public static int someMinAfter(Date date,int min){
    	c.setTime(date);
    	c.add(Calendar.MINUTE, min);
    	return (int) (c.getTimeInMillis()/1000);
    }
	/**
	 * 
	 * @return
	 */
	public static String getDate() {
		return getDate(new Date());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate(Date date) {
		return getDateFormat(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateFormat(String format) {
		return getDateFormat(new Date(), format);
	}

	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateFormat(Date date, String format) {
		String time = new SimpleDateFormat(format).format(date);
		return time;
	}
	/**
	 * 
	 * @param num
	 * @return 时间类型
	 */
	public static Date getDatefromTime(int num){
		return new Date(num * 1000L);
	}

	public static Integer getDate4PHP() {
		 Date date=new Date();
		 Date myFormatter;
		 Integer intDate=0;
		try {
			myFormatter = new SimpleDateFormat("yyyy-MM-dd").parse(getDateFormat(date,"yyyy-MM-dd"));
			Long dateLong= getDateTime4PHP(myFormatter);
			intDate=dateLong.intValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return intDate;
	}
	/**
	 * 
	* @Title: getDate4PHP 
	* @Description: 获得日期00:00:00
	* @param @param dateTime
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static Integer getDate4PHP(Integer dateTime) {
		 Date date=new Date(dateTime*1000L);
		 Date myFormatter;
		 Integer intDate=0;
		try {
			myFormatter = new SimpleDateFormat("yyyy-MM-dd").parse(getDateFormat(date,"yyyy-MM-dd"));
			Long dateLong= getDateTime4PHP(myFormatter);
			intDate=dateLong.intValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return intDate;
	}
	
	public static Integer getCurrentDate() {
		Integer current =(int) (System.currentTimeMillis()/1000L);
		return current;
	}
	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat dd=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String compareWithCurrentDate(long startTime,long endTime){
		java.util.Date nowdate=new java.util.Date(); 
		String nowdateString =DateUtils.getDateFormat(nowdate, "yyyy-MM-dd");
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String startDateString=sf.format(new Date(startTime*1000L));
		String endDateString=sf.format(new Date(endTime*1000L));
		String Flag = "0";
		if(nowdateString.compareTo(endDateString)>0){
			Flag = "2";//过期了
		}else if((nowdateString.compareTo(startDateString)<0)){
			Flag = "0";//未使用
		}else {
			Flag="1";//使用中
		}
		return Flag;
	}
	
	public static Boolean compareTimeWithAfterTwenty(int addTime){
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 // long addTime =1415002939;
		  SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String timeString=sf.format(new Date(addTime*1000L));
		  Date date = DateUtils.StringToDate(timeString, "yyyy-MM-dd HH:mm:ss");
		  String time_yesterday = format.format(date);
//		  System.out.println("验证码日期："+time_yesterday);

		  long time = date.getTime() + 20 * 60 * 1000;
		  Date dateTw = new Date(time);
		  String time_after = format.format(dateTw);
//		  System.out.println("20分钟后的日期："+time_after);
		  Date datenow = new Date();
//		  System.out.println("当前日期："+datenow);
//		  System.out.println(datenow.compareTo(dateTw));
//		  System.out.println(dateTw.compareTo(datenow));
		  if(dateTw.compareTo(datenow)==-1){
			  return false;
		  }else{
			  return true;
		  }
	}
	
	/**
	 * 易洗车日期转换接口
	 * @param stringDate
	 * @return
	 */
	public static String carToDate(String stringDate){
		String returnStr = "";
		String dateString ="";
		String dateTimestring = "";
		
		if ("".equals(stringDate)||null==stringDate) {
			return null;
		}
		//IOS传递的有汉字
		if ((stringDate.indexOf("今天")!=-1)||(stringDate.indexOf("日")!=-1) ) {
			if(stringDate.indexOf("今天")!=-1){
				dateTimestring = stringDate.substring(2,stringDate.length()).trim();
				java.util.Date nowdate=new java.util.Date(); 
				String nowdateString =DateUtils.getDateFormat(nowdate, "MM月dd日");
				returnStr = nowdateString+dateTimestring;
			}else{
				dateString =stringDate.substring(0,6);
				dateTimestring = stringDate.substring(8,stringDate.length()).trim();
				returnStr = dateString+" "+dateTimestring;
			}
		}else{//安卓传递的都是日期
			//.replace("全天", "9:00-16:00")
			String[] stime = stringDate.trim().split(" ");
			Date date =DateUtils.StringToDate(stime[0], "yyyy-MM-dd");
			
			dateString = DateUtils.getDateFormat(date, "MM月dd日");
			
			if("9:00-16:00".equals(stime[1])){
				dateTimestring = "全天";
			}else{
				dateTimestring = stime[1];
			}
			
			returnStr = dateString+" "+dateTimestring;
		}
		return returnStr;
	}
	/**
	 * 
	* @Title: getIntervalTime 
	* @Description: 倒计时时间间隔
	* @param @param dateTime
	* @param @param time
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static Integer getIntervalTime(Integer dateTime,String time){
		SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
		String yyyyMMdd=spf.format(new Date(dateTime*1000l));
		StringBuffer sb=new StringBuffer(yyyyMMdd);
		sb.append(" ");
		sb.append(time.split("-")[0]);
		sb.append(":00");
		String yyyyMMddHHmmss=sb.toString();
		Date takeDate=null;
		try {
			takeDate = sp.parse(yyyyMMddHHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int)(takeDate.getTime()/1000)-DateUtils.getDateTime4PHP();
	}
	
	public static String compareWithCurrentDateApointTime(long startTime,long endTime){
		java.util.Date nowdate=new java.util.Date(); 
		String nowdateString =DateUtils.getDateFormat(nowdate, "yyyy-MM-dd");
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String startDateString=sf.format(new Date(startTime*1000L));
		String endDateString=sf.format(new Date(endTime*1000L));
		String Flag = "0";
		if((nowdateString.compareTo(startDateString)<0)||nowdateString.compareTo(endDateString)<0){
			Flag = "1";//取衣时间 和送衣时间都在今天之前都不合理
		}
		return Flag;
	}
	
	public static String compareWithCurrentDateApointTime(long startTime){
		java.util.Date nowdate=new java.util.Date(); 
		String nowdateString =DateUtils.getDateFormat(nowdate, "yyyy-MM-dd");
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		String startDateString=sf.format(new Date(startTime*1000L));
		String Flag = "0";
		if((nowdateString.compareTo(startDateString)<0)){
			Flag = "1";//取衣时间 和送衣时间都在今天之前都不合理
		}
		return Flag;
	}
	
	public static int compare_date(String CurrentDate, String DATE2) {
		//日期格式 2008-05-22
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date nowDate = df.parse(CurrentDate);
			Date dt2 = df.parse(DATE2);
			if (nowDate.getTime() > dt2.getTime()) {//比现在日期小== 今天之前的日期
//				System.out.println("nowDate 在dt2前");
				return 1;
			} else if (nowDate.getTime() < dt2.getTime()) {//比现在日期大==明后天
//				System.out.println("nowDate在dt2后");
				return -1;
			} else {//同一天
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	public static int compare_date_min(String CurrentDate, String DATE2) {
		//日期格式 2008-05-22
		DateFormat df = new SimpleDateFormat("HH:mm");
		try {
			Date nowDate = df.parse(CurrentDate);
			Date dt2 = df.parse(DATE2);
			if (nowDate.getTime() > dt2.getTime()) {//比现在日期小== 今天之前的日期
//				System.out.println("nowDate 在dt2前");
				return 1;
			} else if (nowDate.getTime() < dt2.getTime()) {//比现在日期大==明后天
//				System.out.println("nowDate在dt2后");
				return -1;
			} else {//同一天
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	public static void main(String[] args) throws ParseException {
		String str = "14:17:00";
		System.out.println(getDateTime4PHP(StringToDate(str,"HH:mm:ss")));
//		System.out.println(getDate4PHP());
//		System.out.println(getDate(new Date(1375755616000l)));
//		System.out.println(getDate(new Date(1347499653000L + 604800000)));
//
//		System.out.println(getDate(new Date(1347517293000L)));
//		System.out.println(getDate(new Date(1347517293000L + 604800000)));
	
		
		
	}
}
