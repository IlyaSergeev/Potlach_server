package com.ilya.sergeev.potlach;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
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

@EnableAutoConfiguration
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = { UserInfoRepository.class, GiftRepository.class, VoteRepository.class })
@Configuration
@ComponentScan
@Import(OAuth2SecurityConfiguration.class)
public class Application extends RepositoryRestMvcConfiguration
{
	private static final String MAX_REQUEST_SIZE = "10MB";
	
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public MultipartConfigElement multipartConfigElement()
	{
		final MultiPartConfigFactory factory = new MultiPartConfigFactory();
		
		factory.setMaxFileSize(MAX_REQUEST_SIZE);
		factory.setMaxRequestSize(MAX_REQUEST_SIZE);
		
		return factory.createMultipartConfig();
	}
}
