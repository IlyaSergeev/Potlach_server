package com.ilya.sergeev.potlach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ilya.sergeev.potlach.auth.OAuth2SecurityConfiguration;
import com.ilya.sergeev.potlach.repository.GiftRepository;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;
import com.ilya.sergeev.potlach.repository.VoteRepository;

//Tell Spring to automatically inject any dependencies that are marked in
//our classes with @Autowired
@EnableAutoConfiguration
// Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
// so that requests can be routed to our Controllers)
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = { UserInfoRepository.class, GiftRepository.class, VoteRepository.class })
// Tell Spring that this object represents a Configuration for the
// application
@Configuration
// Tell Spring to go and scan our controller package (and all sub packages) to
// find any Controllers or other components that are part of our applciation.
// Any class in this package that is annotated with @Controller is going to be
// automatically discovered and connected to the DispatcherServlet.
@ComponentScan
@Import(OAuth2SecurityConfiguration.class)
public class Application extends RepositoryRestMvcConfiguration
{
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
