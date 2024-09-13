package com.example.mytool;

import com.example.mytool.utils.AesUtils;
import com.example.mytool.utils.HMacUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = MyToolApplicationTests.class)
class MyToolApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void encryptAES() {
        //上海站点费率推送专用
        String s = AesUtils.encryptAES("{\"StationFee\":{\"OperatorID\":\"MA2J378Y2\",\"StationID\":\"432956646395973\",\"ChargeFeeDetail\":[{\"EquipmentType\":2,\"StartTime\":\"00:00\",\"EndTime\":\"23:59\",\"ElectricityFee\":0.90,\"ServiceFee\":0.60}]}}", "LUJ3IBENGkH7Fx3E", "VzqjUKtARYxBFNTI");

        System.out.println(s);
    }

    @Test
    void Hmac() throws NoSuchAlgorithmException {
        String sig = "MA2J378Y2mtOGEF40rI0i+GwcsLdc7x4bHlyhwkfcvIobB7/3ZJFjNVrwxQp1PhOX6NA3Axz9geSWrfweQaWhpXF+IzC7d1LNVuZZtImdxQvnC5t85qnwcDHWAcmEadF2ATyYHWv5LDwwLcC5x5JSf/dB4g1J44DQKSQXYpOiN4Mk8Q7qwAzCOcf/AA1ViBpfceKaaZh2tqSegnXK3oHXCCXaKPYNwSUNba7HZM7QearD3vs+aZuYg+uXRsKgleUTGybUUEmq202409120908110001";
        String en = HMacUtils.encrypt("kf9C224Knj3R3n8V", sig);
        System.out.println(en);
    }

}
