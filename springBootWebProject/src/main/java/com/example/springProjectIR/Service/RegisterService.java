package com.example.springProjectIR.Service;

import com.example.springProjectIR.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final AccountRepository accountRepository;
    private final PasswordService passwordService;

    public RegisterService(AccountRepository accountRepository, PasswordService passwordService) {
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
    }

    public boolean checkIfUserNameExsits(String username) {
        return this.accountRepository.checkIfUserNameExists(username);
    }

    public void register(String username, String password, String email) {
        String salt = this.passwordService.generateRandomSalt();
        String encryptedPassword =this.passwordService.hashPasswordWithMD5(password,salt);
        this.accountRepository.insertAccountData(username,encryptedPassword,salt,email);
    }
}
