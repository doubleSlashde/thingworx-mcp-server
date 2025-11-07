package de.doubleslash.thingworx.mcp.server.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

@Service
public class ThingsTools {
    private static final Logger log = LoggerFactory.getLogger(ThingsTools.class);

    private final ThingWorxApi api;

    public ThingsTools(ThingWorxApi api) {
        this.api = api;
    }

    @Tool(name = "listThings", description = "Lists all Things. Call this first when you are unsure of an exact Thing name.")
    @Cacheable(cacheNames = "listings", key = "'things'", unless = "#result == null || #result.startsWith('Error')")
    public String listThings() {
        log.info("Tool executing: listThings");
        try {
            return api.get("Thingworx", "Things");
        } catch (RuntimeException ex) {
            return "Error listing Things: " + ex.getMessage();
        }
    }

    @Tool(name = "getThing", description = "Returns metadata for a Thing by name. Useful to quickly check if a Thing exists.")
    @Cacheable(cacheNames = "things", key = "#thingName", unless = "#result == null || #result.startsWith('Thing not found')")
    public String getThing(@ToolParam(description = "Exact Thing name") String thingName) {
        log.info("Tool called: getThing, thingName={}", thingName);
        try {
            return api.get("Thingworx", "Things", thingName);
        } catch (RuntimeException ex) {
            return "Thing not found or inaccessible: '" + thingName + "'. Call listThings to discover valid names. Details: " + ex.getMessage();
        }
    }

    @Tool(name = "thingExists", description = "Returns true if the Thing exists, false otherwise. Prefer calling listThings when false.")
    public boolean thingExists(@ToolParam(description = "Exact Thing name") String thingName) {
        log.info("Tool called: thingExists, thingName={}", thingName);
        return api.exists("Thingworx", "Things", thingName);
    }
}
