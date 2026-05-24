package org.example.authrole.service;

import org.example.authrole.entity.Permission;

import java.util.List;

public interface AuthService {
    List<Permission> getAllPermission();
}
