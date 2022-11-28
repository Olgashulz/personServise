package telran.java2022.person.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Employee extends Person {
	private static final long serialVersionUID = -1380975707676502551L;
	
	String company;
	int salary;
	
	public Employee(Integer id, String name, LocalDate birthDate, Address address, String companyString,
			int salary) {
		super(id, name, birthDate, address);
		this.company = companyString;
		this.salary = salary;
	}
}
