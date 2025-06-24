package ru.t1.school.the_best_starter.validator;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class TableExistValidator implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static boolean tableExist(Connection conn, String tableName, String tableSchema) throws SQLException {
        var stmt = conn.prepareStatement("SELECT count(*) FROM information_schema.tables WHERE table_name = ? AND table_schema = ?");
        stmt.setString(1, tableName);
        stmt.setString(2, tableSchema);

        try (var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private static void createDataSourceErrorLogDefaultTable(Connection conn) throws SQLException {
        var stmt = conn.createStatement();

        try {
            stmt.executeUpdate("""
                    create table public.data_source_error_log
                    (
                        id bigserial primary key not null,
                        stack_trace text not null,
                        message text not null,
                        signature text not null
                    );""");
        } catch (SQLException ex) {
            log.error("Не удалось создать таблицу data_source_error_log");
            throw ex;
        }
    }

    private static void createTimeLimitExceedLogDefaultTable(Connection conn) throws SQLException {
        var stmt = conn.createStatement();

        try {
            stmt.executeUpdate("""
                    create table public.time_limit_exceed_log
                    (
                        id bigserial primary key not null,
                        start_datetime timestamp not null,
                        end_datetime timestamp not null,
                        duration bigint not null,
                        signature text not null
                    );""");
        } catch (SQLException ex) {
            log.error("Не удалось создать таблицу time_limit_exceed_log");
            throw ex;
        }
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        String jdbcUrl = String.format(
                "jdbc:postgresql://%s/%s",
                System.getenv("DB_HOST"),
                System.getenv("DB_NAME")
        );

        try (HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(System.getenv("DB_USER"))
                .password(System.getenv("DB_PASSWORD"))
                .build()) {

            try (var conn = dataSource.getConnection()) {
                if (!tableExist(conn, "data_source_error_log", "public")) {
                    log.error("Таблица data_source_error_log отсутствует, создаем дефолтную");
                    createDataSourceErrorLogDefaultTable(conn);
                } else {
                    log.info("Таблица data_source_error_log провалидирована");
                }

                if (!tableExist(conn, "time_limit_exceed_log", "public")) {
                    log.error("Таблица time_limit_exceed_log отсутствует, создаем дефолтную");
                    createTimeLimitExceedLogDefaultTable(conn);
                } else {
                    log.info("Таблица time_limit_exceed_log провалидирована");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
