package phtemper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhtemperApplication {
	
    @Autowired
    TemperRepository repository;
	

	public static void main(String[] args) {
		SpringApplication.run(PhtemperApplication.class, args);
	}

}
