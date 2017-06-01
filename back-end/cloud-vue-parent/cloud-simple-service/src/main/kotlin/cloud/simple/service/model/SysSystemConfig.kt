package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_system_config`")
class SysSystemConfig(
        @Column(name = "`name`")
        var name: String? = null,
        /**
         * 配置值
         */
        @Column(name = "`value`")
        var value: String? = null,
        /**
         * 配置分组
         */
        @Column(name = "`group`")
        var group: Byte? = null,
        /**
         * 1需要登录后才能获取，0不需要登录即可获取
         */
        @Column(name = "`need_auth`")
        var needAuth: Byte? = null
) : BaseEntity() {

}