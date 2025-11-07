package de.doubleslash.thingworx.mcp.server.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class ServicesTools {
    private static final Logger log = LoggerFactory.getLogger(ServicesTools.class);

    private final ThingWorxApi api;

    public ServicesTools(ThingWorxApi api) {
        this.api = api;
    }

    @Tool(name = "listThingServices", description = "Lists service definitions for a Thing.")
    @Cacheable(cacheNames = "services", key = "#thingName", unless = "#result == null || #result.startsWith('Error')")
    public String listThingServices(@ToolParam(description = "Exact Thing name") String thingName) {
        log.info("Tool called: listThingServices, thingName={}", thingName);
        return api.get("Thingworx", "Things", thingName, "ServiceDefinitions");
    }

    @Tool(name = "getServiceParameters", description = "Lists parameters of a Service.")
    @Cacheable(cacheNames = "services", key = "#thingName + '::' + #serviceName", unless = "#result == null || #result.startsWith('Error')")
    public String getServiceParameters(@ToolParam(description = "Exact Thing name") String thingName,
            @ToolParam(description = "Exact Service name") String serviceName) {
        log.info("Tool called: getServiceParameters, thingName={}, serviceName={}", thingName, serviceName);
        return api.get("Thingworx", "Things", thingName, "ServiceDefinitions", serviceName);
    }

    @Tool(name = "invokeThingService", description = "Invokes a service on a Thing. If it fails due to name, call listThings then listThingServices."
          + "IMPORTANT: If the Service does a large change, Ask the User before invoking it.")
    @CacheEvict(cacheNames = {"things","listings","services","properties"}, allEntries = true)
    public String invokeThingService(
            @ToolParam(description = "Exact Thing name") String thingName,
            @ToolParam(description = "Exact service name") String serviceName,
            @ToolParam(description = "Optional map of service input parameters", required = false) Map<String, Object> input) {
        log.info("Tool called: invokeThingService, thingName={}, serviceName={}, input={}", thingName, serviceName, input);
        if (!api.exists("Thingworx", "Things", thingName)) {
            return "Thing '" + thingName + "' does not exist. Call listThings to discover valid names.";
        }
        try {
            return api.postJson(input, "Thingworx", "Things", thingName, "Services", serviceName);
        } catch (RuntimeException ex) {
            return "Service invocation failed. Verify the service name by calling listThingServices and check inputs. Details: " + ex.getMessage();
        }
    }
}
