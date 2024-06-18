package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.controller.ServiceController;
import com.trace4eu.offchain.repository.DbOptions;
import hr.irb.Vars;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ServiceController.class)
public class RestServiceApplication {

	public static  void printMessage(){
		System.out.println("Usage:");
		System.out.println("java -jar <application.jar> -i <config.options> <other java options>...");
	}
	public static void main(String[] args) throws Exception {
//		System.out.println("STARTING THE SERVICE");
		DbOptions options = null;
		if (args.length >= 2 && args[0].equals("-i")) {
			options = new DbOptions(args[1]);
			Vars.CASSANDRA_DB_OPTIONS = options;
		} else {
			printMessage();
			return;
		}

		SpringApplication.run(RestServiceApplication.class, args);
	}

}
