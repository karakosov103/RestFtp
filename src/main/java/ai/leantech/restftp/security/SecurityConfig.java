package ai.leantech.restftp.security;

import ai.leantech.restftp.config.FtpProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FtpProperties ftpProperties;

    public SecurityConfig(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(this.ftpProperties);

        http
                .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class)
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

}