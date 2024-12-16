package com.satish.exp.service;

import com.satish.exp.repo.AddressRepository;
import com.satish.exp.repo.EmployeeRepository;
import com.satish.exp.repo.UserRepository;
import com.satish.exp.repo.model.Address;
import com.satish.exp.repo.model.Employee;
import com.satish.exp.repo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DaoService {

    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    private AddressRepository addressRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository){this.employeeRepository=employeeRepository;}
    @Autowired
    public void setAddressRepository(AddressRepository addressRepository){
        this.addressRepository=addressRepository;
    }

    @Transactional
    public User addUser(User user){
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Async
    public void addEmployee(Employee employee){
        employeeRepository.save(employee);
        Address address = new Address();
        address.setId(1l);
        address.setAddress("Varanasi");
        address.setEmployee(employee);
        //if(1==1)
            //throw new RuntimeException("hahahah");
        this.addressRepository.save(address);
        //return employee;
    }

    public CompletableFuture<List<User>> getAllUsers(){
        return CompletableFuture.supplyAsync( () -> userRepository.findAll());
    }
}
