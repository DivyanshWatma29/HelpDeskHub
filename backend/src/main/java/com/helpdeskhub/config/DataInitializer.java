package com.helpdeskhub.config;

import com.helpdeskhub.model.Department;
import com.helpdeskhub.model.User;
import com.helpdeskhub.model.UserRole;
import com.helpdeskhub.repository.DepartmentRepository;
import com.helpdeskhub.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DataInitializer(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            Department it = departmentRepository.save(new Department("IT Support", "IT"));
            departmentRepository.save(new Department("HR Operations", "HR"));
            departmentRepository.save(new Department("Finance IT", "FIN"));
            departmentRepository.save(new Department("Facilities", "FAC"));
            departmentRepository.save(new Department("Access & Security", "SEC"));

            if (userRepository.count() == 0) {
                userRepository.save(new User("System Admin", "admin@helpdeskhub.local", UserRole.ADMIN, it));
                userRepository.save(new User("Vikram Malhotra", "agent@helpdeskhub.local", UserRole.AGENT, it));
            }
        }
    }
}
