package cubes.main;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource myDataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
//		UserBuilder uses = User.withDefaultPasswordEncoder();
//		
//		auth.inMemoryAuthentication()
//		.withUser(uses.username("radovan").password("radovan123").roles("admin"))
//		.withUser(uses.username("nikola").password("nikola123").roles("employee"));
		
		auth.jdbcAuthentication().dataSource(myDataSource);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/administration/employee-list").hasRole("admin")
		.antMatchers("/administration/employee-form").hasRole("admin")
		.antMatchers("/administration/user-list").hasAnyRole("admin")	
		.and().formLogin()
		.loginPage("/loginPage")
		.loginProcessingUrl("/authenticateTheUser").permitAll();
	}

}
