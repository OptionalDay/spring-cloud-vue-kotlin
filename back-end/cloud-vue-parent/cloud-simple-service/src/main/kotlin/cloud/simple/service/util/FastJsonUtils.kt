package cloud.simple.service.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.PropertyFilter
import com.alibaba.fastjson.serializer.SerializerFeature
import org.apache.commons.lang.StringUtils
import java.util.*

/**
 * Created by leo on 2017/5/31.
 */
object  FastJsonUtils {
    val SUCCESS_MSG = "数据加载成功"

    private val features = arrayOf(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)

    /**
     * 生成json返回结果
     */
    fun resultSuccess(code: Int?, msg: String, data: Any?): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        rs.put("error", "")
        return toString(rs)
    }

    fun resultError(code: Int?, error: String, data: Any?): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("data", data ?: Any())
        rs.put("error", if (StringUtils.isNotEmpty(error)) error else "")
        return toString(rs)
    }

    /**
     * 生成json返回结果
     */
    fun resultList(code: Int?, msg: String, pageNo: Int?, pageSize: Int?, data: Any?): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        rs.put("pageNo", pageNo ?: 0)
        rs.put("pageSize", pageSize ?: 10)
        return toString(rs)
    }

    /**
     * 生成json返回结果
     */
    fun resultFeatures(code: Int?, msg: String, data: Any?, vararg feature: SerializerFeature): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        return JSON.toJSONString(rs, *feature)
    }

    /**
     * 生成json返回结果
     */
    fun resultDate(code: Int?, msg: String, data: Any?, dateFormat: String): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        return JSON.toJSONStringWithDateFormat(rs, dateFormat, *features)
    }

    /**
     * 生成json返回结果,包含字段
     */
    fun resultIncludes(code: Int?, msg: String, data: Any?, vararg properties: String): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        return toStringIncludes(rs, *properties)
    }

    /**
     * 生成json返回结果,包含字段
     */
    fun resultExcludes(code: Int?, msg: String, data: Any?, type: Class<*>, vararg properties: String): String {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        return toStringExcludes(rs, type, *properties)
    }

    /**
     * 生成json字符串
     */
    fun toString(data: Any): String {
        return JSON.toJSONString(data, *features)
    }

    /**
     * 生成json字符串
     */
    private fun toStringIncludes(data: Any, vararg properties: String): String {
        val filter = PropertyFilter { source, name, value ->
            if (source.javaClass == HashMap::class.java && ("code" == name || "data" == name || "msg" == name)) {
                return@PropertyFilter true
            }
            for (pro in properties) {
                if (pro == name) {
                    return@PropertyFilter true
                }
            }
            false
        }
        return JSON.toJSONString(data, filter, *features)
    }

    /**
     * 排除字段
     * @param args
     */
    private fun toStringExcludes(data: Any, type: Class<*>, vararg properties: String): String {
        val filter = PropertyFilter { source, name, value ->
            if (source.javaClass == type) {
                for (pro in properties) {
                    if (pro == name) {
                        return@PropertyFilter false
                    }
                }
            }
            true
        }
        return JSON.toJSONString(data, filter, *features)
    }
}