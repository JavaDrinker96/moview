package com.example.moview.mapper;

import com.example.moview.dto.pagination.PaginationDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring")
public interface PaginationMapper {

    default PageRequest dtoToRequest(PaginationDto dto, String property) {

        return PageRequest.of(dto.getPage(), dto.getSize(),
                Sort.by(dto.getDirection(), property));
    }
}
