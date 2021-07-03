package com.weblearnex.app;

import com.weblearnex.app.constant.UserType;
import com.weblearnex.app.datatable.reposatory.UserRepository;
import com.weblearnex.app.entity.setup.User;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
	})

//@EnableEurekaClient
@Configuration
@EnableJpaRepositories(basePackages = "com.weblearnex.app.reposatory")
public class MasterApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(MasterApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MasterApplication.class, args);
	}

	@Autowired
	private UserRepository  userRepository;
	@Override
	public void run(String... args) throws Exception {
		if(userRepository.findByLoginId("admin")==null){
			User user = new User();
			user.setLoginId("admin");
			user.setPassword("admin@4050");
			user.setFisrtName("Admin");
			user.setType(UserType.SELF);
			user.setActive(1);
			user.setAdmin(true);
			userRepository.save(user);
			System.out.println("Admin user create successfully.");
		}

	}
}

