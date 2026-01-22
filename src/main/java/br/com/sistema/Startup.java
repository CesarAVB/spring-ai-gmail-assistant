package br.com.sistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
		System.out.println("🚀 Gmail Assistant rodando na porta 8082");
        System.out.println("📚 Swagger: http://localhost:8082/swagger-ui.html");
	}

}
