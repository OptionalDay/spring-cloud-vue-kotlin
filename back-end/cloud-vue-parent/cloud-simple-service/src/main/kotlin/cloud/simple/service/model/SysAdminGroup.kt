package cloud.simple.service.model

import cloud.simple.service.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Table

/**
 * Created by leo on 2017/5/31.
 */
@Table(name = "`sys_admin_group`")
open class SysAdminGroup(
        @Column(name = "`title`")  var title: String? = null,
        @Column(name = "`rules`")  var rules: String? = null,
        @Column(name = "`pid`")    var pid: Int? = null,
        @Column(name = "`remark`")  var remark: String? = null,
        @Column(name = "`status`")  var status: Byte? = null
): BaseEntity(){
}