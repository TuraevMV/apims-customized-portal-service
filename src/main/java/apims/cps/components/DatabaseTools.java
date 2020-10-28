package apims.cps.components;

import apims.cps.types.UDBQMParameters;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Slf4j
@Component
public class DatabaseTools {
    @Value("${dbConnectionArray}")
    private String dbConnectionArray;

    //Universal database query manager
    public String UDBQM (String dbName, String sqlQuery, List<UDBQMParameters> parameterItems ) {
        Connection conn = null;
        CallableStatement cstmt = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet     = null;
        String resultJSON = "";

        try {
            conn = getConnection(dbName);
            pstmt = conn.prepareStatement(sqlQuery);

            log.debug("Количество параметров=>" + parameterItems.size());
            for (int i = 0; i < parameterItems.size(); i++) {

                switch (parameterItems.get(i).getItemType()) {
                    case "STRING":
                        log.debug("Param " + i + "(STRING): " + parameterItems.get(i).getItemString());
                        pstmt.setString(i + 1, parameterItems.get(i).getItemString());
                        break;
                    case "INTEGER":
                        log.debug("Param " + i + "(INTEGER): " + parameterItems.get(i).getItemInteger());
                        pstmt.setInt(i + 1, parameterItems.get(i).getItemInteger());
                        break;
                    case "BIGDECIMAL":
                        log.debug("Param " + i + " (BIGDECIMAL): " + parameterItems.get(i).getItemBigDecimal());
                        pstmt.setBigDecimal(i + 1, parameterItems.get(i).getItemBigDecimal());
                        break;
                }
            }

            resultSet = pstmt.executeQuery();
            //log.debug(String.valueOf(resultSet.getMetaData().getColumnCount()));
            int cntRow=0;
            while (resultSet.next()) {
                ++cntRow;
                resultJSON = resultSet.getString(1);
//                resultJSON.add(new JSONObject(rs.getString(1)));
//                executeStatus = "OK";
            }
            log.debug("Количество записей в результате запроса: " + cntRow);


        } catch (JSONException | SQLException e) {
            log.error(e.getMessage());
        } finally {
            this.closeConnection(conn, cstmt, pstmt);
        }
        return resultJSON;
    }

    private Connection getConnection(String dbConnectionName) throws JSONException {
        Connection conn = null;
        JSONArray jsnobject = new JSONArray(dbConnectionArray);
        for (int i = 0; i < jsnobject.length(); i++) {
            JSONObject explrObject = jsnobject.getJSONObject(i);

            if (explrObject.get("connName").toString().equals(dbConnectionName)) {
                try {
                    Class.forName(explrObject.get("connDriver").toString());
                    conn = DriverManager.getConnection(
                            explrObject.get("connURL").toString(),
                            explrObject.get("connUser").toString(),
                            explrObject.get("connectionPassword").toString());

                    if (conn != null) {
                        DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                        log.debug("+++++++++++ Connection open ++++++++++++++++++");
                        log.debug("Connection name: " + dbConnectionName);
                        log.debug("Driver name: " + dm.getDriverName());
                        log.debug("Driver version: " + dm.getDriverVersion());
                        log.debug("Product name: " + dm.getDatabaseProductName());
                        log.debug("Product version: " + dm.getDatabaseProductVersion());
                        log.debug("++++++++++++++++++++++++++++++++++++++++++++++");
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return conn;
    }

    private void closeConnection(Connection conn, CallableStatement cstmt, PreparedStatement pstmt) {
        log.debug("+++++++++++++ Close connection +++++++++++++++++");
        if (cstmt != null) try {
            cstmt.close();
            log.debug("cstmt close");
        } catch (Exception e) {}
        if (conn != null) try {
            conn.close();
            log.debug("conn close");
        } catch (Exception e) {}
        if (pstmt != null) try {
            pstmt.close();
            log.debug("pstmt close");
        } catch (Exception e) {}
        log.debug("++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
