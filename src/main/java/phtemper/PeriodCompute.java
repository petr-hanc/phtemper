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
	
	/* auxiliary variables for computation */
	private PeriodD periodMax;		// longest period in temperature range
	private long periodMaxLength;		// length in days
	private Temper oldestInRange, newestInRange;
	private boolean withinPeriod;	// true if period was found and hasn't finished yet

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
		ArrayList<Temper> temperatures = new ArrayList<Temper>(repository.findAll());
		ArrayList<Temper> tempersInTime = new ArrayList<Temper>();
		try {
			for (Temper temper: temperatures) {
				LocalTime time = temper.getTimeStamp().toLocalTime();
				if (time.compareTo(fromTime) >= 0 && time.compareTo(toTime) <= 0) 
					tempersInTime.add(temper);
			}
			//System.out.println("tempersInTime: " + tempersInTime);	// DEBUG
			return longestPeriodInList(lowTemp, hiTemp, tempersInTime);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Returns longest period (starting date and ending date) in temperatures list with all temperatures between lowTemp and hiTemp (included).
	 * If no such temperature is found then it returns null.
	 * If there are more longest periods it returns the oldest one. */
	PeriodD longestPeriodInList(Float lowTemp, Float hiTemp, List<Temper> temperatures) {
		periodMax = null;
		periodMaxLength = -1L;
		oldestInRange = null;
		newestInRange = null;
		withinPeriod = false;
		if (lowTemp == null || hiTemp == null || temperatures == null || lowTemp > hiTemp)
			return null;
		ArrayList<Temper> sortedTemps = new ArrayList<Temper>(temperatures);
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
