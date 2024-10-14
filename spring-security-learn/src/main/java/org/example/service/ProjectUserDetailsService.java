package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found "+username));
        List<GrantedAuthority> authorities = customer.getAuthorities().stream().map(authority ->
                        new SimpleGrantedAuthority(authority.getName()))
              .collect(Collectors.toList());
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
      return new User(customer.getEmail(), customer.getPwd(), authorities);
    }
}
