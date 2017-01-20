package com.tiro.study.spring.config;

import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;
import java.util.Set;

/**
 * @author wb-liyuan.j
 * @date 2016-12-30
 */
@Configuration
public class RootConfig {
    @Bean
    public JavaMailSenderImpl mainSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setProtocol("smtp");
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(25);
        mailSender.setUsername("18758595684@163.com");
        mailSender.setPassword("3x1415926");

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "false");
        prop.put("mail.smtp.timeout", "25000");
        prop.put("mail.debug", "true");
        mailSender.setJavaMailProperties(prop);

        return mailSender;
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
        propertyConfigurer.setLocations(new ClassPathResource("mail/mail.properties"));
        return propertyConfigurer;
    }

    @Bean
    public VelocityEngineFactoryBean velocity() {
        VelocityEngineFactoryBean velocity = new VelocityEngineFactoryBean();
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocity.setVelocityProperties(props);
        return velocity;
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarker(){
        FreeMarkerConfigurationFactoryBean freeMarker = new FreeMarkerConfigurationFactoryBean();
        freeMarker.setTemplateLoaderPath("classpath:mail/");
        return freeMarker;
    }

    @Bean
    public SpringTemplateEngine thymeleaf(Set<ITemplateResolver> resolvers){
        SpringTemplateEngine thymeleaf = new SpringTemplateEngine();
        thymeleaf.setTemplateResolvers(resolvers);
        return thymeleaf;
    }

    @Bean
    ClassLoaderTemplateResolver emailTemplateResolver(){
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("mail/");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        return resolver;
    }


}

