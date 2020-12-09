package apims.cps.controllers;

import apims.cps.model.RequestBodyModel;
import apims.cps.model.UserServiceListModel;
import apims.cps.services.UserSrvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value="Custom portal service list", tags = "Service List")
public class UserSrvList {
    private final UserSrvService userSrvService;

    public UserSrvList(UserSrvService userSrvService) {
        this.userSrvService = userSrvService;
    }
    @ApiOperation(value = "Список сервисов пользователя", notes = "Возвращает список сервисов для пользователя", tags = "Service List")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Что-то пошло не так. Смотрим в логи."),
            @ApiResponse(responseCode = "204", description = "Нет доступных сервисов для клиента"),
            @ApiResponse(responseCode = "200", description = "Успешное выполнение",content = @Content(schema = @Schema(implementation = UserServiceListModel[].class))),
    })
    @RequestMapping(value = "/{serverType}/getUserServiceList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<UserServiceListModel[]> getUserServiceList(
            @Parameter(description="Сервер NSD с которого необходимо получить информацию (dev/prod).", required= true, content = @Content(schema = @Schema(implementation = String.class)), example = "dev")
            @PathVariable("serverType") String serverType,
            @CookieValue("access_token") String access_token,
            @RequestBody() String requestBody
            ){
        RequestBodyModel rBody = null;

        try {
            rBody = new ObjectMapper().readValue(requestBody, RequestBodyModel.class );
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }

        return userSrvService.getAllUserSrvList(serverType, access_token, rBody);
    }
}
