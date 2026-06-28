package org.bigBrotherBooks.service;

import jakarta.inject.Singleton;
import org.bigBrotherBooks.api.HttpClient;
import org.bigBrotherBooks.api.HttpClientBuilder;
import org.bigBrotherBooks.infra.utils.CollectionUtils;
import org.bigBrotherBooks.infra.utils.JsonUtils;
import org.bigBrotherBooks.infra.utils.StringUtils;
import org.bigBrotherBooks.infra.utils.TemplateUtils;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.bigBrotherBooks.model.HttpRequest;
import org.bigBrotherBooks.model.ResourceConfig;
import org.bigBrotherBooks.model.Response;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.bigBrotherBooks.constants.GlobalConstants.CONFIGS_CLASSPATH_PREFIX;

@Singleton
public class ResourceService {

    private final Map<String, HttpClient> httpClientCache = new ConcurrentHashMap<>();
    private final Map<String, ResourceConfig> resourceConfigCache = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);


    public Object getResource(String resourceType, String resourceId, Map<String, Object> inputs) {
        try {
            LOGGER.log(LogType.INFO, "Fetching resource {}:{} with inputs {}", resourceType, resourceId, () -> JsonUtils.toJson(inputs));
            ResourceConfig resourceConfig = JsonUtils.deepCopy(getResourceConfig(resourceType, resourceId));
            resolveInputs(resourceConfig, inputs);
            HttpClient client = getHttpClient(resourceType, resourceId);
            Response<Object> response = client.send(resourceConfig.getHttpRequest());
            LOGGER.log(LogType.INFO, "Resource {}:{} response status {}", resourceType, resourceId, response::getStatus);
            // adapt response based on resourceType if needed
            return response.getResponse();

        } catch (Exception e) {
            LOGGER.log(LogType.ERROR, "Failed to get resource", e);
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
        String resourcePath = CONFIGS_CLASSPATH_PREFIX + "/" + resourceType + "/" + resourceId + ".json";
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource config not found on classpath: " + resourcePath);
            }
            return JsonUtils.fromJson(inputStream, ResourceConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse resource config: " + resourcePath, e);
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
