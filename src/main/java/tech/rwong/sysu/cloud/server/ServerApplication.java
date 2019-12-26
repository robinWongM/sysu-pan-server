package tech.rwong.sysu.cloud.server;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {
	@Bean
	public Module hibernate5Module() {
		return new Hibernate5Module().enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
