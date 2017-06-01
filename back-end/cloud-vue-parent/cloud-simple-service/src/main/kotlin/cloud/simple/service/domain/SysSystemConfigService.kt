package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.dao.SysSystemConfigDao
import cloud.simple.service.model.SysSystemConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
@Service
class SysSystemConfigService: BaseServiceImpl<SysSystemConfig>(){
	@Autowired
	private var sysSystemConfigDao: SysSystemConfigDao? = null;
	
	override fun  getMapper(): Mapper<SysSystemConfig> {
		return sysSystemConfigDao!!;
	}

}
