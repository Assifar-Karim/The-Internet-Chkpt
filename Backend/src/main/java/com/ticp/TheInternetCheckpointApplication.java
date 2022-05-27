package com.ticp;

import com.ticp.model.Checkpoint;
import com.ticp.repository.CheckpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories
public class TheInternetCheckpointApplication implements CommandLineRunner {

	@Autowired
	private CheckpointRepository checkpointRepository;

	public static void main(String[] args) {
		SpringApplication.run(TheInternetCheckpointApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
