package hu.psprog.leaflet.lcfa.web.config;

import hu.psprog.leaflet.jwt.auth.support.filter.SessionExtensionFilter;
import hu.psprog.leaflet.jwt.auth.support.logout.TokenRevokeLogoutHandler;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.rcp.hystrix.support.filter.HystrixContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Optional;

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

    private static final String PATH_LOGIN = "/signin";
    private static final String PATH_LOGIN_FAILURE = "/signin?auth=failure";
    private static final String PATH_PROFILE = "/profile/**";
    private static final String PATH_LOGOUT_REDIRECT = "/?auth=signout";

    private final TokenRevokeLogoutHandler tokenRevokeLogoutHandler;
    private final SessionExtensionFilter sessionExtensionFilter;
    private final WebAppResources webAppResources;
    private final String logoutEndpoint;
    private final Optional<HystrixContextFilter> optionalHystrixContextFilter;

    @Autowired
    public SecurityConfiguration(TokenRevokeLogoutHandler tokenRevokeLogoutHandler, SessionExtensionFilter sessionExtensionFilter,
                                 WebAppResources webAppResources, PageConfigModel pageConfigModel,
                                 @Autowired(required = false) Optional<HystrixContextFilter> optionalHystrixContextFilter) {
        this.tokenRevokeLogoutHandler = tokenRevokeLogoutHandler;
        this.sessionExtensionFilter = sessionExtensionFilter;
        this.webAppResources = webAppResources;
        this.logoutEndpoint = pageConfigModel.getLogoutEndpoint();
        this.optionalHystrixContextFilter = optionalHystrixContextFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        webAppResources.getResources().stream()
                .map(WebAppResources.WebAppResource::getHandler)
                .forEach(web.ignoring()::antMatchers);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        optionalHystrixContextFilter
                .ifPresent(hystrixContextFilter -> http.addFilterBefore(hystrixContextFilter, LogoutFilter.class));

        http
            .addFilterAfter(sessionExtensionFilter, UsernamePasswordAuthenticationFilter.class)

            .authorizeRequests()
                .antMatchers(PATH_PROFILE)
                    .hasAnyAuthority(ROLE_ADMIN, ROLE_EDITOR, ROLE_USER)
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
                .logoutUrl(logoutEndpoint)
                .logoutSuccessUrl(PATH_LOGOUT_REDIRECT)
                .addLogoutHandler(tokenRevokeLogoutHandler);
    }
}
