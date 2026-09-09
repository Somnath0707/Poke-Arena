package com.pokearena.security;

import com.pokearena.entity.Trainer;
import com.pokearena.repository.TrainerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final TrainerRepository trainerRepository;

    public CustomUserDetailsService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Trainer trainer = trainerRepository.findByName(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found" + username));

        return User.builder()
                .username(trainer.getName())
                .password(trainer.getPassword())
                .authorities(trainer.getRole())
                .build();
    }
}
