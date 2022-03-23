package io.opensource;

import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opensource.inbox.DataStaxAstraProperties;
import io.opensource.inbox.folder.Folder;
import io.opensource.inbox.folder.FolderRepository;

@SpringBootApplication
@RestController
public class InboxApplication {

	@Autowired
	FolderRepository folderRepo;

	public static void main(String[] args) {
		SpringApplication.run(InboxApplication.class, args);
	}

	@RequestMapping("/user")
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal);
		return principal.getAttribute("name");
	}

	/**
	 * this is necessary to have boot app use astra secure bundle to connect to
	 * cassandra db
	 */

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {

		folderRepo.save(new Folder("spartan4cs", "Inbox", "blue"));
		folderRepo.save(new Folder("spartan4cs", "Sent", "green"));
		folderRepo.save(new Folder("spartan4cs", "Important", "red"));
		
	}

}
