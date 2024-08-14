package com.sparta.myscheduleserver.service;

import com.sparta.myscheduleserver.entity.Manager;
import com.sparta.myscheduleserver.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public void saveManager(Manager manager) {
        manager.setCreatedDay(new Timestamp(System.currentTimeMillis()));
        manager.setUpdatedDay(new Timestamp(System.currentTimeMillis()));
        managerRepository.save(manager);
    }

    public Manager findByIdManager(Long id) {
        return managerRepository.findById(id);
    }

    public List<Manager> findAllManager() {
        return managerRepository.findAll();
    }

    public void updateManager(Long id, Manager updatedManager) {
        Manager existingManager = managerRepository.findById(id);
        if (existingManager != null) {
            // 업데이트할 이름과 이메일 설정
            String newName = updatedManager.getName();
            String newEmail = updatedManager.getEmail();
            managerRepository.update(id, newName, newEmail);
        } else {
            throw new IllegalArgumentException("Manager with id " + id + " not found.");
        }
    }

    public void deleteManager(Long id) {
        Manager manager = managerRepository.findById(id);
        if (manager != null) {
            managerRepository.delete(id);
        } else {
            throw new IllegalArgumentException("Manager with id " + id + " not found.");
        }
    }
}
