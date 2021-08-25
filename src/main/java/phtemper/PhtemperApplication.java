package phtemper;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
public class PhtemperApplication extends SpringBootServletInitializer /* implements CommandLineRunner */ {
	
    @Autowired
    TemperRepository repository;
    @Autowired
    PeriodCompute periodCompute;
    @Autowired
    private Environment env;
    
	public static void main(String[] args) {
		SpringApplication.run(PhtemperApplication.class, args);
	}
	
	//@Override
	public void run(String... args) throws Exception {
		
		/*
		repository.deleteAll();
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2021-08-01T11:30:00"), 25.4F));
		tempers.add(new Temper(LocalDateTime.parse("2021-07-31T04:13:00"), 9.8F));
		tempers.add(new Temper(LocalDateTime.parse("2021-07-31T09:09:00"), 16.8F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-03T05:15:00"), 5.0F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-05T13:20:00"), 17F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-15T11:40:00"), 17F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-20T11:40:00"), 35F));
		repository.saveAll(tempers);
		*/
		
	}
	
	
	@Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
		Server srv = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
		return srv;
        //return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
	
	/*
	@Bean(destroyMethod = "close")
	public DataSource dataSource(Optional<Server> h2Server) throws PropertyVetoException {
	    HikariDataSource ds = new HikariDataSource();
	    ds.setDriverClassName(env.getProperty("db.driver"));
	    ds.setJdbcUrl(env.getProperty("db.url"));
	    ds.setUsername(env.getProperty("db.user"));
	    ds.setPassword(env.getProperty("db.pass"));
	    return ds;
	}
	*/
	

}
