package de.doubleslash.thingworx.mcp.server.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

@Service
public class CollectionsTools {
    private static final Logger log = LoggerFactory.getLogger(CollectionsTools.class);

    private final ThingWorxApi api;

    public CollectionsTools(ThingWorxApi api) {
        this.api = api;
    }

    @Tool(name = "listCollection", description = "Lists a ThingWorx built-in collection (e.g., Things, Users, DataShapes, ThingTemplates, ThingShapes).")
    @Cacheable(cacheNames = "listings", key = "#collectionSlug", unless = "#result == null || #result.startsWith('Error')")
    public String listCollection(@ToolParam(description = "Collection slug, e.g., 'Things' or 'Users'") String collectionSlug) {
        log.info("Tool called: listCollection, collectionSlug={}", collectionSlug);
        return api.get("Thingworx", collectionSlug);
    }
}
