package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_access`")
class SysAdminAccess(
        @Column(name = "`user_id`")  var userId: Int? = null,
        @Column(name = "`group_id`")  var groupId: Int? = null
) : BaseEntity() {
}