package cloud.simple.service.conf

import com.google.code.kaptcha.impl.DefaultKaptcha
import com.google.code.kaptcha.util.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*


/**
 * Created by leo on 2017/5/31.
 */
@Configuration
open class CaptchaConfig{
    @Bean
    open fun getKaptchaBean(): DefaultKaptcha {
        val defaultKaptcha = DefaultKaptcha()
        val properties: Properties = Properties()
        properties.setProperty("kaptcha.border", "yes")
        properties.setProperty("kaptcha.border.color", "105,179,90")
        properties.setProperty("kaptcha.textproducer.font.color", "blue")
        properties.setProperty("kaptcha.image.width", "125")
        properties.setProperty("kaptcha.image.height", "45")
        properties.setProperty("kaptcha.session.key", "code")
        properties.setProperty("kaptcha.textproducer.char.length", "4")
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑")
        defaultKaptcha.config = Config(properties);
        return defaultKaptcha;
    }
}



