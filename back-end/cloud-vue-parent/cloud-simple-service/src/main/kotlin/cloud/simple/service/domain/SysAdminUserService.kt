package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.contants.Constant
import cloud.simple.service.dao.SysAdminUserDao
import cloud.simple.service.model.SysAdminUser
import cloud.simple.service.util.EncryptUtil
import cloud.simple.service.util.FastJsonUtils
import com.github.pagehelper.PageInfo
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
@Service
class SysAdminUserService: BaseServiceImpl<SysAdminUser>(){
	@Autowired
	var sysAdminUserDao: SysAdminUserDao? = null;
	
	override fun getMapper(): Mapper<SysAdminUser> {
		return sysAdminUserDao!!;
	}
	
	/**
	 * 修改密码
	 * @param currentUser 当前登录的用户信息
	 * @param old_pwd
	 * @param new_pwd
	 * @return 修改失败返回错误信息，修改成功返回authKey信息。
	 */
	fun setInfo(currentUser: SysAdminUser, old_pwd: String, new_pwd: String): String {
		if (currentUser == null){
			return FastJsonUtils.resultError(-400, "请先登录", null);
		}
		
		if (StringUtils.isNotBlank(old_pwd)) {
			return FastJsonUtils.resultError(-400, "旧密码必填", null);
		}
		
		if(StringUtils.isNotBlank(new_pwd)) {
			return FastJsonUtils.resultError(-400, "新密码必填", null);
		}
		
		if (old_pwd.equals(new_pwd)) {
			return FastJsonUtils.resultError(-400, "新旧密码不能一样", null);
		}
		
		if (!currentUser.password.equals(DigestUtils.md5Hex(old_pwd))) {
			return FastJsonUtils.resultError(-400, "原密码错误", null);
		}
		
		if (!currentUser.password.equals(DigestUtils.md5Hex(old_pwd))) {
			return FastJsonUtils.resultError(-400, "原密码错误", null);
		}
		val record = SysAdminUser();
		record.id = currentUser.id
		val md5NewPwd = DigestUtils.md5Hex(new_pwd);
		record.password = md5NewPwd
		sysAdminUserDao!!.updateByPrimaryKeySelective(record);
		val authKey = EncryptUtil.encryptBase64(currentUser.username+"|"+md5NewPwd, Constant.SECRET_KEY);
		//@TODO 更新缓存中auth_key
		return FastJsonUtils.resultError(200, "修改成功", authKey);
	}

	fun getDataList(record: SysAdminUser): PageInfo<SysAdminUser> {
		return super.selectPage(record.page, record.rows, record);
	}

}
