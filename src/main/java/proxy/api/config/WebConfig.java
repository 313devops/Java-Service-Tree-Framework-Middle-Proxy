package proxy.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9999","http://313.co.kr","http://www.313.co.kr","http://a-rms.net","http://www.a-rms.net","http://www.a-rms.net:9999","http://a-rms.net:9999");
    }
}
