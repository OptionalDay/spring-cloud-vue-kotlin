package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.model.SysAdminPost
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import tk.mybatis.mapper.entity.Example

@Service
class SysAdminPostService : BaseServiceImpl<SysAdminPost>() {

    @Autowired
    private val sysAdminPostDao: Mapper<SysAdminPost>? = null;

    override fun getMapper(): Mapper<SysAdminPost> {
        return sysAdminPostDao!!
    }

    fun getDataList(name: String): List<SysAdminPost> {
        val example = Example(SysAdminPost::class.java, false);
        val criteria = example.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("name", name);
        }
        return sysAdminPostDao!!.selectByExample(example);
    }

}
