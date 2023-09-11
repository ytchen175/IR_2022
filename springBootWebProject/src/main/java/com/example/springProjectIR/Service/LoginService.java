package com.example.springProjectIR.Service;


import com.example.springProjectIR.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AccountRepository accountRepository;
    private final PasswordService passwordService;

    public LoginService(AccountRepository accountRepository, PasswordService passwordService) {
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
    }

    public boolean isLoginSuccessful(String userName, String password){
        if(this.accountRepository.checkIfUserNameExists(userName)){
            String saltedHashedPassword = this.accountRepository.getStoredPasswordByUsername(userName);
            String salt = this.accountRepository.getSaltByUsername(userName);
            if(saltedHashedPassword.equals(this.passwordService.hashPasswordWithMD5(password,salt)))
                return true;
        }
        return false;
    }

}
