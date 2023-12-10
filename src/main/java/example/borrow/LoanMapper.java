package example.borrow;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LoanMapper {

    LoanDto toDto(Loan loan);

    Loan toEntity(LoanDto loanDto);
}
