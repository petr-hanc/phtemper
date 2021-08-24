package phtemper;

import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PeriodCompute {
	
	@Autowired
    private TemperRepository repository;
	
	/* auxiliary variables for computation */
	private PeriodD periodMax;		// longest period in temperature range
	private long periodMaxLength;		// length in days
	private Temper oldestInRange, newestInRange;
	private boolean withinPeriod;	// true if period was found and hasn't finished yet

	/** Returns longest period (starting date and ending date) with all temperatures between lowTemp and hiTemp (included).
	 * If no such temperature is found then returns null  */
	public PeriodD longestPeriod(Float lowTemp, Float hiTemp) {
		periodMax = null;
		periodMaxLength = -1L;
		oldestInRange = null;
		newestInRange = null;
		withinPeriod = false;
		if (lowTemp == null || hiTemp == null || lowTemp > hiTemp)
			return null;
		ArrayList<Temper> sortedTemps = new ArrayList<Temper>(repository.findAll());
		Collections.sort(sortedTemps);	// sort by date
		for (Temper temper: sortedTemps) {
			if (temper.getTemper() >= lowTemp && temper.getTemper() <= hiTemp) {	// temper. is in range
				if (oldestInRange == null) {	// new period has started
					oldestInRange = temper;
					newestInRange = temper;
					withinPeriod = true;
				}
				else 		// period continues
					newestInRange = temper;
			} else {	// temper. out of range
				if (withinPeriod == true) 
					finishPeriod();
			}
		}
		if (withinPeriod == true) 
			finishPeriod();
		return periodMax;
	}
	
	/** Finish a period of passing temperatures that was found. Changes member variables. */
	private void finishPeriod() {
		PeriodD period = new PeriodD();
		ChronoPeriod periodChrono;	// interval between dates
		long periodLength = 0;		// length in days
		period.setFromDate(oldestInRange.getTimeStamp().toLocalDate());
		period.setToDate(newestInRange.getTimeStamp().toLocalDate());
		periodChrono = Period.between(period.getFromDate(), period.getToDate());
		periodLength = periodChrono.get(ChronoUnit.DAYS);
		if (periodLength > periodMaxLength) {	// this period has been the longest one
			periodMax = period;
			periodMaxLength = periodLength;
		}
		withinPeriod = false;
		oldestInRange = null;
		newestInRange = null;
	}
	

}	// class
