package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.PageableDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class PageableMapper {

    public static PageableDto toDto(Page page) {
        return  new ModelMapper().map(page, PageableDto.class);
    }
}
