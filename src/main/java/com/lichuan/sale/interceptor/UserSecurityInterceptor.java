package com.lichuan.sale.interceptor;

import com.alibaba.fastjson.JSON;
import com.lichuan.sale.annoation.SysContent;
import com.lichuan.sale.model.User;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.service.SysUserService;
import com.lichuan.sale.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserSecurityInterceptor implements HandlerInterceptor {

    //存放所有不需要拦截的接口
    private static List<String> pageList = new ArrayList<>();
    private UserService userService;
    private SysUserService sysUserService;

    static {
        pageList.add("login");
        pageList.add("index");
        pageList.add("register");
        pageList.add("getVerCode");
        pageList.add("checkVersion");
        pageList.add("getArea");
        pageList.add("searchKey");
        pageList.add("listener");
        pageList.add("getProducts");
        pageList.add("getProductSaleNum");
        pageList.add("getCategories");
        pageList.add("getNotice");
        pageList.add("getStorageList");
        pageList.add("getSaler");
        pageList.add("notifyInfo");
        pageList.add("updateApk");

    }


    /**
     * 接口验证规则：
     * 从header中获取到appkey、timestamp、signature
     * 通过appkey获取用户信息（包括secret）
     * 判断SHA1(appkey_secret_timestamp)和signatrue是否一致，不一致则提示签名不正确，
     * 如果一致，则提取用户的权限，如果权限里面的地址包含了当前的地址，则允许通过，否则提示NO_AUTH
     * 为了安全考虑，强烈建议不要再客户端显示secret，一般通过登录，让服务器返回
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        //response.setCharacterEncoding("UTF-8");
        SingleResult result = new SingleResult();
        response.setCharacterEncoding("UTF-8");
        String url = request.getRequestURL().toString();
        inject(request);
        User user = null;
        if (!contains(pageList, url)) {
            boolean pass = validateSign(request);
            if (pass) {
                String token = request.getParameter("token");
                String flag = request.getParameter("flag");
                if ("sys".equals(flag)) {
                    user = sysUserService.getUserByToken(token);
                } else {
                    user = userService.getUserByToken(token);
                }
                if (null != user) {
                    if (user.getStatus_() == 1) {
                        SysContent.setUser(user);
                        //保存日志 没写
                    } else {
                        result.setCode(Code.DISABLED);
                        response.getWriter().print(JSON.toJSONString(result));
                        return false;
                    }
                } else {
                    result.setCode(Code.EXP_TOKEN);
                    response.getWriter().print(JSON.toJSONString(result));
                    return false;
                }
            } else {
                result.setCode(Code.EXP_SIGNATURE);
                response.getWriter().print(JSON.toJSONString(result));
                return false;
            }
        }
        return true;

    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getHeader("sign");//获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList(request.getParameterMap().keySet());
        Collections.sort(keys);//排序
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);//去除最后一个'&'
        String APP_SECRET = "lichuan";//密钥，自己修改
        String sign = DigestUtils.sha1Hex(linkString + APP_SECRET);//混合密钥md5
        return StringUtils.equals(sign, requestSign);//比较
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

    /**
     * 是否包含指定字符串，模糊匹配
     *
     * @param list
     * @param value
     * @return
     */
    private boolean contains(List<String> list, String value) {
        if (null != list && list.size() > 0) {
            for (String str : list) {
                if (value.indexOf(str) > 0) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void inject(HttpServletRequest request) {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        if (null == userService) {
            userService = factory.getBean(UserService.class);
        }
        if (null == sysUserService) {
            sysUserService = factory.getBean(SysUserService.class);
        }

    }

}
