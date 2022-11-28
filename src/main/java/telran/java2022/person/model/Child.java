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
public class Child extends Person {

	private static final long serialVersionUID = -4773929812822320798L;
	String kindergarten;

	public Child(Integer id, String name, LocalDate birthDate, Address address, String kindergarden) {
		super(id, name, birthDate, address);
		this.kindergarten = kindergarden;
	}

}
