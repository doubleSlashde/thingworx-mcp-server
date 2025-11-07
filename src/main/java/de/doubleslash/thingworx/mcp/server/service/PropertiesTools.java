package de.doubleslash.thingworx.mcp.server.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

@Service
public class PropertiesTools {
    private static final Logger log = LoggerFactory.getLogger(PropertiesTools.class);

    private final ThingWorxApi api;

    public PropertiesTools(ThingWorxApi api) {
        this.api = api;
    }

    @Tool(name = "listThingProperties", description = "Lists property definitions for a Thing.")
    @Cacheable(cacheNames = "properties", key = "#thingName + '::defs'", unless = "#result == null || #result.startsWith('Error')")
    public String listThingProperties(@ToolParam(description = "Exact Thing name") String thingName) {
        log.info("Tool called: listThingProperties, thingName={}", thingName);
        return api.get("Thingworx", "Things", thingName, "PropertyDefinitions");
    }

    @Tool(name = "getThingPropertyValue", description = "Returns the current value of a Thing property. Property name is case-sensitive.")
    @Cacheable(cacheNames = "properties", key = "#thingName + '::' + #propertyName", unless = "#result == null || #result.startsWith('Error')")
    public String getThingPropertyValue(
            @ToolParam(description = "Exact Thing name") String thingName,
            @ToolParam(description = "Exact property name") String propertyName) {
        log.info("Tool called: getThingPropertyValue, thingName={}, propertyName={}", thingName, propertyName);
        return api.get("Thingworx", "Things", thingName, "Properties", propertyName);
    }
}
