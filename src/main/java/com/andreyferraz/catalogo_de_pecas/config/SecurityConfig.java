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
        http
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/uploads/**").permitAll()
                    // Permitindo acesso a URLs sem necessidade de login
                    .requestMatchers("/", "/categorias/**", "/login", "/logout").permitAll()
                    // Páginas que requerem autenticação
                    .requestMatchers(
                        "/categorias/formulario", 
                        "/categorias/listar", 
                        "/produtos", 
                        "/produtos/novo",
                        "/produtos/salvar",
                        "/produtos/formulario", 
                        "/produtos/listar", 
                        "/admin/dashboard").authenticated()
                    // Páginas com restrição para o papel ADMIN
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)  // Página após login bem-sucedido
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL para logout
                        .logoutSuccessUrl("/")  // Página após logout
                        .permitAll())
                .csrf().disable(); // Desabilita CSRF, se necessário (dependendo do seu uso)
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password(passwordEncoder().encode("123"))  // senha criptografada
                .roles("ADMIN")
                .build();


        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Codificador de senha
    }
}
