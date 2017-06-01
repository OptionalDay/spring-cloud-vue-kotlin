package cloud.simple.service.web;

import java.awt.image.BufferedImage;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import cloud.simple.service.contants.Constant;
import cloud.simple.service.domain.SysAdminMenuService;
import cloud.simple.service.domain.SysAdminRuleService;
import cloud.simple.service.domain.SysAdminUserService;
import cloud.simple.service.model.SysAdminRule;
import cloud.simple.service.model.SysAdminUser;
import cloud.simple.service.util.EncryptUtil;
import cloud.simple.service.util.FastJsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 登录控制层
 * @author leo.aqing
 *
 */
@RestController
@RequestMapping("/admin")
@Api(value = "LoginController", description="登录接口")
class LoginController : CommonController(){
	@Autowired
	private var  sysAdminUserService: SysAdminUserService? = null;
	@Autowired
	private var sysAdminRuleService: SysAdminRuleService? = null;
	@Autowired
	private var sysAdminMenuService: SysAdminMenuService? = null;
	@Autowired
	private var captchaProducer: DefaultKaptcha? = null;
	
	/**
	 * 登录
	 * @param record
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "登录", notes = "登录")
	//@ApiImplicitParams(value= arrayOf(@ApiImplicitParam(name = "record", required=true, dataType = "SysAdminUser")))
	@PostMapping(value = "/login", produces = arrayOf("application/json;charset=UTF-8"))
	fun login(@RequestBody  record: SysAdminUser, request: HttpServletRequest): String {
		val data = mutableMapOf<String, Any>();
		if(StringUtils.isBlank(record.username)) {
			return FastJsonUtils.resultError(-100, "账号不能为空", null);
		}
		record.password= DigestUtils.md5Hex(record.password);
		val adminUser: SysAdminUser? = sysAdminUserService!!.selectOne(record);
		if(adminUser==null) {
			return FastJsonUtils.resultError(-100, "帐号与密码错误不正确", null);
		}
		if(adminUser.status!!.compareTo(1.toByte()) != 0 ) {
			return FastJsonUtils.resultError(-100, "帐号已被禁用", null);
		}
		val authKey = EncryptUtil.encryptBase64(adminUser.username+"|"+adminUser.password, Constant.SECRET_KEY);
		// 返回信息
		data.put("rememberKey", authKey);
		data.put("authKey", authKey);
		data.put("sessionId", request.getSession().getId());
		data.put("userInfo", adminUser);
		val rulesTreeList = sysAdminRuleService!!.getTreeRuleByUserId(adminUser.id!!);
		val rulesList = sysAdminRuleService!!.rulesDeal(rulesTreeList);
		data.put("rulesList", rulesList);
		data.put("menusList", sysAdminMenuService!!.getTreeMenuByUserId(adminUser.id!!));
		
		return FastJsonUtils.resultSuccess(200, "登录成功", data);
	}
	
	
	/**
	 * 重新登录
	 * @param rememberKey
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "重新登录", notes = "", httpMethod = "POST")
	//@ApiImplicitParams(value = arrayOf(@ApiImplicitParam(name = "rememberKey", value ="登录成功后的授权码", required = true, dataType = "String")))
	@RequestMapping(value = "/relogin", produces = arrayOf("application/json;charset=UTF-8"))
	fun  relogin(rememberKey: String, request: HttpServletRequest): String {
		val rememberValue = EncryptUtil.decryptBase64(rememberKey, Constant.SECRET_KEY);
		val splits = rememberValue.split("|");
		val record = SysAdminUser();
		record.username=splits[0];
		record.password=splits[1];
		var user = sysAdminUserService!!.selectOne(record);
		if(user == null) {
			return FastJsonUtils.resultError(-400, "重新登录失败", null);
		}
		return FastJsonUtils.resultSuccess(200, "重新登录成功", null);
	}
	
	/**
	 * 登出
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "登出", notes = "")
	@PostMapping(value = "/logout", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun logout(session: HttpSession ): String{
		session.invalidate();
		return FastJsonUtils.resultSuccess(200, "退出成功", null);
	}
	
	/***
	 * 验证码
	 */
	@ApiOperation(value = "验证码", notes = "")
	@GetMapping(value = "/verify")
	fun verify( request: HttpServletRequest,  response: HttpServletResponse){
		response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control",  
                "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");  
  
        val capText = captchaProducer!!.createText();
        println("capText: " + capText);
  
        try {  
            val uuid=UUID.randomUUID().toString();
            //redisTemplate.opsForValue().set(uuid, capText,60*5,TimeUnit.SECONDS);  
            val cookie = Cookie("captchaCode",uuid);
            response.addCookie(cookie);  
        } catch (e: Exception ) {
            e.printStackTrace();  
        }  
  
        val bi = captchaProducer!!.createImage(capText);
        val out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
	}
	
	/**
	 * 修改密码
	 * @param old_pwd
	 * @param new_pwd
	 * @param session
	 */
	@PostMapping(value = "/setInfo", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	@ApiOperation(value = "修改密码", notes = "")
	/*@ApiImplicitParams({
		@ApiImplicitParam(name = "old_pwd", value ="旧密码", required = true, dataType = "String"),
		@ApiImplicitParam(name = "new_pwd", value ="新密码", required = true, dataType = "String")
	})*/
	fun setInfo(old_pwd: String,new_pwd: String): String{
		return sysAdminUserService!!.setInfo(this.getCurrentUser()!!,old_pwd, new_pwd);
	}
}
