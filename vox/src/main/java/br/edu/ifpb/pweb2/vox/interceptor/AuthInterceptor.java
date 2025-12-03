package br.edu.ifpb.pweb2.vox.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        Object usuario = request.getSession().getAttribute("usuario");


        // se não estiver logado e tentando acessar algo diferente de /vox/auth redireciona para o login!!
        if (usuario == null && !request.getRequestURI().contains(request.getContextPath() + "/auth")) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // Sem uso
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Sem uso
    }
}
