package com.cairone.pg.core.pageable;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeePageableConverter implements Converter<Pageable, Pageable> {

    @Override
    public Pageable convert(Pageable pageable) {

        List<Sort.Order> orders = pageable.getSort().get().map(viewOrder -> {
                    if (viewOrder.getProperty().equals("yearsOld")) {
                        Sort.Direction direction = viewOrder.getDirection().isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                        return new Sort.Order(direction, "birthDate");
                    } else {
                        return viewOrder;
                    }
                })
                .collect(Collectors.toList());

        Sort sort = Sort.by(orders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
