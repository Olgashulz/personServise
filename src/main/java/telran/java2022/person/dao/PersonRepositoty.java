package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.model.Person;

public interface PersonRepositoty extends CrudRepository<Person, Integer> {

	@Query("select p from Person p where p.name=?1")
	Stream<Person> findByName(String name);

	@Query("select p from Person p where p.address.city=:city")
	Stream<Person> findPersonByAddress_CityContaining(@Param("city") String city);

	Stream<Person> findByBirthDateBetween(LocalDate min, LocalDate max);

	@Query("select new telran.java2022.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city order by count(p) desc")
	List<CityPopulationDto> getCitiesPopulation();

	@Query("select e from Employee e where e.salary between ?1 and ?2")
	Stream<Person> findEmployeeBySalary(int min, int max);

	@Query("select c from Child c ")
	Stream<Person> findChildren();
}
