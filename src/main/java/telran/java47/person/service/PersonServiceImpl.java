
 package telran.java47.person.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java47.person.dao.PersonRepository;
import telran.java47.person.dto.AddressDto;
import telran.java47.person.dto.ChildDto;
import telran.java47.person.dto.CityPopulationDto;
import telran.java47.person.dto.EmployeeDto;
import telran.java47.person.dto.PersonDto;
import telran.java47.person.dto.exceptions.PersonNotFoundException;
import telran.java47.person.model.Address;
import telran.java47.person.model.Child;
import telran.java47.person.model.Employee;
import telran.java47.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;
	 @Override
	    @Transactional
	    public Boolean addPerson(PersonDto personDto) {
	        if (personRepository.existsById(personDto.getId())) {
	            return false;
	        }
	        Person person = modelMapper.map(personDto, Person.class);
	        personRepository.save(person);
	        return true;
	    }
	 @Override
	    @Transactional
	    public Boolean addEmployee(EmployeeDto employeeDto) {
	        if (personRepository.existsById(employeeDto.getId())) {
	            return false;
	        }
	        Employee employee = modelMapper.map(employeeDto, Employee.class);
	        personRepository.save(employee);
	        return true;
	    }

	    @Override
	    @Transactional
	    public Boolean addChild(ChildDto childDto) {
	        if (personRepository.existsById(childDto.getId())) {
	            return false;
	        }
	        Child child = modelMapper.map(childDto, Child.class);
	        personRepository.save(child);
	        return true;
	    }

	    @Override
	    public PersonDto findPersonById(Integer id) {
	        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
	        return mapToPersonDto(person);
	    }

	    @Override
	    @Transactional
	    public PersonDto removePerson(Integer id) {
	        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
	        personRepository.delete(person);
	        return mapToPersonDto(person);
	    }

	    @Override
	    @Transactional
	    public PersonDto updatePersonName(Integer id, String name) {
	        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
	        person.setName(name);
	        return mapToPersonDto(person);
	    }

	    @Override
	    @Transactional
	    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
	        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
	        person.setAddress(modelMapper.map(addressDto, Address.class));
	        return mapToPersonDto(person);
	    }

	    @Override
	    @Transactional(readOnly = true)
	    public Iterable<PersonDto> findPersonsByCity(String city) {
	        return personRepository.findByAddressCityIgnoreCase(city)
	                .map(this::mapToPersonDto)
	                .collect(Collectors.toList());
	    }

	    @Override
	    @Transactional(readOnly = true)
	    public Iterable<PersonDto> findPersonsByName(String name) {
	        return personRepository.findByNameIgnoreCase(name)
	                .map(this::mapToPersonDto)
	                .collect(Collectors.toList());
	    }

	    @Override
	    @Transactional(readOnly = true)
	    public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
	        LocalDate from = LocalDate.now().minusYears(maxAge);
	        LocalDate to = LocalDate.now().minusYears(minAge);
	        return personRepository.findByBirthDateBetween(from, to)
	                .map(this::mapToPersonDto)
	                .collect(Collectors.toList());
	    }

	    @Override
	    public Iterable<CityPopulationDto> getCitiesPopulation() {
	        return personRepository.getCitiesPopulation();
	    }

	    @Override
	    public void run(String... args) throws Exception {
	        if (personRepository.count() == 0) {
	            Person person = new Person(1000, "John", LocalDate.of(1985, 4, 11), new Address("Tel Aviv", "Ben Gvirol", 87));
	            Child child = new Child(2000, "Mosche", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
	            Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 23), new Address("Rehovot", "Herzl", 7), "Motorola", 20_000);
	            personRepository.save(person);
	            personRepository.save(child);
	            personRepository.save(employee);
	        }
	    }

	    private PersonDto mapToPersonDto(Person person) {
	       	        	
	        if (person instanceof Child) {
	            return modelMapper.map(person, ChildDto.class);
	        }
	        if (person instanceof Employee) {
	            return modelMapper.map(person, EmployeeDto.class);
	        }
	        return modelMapper.map(person, PersonDto.class);
	    }

	   
}
