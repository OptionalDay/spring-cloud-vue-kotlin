package cloud.simple.service.util

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by leo on 2017/5/31.
 */
class UploadUtils {
    companion object {
        val allowUploadImageType = "jpg,jpge,bmp,gif,png"

        fun saveMartipartFile(multipartLocation: String?, request: HttpServletRequest, file: MultipartFile): String? {
            return saveMartipartFile(multipartLocation, request, file, null, "yyyyMMdd")
        }

        fun saveMartipartFile(multipartLocation: String?, request: HttpServletRequest, file: MultipartFile, module: String): String? {
            return saveMartipartFile(multipartLocation, request, file, module, "yyyyMMdd")
        }

        fun saveMartipartFile(multipartLocation: String?, request: HttpServletRequest, file: MultipartFile, module: String?, format: String): String? {
            try {
                val simpleDateFormat = SimpleDateFormat(format)
                val dateString = simpleDateFormat.format(Date())
                val dir = File(multipartLocation + "/upload/" + if (StringUtils.isNotEmpty(module) == true) module + "/" + dateString else dateString)
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        throw Exception("创建保存目录失败")
                    }
                }
                val fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.originalFilename).toLowerCase()
                file.transferTo(File(dir, fileName))
                return "/upload/" + (if (StringUtils.isNotEmpty(module) == true) module + "/" + dateString else dateString) + "/" + fileName
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }
}