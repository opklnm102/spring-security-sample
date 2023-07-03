package com.example.customuser2.domain;

import com.example.customuser2.domain.type.UseYN;
import com.example.customuser2.domain.type.UserGrade;
import com.example.customuser2.entity.user.Privilege;
import com.example.customuser2.entity.user.Role;
import com.example.customuser2.entity.user.User;
import com.example.customuser2.repository.PrivilegeRepository;
import com.example.customuser2.repository.RoleRepository;
import com.example.customuser2.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

//@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PrivilegeRepository repositoryRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = repositoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        var readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        var writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        var deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        var adminRole = createRoleIfNotFound("ROLE_ADMIN", List.of(readPrivilege, writePrivilege, deletePrivilege));
        var staffRole = createRoleIfNotFound("ROLE_STAFF", List.of(readPrivilege, writePrivilege));
        var userRole = createRoleIfNotFound("ROLE_USER", List.of(readPrivilege));

        userRepository.save(new User("bronze@example.com", passwordEncoder.encode("1234"), UserGrade.BRONZE, UseYN.Y, List.of(userRole)));
        userRepository.save(new User("silver@example.com", passwordEncoder.encode("5678"), UserGrade.SILVER, UseYN.Y, List.of(staffRole)));
        userRepository.save(new User("gold@example.com", passwordEncoder.encode("abcd"), UserGrade.GOLD, UseYN.Y, List.of(adminRole)));

        alreadySetup = true;
    }

    private Privilege createPrivilegeIfNotFound(String name) {
        return privilegeRepository.findByName(name)
                .orElseGet(() -> privilegeRepository.save(new Privilege(name)));

    }

    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(name, privileges)));
    }
}
