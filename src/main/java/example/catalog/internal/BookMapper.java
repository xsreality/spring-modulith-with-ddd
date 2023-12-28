package example.catalog.internal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    BookDto toDto(CatalogBook catalogBook);
    CatalogBook toEntity(BookDto bookDto);
}
