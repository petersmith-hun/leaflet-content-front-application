package hu.psprog.leaflet.lcfa.web.config;

import hu.psprog.leaflet.jwt.auth.support.filter.SessionExtensionFilter;
import hu.psprog.leaflet.jwt.auth.support.logout.TokenRevokeLogoutHandler;
import hu.psprog.leaflet.rcp.hystrix.support.filter.HystrixContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Spring Security configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String DEFAULT_SUCCESS_URL = "/?auth=success";
    private static final String USERNAME_PARAMETER = "email";

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_EDITOR = "EDITOR";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_RECLAIM = "RECLAIM";

    private static final String PATH_LOGIN = "/signin";
    private static final String PATH_LOGIN_FAILURE = "/signin?auth=failure";
    private static final String PATH_PROFILE = "/profile/**";
    private static final String PATH_RECLAIM = "/password-reset/**";
    private static final String PATH_LOGOUT = "/signout";

    private final TokenRevokeLogoutHandler tokenRevokeLogoutHandler;
    private final SessionExtensionFilter sessionExtensionFilter;
    private final HystrixContextFilter hystrixContextFilter;

    @Autowired
    public SecurityConfiguration(TokenRevokeLogoutHandler tokenRevokeLogoutHandler, SessionExtensionFilter sessionExtensionFilter,
                                 HystrixContextFilter hystrixContextFilter) {
        this.tokenRevokeLogoutHandler = tokenRevokeLogoutHandler;
        this.sessionExtensionFilter = sessionExtensionFilter;
        this.hystrixContextFilter = hystrixContextFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .addFilterBefore(hystrixContextFilter, LogoutFilter.class)
            .addFilterAfter(sessionExtensionFilter, UsernamePasswordAuthenticationFilter.class)

            .authorizeRequests()
                .antMatchers(PATH_PROFILE)
                    .hasAnyRole(ROLE_ADMIN, ROLE_EDITOR, ROLE_USER)
                .antMatchers(PATH_RECLAIM)
                    .hasRole(ROLE_RECLAIM)
                .anyRequest()
                    .permitAll()
                .and()

            .formLogin()
                .loginPage(PATH_LOGIN)
                .failureUrl(PATH_LOGIN_FAILURE)
                .usernameParameter(USERNAME_PARAMETER)
                .defaultSuccessUrl(DEFAULT_SUCCESS_URL, true)
                .and()

            .logout()
                .logoutUrl(PATH_LOGOUT)
                .logoutSuccessUrl(PATH_LOGIN)
                .addLogoutHandler(tokenRevokeLogoutHandler);
    }
}
