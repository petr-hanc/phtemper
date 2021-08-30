package phtemper;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "temper")
public class Temper implements Comparable<Temper> {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
	private Long id;
    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;
    @Column(name = "temper")
    private Float temper;
    
    public Temper() {
		super();
    }
    
    public Temper(LocalDateTime timeStamp, Float temper) {
		super();
		this.timeStamp = timeStamp;
		this.temper = temper;
	}
    
    /** Compares by timeStamp */
	public int compareTo (Temper temp) {
		return this.timeStamp.compareTo(temp.timeStamp); 		
	}

}
