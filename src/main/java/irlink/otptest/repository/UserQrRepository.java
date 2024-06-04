package irlink.otptest.repository;

import irlink.otptest.entity.User;
import irlink.otptest.entity.UserQr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQrRepository extends JpaRepository<UserQr, Long> {

    UserQr findByUser(User user);
}
