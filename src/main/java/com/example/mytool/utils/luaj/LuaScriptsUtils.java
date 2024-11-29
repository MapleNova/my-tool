package com.example.mytool.utils.luaj;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.luajc.LuaJC;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
@Slf4j
public class LuaScriptsUtils {

    public static Map<String, LuaScriptsEntity> mapActuator = new ConcurrentHashMap<>();

    /**
     * init   初始化
     *
     * @param entities 脚本相关试题
     * @return 返回加载的脚本数
     */
    public static int init(List<LuaScriptsEntity> entities) {
        //校验json.lua文件
        ClassPathResource resource = new ClassPathResource("json.lua");
        if (!resource.exists()) {
            log.error("缺少核心文件json.lua");
            return 0;
        }
        //按构造器分组，并且去除注释行
        Map<String, String> mapConstructor = new HashMap<>();
        for (LuaScriptsEntity info : entities) {
            String filteredOutput = clearAnnotation(info.getScriptContent());
            mapConstructor.put(info.getId(), filteredOutput);
            //将需要执行的信息加载到mapActuator，Key为Id
            //脚本已经加载到执行器，所以不需要
            info.setScriptContent(null);
            mapActuator.put(info.getId(), info);
        }
        //lua构造，并且把每个记录的构造器记录下来
        //脚本务必提前验证
        int scripts = 0;
        for (LuaScriptsEntity info : entities) {
            String entry = mapConstructor.get(info.getId());
            Pair<Integer, Object> luaConstructor = LuaScriptsUtils.luaConstructor(true, Collections.singletonList(entry));
            scripts = scripts + luaConstructor.getLeft();
            if (luaConstructor.getLeft() == 0) {
                log.error("分组标识{}构造器初始化失败", info.getId());
            } else if (luaConstructor.getLeft() != 1) {
                log.error("分组标识{}构造器初始化部分失败", info.getId());
            } else {
                info.setGlobals((Globals) luaConstructor.getRight());
            }
        }
        return scripts;
    }

    /**
     * luaConstructor 执行器构建
     *
     * @param luacInstall true时性能会稍快，但是无法动态加载脚本
     * @param scripts     不定数量脚本文件内容
     * @desc 当脚本错误时返回失败，务必需要先进行测试脚本是否正确
     * require优先会充系统中取、如果不存在会从resources中取
     * globals.set将库引用传递到脚本运行环境中去
     * LuaC.install开启后执行性能有较大提高，但是不能进行热加载
     * 加载错误时继续执行，不退出
     * 返回的左字段为成功加载的脚本数量，如果大于0，则返回Globals，否则返回字符串错误信息
     */
    public static Pair<Integer, Object> luaConstructor(boolean luacInstall, List<String> scripts) {
        Globals globals = JsePlatform.standardGlobals();
        if (scripts.isEmpty()) return new ImmutablePair<>(0, "No scripts content");

        try {
            globals.set("json", globals.get("require").call(LuaValue.valueOf("json")));
            int successRecords = loadScripts(globals, scripts, luacInstall);
            if (successRecords > 0) {
                return new ImmutablePair<>(successRecords, globals);
            } else {
                return new ImmutablePair<>(0, "No scripts loaded");
            }
        } catch (Exception e) {
            log.error("Lua require loading error：[{}]", e.getMessage());
            return new ImmutablePair<>(0, e.getMessage());
        }
    }

    /**
     * addScriptEntity 添加新的脚本实体到mapActuator中
     *
     * @param entity 新的脚本实体
     * @return 添加成功返回true，失败返回false
     */
    public static boolean addScriptEntity(LuaScriptsEntity entity) {
        // 校验参数是否为null以及ID是否已存在
        if (entity == null || entity.getId() == null
                || mapActuator.containsKey(entity.getId())
        ) {
            return false;
        }

        // 过滤注释行&前置空白行
        String filteredOutput = clearAnnotation(entity.getScriptContent());

        // 获取脚本中的命名空间
        String scriptNamespace = filteredOutput.split("=")[0].trim().replaceAll("\\s+", "");

        //判断entity的namespace和脚本中的是否一致
        if (!entity.getNamespace().equals(scriptNamespace)) {
            log.error("分组标识{}与脚本namespace不一致", entity.getNamespace());
            throw new RuntimeException("分组标识与脚本namespace不一致");
        }

        // 加载脚本到执行器
        Pair<Integer, Object> luaConstructor = luaConstructor(true, Collections.singletonList(filteredOutput));
        if (luaConstructor.getLeft() == 1) {
            // 脚本加载成功，将脚本实体添加到mapActuator中
            entity.setGlobals((Globals) luaConstructor.getRight());
            mapActuator.put(entity.getId(), entity);
            return true;
        } else {
            // 脚本加载失败
            return false;
        }
    }

    /**
     * updateScriptEntity 更新新的脚本实体到mapActuator中
     *
     * @param entity 新的脚本实体
     * @return 添加成功返回true，失败返回false
     */
    public static boolean updateScriptEntity(LuaScriptsEntity entity) {
        // 校验参数是否为null
        if (entity == null || entity.getId() == null) {
            return false;
        }

        // 过滤注释行&前置空白行
        String filteredOutput = clearAnnotation(entity.getScriptContent());

        // 获取脚本中的命名空间
        String scriptNamespace = filteredOutput.split("=")[0].trim().replaceAll("\\s+", "");

        //判断entity的namespace和脚本中的是否一致
        if (!entity.getNamespace().equals(scriptNamespace)) {
            log.error("分组标识{}与脚本namespace不一致", entity.getNamespace());
            return false;
        }

        // 加载脚本到执行器
        Pair<Integer, Object> luaConstructor = luaConstructor(true, Collections.singletonList(filteredOutput));
        if (luaConstructor.getLeft() == 1) {
            // 脚本加载成功，将脚本实体添加到mapActuator中
            entity.setGlobals((Globals) luaConstructor.getRight());
            mapActuator.put(entity.getId(), entity);
            return true;
        } else {
            // 脚本加载失败
            return false;
        }
    }

    /**
     * deleteScriptEntity mapActuator中删除指定脚本
     *
     * @param entity 脚本实体
     * @return 添加成功返回true，失败返回false
     */
    public static boolean deleteScriptEntity(LuaScriptsEntity entity) {
        // 校验参数是否为null以及ID是否存在
        if (entity == null || entity.getId() == null) {
            return false;
        }

        mapActuator.remove(entity.getId());
        return true;
    }

    /**
     * queryScriptEntity 查询mapActuator中是否存在脚本
     *
     * @param scriptId 脚本ID
     * @return 存在返回true，不存在返回false
     */
    public static boolean queryScriptEntity(String scriptId) {
        return mapActuator.containsKey(scriptId);
    }


    private static int loadScripts(Globals globals, List<String> scripts, boolean luacInstall) {
        int successRecords = 0;
        for (String str : scripts) {
            try {
                globals.load(str).call();
                successRecords++;
            } catch (LuaError e) {
                log.error("Lua脚本加载异常：[{}]", e.getMessage());
            }
        }
        if (successRecords > 0) {
            LoadState.install(globals);
            LuaJC.install(globals);
            if (luacInstall) LuaC.install(globals);
        }
        return successRecords;
    }


    /**
     * execLuaScript
     *
     * @param globals   globals
     * @param namespace 脚本命名空间
     * @param func      脚本执行方法入口
     * @desc 该方法应该是动态加载执行的方法
     * 脚本空表,以避免方法同名被覆盖
     * 方法函数,相同脚本空间中的方法后加载会覆盖前加载
     * 执行出错时，lua会抛出整个脚本文件，但实际有用的仅为最后一条
     * 所以出错时，截取最后一条错误信息，在脚本中建议使用error或assert抛出故障
     */
    public static Pair<Boolean, String> execLuaScripts(Globals globals, String namespace, String func, String jsonString) {
        try {
            LuaValue method = globals.get(namespace).get(func);
            LuaValue result = method.call(LuaValue.valueOf(jsonString));
            return new ImmutablePair<>(true, result.toString());
        } catch (LuaError e) {
            String errorMessage = e.getMessage().trim();
            log.error("Error Message : {}，源数据为：{}", errorMessage, jsonString);
            if (errorMessage.contains("\n")) {
                errorMessage = errorMessage.substring(errorMessage.lastIndexOf('\n') + 1).trim();
            }
            return new ImmutablePair<>(false, errorMessage);
        } catch (Exception e) {
            log.error("Error Message : {}，源数据为：{}", e.getMessage(), jsonString);
            return new ImmutablePair<>(false, e.getMessage());
        }
    }

    public static Pair<Boolean, String> execLuaScripts(LuaScriptsEntity entity, String jsonString) {
        return execLuaScripts(entity.getGlobals(), entity.getNamespace(), entity.getFunctionEntry(), jsonString);
    }


    /**
     * clearAnnotation 去除lua脚本中的注释行
     *
     * @param script 原始字符串脚本
     * @return 去除注释后的字符串脚本
     */
    public static String clearAnnotation(String script) {

        // 移除多行注释
        script = script.replaceAll("--\\[\\[(?:[^\\[\\]]|(?<=\\[\\[)[^]]|(?<=\\[])\\[\\[)*?]]", "");

        // 移除单行注释
        script = script.replaceAll("--.*", "");

        // 使用正则表达式匹配第一个非空白行之前的所有空白字符，并将其替换为空字符串
        Pattern pattern = Pattern.compile("^(\\s*\\n)*\\s*", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(script);
        if (matcher.find()) {
            script = script.substring(matcher.end());
        }

        return script;
    }


    /**
     * getContentResourceFile
     *
     * @param file resource中的文件
     * @return 读取的文件内容
     */
    public static String getContentResourceFile(String file) {
        ClassPathResource resource = new ClassPathResource(file);
        if (!resource.exists()) {
            log.warn("File '{}' does not exist.", file);
            return null;
        } else {
            StringBuilder content = new StringBuilder();
            try (InputStream inputStream = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
                return content.toString();
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }
    }
}
