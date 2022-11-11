package account.controllers;

import account.controllers.mapper.PaymentMapper;
import account.model.dto.NewPaymentDTO;
import account.model.payment.Payment;
import account.model.record.ResponseStatus;
import account.services.AcctService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/acct")
public class AcctController {

    private final AcctService acctService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/payments")
    public ResponseEntity<ResponseStatus> addPaymentsForEmployee(@RequestBody List<@Valid NewPaymentDTO> paymentsDTO) {
        List<Payment> payments = paymentsDTO.stream()
                .map(paymentMapper::mapNewPaymentDTOToPayment)
                .collect(Collectors.toList());
        return acctService.addPaymentsForEmployee(payments);
    }

    @PutMapping("/payments")
    public ResponseEntity<ResponseStatus> updatePaymentForEmployee(@RequestBody @Valid NewPaymentDTO newPaymentDTO) {
        return acctService.updatePaymentForEmployee(paymentMapper.mapNewPaymentDTOToPayment(newPaymentDTO));
    }
}
