package com.wewe.temjaiShop.security;

import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // 🔍 ใช้ username แทน email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsernameOrEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + usernameOrEmail));

//        // ดึง roles จาก user (เช่น ROLE_USER, ROLE_ADMIN)
//        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .collect(Collectors.toList());

//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(), // หรือ user.getEmail() ถ้าอยากใช้ email เป็น principal
//                user.getPassword(),
//                user.isEnabled(), // enabled
//                true, // accountNonExpired
//                true, // credentialsNonExpired
//                true, // accountNonLocked
//                authorities
//        );
        return UserPrincipal.create(user);  // แปลง User เป็น UserDetails (UserPrincipal คือคลาสของคุณ)
    }
}
