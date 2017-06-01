package cloud.simple.service.domain;

import cloud.simple.service.base.BaseServiceImpl
import cloud.simple.service.dao.SysAdminGroupDao
import cloud.simple.service.dao.SysAdminRuleDao
import cloud.simple.service.model.SysAdminRule
import cloud.simple.service.util.BeanToMapUtil
import cloud.simple.service.util.Category
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class SysAdminRuleService : BaseServiceImpl<SysAdminRule>() {

    @Autowired
    private val sysAdminRuleDao: SysAdminRuleDao? = null;
    @Autowired
    private val sysAdminGroupDao: SysAdminGroupDao? = null;

    override fun getMapper(): Mapper<SysAdminRule> {
        return sysAdminRuleDao!!;
    }

    /**
     * 根据用户名获取rule数组
     * @param userId 用户id
     */
    fun getTreeRuleByUserId(userId: Int): List<SysAdminRule> {
        val rulesList = getRulesByUserId(userId);
        //处理树
        val rulesTreeList = this.buildByRecursiveTree(rulesList);
        return rulesTreeList
    }


    /**
     * 使用递归方法建树
     * @param rootSysAdminRule 原始的数据
     * @return
     */
    fun buildByRecursiveTree(rootSysAdminRules: List<SysAdminRule>): List<SysAdminRule> {
        val trees = Lists.newArrayList<SysAdminRule>()
        for (menu in rootSysAdminRules) {
            if ("0".equals(menu.pid.toString())) {
                trees.add(getChild(menu, rootSysAdminRules, 1));
            }
        }
        return trees;
    }

    /**
     * 递归查找子菜单
     *
     * @param treeMenu
     *            当前菜单id
     * @param treeNodes
     *            要查找的列表
     * @return
     */
    fun getChild(treeMenu: SysAdminRule, treeNodes: List<SysAdminRule>, level: Int): SysAdminRule {
        for (it in treeNodes) {
            if (treeMenu.id == it.pid) {
                if (treeMenu.child == null) {
                    treeMenu.child = Lists.newArrayList();
                }
                treeMenu.child!!.add(getChild(it, treeNodes, level + 1));
            }
        }
        return treeMenu;
    }

    /**
     * 给树状规则表处理成 module-controller-action
     * @return treeNodes
     */
    fun rulesDeal(treeNodes: List<SysAdminRule>): List<String> {
        val ruleStr = Lists.newArrayList<String>();
        if (CollectionUtils.isNotEmpty(treeNodes)) {
            for (root in treeNodes) {
                if (CollectionUtils.isNotEmpty(root.child)) {
                    for (c1 in root.child!!) {
                        if (CollectionUtils.isNotEmpty(c1.child)) {
                            for (c2 in c1.child!!) {
                                ruleStr.add(root.name + "-" + c1.name + "-" + c2.name);
                            }
                        } else {
                            ruleStr.add(root.name + "-" + c1.name);
                        }
                    }
                } else {
                    ruleStr.add(root.name);
                }

            }
        }
        return ruleStr;
    }

    /**
     * 列表页面
     * @param userId 用户id
     * @param type  类型 tree,其它
     * @param status 状态
     * @return
     */
    fun getDataList(userId: Int, type: String): List<Map<String, Any>> {
        var rulesList = getRulesByUserId(userId);
        if (type != null && "tree".equals(type)) {
            //处理树
            rulesList = this.buildByRecursiveTree(rulesList);
            var rawList = Lists.newArrayList<Map<String, Object>>();
            for (m in rawList) {
                val map = BeanToMapUtil.convertBean(m) as MutableMap<String, Any>
                map.put("check", false);
                rawList.add(map as MutableMap<String, Object>);
            }
            return rawList;
        } else {
            val fields = Maps.newHashMap<String, String>();
            fields.put("cid", "id");
            fields.put("fid", "pid");
            fields.put("name", "title");
            fields.put("fullname", "title");
            val rawList = Lists.newArrayList<Map<String, Any>>();
            for (m in rawList) {
                rawList.add(BeanToMapUtil.convertBean(m) as MutableMap<String, Object>);
            }
            val cate = Category(fields, rawList);
            return cate.getList(Integer.valueOf("0"));
        }

    }


    /**
     * 根据用户id查询所属的权限信息
     * @param userId 用户id
     * @return
     */
    fun getRulesByUserId(userId: Int): List<SysAdminRule> {
        var rulesList: List<SysAdminRule>? = listOf();
        //判断是否为管理员
        if (userId.equals(1)) {
            rulesList = sysAdminRuleDao!!.selectByStatus(1);
        } else {
            //查询分组
            val groupsList = sysAdminGroupDao!!.selectByUserId(userId, 1);
            var ruleIds = StringBuffer();
            for (group in groupsList!!) {
                if (ruleIds.length == 0) {
                    ruleIds.append(group.rules);
                } else {
                    ruleIds.append(",").append(group.rules);
                }
            }
            //查询权限
            rulesList = sysAdminRuleDao!!.selectInIds(ruleIds.toString(), 1);
        }
        return rulesList;
    }
}
