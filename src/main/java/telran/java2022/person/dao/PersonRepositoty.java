package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.model.Person;

public interface PersonRepositoty extends CrudRepository<Person, Integer> {

	Stream<Person> findByName(String name);

	Stream<Person> findPersonByAddress_CityContaining(String city);

	Stream<Person> findByBirthDateBetween(LocalDate min, LocalDate max);

	// Stream<CityPopulationDto> countPersonByAddress_City();

}
