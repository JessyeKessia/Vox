package br.edu.ifpb.pweb2.vox.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.pweb2.vox.interceptor.AuthInterceptor;

@Configuration
public class IntercepdorConfiguration implements WebMvcConfigurer  {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authInterceptor)
                .addPathPatterns("/**", "/assuntos/**", "/processos/**")
                .excludePathPatterns("/auth/**", "/css/**", "/imagens/**");
    }

}
