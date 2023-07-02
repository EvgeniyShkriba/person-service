package telran.java47.person.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.person.dao.PersonRepository;
import telran.java47.person.dto.AddressDto;
import telran.java47.person.dto.CityPopulationDto;
import telran.java47.person.dto.PersonDto;
import telran.java47.person.dto.exceptions.PersonNotFoundException;
import telran.java47.person.model.Address;
import telran.java47.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto UpdatePersonByName(Integer id, String newName) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		if (newName != null) {
			person.setName(newName);
		}
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonByAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		if (addressDto.getCity() != null && addressDto.getStreet() != null && addressDto.getBuilding() != null) {

			Address newAddress = new Address(addressDto.getCity(), addressDto.getStreet(), addressDto.getBuilding());
			person.setAddress(newAddress);
			personRepository.save(person);
		}

		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto removePersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.delete(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public List<PersonDto> findPersonsByCity(String city) {

		return personRepository.findAll().stream()
				.filter(p -> p.getAddress().getCity().equalsIgnoreCase(city))
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	
	}
	@Override
	public List<PersonDto> findPersonsByAgePeriod(Integer ageFrom, Integer ageTo) {
	    
	    return personRepository.findAll().stream()
	            .filter(person -> calculateAge(person.getBirthDate())<=ageTo&&
	            		calculateAge(person.getBirthDate())>=ageFrom)
	            .map(person -> modelMapper.map(person, PersonDto.class))
	            .collect(Collectors.toList());
	}
	private int calculateAge(LocalDate birthDate) {
		int age = Period.between(birthDate, LocalDate.now()).getYears();
		return age;
	}
	
	
	@Override

	public List<PersonDto> findPersonsByNameIgnoreCase(String name) {
	    return personRepository.findAll().stream()
	            .filter(person -> person.getName().equalsIgnoreCase(name))
            .map(person -> modelMapper.map(person, PersonDto.class))
            .collect(Collectors.toList());
	}

	@Override
	public List<CityPopulationDto> getPopulationByCity() {
	    Map<String, Long> populationMap = personRepository.findAll().stream()
	            .collect(Collectors.groupingBy(
	                    person -> person.getAddress().getCity(),
	                    Collectors.counting() ));

	    return populationMap.entrySet().stream()
	            .map(entry -> new CityPopulationDto(entry.getKey(), entry.getValue()))
	            .collect(Collectors.toList());
	}


}
