package com.example.cloud_computing_second_homework.repository;

import com.example.cloud_computing_second_homework.entity.WordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends CrudRepository<WordEntity, String> {
}
