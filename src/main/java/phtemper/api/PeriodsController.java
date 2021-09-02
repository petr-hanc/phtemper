package phtemper.api;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import phtemper.PeriodCompute;
import phtemper.PeriodD;
import phtemper.Temper;
import phtemper.TemperRepository;

@RestController
@RequestMapping(path="/periods",
                produces="application/json")
@CrossOrigin(origins="*")
public class PeriodsController {
	
    private PeriodCompute periodCompute;
    
    PeriodsController(PeriodCompute periodCompute) {
    	this.periodCompute = periodCompute;
    }
    
    /** For testing purposes */
    /*
    PeriodsController(TemperRepository repository) {
    	periodCompute = new PeriodCompute(repository);
    }
    */
	
	@GetMapping("/period")
	public ResponseEntity<PeriodD> getLongestPeriod(@RequestParam Float lowTemp, @RequestParam Float hiTemp) {
		PeriodD period = periodCompute.longestPeriod(lowTemp, hiTemp);
		if (period == null) 
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else return ResponseEntity.ok(period);
	}
	
	@GetMapping("/periodTime")
	public ResponseEntity<PeriodD> getLongestPeriodWithTime(
			@RequestParam Float lowTemp, @RequestParam Float hiTemp,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fromTime,  // @DateTimeFormat is used for tests
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime toTime
			) {
		PeriodD period = periodCompute.longestPeriodWithTime(lowTemp, hiTemp, fromTime, toTime);
		if (period == null) 
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else return ResponseEntity.ok(period);
	}
	

}
