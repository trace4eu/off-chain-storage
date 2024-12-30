package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.ArgumentService;
import com.trace4eu.offchain.controller.ServiceController;
import com.trace4eu.offchain.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ServiceController.class)
public class RestServiceApplication {
	public static void main(String[] args) throws Exception {
		if (args.length<2){
			System.out.println("Usage:");
			System.out.println("java -jar <application.jar> -i <config.options> <other java options>...");
			return;
		}

		if (!isConnectionOk(args[1]))
			throw new Exception("Connection to cassandra server failed");

		SpringApplication.run(RestServiceApplication.class, args);
	}

	private static Boolean isConnectionOk(String configFile) throws Exception {
		DbOptions options = new DbOptions(configFile);
//		Vars.DB_OPTIONS = options;
		IIndex indexer = IndexFactory.createIndexer(IndexerType.Cassandra,options);
		return indexer.connect();
//		return  ndx.isConnected();
	}
	@Bean
	public ArgumentService myService(ApplicationArguments args) {
		return new ArgumentService(args);
	}
}
