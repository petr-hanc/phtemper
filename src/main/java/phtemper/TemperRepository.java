package phtemper;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperRepository extends JpaRepository<Temper, Long> {
	
	@Query(nativeQuery=true, value =
			"SELECT id, temper, time_stamp "
			+ "FROM PUBLIC.TEMPER "
			+ "WHERE CAST(time_stamp AS time) BETWEEN ?1 AND ?2 "
			+ "ORDER BY time_stamp")
	public List<Temper> findByTimeRangeOrder(LocalTime fromTime, LocalTime toTime);
}


