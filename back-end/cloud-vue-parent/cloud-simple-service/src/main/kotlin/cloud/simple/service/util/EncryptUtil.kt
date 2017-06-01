package cloud.simple.service.util

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
        private val DECODE_TABLE = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 62, 0, 0, 0, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 0, 0, 0, 0, 0, 0, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 0, 0, 0, 0, 0)

        // "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/ "
        private val ENCODE_TABLE = byteArrayOf(65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47)

        init {
            // create encode table
            // ENCODE_TABLE = new byte[64];
            var index = 0
            run {
                var c = 'A'
                while (c <= 'Z') {
                    ENCODE_TABLE[index++] = c.toByte()
                    c++
                }
            }
            run {
                var c = 'a'
                while (c <= 'z') {
                    ENCODE_TABLE[index++] = c.toByte()
                    c++
                }
            }
            var c = '0'
            while (c <= '9') {
                ENCODE_TABLE[index++] = c.toByte()
                c++
            }
            ENCODE_TABLE[index++] = '+'.toByte()
            ENCODE_TABLE[index++] = '/'.toByte()

            // create decode table
            for (i in 0..63)
                DECODE_TABLE[ENCODE_TABLE[i] as Int] = i.toByte()
        }

        /**
         * **概要：**
         * base64加密
         * **作者：**zhouxw
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param data
         * *
         * @return
         */
        fun Base64Encode(data: ByteArray?): ByteArray? {
            if (data == null)
                return null

            val fullGroups = data.size / 3
            var resultBytes = fullGroups * 4
            if (data.size % 3 != 0)
                resultBytes += 4

            val result = ByteArray(resultBytes)
            var resultIndex = 0
            var dataIndex = 0
            var temp = 0
            for (i in 0..fullGroups - 1) {
                temp = data[dataIndex++] as Int and 0xff shl 16 or (data[dataIndex++] as Int and 0xff shl 8) or (data[dataIndex++] as Int and 0xff)

                result[resultIndex++] = ENCODE_TABLE[temp shr 18 and 0x3f]
                result[resultIndex++] = ENCODE_TABLE[temp shr 12 and 0x3f]
                result[resultIndex++] = ENCODE_TABLE[temp shr 6 and 0x3f]
                result[resultIndex++] = ENCODE_TABLE[temp and 0x3f]
            }
            temp = 0
            while (dataIndex < data.size) {
                temp = temp shl 8
                temp = temp or (data[dataIndex++] as Int and 0xff)
            }
            when (data.size % 3) {
                1 -> {
                    temp = temp shl 8
                    temp = temp shl 8
                    result[resultIndex++] = ENCODE_TABLE[temp shr 18 and 0x3f]
                    result[resultIndex++] = ENCODE_TABLE[temp shr 12 and 0x3f]
                    result[resultIndex++] = 0x3D
                    result[resultIndex++] = 0x3D
                }
                2 -> {
                    temp = temp shl 8
                    result[resultIndex++] = ENCODE_TABLE[temp shr 18 and 0x3f]
                    result[resultIndex++] = ENCODE_TABLE[temp shr 12 and 0x3f]
                    result[resultIndex++] = ENCODE_TABLE[temp shr 6 and 0x3f]
                    result[resultIndex++] = 0x3D
                }
                else -> {
                }
            }

            return result
        }

        /**
         * **概要：**
         * base64解密
         * **作者：**zhouxw
         * **日期：**2015年12月17日
         * TODO 每个参数，返回值注明含义
         * @param base64Data
         * *
         * @return
         */
        fun Base64Decode(base64Data: ByteArray?): ByteArray? {
            if (base64Data == null)
                return null
            if (base64Data.size == 0)
                return ByteArray(0)
            if (base64Data.size % 4 != 0)
                throw IllegalArgumentException("数据不完整，长度为： " + base64Data.size)

            var result: ByteArray? = null
            val groupCount = base64Data.size / 4

            var lastData = base64Data.size
            while (base64Data[lastData - 1].toInt() == 0x3D) {
                if (--lastData == 0)
                    return ByteArray(0)
            }
            result = ByteArray(lastData - groupCount)

            var temp = 0
            var resultIndex: Int = 0
            var dataIndex: Int = 0
            while (dataIndex + 4 < base64Data.size) {
                temp = DECODE_TABLE[base64Data[dataIndex++] as Int].toInt()
                temp = (temp shl 6) + DECODE_TABLE[base64Data[dataIndex++] as Int]
                temp = (temp shl 6) + DECODE_TABLE[base64Data[dataIndex++] as Int]
                temp = (temp shl 6) + DECODE_TABLE[base64Data[dataIndex++] as Int]

                result[resultIndex++] = (temp shr 16 and 0xff).toByte()
                result[resultIndex++] = (temp shr 8 and 0xff).toByte()
                result[resultIndex++] = (temp and 0xff).toByte()
            }

            temp = 0
            var j = 0
            while (dataIndex < base64Data.size) {
                temp = (temp shl 6) + DECODE_TABLE[base64Data[dataIndex] as Int]
                dataIndex++
                j++
            }
            while (j < 4) {
                temp = temp shl 6
                j++
            }

            result[resultIndex++] = (temp shr 16 and 0xff).toByte()
            if (!base64Data[(dataIndex - 2) as Int].equals('='))
                result[resultIndex++] = (temp shr 8 and 0xff).toByte()
            if (!base64Data[(dataIndex - 1) as Int].equals('='))
                result[resultIndex++] = (temp and 0xff).toByte()

            return result
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
         * **作者：**zhouxw
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
         * **作者：**zhouxw
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
         * **作者：**zhouxw
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
                stmp = Integer.toHexString(b[i] as Int and 0XFF)
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
         * **作者：**zhouxw
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
         * **作者：**zhouxw
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
            return String(EncryptUtil.Base64Encode(value!!.toByteArray())!!)
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
            var data = data
            data = String(EncryptUtil.Base64Decode(data.toByteArray())!!)
            val value = EncryptUtil.decrypt(data, key)
            return value!!
        }
    }
}
