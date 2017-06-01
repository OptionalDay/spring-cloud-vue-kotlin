package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_structure`")
class SysAdminStructure(
        @Column(name = "`name`")
        var name: String? = null,

        @Column(name = "`pid`")
        var pid: Int? = null,

        @Column(name = "`status`")
        var status: Byte? = null
) : BaseEntity() {

}