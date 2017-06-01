package cloud.config.server;

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Configuration

/**
 * Created by leo on 2017/5/31.
 */
@Configuration
@EnableAutoConfiguration
@EnableEurekaClient //注解是基于spring-cloud-netflix依赖，只能为eureka作用
//@EnableDiscoveryClient  //注解是基于spring-cloud-commons依赖，并且在classpath中实现
@EnableConfigServer
open class ConfigServerApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(ConfigServerApplication::class.java, *args);
}

