package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;



@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

	private static final Path FILE_PATH = Path.of("counter.txt");
	private int counter = 0;
	private String runtimeMessage = " ";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (!Files.exists(FILE_PATH)) {
			Files.createFile(FILE_PATH);
		}

		runtimeMessage = RunInfo.printInfo();

	}

	@Scheduled(fixedRate = 10000)
	public void updateFile() {
		counter += 5;
		String content = "Counter value: " + counter + "\n";
		try {
			Files.writeString(FILE_PATH, content, StandardOpenOption.APPEND);
			System.out.println("Updated file with: " + content.trim());
		} catch (IOException e) {
			System.err.println("Failed to update file: " + e.getMessage());
		}
	}

	@RestController
	class CounterController {
		@GetMapping("/counter")
		public String getCounterValue() {
			return String.valueOf(counter);
		}

		@GetMapping("/runtime-message")
		public String getRuntimeMessage() {return runtimeMessage;}
	}
}

