package cloud.simple.service.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.simple.service.domain.SysSystemConfigService;
import cloud.simple.service.model.SysSystemConfig;
import cloud.simple.service.util.FastJsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统配置 控制层
 * @author leo.aqing
 */
@RestController
@RequestMapping("/admin")
@Api(value = "SysConfigController", description = "系统配置接口")
class SysConfigController : CommonController(){
	@Autowired
	private val sysSystemConfigService: SysSystemConfigService? = null;
	
	@ApiOperation(value = "获取配置", httpMethod="POST")
	@PostMapping(value = "/configs", produces = arrayOf("application/json;charset=UTF-8"))
	fun configs(@RequestBody(required=false)  record: SysSystemConfig, request: HttpServletRequest): String {
		val data = mutableMapOf<String,String>()
		val configs = sysSystemConfigService!!.select(record);
		for (c in configs) {
			data.put(c.name!!, c.value!!);
		}
		return FastJsonUtils.resultSuccess(200, "查询配置成功", data);
	}
}
