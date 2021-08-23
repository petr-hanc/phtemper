package phtemper;

import java.time.LocalDate;

import lombok.Data;

/** Period with two dates */
@Data
public class PeriodD  {
	LocalDate fromDate;
	LocalDate toDate;
	
	public PeriodD() {
		super();
	}

	public PeriodD(LocalDate fromDate, LocalDate toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

}
