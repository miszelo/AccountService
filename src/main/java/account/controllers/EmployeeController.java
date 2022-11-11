package account.controllers;

import account.services.EmployeeService;
import account.validation.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/empl")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/payment")
    public ResponseEntity<?> getEmployeeInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestParam(required = false) @Date String period) {
        return employeeService.getEmployeeInfo(userDetails, period);
    }


}
