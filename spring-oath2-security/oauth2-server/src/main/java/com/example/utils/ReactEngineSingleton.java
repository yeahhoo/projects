package com.example.utils;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Aleksandr_Savchenko
 */
public class ReactEngineSingleton {

    private NashornScriptEngine nashornScriptEngine;

    private ReactEngineSingleton() {
        nashornScriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        try {
            nashornScriptEngine.eval(read("static/libs/react-15.3.2.js"));
            nashornScriptEngine.eval(read("static/libs/react-dom-15.3.2.js"));
            nashornScriptEngine.eval(read("static/libs/react-dom-server-15.3.2.js"));
            nashornScriptEngine.eval(read("static/libs/server-logout-form.js"));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }

    private static class LazyHolder {
        public static final ReactEngineSingleton INSTANCE = new ReactEngineSingleton();
    }

    public static NashornScriptEngine getReactEngine() {
        return LazyHolder.INSTANCE.nashornScriptEngine;
    }
}
