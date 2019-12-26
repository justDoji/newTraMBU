package be.doji.productivity.trambu.planning.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.properties"})
@ImportAutoConfiguration({InfrastructureAutoConfiguration.class})
public class PLanningApplication {

  public static void main(String[] args) { SpringApplication.run(PLanningApplication.class); }

}
