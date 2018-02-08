package com.prevostc.mediafury.service;

import com.prevostc.mediafury.domain.Category;
import com.prevostc.mediafury.repository.CategoryRepository;
import com.prevostc.mediafury.service.dto.CategoryDTO;
import com.prevostc.mediafury.service.mapper.CategoryMapper;
import com.prevostc.mediafury.service.util.ImportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;


/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ImportUtil<CategoryDTO, Category, Long> importUtil;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.importUtil = new ImportUtil<>(categoryRepository, categoryMapper, (CategoryDTO dto) ->
            categoryRepository.findOneByName(dto.getName())
        );
    }

    /**
     * Imports category data, inserts the entity if it does not already exists
     * given the functional key [title, year]
     * @param personDTO the entity to save
     * @return the newly created entity
     */
    public CategoryDTO importData(CategoryDTO personDTO) {
        return this.importUtil.importData(personDTO);
    }

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        Optional<Category> existingCategory = categoryRepository.findOneByName(categoryDTO.getName());
        Category category = existingCategory.orElseGet(() -> categoryRepository.save(categoryMapper.toEntity(categoryDTO)));
        return categoryMapper.toDto(category);
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll(pageable)
            .map(categoryMapper::toDto);
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CategoryDTO findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        Category category = categoryRepository.findOne(id);
        return categoryMapper.toDto(category);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.delete(id);
    }
}
