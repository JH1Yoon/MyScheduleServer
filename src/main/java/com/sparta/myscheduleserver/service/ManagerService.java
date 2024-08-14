package com.sparta.myscheduleserver.service;

import com.sparta.myscheduleserver.dto.ManagerRequestDto;
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

    // 담당자 생성
    public void saveManager(ManagerRequestDto managerRequestDto) {
        Manager manager = new Manager();
        manager.setName(managerRequestDto.getName());
        manager.setEmail(managerRequestDto.getEmail());
        manager.setCreatedDay(new Timestamp(System.currentTimeMillis()));
        manager.setUpdatedDay(new Timestamp(System.currentTimeMillis()));
        managerRepository.save(manager);
    }

    // 특정 ID에 해당하는 담당자 조회
    public Manager findByIdManager(Long id) {
        return managerRepository.findById(id);
    }

    // 담당자 목록 조회
    public List<Manager> findAllManager() {
        return managerRepository.findAll();
    }

    // 담당자 수정
    // 이메일은 형식을 맞추고 빈칸이면 안된다.
    // 이름도 빈칸이면 안됨
    public void updateManager(Long id, ManagerRequestDto managerRequestDto) {
        Manager existingManager = managerRepository.findById(id);
        if (existingManager != null) {
            // 업데이트할 이름과 이메일 설정
            existingManager.setName(managerRequestDto.getName());
            existingManager.setEmail(managerRequestDto.getEmail());

            managerRepository.update(id, existingManager.getName(), existingManager.getEmail());
        } else {
            throw new IllegalArgumentException(id + "가 Manager에서 발견되지 않았습니다.");
        }
    }

    // 담당자 삭제
    public void deleteManager(Long id) {
        Manager manager = managerRepository.findById(id);
        if (manager != null) {
            managerRepository.delete(id);
        } else {
            throw new IllegalArgumentException(id + "가 Manager에서 발견되지 않았습니다.");
        }
    }
}
