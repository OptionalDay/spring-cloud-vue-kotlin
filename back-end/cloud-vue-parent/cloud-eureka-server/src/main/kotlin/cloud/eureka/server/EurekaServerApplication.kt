package cloud.eureka.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
open class EurekaServerApplication {
}

fun main(args:Array<String>) {
    SpringApplicationBuilder(EurekaServerApplication::class.java).web(true).run(*args)
}