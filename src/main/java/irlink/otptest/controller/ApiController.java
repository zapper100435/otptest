package irlink.otptest.controller;

import irlink.otptest.response.ApiResponse;
import irlink.otptest.service.ApiService;
import irlink.otptest.vo.UserQrVO;
import irlink.otptest.vo.UserVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @PostMapping("/signUp")
    public ApiResponse signUp(@RequestBody UserVO userVO) {

        return apiService.signUp(userVO);
    }

    @PostMapping("/signIn")
    public ApiResponse signIn(@RequestBody UserVO userVO, HttpSession httpSession) {

        return apiService.signIn(userVO, httpSession);
    }

    @PostMapping("/getOtpQRCode")
    public ApiResponse getQrCode(HttpSession httpSession) throws Exception{

        return apiService.getQrCode(httpSession);
    }

    @PostMapping("/checkOtpCode")
    public ApiResponse checkOtpCode(@RequestBody UserQrVO userQrVO, HttpSession httpSession){

        return apiService.checkOtpCode(userQrVO, httpSession);
    }
}
