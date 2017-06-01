package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_user`")
class SysAdminUser(
        /**
         * 管理后台账号
         */
        @Column(name = "`username`")
        var username: String? = null,
        /**
         * 管理后台密码
         */
        @Column(name = "`password`")
        var password: String? = null,
        /**
         * 用户备注
         */
        @Column(name = "`remark`")
        var remark: String? = null,

        @Column(name = "`create_time`")
        var createTime: Int? = null,
        /**
         * 真实姓名
         */
        @Column(name = "`realname`")
        var realname: String? = null,
        /**
         * 部门
         */
        @Column(name = "`structure_id`")
        var structureId: Int? = null,
        /**
         * 岗位
         */
        @Column(name = "`post_id`")
        var postId: Int? = null,
        /**
         * 状态,1启用0禁用
         */
        @Column(name = "`status`")
        var status: Byte? = null
) : BaseEntity() {

}