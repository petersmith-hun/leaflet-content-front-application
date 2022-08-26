package hu.psprog.leaflet.lcfa.web.config;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.rcp.hystrix.support.filter.HystrixContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Optional;

/**
 * Spring Security configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String DEFAULT_SUCCESS_URL = "/?auth=success";

    private static final String PATH_LOGIN_FAILURE = "/signin?auth=failure";
    private static final String PATH_PROFILE = "/profile/**";
    private static final String PATH_LOGOUT_REDIRECT = "/?auth=signout";
    private static final String PROFILE_READ_AUTHORITY = "SCOPE_read:users:own";

    private final WebAppResources webAppResources;
    private final String logoutEndpoint;
    private final Optional<HystrixContextFilter> optionalHystrixContextFilter;

    @Autowired
    public SecurityConfiguration(WebAppResources webAppResources, PageConfigModel pageConfigModel,
                                 @Autowired(required = false) Optional<HystrixContextFilter> optionalHystrixContextFilter) {
        this.webAppResources = webAppResources;
        this.logoutEndpoint = pageConfigModel.getLogoutEndpoint();
        this.optionalHystrixContextFilter = optionalHystrixContextFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> webAppResources.getResources().stream()
                .map(WebAppResources.WebAppResource::getHandler)
                .forEach(web.ignoring()::antMatchers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        optionalHystrixContextFilter
                .ifPresent(hystrixContextFilter -> http.addFilterBefore(hystrixContextFilter, LogoutFilter.class));

        http
            .authorizeRequests()
                .antMatchers(PATH_PROFILE)
                    .hasAuthority(PROFILE_READ_AUTHORITY)
                .anyRequest()
                    .permitAll()
                .and()

            .logout()
                .logoutUrl(logoutEndpoint)
                .logoutSuccessUrl(PATH_LOGOUT_REDIRECT)
                .and()

            .oauth2Login()
                .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
                .failureUrl(PATH_LOGIN_FAILURE);

        return http.build();
    }
}
