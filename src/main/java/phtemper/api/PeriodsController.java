package phtemper.api;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import phtemper.PeriodCompute;
import phtemper.PeriodD;

@RestController
@RequestMapping(path="/periods",
                produces="application/json")
@CrossOrigin(origins="*")
public class PeriodsController {
	
    private PeriodCompute periodCompute;
    
    PeriodsController(PeriodCompute periodCompute) {
    	this.periodCompute = periodCompute;
    }
    
	@GetMapping("/period")
	public ResponseEntity<PeriodD> getLongestPeriod(@RequestParam String lowTemp, @RequestParam String hiTemp) {
		Float lowTempFloat = Float.valueOf(lowTemp);
		Float hiTempFloat = Float.valueOf(hiTemp);
		PeriodD period = periodCompute.longestPeriod(lowTempFloat, hiTempFloat);
		if (period == null) 
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else return ResponseEntity.ok(period);
	}
	
	@GetMapping("/periodTime")
	public ResponseEntity<PeriodD> getLongestPeriodWithTime(
			@RequestParam String lowTemp, @RequestParam String hiTemp,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fromTime,  // @DateTimeFormat is used for tests
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime toTime
			) {
		Float lowTempFloat = Float.valueOf(lowTemp);
		Float hiTempFloat = Float.valueOf(hiTemp);
		PeriodD period = periodCompute.longestPeriodWithTime(lowTempFloat, hiTempFloat, fromTime, toTime);
		if (period == null) 
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else return ResponseEntity.ok(period);
	}
	

}
