package models.database;

import org.sqlite.JDBC;

import java.io.File;
import java.sql.*;

public class SqliteDataBaseHandler implements DataBaseDao {
    private static DataBaseDao instance = null;
    private Connection connection;

    public static DataBaseDao getInstance() {
        if (instance == null)
            instance = new SqliteDataBaseHandler();
        return instance;
    }

    private SqliteDataBaseHandler() {
        String CON_STR = "jdbc:sqlite::resource:data";
        try {
            DriverManager.registerDriver(new JDBC());
            this.connection = DriverManager.getConnection(CON_STR);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public ResultSet executeQuery(String sql) {
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException ignored) {
        }
        return null;
    }

    @Override
    public void executeUpdate(String sql) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void migrate() {
        createTableUser();
        createTableEducationalMaterial();
        createTableTest();
        createTableTestQuestion();
        createTableTestAnswer();
    }

    private void createTableUser() {
        executeUpdate(
                "create table user(" +
                        "id integer not null constraint user_pk primary key autoincrement, " +
                        "username text not null, " +
                        "password text not null " +
                        ");" +
                        "insert into user (username, password) " +
                        "VALUES" +
                        "('obuchalkin.software@yandex.ru', 'Qazxdr12345');"
        );
    }

    private void createTableEducationalMaterial() {
        executeUpdate(
                "create table educational_material(" +
                        "id integer not null constraint educational_material_pk primary key autoincrement, " +
                        "type text not null, " +
                        "name text not null, " +
                        "path text not null " +
                        ");"
        );
    }

    private void createTableTest() {
        executeUpdate(
                "create table test(" +
                        "id integer not null constraint test_pk primary key autoincrement, " +
                        "name text not null" +
                        ");"
        );
    }

    private void createTableTestQuestion() {
        executeUpdate(
                "create table test_question(" +
                        "id integer not null constraint test_question_pk primary key autoincrement, " +
                        "id_test integer not null, " +
                        "question text not null" +
                        ");"
        );
    }

    private void createTableTestAnswer() {
        executeUpdate(
                "create table test_answer(" +
                        "id integer not null constraint test_answer_pk primary key autoincrement, " +
                        "id_question integer not null, " +
                        "answer text not null, " +
                        "isRight text default 'n' not null" +
                        ");"
        );
    }
}