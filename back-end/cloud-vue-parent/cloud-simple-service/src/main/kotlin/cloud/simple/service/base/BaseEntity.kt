package cloud.simple.service.base;

import java.io.Serializable
import javax.persistence.*
/**
 * 实体基类
 * @author leo.aqing
 *
 */
@Entity
open class BaseEntity(
		@Id
		@Column(name = "Id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		var  id: Int? = null,
		@Transient var  page: Int = 1,
		@Transient var rows: Int  = 10
): Serializable {


}
