package apims.cps.controllers;

import apims.cps.components.DatabaseTools;
import apims.cps.types.UDBQMParameters;
import io.jsonwebtoken.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
@CrossOrigin("*")
@Api(value="Custom portal service list", tags = "Service List")
public class UserServiceList {
    @Value("${userServiceListQuery}")
    private String userServiceListQuery;

    private final DatabaseTools databaseTools;

    public UserServiceList(DatabaseTools databaseTools) {
        this.databaseTools = databaseTools;
    }

    @RequestMapping(value = "/{serverType}/getUserServiceList", method = RequestMethod.POST)
    public ResponseEntity<String> getUserServiceList(
            @PathVariable("serverType") String serverType,
            @RequestBody String requestBody,
            @RequestHeader Map<String, String> headers
    ){
        HttpHeaders responseHeaders     = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus responseStatus       = HttpStatus.OK;
        String resultValue = "[]";

        log.debug("=========== RequestHeader check ===========");
            headers.forEach((key, value) -> { log.debug(String.format("Header '%s' = %s", key, value));});
        log.debug("============================================");

        //Откусим подпись. нам не интересно
        String jwtToken = headers.get("authorization").replaceAll("Bearer ","");
        int i = jwtToken.lastIndexOf('.');
        String withoutSignature = jwtToken.substring(0, i+1);

        //Отсечем всякие ошибки просрочки и т.д.
        if (validateToken(withoutSignature)) {
            Jwt<Header,Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
            String userUUID = untrusted.getBody().get("userUUID").toString().replace("employee$","");
            log.debug("userUUID =>" + userUUID);
            BigDecimal userID = BigDecimal.valueOf(Long.parseLong(userUUID));
            List<UDBQMParameters> listParameters = new ArrayList();
            UDBQMParameters pParamItem = new UDBQMParameters("BIGDECIMAL",null,null,userID);
            listParameters.add(pParamItem);
            resultValue = databaseTools.UDBQM(serverType, userServiceListQuery, listParameters );
        }

        return new ResponseEntity<>(resultValue, responseHeaders, responseStatus);
    }



    public boolean validateToken(String token) {
        try {
            Jwts.parser().parseClaimsJwt(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.debug("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.debug("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.debug("Malformed jwt");
        } catch (SignatureException sEx) {
            log.debug("Invalid signature");
        } catch (Exception e) {
            log.debug("invalid token");
        }
        return false;
    }

}
