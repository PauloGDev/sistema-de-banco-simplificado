package com.volt.bancosimplificado.services;

import com.volt.bancosimplificado.domain.user.User;
import com.volt.bancosimplificado.domain.user.UserType;
import com.volt.bancosimplificado.dtos.UserDTO;
import com.volt.bancosimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {


        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("This type of user is unable to carry out transactions");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Insufficient funds");
        }
    }

//    public User findUserById(Long id) throws Exception {
//        return this.repository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
//    }

    public User findUserByDoc(String document) throws Exception {
        return this.repository.findUserByDocument(document).orElseThrow(() -> new Exception("User not found"));
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();

    }

    public void saveUser(User user){
        this.repository.save(user);
    }
}
