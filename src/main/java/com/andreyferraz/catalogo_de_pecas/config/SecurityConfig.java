package com.andreyferraz.catalogo_de_pecas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/uploads/**", "/img/**", "/carrinho/**").permitAll()
                                .requestMatchers("/", "/home", "/categorias/{id}", "/login", "/logout", "/pesquisar")
                                .permitAll() // Apenas '/categorias/{id}' é público
                                .requestMatchers(
                                                "/categorias",
                                                "/categorias/formulario",
                                                "/categorias/listar",
                                                "/produtos/novo",
                                                "/produtos/salvar",
                                                "/produtos/formulario",
                                                "/produtos/listar",
                                                "/categorias-editar",
                                                "/admin/dashboard")
                                .authenticated()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/admin/dashboard", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login")
                                                .permitAll())
                                .csrf().disable();
                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user = User.withUsername("admin")
                                .password(passwordEncoder().encode("admin"))
                                .roles("ADMIN")
                                .build();

                return new InMemoryUserDetailsManager(user);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}