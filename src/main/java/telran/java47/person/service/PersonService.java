package telran.java47.person.service;

import java.time.LocalDate;
import java.util.List;

import telran.java47.person.dto.AddressDto;
import telran.java47.person.dto.CityPopulationDto;
import telran.java47.person.dto.PersonDto;

public interface PersonService {
	Boolean addPerson(PersonDto personDto);

	PersonDto findPersonById(Integer id);

	PersonDto UpdatePersonByName(Integer id,String newName);

	PersonDto updatePersonByAddress(Integer id,AddressDto addressDto);

	PersonDto removePersonById(Integer id);

	List<PersonDto> findPersonsByCity(String city);

	List<PersonDto> findPersonsByAgePeriod(Integer ageFrom, Integer ageTo);

	List<PersonDto> findPersonsByNameIgnoreCase(String name);

	List<CityPopulationDto> getPopulationByCity();

}
