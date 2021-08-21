package phtemper;

import java.time.LocalDate;

import lombok.Data;

/** Period with two dates */
@Data
public class PeriodD {
	LocalDate fromDate;
	LocalDate toDate;

	public PeriodD(LocalDate fromDate, LocalDate toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	
}
