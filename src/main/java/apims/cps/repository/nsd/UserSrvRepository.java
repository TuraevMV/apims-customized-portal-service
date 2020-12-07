package apims.cps.repository.nsd;

import apims.cps.model.RequestBodyModel;
import apims.cps.model.UserServiceListModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserSrvRepository {
    private final JdbcTemplate nsdTemplate;

    public UserSrvRepository(JdbcTemplate nsdTemplate) {
        this.nsdTemplate = nsdTemplate;
    }

    public ResponseEntity<UserServiceListModel[]> getAllUserSrvList(String serverType, String jwtToken, RequestBodyModel requestBody){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        ResponseEntity responseEntity = new ResponseEntity<UserServiceListModel[]>(null, responseHeaders, responseStatus);




        return responseEntity;
    }
}


/*
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jackrutorial.model.User;

@Transactional
@Repository
public class UserDaoImpl {

 @Autowired
 @Qualifier("jdbcTemplate1")
 private JdbcTemplate jdbcTemplate1;

 @Autowired
 @Qualifier("jdbcTemplate2")
 private JdbcTemplate jdbcTemplate2;

 public List getAllUser() {
  String sql1 = "select username,email from user1";
  //get users list from db1
  List list1 = jdbcTemplate1.query(sql1, new UserRowMapper());

  String sql2 = "select username,email from user2";
  //get users list from db2
  List list2 = jdbcTemplate2.query(sql2, new UserRowMapper());

  List listAll = Stream.concat(list1.stream(), list2.stream())
       .collect(Collectors.toList());
  return listAll;
 }

 class UserRowMapper implements RowMapper{

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
   User user = new User();
   user.setUsername(rs.getString("username"));
   user.setEmail(rs.getString("email"));

   return user;
  }

 }

}
 */