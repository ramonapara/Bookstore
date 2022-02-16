package com.bookstore.Bookstore.security;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(
            ApplicationContext applicationContext
    ) {
        /* Utilizat pentru ca Spring sa detecteze bean urile */
        MessageDispatcherServlet result = new MessageDispatcherServlet();

        result.setApplicationContext(applicationContext);
        result.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(result, "/login/*");
    }

    @Bean(name = "Autentificare")
    public DefaultWsdl11Definition defaultWsdl11Definition(
            XsdSchema authSchema
    ) {
        /* Expune standardul WSDL 1.1 folosind XsdSchema */
        DefaultWsdl11Definition result = new DefaultWsdl11Definition();

        result.setPortTypeName("AutentificarePort");
        result.setLocationUri("/login");
        result.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        result.setSchema(authSchema);

        return result;
    }

    @Bean
    public  XsdSchema authSchema() {
        return  new SimpleXsdSchema(new ClassPathResource("Autentificare.xsd"));
    }
}
