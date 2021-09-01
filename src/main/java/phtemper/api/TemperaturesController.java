package phtemper.api;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import phtemper.Temper;
import phtemper.TemperRepository;

@RestController
@RequestMapping(path="/temperatures",
                produces="application/json")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class TemperaturesController {
	
	private final TemperRepository repository;
	
	@GetMapping(produces="application/json")
	public List<Temper> allTempers() {
		return repository.findAll();
	}
	
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Temper> addTemper(@RequestBody Temper temper) {
		temper = repository.save(temper);
		return ResponseEntity.status(HttpStatus.CREATED).body(temper);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Temper> getTemper(@PathVariable("id") Long id) {
		try {
			Temper temper = repository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid temperature id: " + id));
			return ResponseEntity.ok(temper);
		} 
		catch (IllegalArgumentException e) {
			System.err.println(e.getMessage()); // debug
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PatchMapping(path="/{id}", consumes="application/json")
	public ResponseEntity<Temper> updateTemper(@PathVariable("id") Long id, @RequestBody Temper patch) {
		try {
			Temper temper = repository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid temperature id: " + id));
			if (patch.getTimeStamp() != null)
				temper.setTimeStamp(patch.getTimeStamp());
			if (patch.getTemper() != null)
				temper.setTemper(patch.getTemper());
			return ResponseEntity.ok(repository.save(temper));
		}
		catch (IllegalArgumentException e) {
			System.err.println(e.getMessage()); // debug
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delTemper(@PathVariable("id") Long id) {
		try {
			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} 
		catch (EmptyResultDataAccessException e) {
			System.err.println(e.getMessage()); // debug
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
