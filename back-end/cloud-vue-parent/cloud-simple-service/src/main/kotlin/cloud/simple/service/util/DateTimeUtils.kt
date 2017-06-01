package cloud.simple.service.util

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.time.DateUtils
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by leo on 2017/5/31.
 */
class DateTimeUtils: DateUtils() {
    val FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    val FULL_DATE_FORMAT_CN = "yyyy年MM月dd日 HH时mm分ss秒"
    val PART_DATE_FORMAT = "yyyy-MM-dd"
    val PART_DATE_FORMAT_CN = "yyyy年MM月dd日"
    val YEAR_DATE_FORMAT = "yyyy"
    val MONTH_DATE_FORMAT = "MM"
    val DAY_DATE_FORMAT = "dd"
    val WEEK_DATE_FORMAT = "week"


    /**
     * 将日期类型转换为字符串
     * @param date      日期
     * *
     * @param xFormat   格式
     * *
     * @return
     */
    fun getFormatDate(date: Date?, xFormat: String): String {
        var date = date
        var xFormat = xFormat
        date = if (date == null) Date() else date
        xFormat = if (StringUtils.isNotEmpty(xFormat) == true) xFormat else FULL_DATE_FORMAT
        val sdf = SimpleDateFormat(xFormat)
        return sdf.format(date)
    }


    /**
     * 比较日期大小
     * @param dateX
     * *
     * @param dateY
     * *
     * @return x < y return [-1];
     * * 		   x = y return [0] ;
     * *         x > y return [1] ;
     */
    fun compareDate(dateX: Date, dateY: Date): Int {
        return dateX.compareTo(dateY)
    }


    /**
     * 将日期字符串转换为日期格式类型
     * @param xDate
     * *
     * @param xFormat 为NULL则转换如：2012-06-25
     * *
     * @return
     */
    fun parseString2Date(xDate: String, xFormat: String?): Date? {
        var xFormat = xFormat
        while (!isNotDate(xDate)) {
            xFormat = if (StringUtils.isNotEmpty(xFormat) == true) xFormat else PART_DATE_FORMAT
            val sdf = SimpleDateFormat(xFormat!!)
            var date: Date? = null
            try {
                date = sdf.parse(xDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                return null
            }

            return date
        }
        return null
    }


    /**
     * 判断需要转换类型的日期字符串是否符合格式要求
     * @param xDate
     * *
     * @param xFormat 可以为NULL
     * *
     * @return
     */
    fun isNotDate(xDate: String): Boolean {
        val sdf = SimpleDateFormat(PART_DATE_FORMAT)
        try {
            if (StringUtils.isEmpty(xDate)) {
                return true
            }
            sdf.parse(xDate)
            return false
        } catch (e: ParseException) {
            e.printStackTrace()
            return true
        }

    }

    fun isDate(xDate: String): Boolean {
        return !isDate(xDate)
    }


    /**
     * 获取俩个日期之间相差天数
     * @param dateX
     * *
     * @param dateY
     * *
     * @return
     */
    fun getDiffDays(dateX: Date?, dateY: Date?): Int {
        if (dateX == null || dateY == null) {
            return 0
        }

        val dayX = (dateX.time / (60 * 60 * 1000 * 24)).toInt()
        val dayY = (dateY.time / (60 * 60 * 1000 * 24)).toInt()

        return if (dayX > dayY) dayX - dayY else dayY - dayX
    }

    /**
     * 获取俩个日期之间相差天数(日期)
     * @param dateX
     * *
     * @param dateY
     * *
     * @return
     */
    fun getDiffDaysNoABS(dateX: Date?, dateY: Date?): Int {
        if (dateX == null || dateY == null) {
            return 0
        }

        val dayX = (dateX.time / (60 * 60 * 1000 * 24)).toInt()
        val dayY = (dateY.time / (60 * 60 * 1000 * 24)).toInt()

        return dayX - dayY
    }


    /**
     * 获取传值日期之后几天的日期并转换为字符串类型
     * @param date       需要转换的日期 date 可以为NULL 此条件下则获取当前日期
     * *
     * @param after      天数
     * *
     * @param xFormat    转换字符串类型 (可以为NULL)
     * *
     * @return
     */
    fun getAfterCountDate(date: Date?, after: Int, xFormat: String): String {
        var date = date
        var xFormat = xFormat
        date = if (date == null) Date() else date
        xFormat = if (StringUtils.isNotEmpty(xFormat) == true) xFormat else PART_DATE_FORMAT
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, after)
        return getFormatDate(calendar.time, xFormat)
    }

    /**
     * 获取传值日期之前几天的日期并转换为字符串类型
     * @param date       需要转换的日期 date 可以为NULL 此条件下则获取当前日期
     * *
     * @param after      天数
     * *
     * @param xFormat    转换字符串类型 (可以为NULL)
     * *
     * @return
     */
    fun getBeforeCountDate(date: Date?, before: Int, xFormat: String): String {
        var date = date
        var xFormat = xFormat
        date = if (date == null) Date() else date
        xFormat = if (StringUtils.isNotEmpty(xFormat) == true) xFormat else PART_DATE_FORMAT
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, -before)
        return getFormatDate(calendar.time, xFormat)
    }


    /**
     * 获取日期的参数 如：年 , 月 , 日 , 星期几

     * @param xDate 日期 可以为日期格式,可以是字符串格式; 为NULL或者其他格式时都判定为当前日期
     * *
     * @param xFormat 年 yyyy 月 MM 日 dd 星期 week ;其他条件下都返回0
     */
    fun getDateTimeParam(xDate: Any?, xFormat: String): Int {
        var xDate = xDate
        xDate = if (xDate == null) Date() else xDate
        var date: Date? = null
        if (xDate is String) {
            date = parseString2Date(xDate.toString(), null)
        } else if (xDate is Date) {
            date = xDate as Date?
        } else {
            date = Date()
        }
        date = if (date == null) Date() else date
        if (StringUtils.isNotEmpty(xFormat) && (xFormat == YEAR_DATE_FORMAT
                || xFormat == MONTH_DATE_FORMAT
                || xFormat == DAY_DATE_FORMAT)) {
            return Integer.parseInt(getFormatDate(date, xFormat))
        } else if (StringUtils.isNotEmpty(xFormat) && WEEK_DATE_FORMAT == xFormat) {
            val cal = Calendar.getInstance()
            cal.time = date
            val week = if (cal.get(java.util.Calendar.DAY_OF_WEEK) - 1 == 0)
                7
            else
                cal.get(java.util.Calendar.DAY_OF_WEEK) - 1
            return week
        } else {
            return 0
        }
    }


    /**
     * 日期格式转换为时间戳
     * @param time
     * *
     * @param format
     * *
     * @return
     */
    fun getLongTime(time: String, format: String): Long? {
        val sdf = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = sdf.parse(time)
            return date!!.time / 1000
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }


    /**
     * 获取星期字符串
     * @param xDate
     * *
     * @return
     */
    fun getWeekString(xDate: Any): String {
        val week = getDateTimeParam(xDate, WEEK_DATE_FORMAT)
        when (week) {
            1 -> return "星期一"
            2 -> return "星期二"
            3 -> return "星期三"
            4 -> return "星期四"
            5 -> return "星期五"
            6 -> return "星期六"
            7 -> return "星期日"
            else -> return ""
        }
    }

    /**
     * 获得十位时间
     */
    fun getTenBitTimestamp(): Long {
        return System.currentTimeMillis() / 1000
    }

    /**
     * 获得某天的结束时间
     */
    fun getDateEnd(date: Date): Date {
        return Date(date.time + (86400 - 1) * 1000)
    }

    /**
     * 日期格式转换为毫秒
     * @param time
     * *
     * @param format
     * *
     * @return
     */
    fun getLongDateTime(time: String, format: String): Long? {
        val sdf = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = sdf.parse(time)
            return date!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取某天开始时间戳_10位
     */
    fun getStartTimestamp(date: Date?): Long {
        var date = date

        val calendar = Calendar.getInstance()
        date = if (date == null) Date() else date
        calendar.time = date

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time.time / 1000
    }

    /**
     * 获取某天结束时间戳_10位
     */
    fun getEndTimestamp(date: Date?): Long {
        var date = date

        val calendar = Calendar.getInstance()
        date = if (date == null) Date() else date
        calendar.time = date

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        return calendar.time.time / 1000
    }

    /**
     * 获取昨天日期

     * @param date
     * *
     * @return
     */
    fun getYesterday(date: Date): Date {
        var date = date
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, -1)

        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        date = calendar.time
        return date
    }

    /**
     * 获取明天时间（参数时间+1天）
     * @param date
     * *
     * @return
     */
    fun getTomorrowday(date: Date): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DAY_OF_YEAR, +1)
        return c.time
    }

    /* 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
	 *
	 * @param time
	 * @return
	 */
    fun timestampToString(time: Int?, format: String): String {
        // int转long时，先进行转型再进行计算，否则会是计算结束后在转型
        val temp = time as Long * 1000
        val ts = Timestamp(temp)
        var tsStr = ""
        val dateFormat = SimpleDateFormat(format)
        try {
            // 方法一
            tsStr = dateFormat.format(ts)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tsStr
    }

    /**
     * 获取某天开始时间
     */
    fun getStartTime(date: Date?): Date {
        var date = date

        val calendar = Calendar.getInstance()
        date = if (date == null) Date() else date
        calendar.time = date

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    /**
     * 获取某天结束时间
     */
    fun getEndTime(date: Date?): Date {
        var date = date

        val calendar = Calendar.getInstance()
        date = if (date == null) Date() else date
        calendar.time = date

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        return calendar.time
    }

    /**
     * Date类型转换为10位时间戳

     * @param time
     * *
     * @return
     */
    fun DateToTimestamp(time: Date): Int {
        val ts = Timestamp(time.time)

        return (ts.time / 1000).toInt()
    }

    /**
     * 获取当前时间之前或之后几分钟
     * @param minute
     * *
     * @return
     */
    fun getTimeByMinute(minute: Int, time: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = time
        calendar.add(Calendar.MINUTE, minute)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.time)

    }
}