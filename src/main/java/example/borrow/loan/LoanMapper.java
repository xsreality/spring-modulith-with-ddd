package example.borrow.loan;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanMapper {

    LoanDto toDto(Loan loan);

    Loan toEntity(LoanDto loanDto);
}
