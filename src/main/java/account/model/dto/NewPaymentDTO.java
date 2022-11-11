package account.model.dto;

import account.validation.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NewPaymentDTO {

    @Email(regexp = "\\w+(@acme.com)$", message = "Mail does not include @acme.com")
    @JsonProperty("employee")
    private String email;

    @Date
    private String period;

    @Min(value = 0)
    private Long salary;
}
