package cloud.simple.service.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cloud.simple.service.model.SysAdminGroup;
import cloud.simple.service.util.MyMapper;

interface SysAdminGroupDao:  MyMapper<SysAdminGroup>  {
	/**
	 * 查询分组信息
	 * @param userId 用户ID
	 * @param status 状态
	 * @return
	 */
	open fun selectByUserId(@Param("userId") userId: Int,@Param("status") status: Byte): List<SysAdminGroup>;
}