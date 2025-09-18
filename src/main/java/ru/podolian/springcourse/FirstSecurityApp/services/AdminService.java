package ru.podolian.springcourse.FirstSecurityApp.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ADMIN') or hasRole('SOME_OTHER')")
    public void doAdminStuff() {
        System.out.println("Only admin here");
    }
}
