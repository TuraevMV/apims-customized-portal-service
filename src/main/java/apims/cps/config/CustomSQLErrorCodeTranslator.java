package apims.cps.config;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;

public class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {
    @Override
    protected DataAccessException
    customTranslate(String task, String sql, SQLException sqlException) {
        logger.debug("SQL error =>" + sqlException.getMessage());
        return null;
    }
}
