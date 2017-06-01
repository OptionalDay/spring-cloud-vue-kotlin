package cloud.simple.service.conf

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException
import javax.sql.DataSource

/**
 * Created by leo on 2017/5/31.
 */
@Configuration
@EnableConfigurationProperties(DruidProperties::class)
@ConditionalOnClass(DruidDataSource::class)
@ConditionalOnProperty(prefix = "druid", name = arrayOf("url"))
@AutoConfigureBefore(DataSourceAutoConfiguration::class)
open class DruidAutoConfiguration {
    @Autowired
    private val properties: DruidProperties? = null

    @Bean
    open fun dataSource(): DataSource {
        val dataSource: DruidDataSource = DruidDataSource()
        dataSource.url = properties?.url
        dataSource.username = properties?.username
        dataSource.password = properties?.password
        if (properties?.initialSize!! > 0) {
            dataSource.initialSize = properties?.initialSize!!
        }
        if (properties?.minIdle!! > 0) {
            dataSource.minIdle = properties?.minIdle!!
        }
        if (properties?.maxActive!! > 0) {
            dataSource.maxActive = properties?.maxActive!!
        }
        dataSource.isTestOnBorrow = properties?.testOnBorrow!!
        try {
            dataSource.init()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return dataSource
    }
}