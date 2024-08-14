package com.sparta.myscheduleserver.controller;

import com.sparta.myscheduleserver.entity.Manager;
import com.sparta.myscheduleserver.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public void createManager(@RequestBody Manager manager) {
        managerService.saveManager(manager);
    }

    @GetMapping("/{id}")
    public Manager getManager(@PathVariable Long id) {
        return managerService.findByIdManager(id);
    }

    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.findAllManager();
    }

    @PutMapping("/{id}")
    public void updateManager(@PathVariable Long id, @RequestBody Manager updatedManager) {
        managerService.updateManager(id, updatedManager);
    }


    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
    }
}