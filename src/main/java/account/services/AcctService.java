package account.services;


import account.exceptions.PaymentNotFoundException;
import account.model.payment.Payment;
import account.model.record.ResponseStatus;
import account.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcctService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public ResponseEntity<ResponseStatus> addPaymentsForEmployee(List<Payment> payments) {
        payments.forEach(p -> {
            if (paymentRepository.findByEmailIgnoreCaseAndPeriod(p.getEmail(), p.getPeriod()).isPresent()) {
                throw new PaymentNotFoundException();
            }
        });
        paymentRepository.saveAll(payments);

        return ResponseEntity.ok(new ResponseStatus("Added successfully!"));
    }

    @Transactional
    public ResponseEntity<ResponseStatus> updatePaymentForEmployee(Payment payment) {
        Payment paymentToUpdate = paymentRepository
                .findByEmailIgnoreCaseAndPeriod(payment.getEmail(), payment.getPeriod())
                .orElseThrow(PaymentNotFoundException::new);
        paymentToUpdate.setSalary(payment.getSalary());
        paymentRepository.save(paymentToUpdate);

        return ResponseEntity.ok(new ResponseStatus("Updated successfully!"));
    }
}
