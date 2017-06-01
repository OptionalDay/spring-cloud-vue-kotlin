package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table
import javax.persistence.Transient

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_rule`")
class SysAdminRule(

        /**
         * 名称
         */
        @Column(name = "`title`")
        var title: String? = null,

        /**
         * 定义
         */
        @Column(name = "`name`")
        var name: String? = null,

        /**
         * 级别。1模块,2控制器,3操作
         */
        @Column(name = "`level`")
        var level: Byte? = null,

        /**
         * 父id，默认0
         */
        @Column(name = "`pid`")
        var pid: Int? = null,

        /**
         * 状态，1启用，0禁用
         */
        @Column(name = "`status`")
        var status: Byte? = null,

        /**
         * 子权限
         */
        @Transient
        var child: MutableList<SysAdminRule>? = null
) : BaseEntity() {
}