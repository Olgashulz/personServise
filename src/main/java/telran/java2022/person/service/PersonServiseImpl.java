package telran.java2022.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepositoty;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonNotFoundExeption;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Child;
import telran.java2022.person.model.Employee;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiseImpl implements PersonService, CommandLineRunner {

	final PersonRepositoty personRepositoty;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepositoty.existsById(personDto.getId())) {
			return false;
		}
		personRepositoty.save(modelMapper.map(personDto, getModelClass(personDto)));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		return modelMapper.map(person, getModelDto(person));
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		personRepositoty.deleteById(id);
		return modelMapper.map(person, getModelDto(person));
	}

	@Override
	@Transactional(readOnly = true)
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		person.setName(name);
		// personRepositoty.save(person);// @Transactional: commit in a transaction does
		// the job
		return modelMapper.map(person, getModelDto(person));
	}

	@Override
	@Transactional(readOnly = true)
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepositoty.findById(id).orElseThrow(PersonNotFoundExeption::new);
		Address address = modelMapper.map(addressDto, Address.class);
		person.setAddress(address);
		// personRepositoty.save(person);
		return modelMapper.map(person, getModelDto(person));
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		return personRepositoty.findByName(name).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepositoty.findPersonByAddress_CityContaining(city).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAge(Integer min, Integer max) {
		LocalDate to = LocalDate.now().minusYears(min);
		LocalDate from = LocalDate.now().minusYears(max);
		return personRepositoty.findByBirthDateBetween(from, to).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<CityPopulationDto> getCitiesPopulation() {
		return personRepositoty.getCitiesPopulation();
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findEmployeeBySalary(int min, int max) {
		return personRepositoty.findEmployeeBySalaryBetween(min, max).map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}
 
	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> getChildren() {
		return personRepositoty.findChildrenBy().map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person(1000, "John", LocalDate.of(1985, 04, 11), new Address("Tel-Aviv", "Ben-Gurion", 11));
		Child child = new Child(2000, "Moshe", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar-Kohba", 21),
				"Shalom");
		Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 21), new Address("Rehovot", "Herzl", 7),
				"Motorolla", 20000);

		personRepositoty.save(person);
		personRepositoty.save(child);
		personRepositoty.save(employee);
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Person> getModelClass(PersonDto personDto) {
		String className = personDto.getClass().getName();
		className = (className.substring(className.lastIndexOf(".") + 1));
		className = (className.substring(0, className.length() - 3));

		String path = "telran.java2022.person.model.";
		Class<? extends Person> modelClass = null;

		try {
			modelClass = (Class<? extends Person>) Class.forName(path + className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelClass;

	}

	@SuppressWarnings("unchecked")
	private Class<? extends PersonDto> getModelDto(Person person) {
		String className = person.getClass().getName();
		className = (className.substring(className.lastIndexOf(".") + 1)) + "Dto";

		String path = "telran.java2022.person.dto.";
		Class<? extends PersonDto> classDto = null;

		try {
			classDto = (Class<? extends PersonDto>) Class.forName(path + className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classDto;
	}

//	private Class<? extends Person> getModelClass(PersonDto personDto) {
//		if (personDto instanceof EmployeeDto) {
//			return Employee.class;
//		}
//		if (personDto instanceof ChildDto) {
//			return Child.class;
//		}
//		return Person.class;
//	}

//	private Class<? extends PersonDto> getModelDto(Person person) {
//		String className = person.getClass().toString();
//		className = (className.substring(className.lastIndexOf(".") + 1));
//		if (className.equals("Employee")) {
//			return EmployeeDto.class;
//		}
//		if (className.equals("Child")) {
//			return ChildDto.class;
//		}
//		return PersonDto.class;
//		
//
//	}

//	@Override
//	@Transactional(readOnly = true)
//	public Iterable<CityPopulationDto> getCitiesPopulation() {
//		Map<String, Long> populationMap = StreamSupport.stream(personRepositoty.findAll().spliterator(), false)
//				.collect(Collectors.groupingBy(p -> p.getAddress().getCity(), Collectors.counting()));
//
//		return populationMap.entrySet().stream().map(e -> new CityPopulationDto(e.getKey(), e.getValue()))
//				.collect(Collectors.toList());
//	}

}
