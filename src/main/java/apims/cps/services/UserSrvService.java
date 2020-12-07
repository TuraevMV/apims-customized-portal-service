package apims.cps.services;

import apims.cps.model.RequestBodyModel;
import apims.cps.model.UserServiceListModel;
import apims.cps.repository.nsd.UserSrvRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserSrvService {
    private final UserSrvRepository userSrvRepository;

    public UserSrvService(UserSrvRepository userSrvRepository) {
        this.userSrvRepository = userSrvRepository;
    }

    public ResponseEntity<UserServiceListModel[]> getAllUserSrvList(String serverType, String jwtToken, RequestBodyModel requestBody){
        return userSrvRepository.getAllUserSrvList(serverType, jwtToken, requestBody);
    };

}
