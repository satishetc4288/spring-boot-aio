package com.satish.exp.service;

import com.satish.exp.dao.AddressRepository;
import com.satish.exp.dao.EmployeeRepository;
import com.satish.exp.dao.UserRepository;
import com.satish.exp.dao.model.Address;
import com.satish.exp.dao.model.Employee;
import com.satish.exp.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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



}
