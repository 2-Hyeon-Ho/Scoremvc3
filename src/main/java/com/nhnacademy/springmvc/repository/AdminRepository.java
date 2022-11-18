package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Admin;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Repository("adminRepository")
public class AdminRepository {
    private final Map<String, Admin> adminMap = new HashMap<>();

    public boolean exists(String id) {
        return adminMap.containsKey(id);
    }

    public boolean matches(String id, String password) {
        return Optional.ofNullable(getAdmin(id))
                .map(admin -> admin.getPassword().equals(password))
                .orElse(false);
    }

    public Admin getAdmin(String id) {
        return exists(id) ? adminMap.get(id) : null;
    }

    public Admin addAdmin() {
        Admin admin = new Admin();
        adminMap.put("admin", admin);
        return admin;
    }
}
