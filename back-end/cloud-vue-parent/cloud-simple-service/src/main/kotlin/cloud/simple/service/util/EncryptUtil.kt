package cloud.simple.service.util

import cloud.simple.service.contants.Constant
import org.apache.commons.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * <h3>概要:</h3>
 * 加密工具类
 * <br></br>
 * <h3>功能:</h3>
 *
 *  1. 提供md5\sha1\base64\3des 加密
 *
 *
 *
 */
class  EncryptUtil {
    companion object {
        /*========================================================================================*/
        /*===================================BASE64 部分===========================================*/


        /**
         * **概要：**
         * base64加密
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param data
         * *
         * @return
         */
        fun Base64Encode(data: ByteArray?): ByteArray? {
            return Base64.encodeBase64(data)
        }

        /**
         * **概要：**
         * base64解密
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param base64Data
         * *
         * @return
         */
        fun Base64Decode(base64Data: ByteArray?): ByteArray? {
            return Base64.decodeBase64(base64Data)
        }

        /*===================================BASE64 部分===========================================*/
        /*========================================================================================*/

        /*========================================================================================*/
        /*===================================MD5 部分==============================================*/

        private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")

        /**
         * 转换字节数组为16进制字串
         * @param b 字节数组
         * *
         * @return 16进制字串
         */

        fun byteArrayToHexString(b: ByteArray): String {
            val resultSb = StringBuffer()
            for (i in b.indices) {
                resultSb.append(byteToHexString(b[i]))
            }
            return resultSb.toString()
        }

        /**
         * **概要：**
         * 转换字节数组为16进制字符串
         
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param b
         * *
         * @return
         */
        private fun byteToHexString(b: Byte): String {
            var n = b.toInt()
            if (n < 0)
                n = 256 + n
            val d1 = n / 16
            val d2 = n % 16
            return hexDigits[d1] + hexDigits[d2]
        }

        /**
         * **概要：**
         * MD5 摘要计算
         
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param src
         * *
         * @return
         * *
         * @throws Exception
         */
        @Throws(Exception::class)
        fun md5Digest(src: ByteArray): ByteArray {
            val alg = java.security.MessageDigest
                    .getInstance("MD5") // MD5 is 16 bit message digest

            return alg.digest(src)
        }

        /**
         * **概要：**
         * MD5 摘要计算
         
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param src
         * *
         * @return
         * *
         * @throws Exception
         */
        @Throws(Exception::class)
        fun md5Digest(src: String): String {
            return byteArrayToHexString(md5Digest(src.toByteArray(charset("UTF-8"))))
        }

        /*===================================MD5 部分==============================================*/
        /*========================================================================================*/

        /*========================================================================================*/
        /*===================================SHA1 部分==============================================*/

        /**
         * 实现SHA-1消息摘要
         * @param inStr
         * *
         * @return 成功，返回摘要，失败，返回null
         */
        fun sha1(inStr: String): String {
            var outStr: String? = null
            try {
                outStr = EncryptUtil.digest(inStr.toByteArray(charset("UTF-8")), "SHA-1")
            } catch (e: UnsupportedEncodingException) {
            } catch (e: Exception) {
            }

            return outStr!!
        }

        /**
         * 实现SHA-256消息摘要
         * @param inStr
         * *
         * @return 成功，返回摘要，失败，返回null
         */
        fun sha256(inStr: String): String {
            var outStr: String? = null
            try {
                outStr = EncryptUtil.digest(inStr.toByteArray(charset("UTF-8")), "SHA-256")
            } catch (e: UnsupportedEncodingException) {
            } catch (e: Exception) {
            }

            return outStr!!
        }

        fun digest(inputBytes: ByteArray, algorithm: String): String {
            var outputStr: String? = null
            try {
                val alg = MessageDigest.getInstance(algorithm)
                alg.update(inputBytes)
                val digest = alg.digest()
                outputStr = byte2hex(digest)
            } catch (ex: NoSuchAlgorithmException) {
            }

            return outputStr!!
        }

        /**
         * 二进制转十六进制字符串。每一个字节转为两位十六进制字符串。
         */
        fun byte2hex(b: ByteArray): String {
            var hs = ""
            var stmp = ""
            for (i in b.indices) {
                stmp = Integer.toHexString(b[i].toInt() and 0XFF)
                if (stmp.length == 1) {
                    hs = hs + "0" + stmp
                } else {
                    hs = hs + stmp
                }
            }
            return hs.toUpperCase()
        }

        /*===================================SHA1 部分==============================================*/
        /*========================================================================================*/

        /*========================================================================================*/
        /*===================================3DES 部分=============================================*/

        private val CRYPT_ALGORITHM = "DESede"
        /**
         * 3DES加密模式
         */
        fun encrypt(value: String, key: String): String? {
            try {
                val keySpec = SecretKeySpec(key.toByteArray(), CRYPT_ALGORITHM)
                val cipher = Cipher.getInstance(CRYPT_ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, keySpec)
                val encryptedByte = cipher.doFinal(value.toByteArray())
                val encodedByte = byte2hex(encryptedByte)
                return encodedByte
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }

        /**
         * **概要：**
         * 3DES解密
         
         * **日期：**2016年1月6日
         * TODO 每个参数，返回值注明含义
         * @param value
         * *
         * @param key
         * *
         * @return
         */
        fun decrypt(value: String, key: String): String? {
            try {

                val keySpec = SecretKeySpec(key.toByteArray(), CRYPT_ALGORITHM)
                val cipher = Cipher.getInstance(CRYPT_ALGORITHM)
                cipher.init(Cipher.DECRYPT_MODE, keySpec)
                val decryptedByte = cipher.doFinal(hex2byte(value))
                return String(decryptedByte)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }

        /**
         * **概要：**
         * 十六进制转二进制
         
         * **日期：**2016年1月6日
         * TODO 每个参数，返回值注明含义
         * @param hex
         * *
         * @return
         * *
         * @throws IllegalArgumentException
         */
        @Throws(IllegalArgumentException::class)
        fun hex2byte(hex: String): ByteArray {
            var hex = hex
            if (hex.length % 2 != 0) {
                throw IllegalArgumentException()
            }
            if (hex.startsWith("0x")) {
                hex = hex.substring(2)
            }
            val arr = hex.toCharArray()
            val b = ByteArray(hex.length / 2)
            var i = 0
            var j = 0
            val l = hex.length
            while (i < l) {
                val swap = "" + arr[i++] + arr[i]
                val byteint = Integer.parseInt(swap, 16) and 0xFF
                b[j] = byteint.toByte()
                i++
                j++
            }
            return b
        }
        /*===================================3DES 部分=============================================*/
        /*========================================================================================*/


        /**
         * cookie加密
         * @param data 数据
         * *
         * @param key 指定密匙
         * *
         * @return    返回编码base64后的结果
         */
        fun encryptBase64(data: String, key: String): String {
            val value = EncryptUtil.encrypt(data, key)
            return  String(Base64.encodeBase64(value!!.toByteArray()!!)!!)
        }

        /**
         * cookie 解密
         * @param data 数据
         * *
         * @param key 指定密匙
         * *
         * @return 返回解密后的源数据
         */
        fun decryptBase64(data: String, key: String): String {
            var data1 = String(EncryptUtil.Base64Decode(data.toByteArray(Charsets.UTF_8))!!)
            val value = EncryptUtil.decrypt(data, key)
            return value!!
        }
    }
}



fun main(args: Array<String>) {
    var sourceAuthKey = "admin|e10adc3949ba59abbe56e057f20f883e"
    //加密
    /* var encrypt = EncryptUtil.encrypt(sourceAuthKey, Constant.SECRET_KEY)
     println("加密值："+ encrypt)

     var decrypt = EncryptUtil.decrypt(encrypt!!, Constant.SECRET_KEY)
     println("解密值："+ decrypt)

     //加密
     var encryptBase = EncryptUtil.encryptBase64(sourceAuthKey, Constant.SECRET_KEY)
     println("加密值Base："+ encryptBase)

     var decryptBase = EncryptUtil.decryptBase64(encryptBase!!, Constant.SECRET_KEY)
     println("解密值Base："+ decryptBase)

     var authKey = "YWRtaW58ZTEwYWRjMzk0OWJhNTlhYmJlNTZlMDU3ZjIwZjg4M2U="
     var data1 = String(EncryptUtil.Base64Decode(authKey.toByteArray(Charsets.UTF_8))!!)
     println("EncryptUtil.Base64Decode解密："+ data1!!)
    // val value = EncryptUtil.decrypt(authKey, Constant.SECRET_KEY)
     println("base64Encode:" + String(EncryptUtil.Base64Encode(sourceAuthKey.toByteArray())!!));*/
    var encrypt = EncryptUtil.encrypt(sourceAuthKey, Constant.SECRET_KEY)
    println("加密值："+ encrypt)

    println("ccc:" +  EncryptUtil.encryptBase64(sourceAuthKey, Constant.SECRET_KEY))

    println("aaa:" + String(Base64.encodeBase64("C9E89ECC928743C7E9F8FFD2B4F6275827A0A87C56B199EFE5198D1577DBD6270CAECBE13C778D50".toByteArray())))
}
