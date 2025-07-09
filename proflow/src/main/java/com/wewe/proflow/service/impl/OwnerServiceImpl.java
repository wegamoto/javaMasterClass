package com.wewe.proflow.service.impl;

import com.wewe.proflow.dto.OwnerDTO;
import com.wewe.proflow.model.Owner;
import com.wewe.proflow.repository.OwnerRepository;
import com.wewe.proflow.service.OwnerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @Override
    public Optional<Owner> getOwnerById(Long id) {
        return ownerRepository.findById(id);
    }

    @Override
    public Owner createOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public Owner updateOwner(Long id, Owner updatedOwner) {
        return ownerRepository.findById(id)
                .map(owner -> {
                    owner.setName(updatedOwner.getName());
                    owner.setEmail(updatedOwner.getEmail());
                    // อัพเดตฟิลด์อื่น ๆ ตามต้องการ
                    return ownerRepository.save(owner);
                })
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + id));
    }

    @Override
    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }

//    @Override
//    public List<Owner> getAllOwners() {
//        return ownerRepository.findAll();
//    }

    private OwnerDTO convertToDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        // เพิ่ม fields อื่นถ้ามี
        return dto;
    }
}
