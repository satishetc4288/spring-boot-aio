package com.satish.exp.controller;

import com.satish.exp.dao.model.Employee;
import com.satish.exp.model.Greeting;
import com.satish.exp.dao.model.User;
import com.satish.exp.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(path="/demo")
public class BaseController {

    private static final String template = "hello %s";
    @Autowired
    private DaoService daoService;

    private static final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting getGreeting(@RequestParam String name){
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @PostMapping("/add/user")
    public User addNewUser(@RequestParam String name, @RequestParam String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        daoService.addUser(user);
        return user;
    }

    @PostMapping("/add/employee")
    public CompletableFuture<Employee> addEmployee(@RequestParam String name) throws InterruptedException{
        Employee employee = new Employee();
        employee.setName(name);
        daoService.addEmployee(employee);
        //Thread.sleep(5000);
        return CompletableFuture.completedFuture(employee);
    }
}
