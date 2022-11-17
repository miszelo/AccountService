package account.model.security;

import account.model.record.Action;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class SecurityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private Action action;
    private String subject;
    private String object;
    private String path;
}