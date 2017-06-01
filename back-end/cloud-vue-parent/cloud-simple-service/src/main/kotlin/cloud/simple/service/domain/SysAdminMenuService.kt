package cloud.simple.service.domain

import java.util.ArrayList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.google.common.collect.Lists
import com.google.common.collect.Maps

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.dao.SysAdminGroupDao
import cloud.simple.service.dao.SysAdminMenuDao
import cloud.simple.service.model.SysAdminGroup
import cloud.simple.service.model.SysAdminMenu
import cloud.simple.service.util.BeanToMapUtil
import cloud.simple.service.util.Category
import tk.mybatis.mapper.common.Mapper
import java.awt.SystemColor.menu

@Service
class SysAdminMenuService : BaseServiceImpl<SysAdminMenu>(){
	@Autowired
	private val sysAdminMenuDao: SysAdminMenuDao? = null
	@Autowired
	private val sysAdminGroupDao: SysAdminGroupDao? = null

	override fun getMapper(): Mapper<SysAdminMenu> {
		return sysAdminMenuDao!!
	}

	/**
	 * 获取用户对应的菜单
	 * @param userId
	 * @return
	 */
	fun  getTreeMenuByUserId(userId: Int): List<SysAdminMenu>{
		//查看用户对应未禁用的菜单
		val menusList = getMenusByUserId(userId, 1)
		//处理树菜单
		val menusTreeList = this.buildByRecursiveTree(menusList)
		return menusTreeList
	}

	/**
	 * 根据用户id查询所属的菜单信息
	 * @param userId 用户id
	 * @param status 状态 0：禁用，1：启用，null：全部
	 * @return
	 */
	fun  getMenusByUserId(userId: Int,  status: Byte): List<SysAdminMenu> {
		var menusList: List<SysAdminMenu>
		//判断是否为管理员
		if(userId.equals(1)) {
			val menu = SysAdminMenu()
			menu.status = status
			menusList = this.select(menu)
		} else {
			//查询分组
			val groupsList = sysAdminGroupDao?.selectByUserId(userId, status)
			val ruleIds: StringBuffer =  StringBuffer()
			for(group in groupsList!!) {
				if(ruleIds.length == 0) {
					ruleIds.append(group.rules)
				} else {
					ruleIds.append(",").append(group.rules)
				}
			}
			//查询菜单
			menusList =  sysAdminMenuDao!!.selectInRuleIds(ruleIds.toString(), 1)
		}

		return menusList
	}

	/**
	 * 使用递归方法建树
	 * @param rootSysAdminMenu 原始的数据
	 * @return
	 */
	fun  buildByRecursiveTree(rootSysAdminMenus: List<SysAdminMenu>): List<SysAdminMenu>{
		var trees =  Lists.newArrayList<SysAdminMenu>()
		for( menu in rootSysAdminMenus) {
			if ("0".equals(menu.pid.toString())) {
				trees.add(getChild(menu,rootSysAdminMenus, 1))
			}
		}
		return trees
	}

	/**
	 * 递归查找子菜单
	 *
	 * @param treeMenu
	 *            当前菜单id
	 * @param treeNodes
	 *            要查找的列表
	 * @param level
	 * 			  级别
	 * @return
	 */
	fun getChild(treeMenu: SysAdminMenu,  treeNodes: List<SysAdminMenu>, level: Int): SysAdminMenu {
		treeMenu.selected=false
		treeMenu.level=level
		for (it in treeNodes) {
			if (treeMenu.id == it.pid) {
				treeMenu.child?.add(getChild(it, treeNodes, level + 1))
			}
		}
		return treeMenu
	}


	/**
	 * 查询对应用户Id的菜单
	 * @param userId
	 * @return
	 */
	fun  getDataList(userId: Int,status: Byte): List<Map<String, Any>> {
		val rootSysAdminMenus = this.getMenusByUserId(userId, status)
		val fields = Maps.newHashMap<String, String>()
		fields.put("cid", "id")
		fields.put("fid", "pid")
		fields.put("name", "title")
		fields.put("fullname", "title")
		val rawList = Lists.newArrayList<Map<String, Any>>()
		for (m in rootSysAdminMenus!!) {
			rawList.add(BeanToMapUtil.convertBean(m) as Map<String, Any>)
		}
		val cate: Category = Category(fields as Map<String, String>, rawList)
		return cate.getList(Integer.valueOf("0"))
	}

}

