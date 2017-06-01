package cloud.simple.service.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalDate

/**
 * Created by leo on 2017/5/31.
 */
@Configuration
@EnableSwagger2
open class Swagger2Config {
    /**
     * 通过 createRestApi函数来构建一个DocketBean
     * 函数名,可以随意命名,喜欢什么命名就什么命名
     */
    @Bean
    open fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cloud.simple.service"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate::class.java, String::class.java)
                .genericModelSubstitutes(ResponseEntity::class.java)
                .useDefaultResponseMessages(false)
                .enableUrlTemplating(true)
    }

    //构建 api文档的详细信息函数
    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                //页面标题
                .title("SpringCloud + vue RESTful API")
                //创建人
                .contact("leo.aqing")
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build()
    }
}