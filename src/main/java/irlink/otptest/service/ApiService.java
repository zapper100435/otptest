package irlink.otptest.service;

import de.taimos.totp.TOTP;
import irlink.otptest.common.EntityBuilder;
import irlink.otptest.entity.User;
import irlink.otptest.entity.UserQr;
import irlink.otptest.repository.UserQrRepository;
import irlink.otptest.repository.UserRepository;
import irlink.otptest.response.ApiResponse;
import irlink.otptest.vo.UserQrVO;
import irlink.otptest.vo.UserVO;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.security.SecureRandom;

@Service
public class ApiService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserQrRepository userQrRepository;

    @Autowired
    EntityBuilder entityBuilder;

    public ApiResponse signUp(UserVO userVO){
        if(userVO.getUserId() == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("ID를 입력해주세요.")
                    .build();
        }else if(userVO.getUserName() == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("이름을 입력해주세요.")
                    .build();
        }else if(userVO.getUserPw() == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("비밀번호를 입력해주세요.")
                    .build();
        }
        User user = userRepository.findByUserId(userVO.getUserId());
        if(user != null) {
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("이미 존재하는 계정입니다.")
                    .build();
        }

        userRepository.save(entityBuilder.buildUser(userVO));

        return ApiResponse
                .builder()
                .resultCode(1)
                .resultMessage("회원가입 성공.")
                .build();
    }

    public ApiResponse signIn(UserVO userVO, HttpSession httpSession){
        if(userVO.getUserId() == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("ID를 입력해주세요.")
                    .build();
        }else if(userVO.getUserPw() == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("비밀번호를 입력해주세요.")
                    .build();
        }
        User user = userRepository.findByUserId(userVO.getUserId());
        if(user == null) {
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("존재하지 않는 계정입니다.")
                    .build();
        }
        if(!user.getUserPw().equals(userVO.getUserPw())){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("비밀번호가 일치하지 않습니다.")
                    .build();
        }

        httpSession.removeAttribute("userId");
        httpSession.setAttribute("userId", user.getUserId());

        return ApiResponse
                .builder()
                .resultCode(1)
                .resultMessage("로그인 성공.")
                .build();
    }

    public ApiResponse getQrCode(HttpSession httpSession) throws Exception{
        User user = userRepository.findByUserId((String) httpSession.getAttribute("userId"));
        if(user == null) {
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("로그인 후 QR코드가 생성됩니다.")
                    .build();
        }

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        UserQr orgnUserQr = userQrRepository.findByUser(user);
        if(orgnUserQr != null){
            userQrRepository.delete(orgnUserQr);
        }
        UserQr userQr = userQrRepository.save(entityBuilder.buildUserQr(user, base32.encodeToString(bytes)));

        return ApiResponse
                .builder()
                .resultCode(1)
                .resultMessage(
                        "otpauth://totp/"
                                + URLEncoder.encode(user.getUserId(), "UTF-8").replace("+", "%20")
                                + "?secret=" + URLEncoder.encode(userQr.getSecretKey(), "UTF-8").replace("+", "%20")
                )
                .build();
    }

    public ApiResponse checkOtpCode(UserQrVO userQrVO, HttpSession httpSession){
        if(userQrVO.getOtp() == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("OTP 번호를 입력해주세요.")
                    .build();
        }
        User user = userRepository.findByUserId((String) httpSession.getAttribute("userId"));
        if(user == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("로그인 후 사용해주세요.")
                    .build();
        }

        UserQr userQr = userQrRepository.findByUser(user);
        if(userQr == null){
            return ApiResponse
                    .builder()
                    .resultCode(0)
                    .resultMessage("QR코드 생성 후 사용해주세요.")
                    .build();
        }

        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(userQr.getSecretKey());
        String hexKey = Hex.encodeHexString(bytes);

        String otp = TOTP.getOTP(hexKey);
        if(otp.equals(userQrVO.getOtp())){
            return ApiResponse
                    .builder()
                    .resultCode(1)
                    .resultMessage("OTP 인증 성공")
                    .build();
        }

        return ApiResponse
                .builder()
                .resultCode(0)
                .resultMessage("OTP 인증 실패")
                .build();
    }
}
