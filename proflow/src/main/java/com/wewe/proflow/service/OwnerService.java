package com.wewe.proflow.service;

import com.wewe.proflow.dto.OwnerDTO;
import com.wewe.proflow.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerService {
    List<Owner> getAllOwners();
    Optional<Owner> getOwnerById(Long id);
    Owner createOwner(Owner owner);
    Owner updateOwner(Long id, Owner updatedOwner);
    void deleteOwner(Long id);

}
