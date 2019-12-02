package picture.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Properties;

@ConfigurationProperties(prefix = "application.proxy")
@Component
@Data
public class ProxyConfig {
    private String host;
    private String port;
    private String noProxy;

    @PostConstruct
    void setupProxy() {
        final Properties systemProps = System.getProperties();
        if (!StringUtils.isEmpty(host)) {
            systemProps.put("proxySet", "true");
            Optional.ofNullable(host).ifPresent(h -> systemProps.put("proxyHost", h));
            Optional.ofNullable(port).ifPresent(h -> systemProps.put("proxyPort", h));
            Optional.ofNullable(noProxy).ifPresent(h -> systemProps.put("nonProxyHosts", h));
            Optional.ofNullable(noProxy).ifPresent(h -> systemProps.put("http.nonProxyHosts", h));
            Optional.ofNullable(noProxy).ifPresent(h -> systemProps.put("https.nonProxyHosts", h));
        }
    }
}
