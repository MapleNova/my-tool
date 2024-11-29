package com.example.mytool.utils.luaj;

import lombok.Data;
import lombok.experimental.Accessors;
import org.luaj.vm2.Globals;

@Data
@Accessors(chain = true)
public class LuaScriptsEntity {

    private String id;

    private String namespace;

    private String functionEntry;

    private String scriptContent;

    private Globals globals;

}
