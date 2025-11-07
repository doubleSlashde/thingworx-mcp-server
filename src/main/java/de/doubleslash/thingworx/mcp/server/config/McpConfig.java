package de.doubleslash.thingworx.mcp.server.config;

import de.doubleslash.thingworx.mcp.server.service.CollectionsTools;
import de.doubleslash.thingworx.mcp.server.service.PropertiesTools;
import de.doubleslash.thingworx.mcp.server.service.ServicesTools;
import de.doubleslash.thingworx.mcp.server.service.ThingsTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class McpConfig {

   @Bean
   public ToolCallbackProvider thingWorxTools(ThingsTools thingsTools,
                     PropertiesTools propertiesTools,
                     ServicesTools servicesTools,
                     CollectionsTools collectionsTools) {
      return MethodToolCallbackProvider.builder()
         .toolObjects(thingsTools, propertiesTools, servicesTools, collectionsTools)
         .build();
   }
}