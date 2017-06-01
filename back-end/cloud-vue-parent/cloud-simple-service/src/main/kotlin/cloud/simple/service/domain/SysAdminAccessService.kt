package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.dao.SysAdminAccessDao
import cloud.simple.service.model.SysAdminAccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class SysAdminAccessService: BaseServiceImpl<SysAdminAccess>(){
	
	@Autowired
	private val sysAdminAccessDao: SysAdminAccessDao? = null;
	
	override fun  getMapper(): Mapper<SysAdminAccess> {
		return sysAdminAccessDao!!;
	}

}
