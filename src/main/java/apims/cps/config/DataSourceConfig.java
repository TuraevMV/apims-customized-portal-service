package apims.cps.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "nsd")
    @ConfigurationProperties(prefix = "app.datasource.nsd")
    public DataSource nsdDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateNSD")
    public JdbcTemplate jdbcTemplate1(@Qualifier("nsd") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "nsd.dev")
    @ConfigurationProperties(prefix = "app.datasource.nsd.dev")
    public DataSource nsdDataSourceDev() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateNSD_DEV")
    public JdbcTemplate jdbcTemplate2(@Qualifier("nsd.dev") DataSource ds) {
        return new JdbcTemplate(ds);
    }

}
