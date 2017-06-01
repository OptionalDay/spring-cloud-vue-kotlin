package cloud.simple.service.conf

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by leo on 2017/5/31.
 */
@ConfigurationProperties(prefix = "druid")
open class DruidProperties {
    
     var url: String? = null
     var username: String? = null
     var password: String? = null
     var driverClass: String? = null
     var maxActive: Int = 0
     var minIdle: Int = 0
     var initialSize: Int = 0
     var testOnBorrow: Boolean = false
}