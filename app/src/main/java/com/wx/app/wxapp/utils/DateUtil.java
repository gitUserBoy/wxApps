package com.wx.app.wxapp.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期操作工具类
 */

public class DateUtil {
  private static final String COLON = ":";
  // 格式：年－月－日 小时：分钟：秒
  public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";
  public static final String FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String FORMAT_ONE_DATE = "yyyy-MM-dd";
  public static final String FORMAT_ONE_TIME = "HH:mm";
  public static final String FORMAT_LOG_APP_START = "yyyyMMddHHmmss.SSS";
  public static final String FORMAT_MONTH_AND_DAY = "MM.dd";

  /**
   * 秒数转换
   *
   * @param duration
   * @return (00:00:00)
   */
  public static String secondTurnTime(int duration) {
    String timeStr = null;
    int hour = 0;
    int minute = 0;
    int second = 0;
    if (duration <= 0)
      return "00:00";
    else {
      minute = duration / 60;
      if (minute < 60) {
        second = duration % 60;
        timeStr = unitFormat(minute) + ":" + unitFormat(second);
      } else {
        hour = minute / 60;
        if (hour > 99)
          return "99:59:59";
        minute = minute % 60;
        second = duration - hour * 3600 - minute * 60;
        timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
      }
    }
    return timeStr;
  }

  public static String unitFormat(int i) {
    String retStr = null;
    if (i >= 0 && i < 10)
      retStr = "0" + Integer.toString(i);
    else
      retStr = "" + i;
    return retStr;
  }

  /**
   * 两个日期相减
   *
   * @param firstTime
   * @param secTime
   * @return 相减得到的秒数
   */
  public static int timeSub(String firstTime, String secTime) {
    try {
      long first = stringtoDate(firstTime, FORMAT_ONE).getTime();
      long second = stringtoDate(secTime, FORMAT_ONE).getTime();
      return Integer.parseInt(String.valueOf(((second - first) / 1000)));
    } catch (Exception e) {
      return 0;
    }

  }

  public static int timeSubDay(String firstTime, String secTime) {
    try {
      long first = stringtoDate(firstTime, DateUtil.FORMAT_MONTH_AND_DAY).getTime();
      long second = stringtoDate(secTime, DateUtil.FORMAT_MONTH_AND_DAY).getTime();
      return Integer.parseInt(String.valueOf(((second - first) / 1000)));
    } catch (Exception e) {
      return 0;
    }

  }

  /**
   * 把符合日期格式的字符串转换为日期类型
   *
   * @param dateStr
   * @return
   */
  public static Date stringtoDate(String dateStr, String format) {
    Date d = null;
    SimpleDateFormat formater = new SimpleDateFormat(format);
    try {
      formater.setLenient(false);
      d = formater.parse(dateStr);
    } catch (Exception e) {
      // log.error(e);
      d = null;
    }
    return d;
  }

  public static Date esgStringToDate(String dateStr) {
    Date d = null;
    SimpleDateFormat formater = new SimpleDateFormat(FORMAT_ONE);
    try {
      formater.setLenient(false);
      d = formater.parse(dateStr);
    } catch (Exception e) {
      // log.error(e);
      d = null;
      formater = new SimpleDateFormat(FORMAT_T);
      try {
        d = formater.parse(dateStr);
      } catch (Exception e2) {
        d = null;
      }
    }
    return d;
  }

  public static Date calculateBeginDate(Date date) {
    if (date == null) {
      return null;
    }

    String datestr;

    datestr = dateToString(date, FORMAT_ONE);
    if (datestr == null) {
      return null;
    }

    return stringtoDate(datestr, DateUtil.FORMAT_ONE_DATE);
  }

  public static long calculateTodayTimeOffset(Date date, Date todayBeginDate) {
    if (todayBeginDate == null) {
      todayBeginDate = calculateBeginDate(date);
      if (todayBeginDate == null) {
        return 0;
      }
    }

    return date.getTime() - todayBeginDate.getTime();
  }

  /**
   * 获取当前时间的指定格式
   *
   * @param format
   * @return
   */
  public static String getCurrDate(String format) {
    return dateToString(new Date(), format);
  }

  /**
   * 获取当前时间的指定格式
   *
   * @return
   */
  public static String getCurrDate() {
    return dateToString(new Date(), FORMAT_ONE);
  }

  /**
   * 把日期转换为字符串
   *
   * @param date
   * @return
   */
  public static String dateToString(Date date, String format) {
    String result = "";
    SimpleDateFormat formater = new SimpleDateFormat(format);
    try {
      result = formater.format(date);
    } catch (Exception e) {
      // log.error(e);
    }
    return result;
  }

  public static Date dateByAddingDays(Date date, int delta) {
    Calendar cal;

    cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, delta);
    return cal.getTime();
  }

  /**
   * Date 转换 HH:mm:ss
   *
   * @param date
   * @return
   */
  public static String date2HHmmss(Date date) {
    return new SimpleDateFormat("HH:mm:ss").format(date);
  }

  /**
   * String 转换 Date
   *
   * @param str
   * @return
   */
  public static Date string2Date(String str) {
    try {
      return new SimpleDateFormat(FORMAT_ONE).parse(str);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return new Date();
  }

  /**
   * 格式化日期
   *
   * @return
   */
  public static String dateFormatterDate(String time, String original, String destination) {
    SimpleDateFormat sdf = new SimpleDateFormat(original, Locale.getDefault());
    String format = null;
    if (!TextUtils.isEmpty(time) && !TextUtils.isEmpty(original) && !TextUtils.isEmpty(destination)) {
      try {
        Date date = sdf.parse(time);
        sdf = new SimpleDateFormat(destination, Locale.getDefault());
        format = sdf.format(date);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return format;
  }

  /**
   * 格式化日期
   * 00:00-00:00
   *
   * @return
   */
  public static String dateFormatter(String startTime, int duration) {
    //        Log.d("Category2Activity", "convert:startTime= "+startTime+",duration"+duration );
    String startTimeStr = DateUtil.dateFormatterDate(startTime, DateUtil.FORMAT_ONE, DateUtil.FORMAT_ONE_TIME);
    if (startTimeStr == null) {
      //盒子返回时间2017-03-06T17:15:10.500629
      startTimeStr = DateUtil.dateFormatterDate(startTime, DateUtil.FORMAT_T, DateUtil.FORMAT_ONE_TIME);
    }
    Date startDate = DateUtil.stringtoDate(startTime, DateUtil.FORMAT_ONE);
    if (startDate == null) {
      //盒子返回时间2017-03-06T17:15:10.500629
      startDate = DateUtil.stringtoDate(DateUtil.dateFormatterDate(startTime, DateUtil.FORMAT_T, DateUtil.FORMAT_ONE), DateUtil.FORMAT_ONE);
    }
    String endTimeStr = "";
    if (startDate != null) {
      long endTime = startDate.getTime() + duration * 1000;
      endTimeStr = DateUtil.dateToString(new Date(endTime), DateUtil.FORMAT_ONE_TIME);
    }
    //    viewHolder.setText(R.id.tv_program_time, startTimeStr + "-" + endTimeStr);
    return startTimeStr + "-" + endTimeStr;

  }


  public static String dateFormatterLong(long lSysTime1) {
    SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_ONE);
    Date dt = new Date(lSysTime1);
    return sdf.format(dt);
  }

  /**
   * ntp是从1900.1.1 00:00:00开始的，unix是1970.1.1 00:00:00
   * 中间相差70年+闰年多的17天
   */
  private static final long OFFSET_1900_TO_1970 = ((365L * 70L) + 17L) * 24L * 60L * 60L;

  /**
   * ntp格式时间戳转换为时间
   *
   * @param ntpTime ntp时间戳，单位为秒
   * @return
   */
  public static String dateFormatNTPLong(long ntpTime) {
    long unixTime = ntpTime - OFFSET_1900_TO_1970;
    SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_ONE);
    Date dt = new Date(unixTime * 1000);
    return sdf.format(dt);
  }

  /**
   * 判断当前时间是否在指定时间范围内（ntp时间戳，单位为秒）
   *
   * @return true : 在范围内； false ：不在范围内
   */
  public static boolean currentDateAtRange(long startNtpTime, long endNtpTime) {
    try {
      Date date = new Date();
      long time = date.getTime() / 1000;
      long ntpCurrTime = time + OFFSET_1900_TO_1970;
      return ntpCurrTime >= startNtpTime && ntpCurrTime <= endNtpTime;
    } catch (Exception e) {
    }
    return false;
  }
}
