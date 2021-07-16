package com.zjj.coursestudy.filter;

import com.zjj.coursestudy.vo.JwtToken;
import com.zjj.coursestudy.utils.JwtUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查有没有需要用户权限的注解 没有注解需要验证
        if (!method.isAnnotationPresent(JwtToken.class)) {
            /*
            JwtToken jwtToken = method.getAnnotation(JwtToken.class);
            if (!jwtToken.unRequired()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 userName
                String userName = JwtUtil.getUserName(token);

                // 验证 token
                JwtUtil.checkSign(token);
            }
             */
            if (token == null) {
                throw new RuntimeException("无token，请重新登录");
            }
            // 验证 token
            JwtUtil.checkSign(token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}