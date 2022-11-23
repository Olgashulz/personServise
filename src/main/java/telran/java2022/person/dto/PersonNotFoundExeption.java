package telran.java2022.person.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonNotFoundExeption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5909431443943172364L;

}
