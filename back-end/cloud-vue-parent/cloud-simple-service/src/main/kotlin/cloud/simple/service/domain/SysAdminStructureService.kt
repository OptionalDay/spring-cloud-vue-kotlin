package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.dao.SysAdminStructureDao
import cloud.simple.service.model.SysAdminStructure
import cloud.simple.service.util.BeanToMapUtil
import cloud.simple.service.util.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import tk.mybatis.mapper.entity.Example

@Service
class SysAdminStructureService : BaseServiceImpl<SysAdminStructure>() {

    @Autowired
    var sysAdminStructureDao: SysAdminStructureDao? = null;


    override fun getMapper(): Mapper<SysAdminStructure> {
        return sysAdminStructureDao!!;
    }

    fun getDataList(): List<Map<String, Any>> {
        var example = Example(SysAdminStructure::class.java);
        example.orderByClause = " id asc";
        val rootSysAdminStructure = sysAdminStructureDao!!.selectByExample(example);
        val fields: MutableMap<String, String> = mutableMapOf()
        fields.put("cid", "id");
        fields.put("fid", "pid");
        fields.put("name", "name");
        fields.put("fullname", "title");
        val rawList = mutableListOf<Map<String, Any>>()
        for (m in rootSysAdminStructure) {
            rawList.add(BeanToMapUtil.convertBean(m))
        }
        val cate = Category(fields, rawList);
        return cate.getList(Integer.valueOf("0"));
    }

}
