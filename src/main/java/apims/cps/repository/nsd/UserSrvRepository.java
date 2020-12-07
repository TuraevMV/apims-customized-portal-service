package apims.cps.repository.nsd;

import apims.cps.model.RequestBodyModel;
import apims.cps.model.UserServiceListModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Transactional
@Repository
public class UserSrvRepository {
    @Value("${userServiceListQuery}")
    private String userServiceListQuery;

    @Value("${allUserServiceListQuery}")
    private String allUserServiceListQuery;

    private final JdbcTemplate jdbcNSD;
    private final JdbcTemplate jdbcNSD_DEV;

    public UserSrvRepository(@Qualifier("jdbcTemplateNSD") JdbcTemplate jdbcTemplate1,
                             @Qualifier("jdbcTemplateNSD-DEV") JdbcTemplate jdbcTemplate2) {
        this.jdbcNSD = jdbcTemplate1;
        this.jdbcNSD_DEV = jdbcTemplate2;
    }

    public ResponseEntity<UserServiceListModel[]> getAllUserSrvList(String serverType, String jwtToken, RequestBodyModel requestBody) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity responseEntity = new ResponseEntity<UserServiceListModel[]>(null, responseHeaders, HttpStatus.NO_CONTENT);
        String result = null;
        String queryString;

        log.debug("============== Cookies check ===============");
        log.debug("access_token =>" + jwtToken);
        log.debug("============================================");

        try {
            //Откусим подпись. нам не интересно
            jwtToken = jwtToken.replaceAll("Bearer ","");
            int i = jwtToken.lastIndexOf('.');
            String withoutSignature = jwtToken.substring(0, i+1);

            if (validateToken(withoutSignature)) {

                //Получим идентификатор клиента
                Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
                String userUUID = untrusted.getBody().get("userUUID").toString().replace("employee$","");
                log.debug("userUUID =>" + userUUID);
                BigDecimal userID = BigDecimal.valueOf(Long.parseLong(userUUID));

                //Определимся с видом запроса ВСЕ сервисы или ПОПУЛЯРНЫЕ
                log.debug("Количество запросов для возврата =>" + requestBody.getMaxResult());
                if (requestBody.getMaxResult() >12) {
                    //получение сервисов ПОПУЛЯРНЫЕ
                    queryString = userServiceListQuery;
                } else {
                    //получение сервисов ВСЕ
                    queryString = allUserServiceListQuery;
                }

                switch (serverType)
                {
                    case "dev" :
                        result = this.jdbcNSD_DEV.queryForObject(queryString,new Object[]{userID}, String.class);
                        break;
                    case "prod":
                        result = this.jdbcNSD.queryForObject(queryString,new Object[]{userID}, String.class);
                        break;
                }
                    log.debug("Result =>" + result);
                    responseEntity = new ResponseEntity<UserServiceListModel[]>(new ObjectMapper().readValue(result, UserServiceListModel[].class ), responseHeaders, HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<UserServiceListModel[]>(null, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            log.debug(e.toString());
        }
        return responseEntity;
    }

    private boolean validateToken(String token) {
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