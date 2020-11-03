package apims.cps.controllers;

import apims.cps.components.DatabaseTools;
import apims.cps.types.UDBQMParameters;
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
        String resultValue;

        //Формируем параметры для UDBQM
        BigDecimal userID = BigDecimal.valueOf(37538377);
        List<UDBQMParameters> listParameters = new ArrayList();
        UDBQMParameters pParamItem = new UDBQMParameters("BIGDECIMAL",null,null,userID);
        listParameters.add(pParamItem);

        resultValue = databaseTools.UDBQM(serverType, userServiceListQuery, listParameters );


        return new ResponseEntity<>(resultValue, responseHeaders, responseStatus);
    }
}
