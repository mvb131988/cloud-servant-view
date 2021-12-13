package main;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    
  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
      super.addResourceHandlers(registry);
  }
  
}
