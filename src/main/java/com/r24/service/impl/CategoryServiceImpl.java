package com.r24.service.impl;

import com.r24.entity.Category;
import com.r24.repository.CategoryRepository;
import com.r24.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        Category existing = categoryRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setName(category.getName());
        existing.setImageUrl(category.getImageUrl());
        existing.setDescription(category.getDescription());
        existing.setActive(category.getActive());

        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}