package cloud.simple.service

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Created by leo on 2017/5/31.
 */
@Controller
@Configuration
@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaClient
//@EnableWebMvc
@MapperScan(basePackages = arrayOf("cloud.simple.service.dao", "com.framework.common.base"))
open class CloudServiceApplication : WebMvcConfigurerAdapter() {
}

fun main(args:Array<String>) {
    SpringApplicationBuilder(CloudServiceApplication::class.java).web(true).run(*args)
}