package cloud.simple.service.web;

import cloud.simple.service.contants.Constant
import cloud.simple.service.domain.SysAdminUserService
import cloud.simple.service.model.SysAdminUser
import cloud.simple.service.util.EncryptUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 公共控制器
 * @author leo
 *
 */
open class CommonController {
	@Autowired
	private var sysAdminUserService: SysAdminUserService? = null;
	
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
	fun getCurrentUser(): SysAdminUser?{
		val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).getRequest();
		val authKey = request.getHeader(Constant.AUTH_KEY);
		if(StringUtils.isNotBlank(authKey)) {
			val decryptAuthKey = EncryptUtil.decryptBase64(authKey, Constant.SECRET_KEY);
			val auths = decryptAuthKey.split("\\|");
			val username = auths[0];
			val password = auths[1];
			val record = SysAdminUser();
			record.username=username;
			record.password=password;
			return sysAdminUserService!!.selectOne(record);
		}
		return null;
	}
}
