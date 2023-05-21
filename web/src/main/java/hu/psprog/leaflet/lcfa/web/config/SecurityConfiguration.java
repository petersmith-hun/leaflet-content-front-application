package hu.psprog.leaflet.lcfa.web.config;

import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

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

    @Autowired
    public SecurityConfiguration(WebAppResources webAppResources, PageConfigModel pageConfigModel) {
        this.webAppResources = webAppResources;
        this.logoutEndpoint = pageConfigModel.getLogoutEndpoint();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> webAppResources.getResources().stream()
                .map(WebAppResources.WebAppResource::getHandler)
                .forEach(web.ignoring()::requestMatchers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(PATH_PROFILE)
                            .hasAuthority(PROFILE_READ_AUTHORITY)
                        .anyRequest()
                            .permitAll())

                .logout(logout -> logout
                        .logoutUrl(logoutEndpoint)
                        .logoutSuccessUrl(PATH_LOGOUT_REDIRECT))

                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
                        .failureUrl(PATH_LOGIN_FAILURE))

                .build();
    }
}
