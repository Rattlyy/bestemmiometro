package it.rattly.bestemmiometro.utils;

import it.rattly.bestemmiometro.Bestemmiometro;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    @Getter
    private Connection connection;

    @SneakyThrows
    public Database(String fileName) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch ( Exception e ) {
            System.out.println("[DB] SQLite non trovato.");
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("[DB] Database attivato correttamente.");

        String stmt =
                "CREATE TABLE IF NOT EXISTS users   " +
                "(id INT PRIMARY KEY NOT NULL,      " +
                " discord_id LONG NOT NULL,         " +
                " bestemmie INT NOT NULL)           ";

        connection.prepareStatement(stmt).execute();
    }

    @SneakyThrows
    public @Nullable Integer getBestemmieById(long id) {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE discord_id = ?");
        stmt.setLong(1, id);

        @Cleanup ResultSet set = stmt.executeQuery();

        Integer bestemmie = null;

        while (set.next()) {
            bestemmie = set.getInt("bestemmie");
        }

        return bestemmie;
    }

    @SneakyThrows
    public void setBestemmieOfId(long id, int bestemmie) {
        PreparedStatement stmt = connection.prepareStatement("REPLACE INTO users(id, discord_ID, bestemmie) VALUES (?, ?, ?)");

        stmt.setInt(1, 0);
        stmt.setLong(2, id);
        stmt.setInt(3, bestemmie);

        stmt.execute();
    }

    @SneakyThrows
    public void resetBestemmie(long id) {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE discord_id = ?");
        stmt.setLong(1, id);
        stmt.execute();
    }

    @SneakyThrows
    public int getTotalBestemmie() {
        @Cleanup ResultSet rs = Bestemmiometro.getDatabase().getConnection().createStatement().executeQuery("SELECT * FROM users");

        int totalBestemmie = 0;

        while (rs.next()) {
            totalBestemmie += rs.getInt("bestemmie");
        }

        return totalBestemmie;
    }
}
