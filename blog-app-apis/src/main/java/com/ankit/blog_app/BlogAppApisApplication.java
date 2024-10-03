package com.ankit.blog_app;

import com.ankit.blog_app.entities.Role;
import com.ankit.blog_app.repositories.RoleRepo;
import com.ankit.blog_app.utils.AppConstant;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Blogging Application : Backend APIs", version = "1.0", description = "Backend APIs for blogging application",
	contact = @Contact(name = "Ankit", url = "ankit-kumar-singh-portfolio.netlify.app", email = "ankitajnavi123@gmail.com"),
	license = @License(name = "Blog-App-1.0", url = "blogging_app.in")
))
@SecurityScheme(name = "javainuseapi", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP)
public class BlogAppApisApplication implements CommandLineRunner {
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	@Override
	public void run(String... args) {
		try {
			Role role1 = new Role();
			role1.setId(AppConstant.ADMIN_USER);
			role1.setRole("ROLE_ADMIN");

			Role role2 = new Role();
			role2.setId(AppConstant.NORMAL_USER);
			role2.setRole("ROLE_USER");

			List<Role> roles = List.of(role1, role2);
			List<Role> result = roleRepo.saveAll(roles);

			result.forEach(r -> {
				System.out.println(r.getRole());
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
