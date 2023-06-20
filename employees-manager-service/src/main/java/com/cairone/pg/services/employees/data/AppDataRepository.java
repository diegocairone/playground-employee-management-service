package com.cairone.pg.services.employees.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppDataRepository<T, ID> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
}
