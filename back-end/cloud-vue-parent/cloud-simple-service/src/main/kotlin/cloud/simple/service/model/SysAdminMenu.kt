package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import com.google.common.collect.Lists
import javax.persistence.Column
import javax.persistence.Table
import javax.persistence.Transient
/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_menu`")
class SysAdminMenu(
        /**
         * 上级菜单ID
         */
        @Column(name = "`pid`") var pid: Int? = null,
        /**
         * 菜单名称
         */
        @Column(name = "`title`") var title: String? = null,

        /**
         * 链接地址
         */
        @Column(name = "`url`") var url: String? = null,

        /**
         * 图标
         */
        @Column(name = "`icon`") var icon: String? = null,

        /**
         * 菜单类型
         */
        @Column(name = "`menu_type`")
        var menuType: Byte? = null,

        /**
         * 排序（同级有效）
         */
        @Column(name = "`sort`") var sort: Byte? = null,

        /**
         * 状态
         */
        @Column(name = "`status`") var status: Byte? = null,

        /**
         * 权限id
         */
        @Column(name = "`rule_id`") var ruleId: Int? = null,

        @Column(name = "`rule_name`") var ruleName: String? = null,

        @Column(name = "`module`") var module: String? = null,

        /**
         * 三级菜单吗
         */
        @Column(name = "`menu`") var menu: String? = null,

        /**
         * 子菜单
         */
        @Transient
        var child: MutableList<SysAdminMenu>? = Lists.newArrayList<SysAdminMenu>(),
        /**
         * 是否选中
         */
        @Transient var selected: Boolean? = false,
        /**
         * 级别
         */
        @Transient var level: Int? = null,

        /**
         * 全名
         */
        @Transient var fullName: String? = null,

        /**
         * 关键权限
         */
        @Transient var rule: SysAdminRule? = null

) : BaseEntity() {
}