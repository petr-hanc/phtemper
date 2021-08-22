package phtemper;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

public class PeriodCompute {
	
	@Autowired
    static TemperRepository repository;

	/** Returns longest period (starting date and ending date) with all temperatures between lowTemp and hiTemp (included).
	 * If no such temperature is found then returns null  */
	public static PeriodD longestPeriod(Float lowTemp, Float hiTemp) {
		Temper oldestInRange = null, newestInRange = null;
		PeriodD period = new PeriodD();
		
		if (lowTemp == null || hiTemp == null || lowTemp > hiTemp)
			return null;
		ArrayList<Temper> sortedTemps = new ArrayList<Temper>(repository.findAll());
		Collections.sort(sortedTemps);	// sort by date
		for (Temper temper: sortedTemps) {
			if (temper.getTemper() >= lowTemp && temper.getTemper() <= hiTemp) {
				if (oldestInRange == null) {
					oldestInRange = temper;
					newestInRange = temper;
				}
				else 
					newestInRange = temper;
			}
		}
		if (oldestInRange == null)	// no temperature in range found
			return null;
		else {
			period.setFromDate(oldestInRange.getTimeStamp().toLocalDate());
			period.setToDate(newestInRange.getTimeStamp().toLocalDate());
			return period;
		}
			
	}

}
