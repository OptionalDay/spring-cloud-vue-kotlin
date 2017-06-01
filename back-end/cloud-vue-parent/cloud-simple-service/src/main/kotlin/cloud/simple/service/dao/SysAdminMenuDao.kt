package cloud.simple.service.dao;

import org.apache.ibatis.annotations.Param;

import cloud.simple.service.model.SysAdminMenu;
import cloud.simple.service.util.MyMapper;

interface SysAdminMenuDao:  MyMapper<SysAdminMenu>  {
	/**
	 * 根据ruleIds查询菜单信息
	 * @param ruleIds 权限id
	 * @param status 状态值
	 * @return List<SysAdminMenu>
	 */
	open fun selectInRuleIds(@Param("ruleIds") ruleIds: String, @Param("status") status: Int): List<SysAdminMenu>;
}