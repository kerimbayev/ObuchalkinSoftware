package models;

import entities.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel extends BaseModel {
    private static UserModel instance = null;

    public static synchronized UserModel getInstance() {
        if (instance == null)
            instance = new UserModel();
        return instance;
    }

    private UserModel() { }

    public UserEntity getUser() {
        return getUserFromResultSet(
                dataBaseHandler.executeQuery(
                        "SELECT id, username, password " +
                                "FROM user"
                )
        );
    }

    private UserEntity getUserFromResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                if (resultSet.next())
                    return new UserEntity(resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
            } catch (SQLException ignored) {
            }
        }
        return null;
    }
}
