package phtemper.api;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import phtemper.PeriodCompute;
import phtemper.PeriodD;
import phtemper.Temper;
import phtemper.TemperRepository;

@RestController
@RequestMapping(path="/periods",
                produces="application/json")
@CrossOrigin(origins="*")
public class PeriodsController {
	
	@Autowired
    PeriodCompute periodCompute;
	
	@GetMapping("/period")
	public ResponseEntity<PeriodD> getLongestPeriod(@RequestParam Float lowTemp, @RequestParam Float hiTemp) {
		PeriodD period = periodCompute.longestPeriod(lowTemp, hiTemp);
		return ResponseEntity.ok(period);
	}
	
	@GetMapping("/periodTime")
	public ResponseEntity<PeriodD> getLongestPeriodWithTime(
			@RequestParam Float lowTemp, @RequestParam Float hiTemp, @RequestParam LocalTime fromTime, @RequestParam LocalTime toTime
			) {
		PeriodD period = periodCompute.longestPeriodWithTime(lowTemp, hiTemp, fromTime, toTime);
		return ResponseEntity.ok(period);
	}
	

}
