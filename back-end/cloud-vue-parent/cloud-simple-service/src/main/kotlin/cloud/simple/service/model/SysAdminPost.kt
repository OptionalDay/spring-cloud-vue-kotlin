package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_post`")
class SysAdminPost(
        /**
         * 岗位名称
         */
        @Column(name = "`name`")
        var name: String? = null,

        /**
         * 岗位备注
         */
        @Column(name = "`remark`")
        var remark: String? = null,

        /**
         * 数据创建时间
         */
        @Column(name = "`create_time`")
        var createTime: Int? = null,

        /**
         * 状态1启用,0禁用
         */
        @Column(name = "`status`")
        var status: Byte? = null

) : BaseEntity() {
}