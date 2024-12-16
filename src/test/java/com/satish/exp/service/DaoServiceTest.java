package com.satish.exp.service;

import com.satish.exp.repo.AddressRepository;
import com.satish.exp.repo.EmployeeRepository;
import com.satish.exp.repo.UserRepository;
import com.satish.exp.repo.model.Employee;
import com.satish.exp.repo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


public class DaoServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private AddressRepository addressRepository;

    @InjectMocks
    public DaoService daoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        daoService.setAddressRepository(addressRepository);
        daoService.setUserRepository(userRepository);
        daoService.setEmployeeRepository(employeeRepository);
    }

    @Test
    public void testUserAddSuccess(){
        when(userRepository.save(Mockito.any())).thenReturn(new User());
        daoService.addUser(new User());
        assertTrue(true);
    }

    @Test
    public void testEmployeeAddSuccess(){
        when(employeeRepository.save(Mockito.any())).thenReturn(new Employee());
        daoService.addEmployee(new Employee());
        assertTrue(true);
    }

    @Test
    public void testGetAllUsers() throws ExecutionException, InterruptedException {
        when(userRepository.findAll()).thenReturn(List.of(new User()));
        CompletableFuture<List<User>> users = daoService.getAllUsers();
        assertEquals(1, users.get().size());
    }

}
