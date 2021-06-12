package com.frlib.web;

/**
 * @author ???
 * @since 1.0.0
 */
public class HookManager {

    public static AgentWeb hookAgentWeb(AgentWeb agentWeb, AgentWeb.AgentBuilder agentBuilder) {
        return agentWeb;
    }

    public static boolean permissionHook(String url, String[]permissions){
        return true;
    }
}
