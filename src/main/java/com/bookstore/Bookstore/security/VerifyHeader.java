package com.bookstore.Bookstore.security;

import io.spring.guides.gs_producing_web_service.ValidateRequest;
import io.spring.guides.gs_producing_web_service.ValidateResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Log4j2
@Service
@Component
public class VerifyHeader extends WebServiceGatewaySupport {
    @Autowired
    AuthenticationEndpoint authenticationEndpoint;

    public ResponseEntity<?> isTokenValid(String header) {
        String jwtToken = null;
        if (header != null && header.startsWith("Bearer"))
        {
            log.info("JWT ul incepe cu Bearer...");
            jwtToken = header.substring(7);

            // Apelez serviciul SOAP
            String xmlAnvelopeForSOAP = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                    "\t\t\t\t  xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "    <gs:validateRequest>\n" +
                    "         <gs:token-value>"+jwtToken+"</gs:token-value>\n" +
                    "    </gs:validateRequest>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            ValidateRequest validateRequest = new ValidateRequest();
            validateRequest.setTokenValue(jwtToken);
            log.info(validateRequest.getTokenValue());
            try {
                log.info("Sunt in try");
                log.info(authenticationEndpoint);
                ValidateResponse validateResponse = authenticationEndpoint.validate(validateRequest);
                log.info("Validare ok la JWT");
                return ResponseEntity.status(HttpStatus.OK).body(validateResponse.getRole() + "-" + validateResponse.getSub());

            } catch (Exception exception) {
                log.info(exception.toString());
                log.info("Nu l-am validat");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }
}
