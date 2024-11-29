package com.example.mytool;

import com.example.mytool.utils.AesUtils;
import com.example.mytool.utils.HMacUtils;
import com.example.mytool.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SecretTest.class)
class SecretTest {

    @Test
    void contextLoads() {
    }

    @Test
    void encryptAES() {
        //上海站点费率推送专用
//        String s = AesUtils.encryptAES("{\"StationFee\":{\"OperatorID\":\"MA2J378Y2\",\"StationID\":\"432956646395973\",\"ChargeFeeDetail\":[{\"EquipmentType\":2,\"StartTime\":\"00:00\",\"EndTime\":\"23:59\",\"ElectricityFee\":0.90,\"ServiceFee\":0.60}]}}", "LUJ3IBENGkH7Fx3E", "VzqjUKtARYxBFNTI");
        //宁波市政请求我方token
//        String s = AesUtils.encryptAES("{\"OperatorID\":\"14405899X\",\"OperatorSecret\":\"e4xlQu40IvS4LEwO8489V757Xgg44nK9\"}", "qW55fHWdRE8H71T6", "5Z1S67z578kYx6vS");
        //宁波市政查询我方站点
//        String s = AesUtils.encryptAES("{\"PageNo\":1,\"PageSize\":10}", "qW55fHWdRE8H71T6", "5Z1S67z578kYx6vS");
        //宁波市政查询设备接口状态
//        String s = AesUtils.encryptAES("{\"StationIDs\":[\"433157064675397\"]}", "qW55fHWdRE8H71T6", "5Z1S67z578kYx6vS");
        //杭州市政请求我方token
//        String s = AesUtils.encryptAES("{\"OperatorID\":\"MA27UHZZF\",\"OperatorSecret\":\"TfJc5VdrzzWQQGFU\"}", "RyTLsTUJpPZNH6qJ", "j3HkHhWrT93W7adr");
        //杭州市政查询我方站点
//        String s = AesUtils.encryptAES("{\"PageNo\":1,\"PageSize\":10}", "RyTLsTUJpPZNH6qJ", "j3HkHhWrT93W7adr");
        //杭州市政查询设备接口状态
//        String s = AesUtils.encryptAES("{\"StationIDs\":[\"601662993293381\"]}", "RyTLsTUJpPZNH6qJ", "j3HkHhWrT93W7adr");
        //杭州市政查询站点统计信息
//        String s = AesUtils.encryptAES("{\"StationID\": \"601662993293381\",\"StartTime\": \"2024-01-01\",\"EndTime\": \"2024-11-01\"}", "RyTLsTUJpPZNH6qJ", "j3HkHhWrT93W7adr");
        //杭州市政推送设备所属方数据
//        String s = AesUtils.encryptAES("{\"EquipmentOwnerInfo\":{\"EquipmentOwnerID\":\"MABM7G6QX\",\"EquipmentOwnerName\":\"杭州均悦充新能源有限公司\",\"EquipmentOwnerCode\":\"91330110MABM7G6QXL\",\"EquipmentOwnerContacts\":\"陈燕\",\"EquipmentOwnerContactsPhone\":\"13777577150\",\"EquipmentOwnerInsertDate\":\"2024-11-06\"}}", "RyTLsTUJpPZNH6qJ", "j3HkHhWrT93W7adr");
        //嘉兴市政请求我方token
//        String s = AesUtils.encryptAES("{\"OperatorID\":\"MA27U0A2J\",\"OperatorSecret\":\"j3HkHhWrT93W7ad1\"}", "RyTLsTUJpPZNH6q1", "L3FpLSvERgZGoZv1");
        //嘉兴市政查询我方站点
//        String s = AesUtils.encryptAES("{\"PageNo\":1,\"PageSize\":10}", "RyTLsTUJpPZNH6q1", "L3FpLSvERgZGoZv1");
        //嘉兴市政查询设备接口状态
//        String s = AesUtils.encryptAES("{\"StationIDs\":[\"609756403662917\"]}", "RyTLsTUJpPZNH6q1", "L3FpLSvERgZGoZv1");
        //嘉兴市政查询站点统计信息
        String s = AesUtils.encryptAES("{\"StationID\": \"609756403662917\",\"StartTime\": \"2024-01-01\",\"EndTime\": \"2024-11-01\"}", "RyTLsTUJpPZNH6q1", "L3FpLSvERgZGoZv1");

        //随便用的
//        String s = AesUtils.encryptAES("{\"OperatorID\":\"MA01DA4G2\",\"OperatorSecret\":\"Jy9P764AB2ZGBI3d\"}", "IwsHgkKWecAYRENw", "r4cKAy5vjPegoYAc");
//        String s = AesUtils.encryptAES("{\"AccessToken\":\"61f5f7d2988b458a88eb60e20b8471de\",\"FailReason\":0,\"OperatorID\":\"MA2J378Y2\",\"SuccStat\":0,\"TokenAvailableTime\":86400}", "94343c121e6d4677", "af447fd3b8d54ccc");

        System.out.println(s);
    }

    @Test
    void Hmac() {
        String sig = "MA27U0A2JkBy8sSgHbpeEq5R/eYlhHTNCw65h5oJEYGCbU4mfaaKyZOAQDEFIGL59TOhp07QgECcSTEyOfb3eqCR+bMqbHpZR/9R0T8lah4kLD7N9TLjvF2GLoXk7QNFt3qjBJpaM202410211500000001";
        String en = HMacUtils.encrypt("TfJc5VdrzzWQQGF1", sig);
        System.out.println(en);
    }

    @Test
    void decryptAES() {
        String s = AesUtils.decryptAES("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", "RyTLsTUJpPZNH6qJ", "j3HkHhWrT93W7adr");
        System.out.println(s);
    }

}
