package be.doji.productivity.trambu.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The purpose of this class is to allow spring to componentscan automatically.
 * It is a hassle to get rid of this, and changing it will make the code a lot more verbose.
 *
 */
@SpringBootApplication
public class TrambuInfrastructureApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrambuInfrastructureApplication.class, args);
	}

}
