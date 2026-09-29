package com.as.crichub.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Serves the built React SPA (copied into {@code classpath:/static/} by the
 * frontend's {@code npm run build:backend}) and makes client-side routes work
 * on a full page load.
 *
 * <p>Spring already serves {@code /} and real static files. The custom resolver
 * adds the single-page-app fallback: any path that isn't a real file and isn't
 * an API call returns {@code index.html}, so React Router can take over for
 * deep links like {@code /tournaments/5/teams}.
 */
@Configuration
public class SpaWebConfig implements WebMvcConfigurer {

	private static final String STATIC_LOCATION = "classpath:/static/";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations(STATIC_LOCATION)
				.resourceChain(true)
				.addResolver(new PathResourceResolver() {
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						Resource requested = location.createRelative(resourcePath);
						if (requested.exists() && requested.isReadable()) {
							// A real asset (index.html, /assets/*, favicon, …).
							return requested;
						}
						if (resourcePath.startsWith("api/")) {
							// Let unmatched API paths 404 as JSON, not the SPA shell.
							return null;
						}
						// Unknown non-API path → hand back the SPA so the client router resolves it.
						return location.createRelative("index.html");
					}
				});
	}
}
