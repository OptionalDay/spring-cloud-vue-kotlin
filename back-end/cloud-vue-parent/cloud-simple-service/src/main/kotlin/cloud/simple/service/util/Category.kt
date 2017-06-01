package cloud.simple.service.util

import cloud.simple.service.contants.Constant
import com.google.common.collect.Lists
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.StringUtils

/**
 * Created by leo on 2017/5/31.
 */
class Category {
    //原始的分类数据
    private var rawList: List<Map<String, Any>> = Lists.newArrayList<Map<String, Any>>()
    //格式化后的分类
    private var formatList: MutableList<Map<String, Any>> = Lists.newArrayList<Map<String, Any>>()
    //字段映射
    private var fields: Map<String, String>? = null

    /**
     * @param fields
     * * 			cid 当前分类id
     * * 			fid 当前分类的父id
     * *
     * @param rawList
     */
    constructor(fields: Map<String, String>, rawList: List<Map<String, Any>>){
        this.fields = fields as MutableMap
        this.rawList = rawList

        fields.put("cid", if (fields["cid"] != null) fields!!["cid"]!! else "cid")
        fields.put("fid", if (fields["fid"] != null) fields!!["fid"]!! else "fid")
        fields.put("name", if (fields["name"] != null) fields!!["name"]!! else "name")
        fields.put("fullname", if (fields["fullname"] != null) fields!!["fullname"]!! else "fullname")
    }

    fun getRawList(): List<Map<String, Any>> {
        return rawList
    }

    fun setRawList(rawList: List<Map<String, Any>>) {
        this.rawList = rawList
    }

    fun getFormatList(): List<Map<String, Any>> {
        return formatList
    }

    fun setFormatList(formatList: MutableList<Map<String, Any>>) {
        this.formatList = formatList
    }


    /**
     * 返回指定的上级分类的所有同一级子分类
     * @param fid 查询的分类id
     * *
     * @return
     */
    fun getChild(fid: Any): List<Map<String, Any>> {
        val results = Lists.newArrayList<Map<String, Any>>()
        for (m in rawList) {
            if (fid == m[fields!!["fid"]])
                results.add(m)
        }
        return results
    }

    /**
     * 递归格式化分类前的字符
     * @param pid 分类id
     * *
     * @param space 空白
     * *
     * @param level 级别
     * *
     * @param p_name 父名称
     * *
     * @return
     */
    private fun _searchList(cid: Any, space: String, level: Int, pname: Any) {
        val childs = this.getChild(cid)
        //如果没有下级分类，结束递归
        if (CollectionUtils.isEmpty(childs)) {
            return
        }
        val n = childs.size
        var m = 1
        for (i in 0..n - 1) {
            val child = childs[i] as MutableMap
            var pre = ""
            var pad = ""
            if (n == m) {
                pre = Constant.ICON[2]
            } else {
                pre = Constant.ICON[1]
                pad = if (StringUtils.isBlank(space)) Constant.ICON[0] else ""
            }
            child.put("p_title", pname)
            child.put("else", child[fields!!["name"]]!!)
            child.put(fields!!["fullname"]!!, (if (cid != 0) space + pre else "") + child[fields!!["name"]])
            child.put("level", level)
            formatList.add(child)
            this._searchList(child.get(fields!!["cid"])!!, space + pad + "  ", level + 1, child["else"]!!)
            m++
        }
    }

    /**
     * 递归格式化分类
     * @param cid 起始分类
     * *
     * @return
     */
    fun getList(cid: Any): List<Map<String, Any>> {
        this._searchList(cid, "", 1, "")
        return formatList
    }
}