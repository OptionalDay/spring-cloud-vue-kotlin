package cloud.simple.service.util

/**
 * Created by leo on 2017/5/31.
 */

import org.apache.commons.lang.StringUtils
import java.io.*


/****
 * 文件操作类
 * @version 1.0
 */
class FileUtils : org.apache.commons.io.FileUtils() {
    companion object {
        private val BUFFER_SIZE = 16 * 1024
        private val IMG_MAX_SIZE = (2 * 1024).toLong()
        private val vidExt = arrayOf("rm", "rmvb", "mov", "mtv", "dat", "wmv", "avi", "3gp", "amv", "dmv")
        private val imgExt = arrayOf("bmp", "png", "gif", "jpeg", "jpg")
        private val docExt = arrayOf("doc", "docx")

        /**
         * 删除指定目录下的所有文件

         * @param folderPath
         * *            目录路径
         * *
         * @return true:删除成功 false:删除失败
         */
        fun delAllFile(folderPath: String): Boolean {
            var flag = false
            val file = File(folderPath)
            if (!file.exists()) {
                return flag
            }
            if (!file.isDirectory) {
                return flag
            }
            val tempList = file.list()
            var temp: File? = null
            for (i in tempList!!.indices) {
                if (folderPath.endsWith(File.separator)) {
                    temp = File(folderPath + tempList[i])
                } else {
                    temp = File(folderPath + File.separator + tempList[i])
                }
                if (temp.isFile) {
                    temp.delete()
                }
                if (temp.isDirectory) {
                    delAllFile(folderPath + "/" + tempList[i])// 先删除文件夹里面的文件
                    delFolder(folderPath + "/" + tempList[i])// 再删除空文件夹
                    flag = true
                }
            }
            return flag
        }

        /**
         * 删除指定文件

         * @param filePath
         * *            指定文件的路径
         * *
         * @return true:删除成功 false:删除失败
         */
        fun delFile(filePath: String): Boolean {
            var flag = false
            val file = File(filePath)
            if (!file.exists()) {
                return flag
            }
            flag = File(filePath).delete()
            return flag
        }

        /**
         * 删除指定文件夹(包括文件夹下的所有文件)

         * @param folderPath
         * *            指定文件夹路径
         * *
         * @return true:删除成功 false:删除失败
         */
        fun delFolder(folderPath: String): Boolean {
            try {
                delAllFile(folderPath) // 删除完里面所有内容
                var filePath = folderPath
                filePath = filePath.toString()
                val myFilePath = java.io.File(filePath)
                myFilePath.delete() // 删除空文件夹
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

            return true
        }

        /**
         * 读取文本文件的内容

         * @param curfile
         * *            文本文件路径
         * *
         * @return 返回文件内容
         */
        fun readFile(curfile: String): String {
            val f = File(curfile)
            try {
                if (!f.exists())
                    throw Exception()
                val cf = FileReader(curfile)
                val `is` = BufferedReader(cf)
                val filecontent = StringBuilder("")
                var str: String? = `is`.readLine()
                while (str != null) {
                    filecontent.append(str)
                    str = `is`.readLine()
                    if (str != null)
                        filecontent.append(System.getProperty("line.separator", "\n"))
                }
                `is`.close()
                cf.close()
                return filecontent.toString()
            } catch (e: Exception) {
                System.err.println("不能读属性文件: " + curfile + " \n" + e.message)
                return ""
            }

        }

        /**
         * 取指定文件的扩展名

         * @param filePathName
         * *            文件路径
         * *
         * @return 扩展名
         */
        fun getFileExt(filePathName: String): String {
            var pos = 0
            pos = filePathName.lastIndexOf('.')
            if (pos != -1)
                return filePathName.substring(pos + 1, filePathName.length)
            else
                return ""

        }

        /**
         * 读取文件大小

         * @param filename
         * *            指定文件路径
         * *
         * @return 文件大小
         */
        fun getFileSize(filename: String): Int {
            try {
                val fl = File(filename)
                val length = fl.length().toInt()
                return length
            } catch (e: Exception) {
                return 0
            }

        }

        /**
         * 文件拷贝

         * @param src
         * *            源文件
         * *
         * @param dst
         * *            目标文件
         * *
         * @param delete
         * *            是否删除源文件
         */
        fun copyFile(src: File, dst: File, delete: Boolean) {
            try {
                var input: InputStream? = null
                var out: OutputStream? = null
                try {
                    input = BufferedInputStream(FileInputStream(src),
                            BUFFER_SIZE)

                    out = BufferedOutputStream(FileOutputStream(dst),
                            BUFFER_SIZE)
                    val buffer = ByteArray(BUFFER_SIZE)
                    var n = input.read(buffer!!)
                    while (n > 0) {
                        n = input.read(buffer!!)
                        out.write(buffer, 0, n)
                    }
                } finally {
                    if (null != input) {
                        input.close()
                    }
                    if (null != out) {
                        out.close()
                    }
                }
                if (delete)
                    src.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * 获取源文件类型

         * @param ext
         * *
         * @return
         */
        fun getSourceFileType(ext: String): String? {

            for (img in imgExt) {
                if (ext.equals(img, ignoreCase = true))
                    return "IMG"
            }

            for (doc in docExt) {
                if (ext.equals(doc, ignoreCase = true))
                    return "DOC"
            }

            for (vid in vidExt) {
                if (ext.equals(vid, ignoreCase = true))
                    return "VID"
            }

            return null
        }

        /**
         * 检查上传文件有效性

         * @param file
         * *
         * @param fileName
         * *
         * @param type
         * *            1、 图片 2、文档 3、音频
         * *
         * @return
         */
        fun checkUploadFile(file: File, fileName: String,
                            type: Int?): Array<Any> {
            val returns = arrayOf(true, "上传文件有效。")
            if (file.length() > IMG_MAX_SIZE) {
                returns[0] = false
                returns[1] = "上传文件过大,请重新上传。"
                return returns
            }

            val ext = fileName.substring(fileName.lastIndexOf(".") + 1)

            // 上传文件资源类型
            var xtype: String = getSourceFileType(ext)!!
            xtype = if (StringUtils.isNotEmpty(xtype) == true) xtype else "UNKWON"
            when (type) {
                1 -> if (!xtype.equals("IMG", ignoreCase = true)) {
                    returns[0] = false
                    returns[1] = "上传图片文件错误,请重新上传。格式为[bmp, png, gif, jpeg, jpg]"
                }
                2 -> if (!xtype.equals("DOC", ignoreCase = true)) {
                    returns[0] = false
                    returns[1] = "上传文档文件错误,请重新上传。格式为[doc, docx]"
                }
                3 -> if (!xtype.equals("DOC", ignoreCase = true)) {
                    returns[0] = false
                    returns[1] = "上传音频文件错误,请重新上传。格式为[rm,rmvb,mov,mtv,dat,wmv,avi,3gp,amv,dmv]"
                }
                else -> {
                }
            }

            return returns
        }

        fun WriteFile(file_name: String, content: String, append: Boolean, huanhang: Boolean) {
            try {
                val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(file_name, append), "UTF-8"))
                bw.write(content)
                if (huanhang) {
                    bw.newLine()
                }
                bw.flush()
                bw.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

}