package com.acme.mtatest.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import com.acme.mtatest.webservice.MtaTestResource;
import com.acme.mtatest.webservice.AccountResource;
import com.acme.mtatest.webservice.TransitTimeResource;
import com.acme.mtatest.exception.GlobalExceptionHandler;
import com.acme.mtatest.filter.CorsFilter;

@ApplicationPath("/api")
public class MtaTestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(MtaTestResource.class);
        classes.add(AccountResource.class);
        classes.add(TransitTimeResource.class);
        classes.add(GlobalExceptionHandler.class);
        classes.add(CorsFilter.class);
        classes.add(JacksonConfig.class);
        return classes;
    }
}