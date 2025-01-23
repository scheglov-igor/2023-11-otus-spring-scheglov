package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
				java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		System.out.println("availableProcessors:" + Runtime.getRuntime().availableProcessors());
		System.out.println("TotalMemorySize, mb:" + os.getTotalMemorySize() / 1024 / 1024);
		System.out.println("maxMemory, mb:" + Runtime.getRuntime().maxMemory() / 1024 / 1024);
		System.out.println("freeMemory, mb:" + Runtime.getRuntime().freeMemory() / 1024 / 1024);

		SpringApplication.run(Application.class, args);
		System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n",
				"http://localhost:8080");

		System.out.printf("Чтобы перейти на actuator: %n%s%n",
				"http://localhost:8080/actuator");

		System.out.printf("Чтобы перейти на actuator health: %n%s%n",
				"http://localhost:8080/actuator/health");

		System.out.printf("Чтобы перейти на HAL Explorer: %n%s%n",
				"http://localhost:8080/datarest");

	}

}
