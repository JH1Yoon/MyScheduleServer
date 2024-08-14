package com.sparta.myscheduleserver.controller;

import com.sparta.myscheduleserver.dto.ManagerRequestDto;
import com.sparta.myscheduleserver.entity.Manager;
import com.sparta.myscheduleserver.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@Validated
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    // 담당자 생성
    @PostMapping
    public void createManager(@Valid @RequestBody ManagerRequestDto managerRequestDto) {
        managerService.saveManager(managerRequestDto);
    }

    // 특정 ID에 해당하는 담당자 조회
    @GetMapping("/{id}")
    public Manager getManager(@PathVariable Long id) {
        return managerService.findByIdManager(id);
    }

    // 담당자 목록 조회
    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.findAllManager();
    }

    // 담당자 수정
    @PutMapping("/{id}")
    public void updateManager(@PathVariable Long id, @Valid @RequestBody ManagerRequestDto managerRequestDto) {
        managerService.updateManager(id, managerRequestDto);
    }
    
    // 담당자 삭제
    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
    }
}