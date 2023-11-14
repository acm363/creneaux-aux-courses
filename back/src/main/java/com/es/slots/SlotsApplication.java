package com.es.slots;

import com.es.slots.user.entities.Admin;
import com.es.slots.user.entities.Client;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.user.repositories.UserRepository;
import com.es.slots.slot.repositories.StandardSlotRepository;
import com.es.slots.slot.repositories.ExceptionalSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import UUID
import java.util.UUID;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class SlotsApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StandardSlotRepository standardSlotRepository;

	@Autowired
	private ExceptionalSlotRepository exceptionalSlotRepository;

	public static void main(String[] args) {
        SpringApplication.run(SlotsApplication.class, args);
    }

	@Override
	public void run(String... args) {
		// On efface la bdd pour s'assurer qu'elle démarre sans erreurs.
		userRepository.deleteAll();

		Admin admin = new Admin();
		admin.setEmail("admin@gmail.com");
		admin.setName("admin");
		admin.setPublicId("134a7cda-6a8b-4c3b-9bb2-6fa0fa98838c");
		admin.setPassword("$2y$10$XHsgcUPHFTlZhNmHYWJV8eHdnUAZdFt2SMYe1r7dl71PFlqtcEN12");
		userRepository.save(admin);

		Client client = new Client();
		client.setEmail("client@gmail.com");
		client.setName("client");
		client.setPublicId("8e76f4cd-2518-4cb9-a2de-45233e3db709");
		client.setPassword("$2y$10$dlKUecvUttj5/wB/y4Z3COZ.2nLm0vxbMvl9.N9GTZjdElvjMyvCm");
		userRepository.save(client);
	}

}