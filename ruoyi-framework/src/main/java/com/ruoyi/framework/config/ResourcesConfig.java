package com.ruoyi.framework.config;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 通用配置
 * 
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + RuoYiConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

    /**
     * 消息转换器配置
     */
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter()
    {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converter.setWriteAcceptCharset(true);
        return converter;
    }

    @Bean
    public org.springframework.http.converter.json.MappingJackson2HttpMessageConverter jackson2HttpMessageConverter()
    {
        org.springframework.http.converter.json.MappingJackson2HttpMessageConverter converter = new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter();
        org.springframework.http.MediaType mediaType = new org.springframework.http.MediaType("application", "json", StandardCharsets.UTF_8);
        java.util.List<org.springframework.http.MediaType> mediaTypes = new java.util.ArrayList<>();
        mediaTypes.add(mediaType);
        converter.setSupportedMediaTypes(mediaTypes);
        return converter;
    }

    @Override
    public void extendMessageConverters(java.util.List<org.springframework.http.converter.HttpMessageConverter<?>> converters)
    {
        // 确保StringHttpMessageConverter使用UTF-8编码
        for (org.springframework.http.converter.HttpMessageConverter<?> converter : converters)
        {
            if (converter instanceof StringHttpMessageConverter)
            {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(true);
            }
            else if (converter instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter)
            {
                ((org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
                org.springframework.http.MediaType mediaType = new org.springframework.http.MediaType("application", "json", StandardCharsets.UTF_8);
                java.util.List<org.springframework.http.MediaType> mediaTypes = new java.util.ArrayList<>();
                mediaTypes.add(mediaType);
                ((org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypes);
            }
        }
    }
}