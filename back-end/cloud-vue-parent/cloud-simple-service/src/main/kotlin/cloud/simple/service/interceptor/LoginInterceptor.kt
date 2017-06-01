package cloud.simple.service.interceptor;

import cloud.simple.service.contants.Constant
import cloud.simple.service.domain.SysAdminUserService
import cloud.simple.service.model.SysAdminUser
import cloud.simple.service.util.EncryptUtil
import cloud.simple.service.util.FastJsonUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Component("loginInterceptor")
class LoginInterceptor : HandlerInterceptorAdapter() {

    @Autowired
    private var sysAdminUserService: SysAdminUserService? = null;

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        println("请求url:" + request.getRequestURI());
        println("header auth key:" + request.getHeader(Constant.AUTH_KEY));
        val authKey = request.getHeader(Constant.AUTH_KEY);
        val sessionId = request.getHeader(Constant.SESSION_ID);
        val session: HttpSession = request.getSession();
        // 校验sessionid和authKey
        if (StringUtils.isEmpty(authKey) || StringUtils.isEmpty(sessionId)) {
            response.contentType = "application/json;charset=UTF-8";
            val writer = response.getWriter();
            writer.write(FastJsonUtils.resultError(-100, "authKey或sessionId不能为空！", null));
            writer.flush();
            return false;
        }

        //检查账号有效性
        var sessionAdminUser = session.getAttribute(Constant.LOGIN_ADMIN_USER) as SysAdminUser?;
        if (sessionAdminUser == null) {
            val decryptAuthKey = EncryptUtil.decryptBase64(authKey, Constant.SECRET_KEY);
            val auths = decryptAuthKey.split("\\|");
            val username = auths[0];
            val password = auths[1];
            val record = SysAdminUser();

            record.username = username;
            record.password = password;
            sessionAdminUser = sysAdminUserService!!.selectOne(record);
            //设置登录用户id
            session.setAttribute(Constant.LOGIN_ADMIN_USER, sessionAdminUser);
        }

        if (sessionAdminUser == null || sessionAdminUser.status!!.equals(0)) {
            response.contentType = "application/json;charset=UTF-8";
            val writer = response.getWriter();
            writer.write(FastJsonUtils.resultError(-101, "账号已被删除或禁用", null));
            writer.flush();
            return false;
        }

        return true;
    }
}
