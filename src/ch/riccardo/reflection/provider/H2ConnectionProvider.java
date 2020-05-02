package ch.riccardo.reflection.provider;

import ch.riccardo.reflection.annotations.Provides;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionProvider {

    @Provides
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:~/IdeaProjects/simple-ORM/src/db-files/db-orm",
                "sa",
                "");
    }

}
