package com.example.mytool;

import com.example.mytool.utils.luaj.LuaScriptsEntity;
import com.example.mytool.utils.luaj.LuaScriptsUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = LuaTest.class)
public class LuaTest {

    @Test
    public void testNotificationStationStatus() {

        //加载脚本
        LuaScriptsEntity entity = new LuaScriptsEntity();
        entity.setId("100000000");
        entity.setFunctionEntry("notification_stationInfo");
        entity.setNamespace("HzGov_NotificationStationInfo");
        entity.setScriptContent(LuaScriptsUtils.getContentResourceFile("notificationStationInfoRequest.lua"));

        // 将 LuaScriptsEntity 对象添加到列表中
        List<LuaScriptsEntity> entities = Collections.singletonList(entity);

        // 初始化 LuaScriptsUtils
        int scriptsLoaded = LuaScriptsUtils.init(entities);

        if (scriptsLoaded < entities.size()) {
            System.out.println("加载失败");
        }
        String struct = LuaScriptsUtils.getContentResourceFile("notificationStationInfoRequest.json");

        // 执行 Lua 脚本
        Pair<Boolean, String> result = LuaScriptsUtils.execLuaScripts(LuaScriptsUtils.mapActuator.get("100000000"), struct);

        System.out.println(result.getValue());
        System.out.println();

    }
}
