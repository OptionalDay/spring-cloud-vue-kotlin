package cloud.simple.service.dao;

import cloud.simple.service.model.SysAdminRule
import cloud.simple.service.util.MyMapper
import org.apache.ibatis.annotations.Param

interface SysAdminRuleDao : MyMapper<SysAdminRule> {

    open fun selectInIds(@Param("ruleIds") ruleIds: String,@Param("status") status: Int): List<SysAdminRule>;

    open fun selectByStatus(@Param("status") status: Int): List<SysAdminRule>;
}