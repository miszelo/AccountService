package account.model.payment;

import account.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private LocalDate period;

    @NonNull
    private Long salary;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
