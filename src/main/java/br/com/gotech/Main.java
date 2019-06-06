package br.com.gotech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import br.com.gotech.config.property.LocafacilApiProperty;

@SpringBootApplication
// @EnableConfigurationProperties(LocafacilApiProperty.class)
public class Main {
	
	@Autowired
	LocafacilApiProperty properties;

	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(Main.class, args);
	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}
