package irlink.otptest.entity;

import irlink.otptest.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "USER_QR")
public class UserQr extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String secretKey;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

}
