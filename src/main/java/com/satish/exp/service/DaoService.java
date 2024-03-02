package com.satish.exp.service;

import com.satish.exp.dao.UserRepository;
import com.satish.exp.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaoService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User addUser(User user){
        userRepository.save(user);
        return user;
    }

}
