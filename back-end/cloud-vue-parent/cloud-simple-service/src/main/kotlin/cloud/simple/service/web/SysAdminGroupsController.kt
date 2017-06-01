package cloud.simple.service.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cloud.simple.service.domain.SysAdminGroupService;
import cloud.simple.service.model.SysAdminGroup;
import cloud.simple.service.util.FastJsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统分组 控制层
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/groups")
@Api(value = "SysAdminGroupsController", description = "系统分组接口")
public class SysAdminGroupsController : CommonController() {
    @Autowired
    private var sysAdminGroupService: SysAdminGroupService? = null;

    /**
     * 列表
     */
    @ApiOperation(value = "列表", httpMethod = "GET")
    @GetMapping(value = "", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun index(request: HttpServletRequest): String {
        val goups = sysAdminGroupService!!.getDataList();
        return FastJsonUtils.resultSuccess(200, "成功", goups);
    }

    /**
     * 读取
     */
    @ApiOperation(value = "编辑", httpMethod = "GET")
    @GetMapping(value = "edit/{id}", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun read(@PathVariable id: Int, request: HttpServletRequest): String {
        val goup = sysAdminGroupService!!.selectByPrimaryKey(id);
        return FastJsonUtils.resultSuccess(200, "成功", goup);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod = "POST")
    @PostMapping(value = "save", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun save(@RequestBody(required = false) record: SysAdminGroup, request: HttpServletRequest): String {
        if (record.id == null) {
            record.pid = 0;
        }
        val row = sysAdminGroupService!!.save(record);
        if (row == 0) {
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }


    /**
     * 更新
     */
    @ApiOperation(value = "更新")
    @PostMapping(value = "update", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun update(@RequestBody(required = false) record: SysAdminGroup, request: HttpServletRequest): String {
        val row = sysAdminGroupService!!.save(record);
        if (row == 0) {
            return FastJsonUtils.resultError(-200, "更新失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "更新成功", null);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun delete(@PathVariable id: Int): String {
        val row = sysAdminGroupService!!.deleteByPrimaryKey(id);
        if (row == 0) {
            return FastJsonUtils.resultError(-200, "删除失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "删除成功", null);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据ids批量删除")
    @PostMapping(value = "deletes", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun deletes(@RequestBody params: Map<String, Object>): String {
        val ids = params.get("ids") as List<Int>
        if (CollectionUtils.isEmpty(ids)) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        try {
            for (id in ids) {
                sysAdminGroupService!!.deleteByPrimaryKey(id);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }

    /**
     * 启用或禁用
     */
    @ApiOperation(value = "根据ids批量启用或禁用")
    @PostMapping(value = "enables", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun enables(@RequestBody params: Map<String, Object>): String {
        val ids = params.get("ids") as List<Int>
        val status = params.get("status") as Byte;
        if (CollectionUtils.isEmpty(ids)) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        try {
            for (id in ids) {
                val record = SysAdminGroup();
                record.id = id
                record.status = status
                sysAdminGroupService!!.updateByPrimaryKeySelective(record);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }
}
