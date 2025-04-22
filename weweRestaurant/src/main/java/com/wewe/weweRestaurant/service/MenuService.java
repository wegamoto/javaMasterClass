package com.wewe.weweRestaurant.service;

import com.wewe.weweRestaurant.model.MenuItem;
import com.wewe.weweRestaurant.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private final MenuItemRepository repo;

    public MenuService(MenuItemRepository repo) {
        this.repo = repo;
    }

    public List<MenuItem> getAll() { return repo.findAll(); }
}

