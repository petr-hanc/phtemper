package phtemper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperRepository extends JpaRepository<Temper, Long> {

}


