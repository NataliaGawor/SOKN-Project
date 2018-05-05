package pl.sokn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sokn.definitions.SoknDefinitions.Api;
import pl.sokn.definitions.SoknDefinitions.Roles;
import pl.sokn.security.jwt.JwtAuthenticationEntryPoint;
import pl.sokn.security.jwt.AuthenticationTokenFilter;

/**
 * Setting for Spring Security
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    public SecurityConfig(UserDetailsService customUserDetailsService,
                          JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                // custom exception handler
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // we don't create session !
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE).hasAuthority(Roles.ADMIN_ROLE) // only admins can use DELETE methods in REST controllers
                .antMatchers(HttpMethod.GET).permitAll()
                // when you create new end point which should be accessible for unauthenticated user only
                // please add the path here
                .antMatchers(
                        Api.LOGIN,
                        Api.REFRESH,
                        Api.REGISTER + "/**",
                        "/user/registerReviewer",
                        Api.REGISTRATION_CONFIRM + "/**",
                        Api.RESEND_REGISTRATION_TOKEN + "/**",
                        Api.CHANGE_PASSWORD + "/**",
                        Api.FORGOT_PASSWORD + "/**",
                        Api.RESEND_FORGOT_PASSWORD + "/**",
                        Api.RESET_PASSWORD + "/**",
                        Api.CHECK_IF_REGISTERED+"/**",
                        Api.SEND_CONTACT_EMAIL+"/**",
                        Api.MAILING_LIST + "/**",
                        Api.GET_ALL_FIELDS_OF_ARTICLES + "/**",
                        "/documentation/**",
                        "/swagger-resources/**",
                        "/v2/api-docs").permitAll() // access for every user
                .anyRequest().authenticated(); // any request is not accessible by default

        // Custom JWT based security filter
        // It is used when user tries access the data accessible only for authenticated users
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() {
        return new AuthenticationTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // default password encryption algorithm is BCrypt
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
