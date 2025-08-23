package org.bigBrotherBooks.service;

import jakarta.inject.Singleton;
import org.bigBrotherBooks.api.HttpClient;
import org.bigBrotherBooks.api.HttpClientBuilder;
import org.bigBrotherBooks.infra.utils.CollectionUtils;
import org.bigBrotherBooks.infra.utils.JsonUtils;
import org.bigBrotherBooks.infra.utils.StringUtils;
import org.bigBrotherBooks.infra.utils.TemplateUtils;
import org.bigBrotherBooks.model.HttpRequest;
import org.bigBrotherBooks.model.ResourceConfig;
import org.bigBrotherBooks.model.Response;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.bigBrotherBooks.constants.GlobalConstants.CONFIGS_BASE_PATH;

@Singleton
public class ResourceService {

    private final Map<String, HttpClient> httpClientCache = new ConcurrentHashMap<>();
    private final Map<String, ResourceConfig> resourceConfigCache = new ConcurrentHashMap<>();


    public Object getResource(String resourceType, String resourceId, Map<String, Object> inputs) {
        try {
            ResourceConfig resourceConfig = getResourceConfig(resourceType, resourceId);
            resolveInputs(resourceConfig, inputs);
            HttpClient client = getHttpClient(resourceType, resourceId);
            Response<Object> response = client.send(resourceConfig.getHttpRequest());
            // adapt response based on resourceType if needed
            return response.getResponse();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get resource", e);
        }

    }

    private HttpClient getHttpClient(String resourceType, String resourceId) {
        String cacheKey = resourceType + ":" + resourceId;
        return httpClientCache.computeIfAbsent(cacheKey, k -> {
            HttpClientBuilder builder = new HttpClientBuilder();
            return builder.build();
        });
    }


    private ResourceConfig getResourceConfig(String resourceType, String resourceId) {
        String cacheKey = resourceType + ":" + resourceId;

        return resourceConfigCache.computeIfAbsent(cacheKey, k -> {
            try {
                return loadResourceConfig(resourceType, resourceId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load resource config", e);
            }
        });
    }

    private ResourceConfig loadResourceConfig(String resourceType, String resourceId) throws Exception {
        Path configPath = Paths.get(CONFIGS_BASE_PATH, resourceType, resourceId + ".json");
        if (!Files.exists(configPath)) {
            throw new FileNotFoundException("Resource config not found: " + configPath);
        }
        // Load and parse JSON configuration
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            return JsonUtils.fromJson(inputStream, ResourceConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse resource config: " + configPath, e);
        }
    }

    private void resolveInputs(ResourceConfig resourceConfig, Map<String, Object> inputs) {
        HttpRequest httpRequest = resourceConfig.getHttpRequest();
        if (httpRequest == null) {
            return;
        }
        if (httpRequest.getMethod() != null) {
            httpRequest.setMethod(TemplateUtils.resolveTemplate(String.valueOf(httpRequest.getMethod()), inputs).toString());
        }
        if (!StringUtils.isEmpty(httpRequest.getBaseUrl())) {
            httpRequest.setBaseUrl(TemplateUtils.resolveTemplate(httpRequest.getBaseUrl(), inputs).toString());
        }
        if (!StringUtils.isEmpty(httpRequest.getPath())) {
            httpRequest.setPath(TemplateUtils.resolveTemplate(httpRequest.getPath(), inputs).toString());
        }
        if (!CollectionUtils.isEmpty(httpRequest.getHeaders())) {
            httpRequest.getHeaders().replaceAll((k, v) -> TemplateUtils.resolveTemplate(v, inputs).toString());
        }
        if (!CollectionUtils.isEmpty(httpRequest.getQueryParams())) {
            httpRequest.setQueryParams(TemplateUtils.resolveTemplate(httpRequest.getQueryParams(), inputs));
        }
        if (!StringUtils.isEmpty(httpRequest.getBody())) {
            httpRequest.setBody(TemplateUtils.resolveTemplate(httpRequest.getBody(), inputs).toString());
        }
    }

}
