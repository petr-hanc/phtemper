package phtemper;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/** Period with two dates */
@Data
public class PeriodD  {
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate fromDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate toDate;
	
	public PeriodD() {
		super();
	}

	public PeriodD(LocalDate fromDate, LocalDate toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

}
