package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.dao.SysAdminGroupDao
import cloud.simple.service.model.SysAdminGroup
import cloud.simple.service.util.BeanToMapUtil
import cloud.simple.service.util.Category
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import tk.mybatis.mapper.entity.Example

@Service
class SysAdminGroupService : BaseServiceImpl<SysAdminGroup>(){

	@Autowired
	private val  sysAdminGroupDao: SysAdminGroupDao? = null
	
	override fun getMapper(): Mapper<SysAdminGroup> {
		return sysAdminGroupDao!!
	}
	/**
	 * 列表
	 * @return
	 */
	fun getDataList(): List<Map<String, Any>> {
		val example =  Example(SysAdminGroup::class.java);
		val rootSysAdminGroups = sysAdminGroupDao?.selectByExample(example);
		val fields = Maps.newHashMap<String, String>()
		fields.put("cid", "id");
		fields.put("fid", "pid");
		fields.put("name", "title");
		fields.put("fullname", "title");
		var rawList  = mutableListOf<Map<String, Any>>()
		for (m in rootSysAdminGroups!!) {
			rawList.add(BeanToMapUtil.convertBean(m) as Map<String, Any>);
		}
		val cate: Category = Category(fields, rawList);
		return cate.getList(Integer.valueOf("0") as Any) as List<Map<String, Any>>;
	}
}
