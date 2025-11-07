package de.doubleslash.thingworx.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class McpServerApplication {

   public static void main(String[] args) {
      SpringApplication.run(McpServerApplication.class, args);
   }
}
