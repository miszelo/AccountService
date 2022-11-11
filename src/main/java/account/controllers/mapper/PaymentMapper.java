package account.controllers.mapper;

import account.exceptions.UserNotFoundException;
import account.model.dto.NewPaymentDTO;
import account.model.dto.PaymentInfoDTO;
import account.model.payment.Payment;
import account.repositories.UserRepository;
import account.util.PaymentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final UserRepository userRepository;

    public PaymentInfoDTO mapPaymentToEmployeeInfoDTO(Payment payment) {
        return PaymentInfoDTO
                .builder()
                .name(payment.getUser().getName())
                .lastname(payment.getUser().getLastname())
                .period(PaymentUtil.formatPeriodFromDateToString(payment.getPeriod()))
                .salary(PaymentUtil.formatSalaryFromLongToDollarsAndCents(payment.getSalary()))
                .build();
    }

    public Payment mapNewPaymentDTOToPayment(NewPaymentDTO newPaymentDTO) {
        return Payment.builder()
                .email(newPaymentDTO.getEmail())
                .period(PaymentUtil.formatPeriodFromStringToDate(newPaymentDTO.getPeriod()))
                .salary(newPaymentDTO.getSalary())
                .user(userRepository.findByEmailIgnoreCase(newPaymentDTO.getEmail()).orElseThrow(UserNotFoundException::new))
                .build();
    }
}
