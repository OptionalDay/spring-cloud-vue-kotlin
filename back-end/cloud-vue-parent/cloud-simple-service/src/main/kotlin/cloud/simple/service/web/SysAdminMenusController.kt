package cloud.simple.service.web;

import cloud.simple.service.domain.SysAdminMenuService
import cloud.simple.service.model.SysAdminMenu
import cloud.simple.service.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.List
import java.util.Map
import javax.servlet.http.HttpServletRequest

/**
 * 系统菜单控制层
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/menus")
@Api(value = "SysAdminMenusController", description="系统菜单接口")
class SysAdminMenusController : CommonController(){
	@Autowired
	private var sysAdminMenuService: SysAdminMenuService? = null;

	/**
	 * 列表
	 */
	@ApiOperation(value = "列表", httpMethod="GET")
	@RequestMapping(value = "", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun index(@RequestBody(required=false) record: SysAdminMenu,request: HttpServletRequest): String {
		val menus = sysAdminMenuService!!.getDataList(this.getCurrentUser()!!.id!!, record.status!!);
		return FastJsonUtils.resultSuccess(200, "成功", menus);
	}

	/**
	 * 读取
	 */
	@ApiOperation(value = "编辑", httpMethod="GET")
	@GetMapping(value = "edit/{id}", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun read(@PathVariable id: Integer, request: HttpServletRequest): String {
		val menu = sysAdminMenuService!!.selectByPrimaryKey(id);
		return FastJsonUtils.resultSuccess(200, "成功", menu);
	}

	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", httpMethod="POST")
	@PostMapping(value = "save", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun save(@RequestBody(required=false)  record: SysAdminMenu, request: HttpServletRequest): String {
		val row = sysAdminMenuService!!.save(record);
		if(row == 0) {
			return FastJsonUtils.resultError(-200, "保存失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "成功", null);
	}


	/**
	 * 更新
	 */
	@ApiOperation(value = "更新", httpMethod="POST")
	@PostMapping(value = "update", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun update(@RequestBody(required=false)  record: SysAdminMenu, request: HttpServletRequest): String {
		val row = sysAdminMenuService!!.save(record);
		if(row == 0) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "操作成功", null);
	}

	/**
	 * 删除
	 */
	@ApiOperation(value = "删除")
	@DeleteMapping(value = "delete/{id}", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun delete(@PathVariable  id: Int): String {
		val row = sysAdminMenuService!!.deleteByPrimaryKey(id);
		if(row == 0) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "操作成功", null);
	}

	/**
	 * 删除
	 */
	@ApiOperation(value = "根据ids批量删除")
	@PostMapping(value = "deletes", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun deletes(@RequestBody  params: Map<String, Object>):String {
		val ids = params!!.get("ids") as List<Integer>;
		if (CollectionUtils.isEmpty(ids)) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		try {
			for (id in ids) {
				sysAdminMenuService!!.deleteByPrimaryKey(id);
			}
		} catch (e: Exception) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "操作成功", null);
	}

	/**
	 * 启用或禁用
	 */
	@ApiOperation(value = "根据ids批量启用或禁用")
	@PostMapping(value = "enables", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun enables(@RequestBody params: Map<String, Object>): String {
		val ids = params.get("ids") as List<Integer>
		val status = params.get("status") as Byte
		if (CollectionUtils.isEmpty(ids)) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		try {
			for (id in ids) {
				val record = SysAdminMenu();
				record.id = Integer.valueOf(id.toString()!!);
				record.status = status;
				sysAdminMenuService!!.updateByPrimaryKeySelective(record);
			}
		} catch (e : Exception) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "成功", null);
	}
}
