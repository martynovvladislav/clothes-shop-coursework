package edu.mirea.clothes_shop.service;

import edu.mirea.clothes_shop.dto.auth.SignUpDto;
import edu.mirea.clothes_shop.exception.UserAlreadyExistException;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        return user.get();
    }

    public UserDetails signUp(SignUpDto data) {
        if (userRepository.findByEmail(data.email()).isPresent()) {
            throw new UserAlreadyExistException("This email is already in use");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User();
        newUser.setEmail(data.email());
        newUser.setFirstName(data.firstName());
        newUser.setLastName(data.lastName());
        newUser.setRole(UserRole.USER);
        newUser.setOrders(new ArrayList<>());
        newUser.setPassword(encryptedPassword);
        return userRepository.save(newUser);
    }
}
