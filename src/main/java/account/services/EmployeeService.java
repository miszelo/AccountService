package account.services;

import account.mapper.PaymentMapper;
import account.exceptions.PaymentNotFoundException;
import account.exceptions.UserNotFoundException;
import account.model.dto.PaymentInfoDTO;
import account.model.payment.Payment;
import account.model.user.User;
import account.repositories.PaymentRepository;
import account.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public ResponseEntity<?> getEmployeeInfo(UserDetails userDetails, String period) {
        if (period == null) {
            User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                    .orElseThrow(UserNotFoundException::new);

            return ResponseEntity.ok(
                    user.getPayments()
                            .stream()
                            .sorted(Comparator.comparing(Payment::getPeriod).reversed())
                            .map(paymentMapper::mapPaymentToEmployeeInfoDTO)
                            .collect(Collectors.toList()));
        }

        Payment payment = paymentRepository.findByEmailIgnoreCaseAndPeriod(userDetails.getUsername(),
                        YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy")).atDay(1))
                .orElseThrow(PaymentNotFoundException::new);
        PaymentInfoDTO paymentInfoDTO = paymentMapper.mapPaymentToEmployeeInfoDTO(payment);
        return ResponseEntity.ok(paymentInfoDTO);
    }
}