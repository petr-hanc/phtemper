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
    TemperRepository repository;

	/** Returns longest period (starting date and ending date) with all temperatures between lowTemp and hiTemp (included).
	 * If no such temperature is found then returns null  */
	public PeriodD longestPeriod(Float lowTemp, Float hiTemp) {
		PeriodD periodMax = null;		// longest period in temperature range
		long periodMaxLength = -1;		// length in days
		Temper oldestInRange = null;
		Temper newestInRange = null;
		boolean withinPeriod = false;	// true if period was found and hasn't finished yet
		
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
				if (withinPeriod == true) {		// period has ended
					PeriodD period = new PeriodD();
					ChronoPeriod periodChrono;	// interval between dates
					long periodLength = 0;		// length in days
					period.setFromDate(oldestInRange.getTimeStamp().toLocalDate());
					period.setToDate(newestInRange.getTimeStamp().toLocalDate());
					periodChrono = Period.between(period.getFromDate(), period.getFromDate());
					periodLength = periodChrono.get(ChronoUnit.DAYS);
					if (periodLength > periodMaxLength) {	// this period has been the longest one
						periodMax = period;
						periodMaxLength = periodLength;
					}
					withinPeriod = false;
					oldestInRange = null;
					newestInRange = null;
				}
			}
		}
		return periodMax;
	}

}	// class
