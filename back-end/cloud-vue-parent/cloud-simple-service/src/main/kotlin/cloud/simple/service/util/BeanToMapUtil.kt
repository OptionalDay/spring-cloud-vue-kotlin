package cloud.simple.service.util

import java.beans.BeanInfo
import java.beans.IntrospectionException
import java.beans.Introspector
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Created by leo on 2017/6/1.
 */
object BeanToMapUtil {
    /**
     * 将一个 Map 对象转化为一个 JavaBean

     * @param type
     * *            要转化的类型
     * *
     * @param map
     * *            包含属性值的 map
     * *
     * @return 转化出来的 JavaBean 对象
     * *
     * @throws IntrospectionException
     * *             如果分析类属性失败
     * *
     * @throws IllegalAccessException
     * *             如果实例化 JavaBean 失败
     * *
     * @throws InstantiationException
     * *             如果实例化 JavaBean 失败
     * *
     * @throws InvocationTargetException
     * *             如果调用属性的 setter 方法失败
     */
    @Throws(IntrospectionException::class, IllegalAccessException::class, InstantiationException::class, InvocationTargetException::class)
    fun convertMap(type: Class<*>, map: Map<String, Any>): Any {
        val beanInfo = Introspector.getBeanInfo(type) // 获取类属性
        val obj = type.newInstance() // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        val propertyDescriptors = beanInfo
                .propertyDescriptors
        for (i in propertyDescriptors.indices) {
            val descriptor = propertyDescriptors[i]
            val propertyName = descriptor.name

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                val value = map[propertyName]

                val args = arrayOfNulls<Any>(1)
                args[0] = value

                descriptor.writeMethod.invoke(obj, *args)
            }
        }
        return obj
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map

     * @param bean
     * *            要转化的JavaBean 对象
     * *
     * @return 转化出来的 Map 对象
     * *
     * @throws IntrospectionException
     * *             如果分析类属性失败
     * *
     * @throws IllegalAccessException
     * *             如果实例化 JavaBean 失败
     * *
     * @throws InvocationTargetException
     * *             如果调用属性的 setter 方法失败
     */
    fun convertBean(bean: Any): Map<String, Any> {
        val type = bean.javaClass
        val returnMap = HashMap<String, Any>()
        val beanInfo: BeanInfo
        try {
            beanInfo = Introspector.getBeanInfo(type)
            val propertyDescriptors = beanInfo
                    .propertyDescriptors
            for (i in propertyDescriptors.indices) {
                val descriptor = propertyDescriptors[i]
                val propertyName = descriptor.name
                if (propertyName != "class") {
                    val readMethod = descriptor.readMethod
                    val result = readMethod.invoke(bean, *arrayOfNulls<Any>(0))
                    if (result != null) {
                        returnMap.put(propertyName, result)
                    } else {
                        returnMap.put(propertyName, "")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return returnMap
    }
}