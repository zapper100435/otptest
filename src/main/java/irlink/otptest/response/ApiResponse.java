package irlink.otptest.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse {
    private int resultCode;
    private String resultMessage;
}
