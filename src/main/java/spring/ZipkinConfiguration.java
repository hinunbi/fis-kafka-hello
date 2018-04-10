package spring;

import org.apache.camel.CamelContext;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.zipkin.ZipkinTracer;
import org.apache.camel.zipkin.starter.ZipkinConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

@Configuration
@EnableConfigurationProperties(ZipkinConfigurationProperties.class)
@ConditionalOnProperty(value = "camel.zipkin.enabled", matchIfMissing = true)
public class ZipkinConfiguration {

  @Bean
  @ConditionalOnMissingBean(ZipkinTracer.class)
  ZipkinTracer zipkin(CamelContext camelContext,
                      ZipkinConfigurationProperties config) {

    ZipkinTracer zipkin = new ZipkinTracer();
    zipkin.setEndpoint(config.getEndpoint());
    zipkin.setHostName(config.getHostName());
    zipkin.setPort(config.getPort());
    zipkin.setRate(config.getRate());
    if (ObjectHelper.isNotEmpty(config.getServiceName())) {
      zipkin.setServiceName(config.getServiceName());
    }
    if (config.getExcludePatterns() != null) {
      zipkin.setExcludePatterns(config.getExcludePatterns());
    }
    if (config.getClientServiceMappings() != null) {
      zipkin.setClientServiceMappings(config.getClientServiceMappings());
    }
    if (config.getServerServiceMappings() != null) {
      zipkin.setServerServiceMappings(config.getServerServiceMappings());
    }
    zipkin.setIncludeMessageBody(config.isIncludeMessageBody());
    zipkin.setIncludeMessageBodyStreams(config.isIncludeMessageBodyStreams());

    OkHttpSender sender = OkHttpSender.create(config.getEndpoint());
    zipkin.setSpanReporter(AsyncReporter.create(sender));

    zipkin.init(camelContext);

    return zipkin;
  }
}
