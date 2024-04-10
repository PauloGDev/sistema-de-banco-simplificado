package com.volt.bancosimplificado.services;

import com.volt.bancosimplificado.domain.transaction.Transaction;
import com.volt.bancosimplificado.domain.user.User;
import com.volt.bancosimplificado.dtos.TransactionDTO;
import com.volt.bancosimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
public class TransactionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserByDoc(transaction.senderDoc());
        User receiver = this.userService.findUserByDoc(transaction.receiverDoc());

        //Checks the amount available in the sender's balance

        if(!sender.getPassword().equalsIgnoreCase(transaction.senderPass())){
            throw new Exception("Invalid Password!");
        }

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction();
        if(!isAuthorized){
            throw new Exception("Unauthorized transaction");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transaction completed successfully!");

        this.notificationService.sendNotification(receiver, "You received a transaction!");

        return newTransaction;
    }

    public boolean authorizeTransaction(){
        ResponseEntity<Map> authorizationresponse = restTemplate.getForEntity(
                "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);

        if(authorizationresponse.getStatusCode() == HttpStatus.OK){
            String message = (String) Objects.requireNonNull(authorizationresponse.getBody()).get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }else{
            return false;
        }
    }
}
