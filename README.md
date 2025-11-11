# ThingWorx MCP Server

[LLM-based agent for ThingWorx: Architecture and implementation with MCP](https://blog.doubleslash.de/en/iot-and-connected-things/llm-based-agent-for-thingworx-architecture-and-implementation-with-mcp/)

A Spring Boot-based server that exposes ThingWorx as a set of AI-friendly tools and APIs, with Redis-backed caching and flexible configuration for cloud or local deployment.

Disclaimer: This repository is an experimental project created for evaluation and testing purposes only. It is not intended for production use and comes without any warranties or guarantees.

## Features

- **REST API** for interacting with ThingWorx Things, Properties, Services, and Collections
- **AI Tooling**: Annotated methods for use with Spring AI MCP
- **Caching**: Redis-backed, with toggle to enable/disable at runtime
- **Configurable**: All important settings can be set via environment variables
- **Docker-ready**: Includes Dockerfile and docker-compose for easy local or cloud deployment

---

## Quick Start

### 1. Prerequisites

- Docker (for local dev/testing)
- Access to a ThingWorx instance (local or remote)

### 2. Run with Docker Compose (Recommended)

This will start both Redis and the MCP server, wired together. Adapt env variables in docker-compose.yaml to configure it for your environment (see below for explanation). And then run

```bash
docker-compose up --build
```

- The MCP server will be available at [http://localhost:8081](http://localhost:8081)
- Redis will be available at `localhost:6379` (internal to the Docker network)

#### Environment Variables (override as needed)

- `THINGWORX_BASE_URL` (default: `http://localhost`)
- `THINGWORX_APP_KEY` (your ThingWorx app key)
- `CACHE_ENABLED` (`true` or `false`, default: `true`)
- `SERVER_PORT` (default: `8081`)
- `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD` (default: `redis`, `6379`, empty)
- `MCP_SERVER_*` (see below for advanced MCP endpoints)

### 3. Run Locally (without Docker - needs Java 21+, Apache Maven)

- Start Redis yourself (e.g. `docker run -p 6379:6379 redis:7-alpine`)
- Set environment variables as needed
- Run the Spring Boot app:

```bash
mvn spring-boot:run
```

## API Overview

The server exposes REST endpoints (and AI tool endpoints) for:

- **Things**: List, get metadata, check existence
- **Properties**: List properties, get property value
- **Services**: List services, get parameters, invoke service
- **Collections**: List built-in ThingWorx collections (Things, Users, etc.)

All endpoints are available under the configured MCP endpoints (see `MCP_SERVER_ENDPOINT`, default `/mcp`).

---

## Caching

- By default, all tool methods use Redis for caching (see `@Cacheable` annotations in the code).
- To disable caching (for debugging or dev), set `CACHE_ENABLED=false`.
- Cache TTL and Redis connection are configurable via env vars.

---

## Advanced: Customizing MCP Endpoints

You can change the MCP endpoints via these env vars:

- `MCP_SERVER_ENABLED` (default: `true`)
- `MCP_SERVER_TYPE` (default: `SYNC`)
- `MCP_SERVER_PROTOCOL` (default: `STREAMABLE`)
- `MCP_SERVER_SSE` (default: `/sse`)
- `MCP_SERVER_ENDPOINT` (default: `/mcp`)
- `MCP_SERVER_SSE_MESSAGE_ENDPOINT` (default: `/mcp/message`)
