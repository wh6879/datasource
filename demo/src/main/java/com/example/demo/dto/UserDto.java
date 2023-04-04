package com.example.demo.dto;

import com.example.demo.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDto extends PagingAndSortingRepository<User, Integer> {
}
