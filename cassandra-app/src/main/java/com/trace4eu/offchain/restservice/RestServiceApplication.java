package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.MyService;
import com.trace4eu.offchain.controller.ServiceController;
import com.trace4eu.offchain.repository.DbOptions;
import com.trace4eu.offchain.Vars;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackageClasses = ServiceController.class)
public class RestServiceApplication {
	public static void main(String[] args) throws Exception {
		DbOptions options = new DbOptions();
		if (args.length<2){
			System.out.println("Usage:");
			System.out.println("java -jar <application.jar> -i <config.options> <other java options>...");
			return;
		}

		SpringApplication.run(RestServiceApplication.class, args);
	}
	@Bean
	public MyService myService(ApplicationArguments args) {
		return new MyService(args);
	}
}
