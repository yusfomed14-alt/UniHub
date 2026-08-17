package com.unihub.service;

import com.unihub.entity.Person;
import com.unihub.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person=personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Person not found by Email:"+email));
        return new User(
                person.getEmail(),
                person.getPassword() !=null ? person.getPassword() : "",
                List.of(new SimpleGrantedAuthority("ROLE_"+person.getRole().name()))
        );
    }
}
