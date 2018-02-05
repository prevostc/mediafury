package com.prevostc.mediafury.service.util;

import com.prevostc.mediafury.service.mapper.EntityMapper;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;

/**
 * Utility class for inserting stub data
 */
public class ImportUtil<D, E, ID extends Serializable> {

    CrudRepository<E, ID> repository;
    EntityMapper<D, E> mapper;
    Callback<D, E> fetch;

    /**
     * Creates an import util instance
     * @param repository An entity repository
     * @param mapper A DTO<->Entity mapper
     * @param fetch A callback that fetches an entity from a DTO object. If the entity is found, the import is skipped
     */
    public ImportUtil(CrudRepository<E, ID> repository, EntityMapper<D, E> mapper, Callback<D, E> fetch) {
        this.repository = repository;
        this.mapper = mapper;
        this.fetch = fetch;
    }

    /**
     * Insert an entity if it is not found in the database
     * @param dto
     * @return The newly created entity DTO
     */
    public D importData(D dto) {
        Optional<E> existingEntity = fetch.callback(dto);
        E entity = existingEntity.orElseGet(() -> repository.save(mapper.toEntity(dto)));
        return mapper.toDto(entity);
    }

    public interface Callback<D, E> {
        Optional<E> callback(D dto);
    }
}
