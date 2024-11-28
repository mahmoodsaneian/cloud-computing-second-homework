package com.example.cloud_computing_second_homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Dictionary")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WordEntity implements Serializable {

    @Id
    private String word;
    private String definition;
    private boolean valid;
}
