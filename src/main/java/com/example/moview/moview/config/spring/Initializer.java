package com.example.moview.moview.config.spring;

import com.example.moview.moview.config.db.HibernateConfig;
import com.example.moview.moview.config.db.JpaAuditingConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;


public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class, JpaAuditingConfig.class, HibernateConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new CORSFilter()};
    }
}
