package hr.irb.CIR.DAP.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class RestServiceApplication {

	public static void main(String[] args) {
		System.out.println("STARTING THE SERVICE");
		SpringApplication.run(RestServiceApplication.class, args);
	}

}
