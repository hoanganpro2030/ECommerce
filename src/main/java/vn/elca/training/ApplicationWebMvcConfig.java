package vn.elca.training;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;

/**
 * @author gtn
 */
@Configuration
public class ApplicationWebMvcConfig extends WebMvcConfigurerAdapter {
    private static final String SERVICE_PATH = "/service";
    private static final String PATH_PATTERNS = "/**";
    private static final String FRONT_CONTROLLER = "index.prod.html";
    private static final String CONTEXT_PATH_PLACEHOLDER = "#context-path#";
    private static final String FRONT_CONTROLLER_ENCODING = "UTF-8";

    private final String baseHref;

    public ApplicationWebMvcConfig(@Value("${server.contextPath}") final String baseHref) {
        this.baseHref = baseHref;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PATH_PATTERNS)
                .addResourceLocations("/")
                .resourceChain(true)
                .addResolver(new SinglePageAppResourceResolver());
    }

    private class SinglePageAppResourceResolver extends PathResourceResolver {

        private TransformedResource transformedResource(final Resource resource) throws IOException {
            String fileContent = IOUtils.toString(resource.getInputStream(), FRONT_CONTROLLER_ENCODING);
            if (fileContent.contains(CONTEXT_PATH_PLACEHOLDER)) {
                fileContent = fileContent.replace(CONTEXT_PATH_PLACEHOLDER, baseHref);
            } else {
                // This is prevent in case the CONTEXT_PATH_PLACEHOLDER is not filled when build with Angular.
                fileContent = fileContent.replace("<base href=\"/\">", "<base href=\"" + baseHref + "\">");
            }

            return new TransformedResource(resource, fileContent.getBytes());
        }

        @Override
        protected Resource getResource(final String resourcePath, final Resource location) throws IOException {
            Resource resource = location.createRelative(resourcePath);
            if (resource.exists() && resource.isReadable()) {
                //if the asked resource is index.html, we serve it with the base-href rewritten
                if (resourcePath.contains(FRONT_CONTROLLER)) {
                    return transformedResource(resource);
                }

                return resource;
            }

            //do not serve a Resource on an reserved URI
            if (("/" + resourcePath).startsWith(SERVICE_PATH)) {
                return null;
            }

            //we just refreshed a page, no ?
            resource = location.createRelative(FRONT_CONTROLLER);
            if (resource.exists() && resource.isReadable()) {
                return transformedResource(resource);
            }

            return null;
        }
    }
}
