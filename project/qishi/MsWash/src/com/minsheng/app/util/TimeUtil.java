package com.minsheng.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.minsheng.wash.R;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 
 * 
 * @describe:时间转化工具类
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-11-4上午10:35:35
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	public static final String yyyyMMddFormat = "yyyy-MM-dd";
	public static final String yyyyMMddHHMMSSFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyyMMddHHMMFormat = "yyyy-MM-dd HH:mm";
	public static final String yyyyMMddHHmmssNospaceFormat = "yyyyMMddHHmmss";
	public static final String yyyyPMMPddFormat = "yyyy.MM.dd";

	public static String getCurrentSecond() {
		Long tsLong = System.currentTimeMillis() / 1000;
		String ts = tsLong.toString();
		// return (int) System.currentTimeMillis();
		return ts;
	}

	/**
	 * 
	 * 
	 * @describe:获取当天日期 格式yyyy-MM-dd-HH-mm-ss
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:35:59
	 * 
	 */
	public static String getCurrentTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat(
				"yyyy-MM-dd-HH-mm-ss");
		String time = dataFormat.format(new Date());
		return time;
	}

	public static String getCurrentDate() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMM");
		String time = dataFormat.format(new Date());
		return time;
	}

	public static String getCurrentDateC() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月");
		String time = dataFormat.format(new Date());
		return time;
	}

	/**
	 * 
	 * 
	 * @describe:获取当天日期 格式yyyy-MM-dd
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:36:11
	 * 
	 */
	public static String getCurrentDayTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = dataFormat.format(new Date());
		return time;
	}

	/**
	 * 
	 * 
	 * @describe:获取日期
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:36:21
	 * 
	 */
	public static String getDate(String cc_time) {
		String re_StrTime = null;
		if (cc_time == null) {
			cc_time = System.currentTimeMillis() + "";
		}

		if (cc_time.length() == 10) { // 单位 秒
			cc_time += "000";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time));

		return re_StrTime;

	}

	/**
	 * 
	 * 
	 * @describe:获取现在时间
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-11-4上午10:36:40
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringDate(String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间差值
	 * 
	 * 差值天
	 * 
	 * @return
	 */
	public static long getTimeDiff(String leftTime, String rightTime) {
		long day = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date right;
		Date left;
		try {
			right = df.parse(rightTime);
			left = df.parse(leftTime);
			long l = right.getTime() - left.getTime();
			day = l / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param context
	 * @param pTime
	 * @return
	 * @throws Exception
	 */
	public static String getDayForWeek(Context context, String pTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtil.e("TimeUtil getDayForWeek e = " + e);
		}
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}

		if (isDateToday(pTime)) {
			return context.getString(R.string.today);
		}
		return getWeekName(context, dayForWeek);
	}

	/**
	 * 判断当前星期中文名
	 */
	public static String getWeekName(Context context, int week) {
		String weekName = context.getString(R.string.sunday);
		switch (week) {
		case 1:
			weekName = context.getString(R.string.monday);
			break;
		case 2:
			weekName = context.getString(R.string.tuesday);
			break;
		case 3:
			weekName = context.getString(R.string.wednesday);
			break;
		case 4:
			weekName = context.getString(R.string.thursday);
			break;
		case 5:
			weekName = context.getString(R.string.friday);
			break;
		case 6:
			weekName = context.getString(R.string.saturday);
			break;
		case 7:
			weekName = context.getString(R.string.sunday);
			break;
		default:
			break;
		}
		return weekName;
	}

	/**
	 * 
	 * 判断日期是否为今天，日期格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isDateToday(String date) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = "";
		// 使用服务器系统时间
		// if (ExpressTime.SERVER_SYSTEM_TIME_NULL !=
		// ExpressTime.currentServerSystemTime) {
		// currentTime =
		// changeTimeStempToDate(ExpressTime.currentServerSystemTime);
		// } else {
		// currentTime = f.format(c.getTime());
		// }
		// LogUtil.d("TimeUtil isDateToday currentTime = " + currentTime +
		// " date = " + date);
		if (currentTime.equals(date)) {
			return true;
		}
		return false;
	}

	/**
	 * yyyy-MM-dd转换为MM月dd日
	 * 
	 * @param date
	 * @return
	 */
	public static String changeTimeFormat(String date) {
		SimpleDateFormat date0 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat date1 = new SimpleDateFormat("yyyy年MM月dd");
		String nowTime = "";
		try {
			nowTime = date1.format(date0.parse(date)).substring(5) + "日";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowTime;
	}

	/**
	 * 将时间戳转换成时间字符串
	 * 
	 * @param mill
	 * @return yyyy-MM-dd hh:mm:ss
	 */
	public static String changeTimeStempToString(long mill) {
		Date date = new Date(mill * 1000L);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 将时间戳转换成时间字符串
	 * 
	 * @param mill
	 * @return
	 */
	public static String changeTimeStempToString(long mill, String format) {
		Date date = new Date(mill * 1000L);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 将时间戳转换成日期符串
	 * 
	 * @param mill
	 * @return yyyy-MM-dd
	 */
	public static String changeTimeStempToDate(int mill) {
		Date date = new Date(mill * 1000L);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}
	
	public static String changeTime_M_D(int mill){
		Date date = new Date(mill * 1000L);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 将字符串转换成时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static int getTimeStemp(String time) {
		int timeStemp = -1;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date date = simpleDateFormat.parse(time);
			timeStemp = (int) (date.getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeStemp;
	}

	public static long getTimeStemp(String time, String format) {
		long timeStemp = -1;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			Date date = simpleDateFormat.parse(time);
			timeStemp = date.getTime() / 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeStemp;
	}

	/**
	 * 将字符串转换成时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static int getDateTimeStemp(String time) {
		int timeStemp = -1;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			Date date = simpleDateFormat.parse(time);
			timeStemp = (int) (date.getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeStemp;
	}

	public static long dateToTime(String dateTime) {
		long time = 0;
		// Date或者String转化为时间戳
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = format.parse(dateTime);
			time = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 时间戳转换Date对象
	 * 
	 * @param mill
	 * @return
	 */
	public static Date TimeLongToDate(long mill) {
		Date date = new Date(mill * 1000);
		return date;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toString(Date date, String format) {
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 判断某一时间戳到现在的时间 例如一分钟前，一天前
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatTimeStamp(Long timeStamp) {
		long minute = (long) (1000 * 60);
		long hour = minute * 60;
		long day = hour * 24;
		long mouth = day * 30;
		long year = mouth * 12;

		int num = 0;
		String unit = null;
		long offsetSecons = System.currentTimeMillis() - 1000 * timeStamp;
		if (offsetSecons < minute) {
			return "刚刚";
		} else if (offsetSecons > minute && offsetSecons < hour) {
			num = (int) (offsetSecons / minute);
			unit = "分钟";
		} else if (offsetSecons >= hour && offsetSecons < day) {
			num = (int) (offsetSecons / hour);
			unit = "小时";
		} else if (offsetSecons >= day && offsetSecons < mouth) {
			num = (int) (offsetSecons / day);
			unit = "天";
		} else if (offsetSecons >= mouth && offsetSecons < year) {
			num = (int) (offsetSecons / mouth);
			unit = "月";
		} else if (offsetSecons >= year) {
			num = (int) (offsetSecons / year);
			unit = "年";
		}
		return num + unit + "前";
	}

	public static String getCountTime(long millisUntilFinished) {
		// 获取小时值
		long hours = millisUntilFinished / (60 * 60 * 1000);
		// 获取分值
		long minutes = (millisUntilFinished - hours * (60 * 60 * 1000))
				/ (60 * 1000);
		// 获取秒值
		long seconds = (millisUntilFinished - hours * (60 * 60 * 1000) - minutes
				* (60 * 1000)) / 1000;
		StringBuilder sb = new StringBuilder();
		if (hours < 10) {
			// tvHour.setText("0" + hours);
			sb.append("0" + hours + ":");
		} else
			// tvHour.setText(hours + "");
			sb.append(hours + ":");
		if (minutes < 10) {
			// tvMinute.setText("0" + minutes);
			sb.append("0" + minutes + ":");
		} else
			// tvMinute.setText(minutes + "");
			sb.append(minutes + ":");
		if (seconds < 10) {
			// tvSeconds.setText("0" + seconds);
			sb.append("0" + seconds + "");
		} else
			// tvSeconds.setText(seconds + "");
			sb.append(seconds + "");
		return sb.toString();
	}

}
