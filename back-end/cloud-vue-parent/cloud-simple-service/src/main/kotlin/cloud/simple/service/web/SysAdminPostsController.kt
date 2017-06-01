package cloud.simple.service.web;

import cloud.simple.service.domain.SysAdminPostService
import cloud.simple.service.model.SysAdminPost
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
 * 系统岗位控制层
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin/posts")
@Api(value = "SysAdminPostsController", description="系统岗位接口")
public class SysAdminPostsController : CommonController(){
	@Autowired
	private var sysAdminPostService: SysAdminPostService? = null;
	
	/**
	 * 列表
	 */
	@ApiOperation(value = "列表", httpMethod="GET")
	@RequestMapping(value = "", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun index(name: String, request: HttpServletRequest): String {
		val goups = sysAdminPostService!!.getDataList(name);
		return FastJsonUtils.resultSuccess(200, "成功", goups);
	}
	
	/**
	 * 读取
	 */
	@ApiOperation(value = "编辑", httpMethod="GET")
	@GetMapping(value = "edit/{id}", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun read(@PathVariable id: Int, request: HttpServletRequest): String{
		val goup = sysAdminPostService!!.selectByPrimaryKey(id);
		return FastJsonUtils.resultSuccess(200, "成功", goup);
	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", httpMethod="POST")
	@PostMapping(value = "save", produces= arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun save(@RequestBody(required=false) record: SysAdminPost,request: HttpServletRequest): String {
		val row = sysAdminPostService!!.save(record);
		if(row == 0) {
			return FastJsonUtils.resultError(-200, "保存失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "成功", null);
	}
	
	
	/**
	 * 更新
	 */
	@ApiOperation(value = "更新", httpMethod="POST")
	@PostMapping(value = "update", produces= arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun update(@RequestBody(required=false)  record: SysAdminPost, request: HttpServletRequest): String {
		val row = sysAdminPostService!!.save(record);
		if(row == 0) {
			return FastJsonUtils.resultError(-200, "更新失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "更新成功", null);
	}
	
	/**
	 * 删除
	 */
	@ApiOperation(value = "删除")
	@DeleteMapping(value = "delete/{id}", produces= arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun delete(@PathVariable id: Int): String {
		val row = sysAdminPostService!!.deleteByPrimaryKey(id);
		if(row == 0) {
			return FastJsonUtils.resultError(-200, "删除失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "删除成功", null);
	}
	
	/**
	 * 删除
	 */
	@ApiOperation(value = "根据ids批量删除")
	@PostMapping(value = "deletes", produces= arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun deletes(@RequestBody  params: Map<String, Object>): String {
		val ids = params.get("ids") as List<Int>;
		if (CollectionUtils.isEmpty(ids)) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		try {
			for (id in ids) {
				sysAdminPostService!!.deleteByPrimaryKey(id);
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
	@PostMapping(value = "enables", produces= arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun enables(@RequestBody  params: Map<String, Object>): String {
		val ids = params.get("ids") as List<Int>;
		val status = params.get("status") as Byte
		if (CollectionUtils.isEmpty(ids)) {
			return FastJsonUtils.resultError(-200, "操作失败", null);
		}
		try {
			for (id in ids) {
				val record =  SysAdminPost();
				record.id = id;
				record.status=status;
				sysAdminPostService!!.updateByPrimaryKeySelective(record);
			}
		} catch (e: Exception) {
			return FastJsonUtils.resultError(-200, "保存失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "成功", null);
	}
}
