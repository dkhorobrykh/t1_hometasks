package ru.t1.school.main_project.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.t1.school.common.model.dto.ClientStatusResponse;
import ru.t1.school.main_project.config.FeignConfig;
import ru.t1.school.main_project.config.FeignRequestInterceptor;

import java.util.UUID;

@FeignClient(
        value = "external-service-client",
        url = "${service-2.url}",
        configuration = {FeignConfig.class, FeignRequestInterceptor.class}
)
public interface ClientServiceClient {

    @GetMapping("/client/{clientId}")
    ResponseEntity<ClientStatusResponse> getClientStatus(@PathVariable("clientId") UUID clientId);
}
