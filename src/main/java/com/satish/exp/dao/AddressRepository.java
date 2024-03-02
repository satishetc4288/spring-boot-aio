package com.satish.exp.dao;

import com.satish.exp.dao.model.Address;
import com.satish.exp.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
