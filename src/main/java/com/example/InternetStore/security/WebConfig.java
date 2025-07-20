//package com.example.InternetStore.security;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.*;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
////        registry.addMapping("/**") // разрешить все пути
////                .allowedOrigins("http://localhost:3000") // разрешить React
////                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
////                .allowedHeaders("*");
//
//
//        registry.addMapping("/**")
//                .allowedOrigins("https://edinternetshop.netlify.app")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Дозволені HTTP методи
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .allowedHeaders("*");
//
//    }
//}
