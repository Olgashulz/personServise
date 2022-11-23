package telran.java2022.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepositoty;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonNotFoundExeption;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiseImpl implements PersonService {
	final PersonRepositoty personRepositoty;
	final ModelMapper modelMapper;

	@Override
	public Boolean addPerson(PersonDto personDto) {
		personRepositoty.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto removePerson(Integer id) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		personRepositoty.deleteById(id);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		person.setName(name);
		personRepositoty.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		Address address = modelMapper.map(addressDto, Address.class);
		person.setAddress(address);
		personRepositoty.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepositoty.findByName(name).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepositoty.findPersonByAddress_CityContaining(city).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsBetweenAge(Integer min, Integer max) {
		LocalDate to = LocalDate.now().minusYears(min);
		LocalDate from = LocalDate.now().minusYears(max);
		return personRepositoty.findByBirthDateBetween(from, to).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		return null;
	}

}
