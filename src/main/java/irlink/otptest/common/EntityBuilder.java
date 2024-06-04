package irlink.otptest.common;

import irlink.otptest.entity.User;
import irlink.otptest.entity.UserQr;
import irlink.otptest.vo.UserVO;
import org.springframework.stereotype.Component;

@Component
public class EntityBuilder {

    public User buildUser(UserVO userVO){
        return User
                .builder()
                .userId(userVO.getUserId())
                .userPw(userVO.getUserPw())
                .userName(userVO.getUserName())
                .build();
    }

    public UserQr buildUserQr(User user, String secretKey){
        return UserQr
                .builder()
                .user(user)
                .secretKey(secretKey)
                .build();
    }
}
