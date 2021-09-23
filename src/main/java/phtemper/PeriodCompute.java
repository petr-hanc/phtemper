package phtemper;

import java.time.LocalTime;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PeriodCompute {
	
    private final TemperRepository repository;
	
	/** Returns longest period (starting date and ending date) in temperatures stored in repository 
	 * with all temperatures between lowTemp and hiTemp (included).
	 * If no such temperature is found then it returns null. */
	public PeriodD longestPeriod(Float lowTemp, Float hiTemp) {
		ArrayList<Temper> temperatures = new ArrayList<Temper>(repository.findAll());
		return longestPeriodInList(lowTemp, hiTemp, temperatures);
	}
	
	/** Returns longest period (starting date and ending date) in temperatures stored in repository 
	 * with all temperatures between lowTemp and hiTemp (included). Only temperatures with time between fromTime and 
	 * toTime (included) are considered, all others are ignored.
	 * If no such temperature is found then it returns null. */
	public PeriodD longestPeriodWithTime(Float lowTemp, Float hiTemp, LocalTime fromTime, LocalTime toTime) {
		ArrayList<Temper> tempersInTime = new ArrayList<Temper>(repository.findByTimeRangeOrder(fromTime, toTime));
		return longestPeriodInList(lowTemp, hiTemp, tempersInTime);
	}
	
	/** Returns longest period (starting date and ending date) in temperatures list with all temperatures between lowTemp and hiTemp (included).
	 * If no such temperature is found then it returns null.
	 * If there are more longest periods it returns the oldest one. */
	PeriodD longestPeriodInList(Float lowTemp, Float hiTemp, List<Temper> temperatures) {
		ComputeVars computeVars = new ComputeVars();	// auxiliary variables for computation
		
		if (lowTemp == null || hiTemp == null || temperatures == null || lowTemp > hiTemp)
			return null;
		ArrayList<Temper> sortedTemps = new ArrayList<Temper>(temperatures);
		Collections.sort(sortedTemps);	// sort by date
		for (Temper temper: sortedTemps) {
			if (temper.getTemper() >= lowTemp && temper.getTemper() <= hiTemp) {	// temper. is in range
				if (computeVars.oldestInRange == null) {	// new period has started
					computeVars.oldestInRange = temper;
					computeVars.newestInRange = temper;
					computeVars.withinPeriod = true;
				}
				else 		// period continues
					computeVars.newestInRange = temper;
			} else {	// temper. out of range
				if (computeVars.withinPeriod == true) 
					finishPeriod(computeVars);
			}
		}
		if (computeVars.withinPeriod == true) 
			finishPeriod(computeVars);
		return computeVars.periodMax;
	}
	
	/** Finish a period of fitting temperatures that was found.
	 * @param computeVars auxiliary variables for computation */
	private void finishPeriod(ComputeVars computeVars) {
		PeriodD period = new PeriodD();
		ChronoPeriod periodChrono;	// interval between dates
		long periodLength = 0;		// length in days
		period.setFromDate(computeVars.oldestInRange.getTimeStamp().toLocalDate());
		period.setToDate(computeVars.newestInRange.getTimeStamp().toLocalDate());
		periodChrono = Period.between(period.getFromDate(), period.getToDate());
		periodLength = periodChrono.get(ChronoUnit.DAYS);
		if (periodLength > computeVars.periodMaxLength) {	// this period has been the longest one
			computeVars.periodMax = period;
			computeVars.periodMaxLength = periodLength;
		}
		computeVars.withinPeriod = false;
		computeVars.oldestInRange = null;
		computeVars.newestInRange = null;
	}
}	

/** Auxiliary variables for computation used in 2 methods */
class ComputeVars {
	PeriodD periodMax;		// longest period in temperature range
	long periodMaxLength;	// length of the longest period in days
	Temper oldestInRange;	// oldest temperature in uninterrupted line (in temperature range)
	Temper newestInRange;	// newest temperature in uninterrupted line (in temperature range)
	boolean withinPeriod;	// true if period was found and hasn't finished yet
	
	ComputeVars() {
		periodMax = null;
		periodMaxLength = -1L;
		oldestInRange = null;
		newestInRange = null;
		withinPeriod = false;
	}
}
