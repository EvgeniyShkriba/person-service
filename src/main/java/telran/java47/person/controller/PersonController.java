package telran.java47.person.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java47.person.dto.AddressDto;
import telran.java47.person.dto.CityPopulationDto;
import telran.java47.person.dto.PersonDto;
import telran.java47.person.service.PersonService;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

	final PersonService personService;

	@PostMapping
	public Boolean addPerson(@RequestBody PersonDto personDto) {
		return personService.addPerson(personDto);
	}

	@GetMapping("/{id}")
	public PersonDto findPersonById(@PathVariable Integer id) {
		return personService.findPersonById(id);
	}

	@PutMapping("/{id}/name/{name}")
	public PersonDto UpdatePersonByName(@PathVariable Integer id, @PathVariable String name) {
		return personService.UpdatePersonByName(id, name);
	}

	@PutMapping("/{id}/address")
	public PersonDto updatePersonByAddress(@PathVariable Integer id,@RequestBody AddressDto addressDto) {
		return personService.updatePersonByAddress(id,addressDto);
	}

	@DeleteMapping("/{id}")
	public PersonDto removePersonById(@PathVariable Integer id) {
		return personService.removePersonById(id);
	}

	@GetMapping("/city/{city}")
	public List<PersonDto> findPersonsByCity(@PathVariable String city) {

		return personService.findPersonsByCity(city);
	}

	@GetMapping("/ages/{ageFrom}/{ageTo}")
	public List<PersonDto> findPersonsByAgePeriod(@PathVariable Integer ageFrom,@PathVariable Integer ageTo) {
		return personService.findPersonsByAgePeriod(ageFrom, ageTo);
	}

	@GetMapping("/name/{name}")
	public List<PersonDto> findPersonsByNameIgnoreCase(@PathVariable String name) {
		return personService.findPersonsByNameIgnoreCase(name);
	}

	@GetMapping("/population/city")
	public List<CityPopulationDto> getPopulationByCity() {
		return personService.getPopulationByCity();
	}

}
