package models;

import entities.EducationalMaterialEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EducationalMaterialModel extends BaseModel {
    public static final String EDUCATIONAL_MATERIAL_TYPE_TEXT = "text";
    public static final String EDUCATIONAL_MATERIAL_TYPE_VIDEO = "video";
    public static final String EDUCATIONAL_MATERIAL_TYPE_AUDIO = "audio";

    private static EducationalMaterialModel instance = null;

    public static synchronized EducationalMaterialModel getInstance() {
        if (instance == null)
            instance = new EducationalMaterialModel();
        return instance;
    }

    private EducationalMaterialModel() { }

    public EducationalMaterialEntity getEducationalMaterial(int id) {
        return getEducationalMaterialFromResultSet(
                dataBaseHandler.executeQuery(
                        "SELECT id, type, name, path " +
                                "FROM educational_material " +
                                "WHERE id = " + id
                )
        );
    }

    public ObservableList<EducationalMaterialEntity> getEducationalMaterials() {
        return getEducationalMaterialsFromResultSet(
                dataBaseHandler.executeQuery(
                        "SELECT id, type, name, path " +
                                "FROM educational_material"
                )
        );
    }

    public ObservableList<EducationalMaterialEntity> getTextEducationalMaterials() {
        return getEducationalMaterialsFromResultSet(
                dataBaseHandler.executeQuery(
                        "SELECT id, type, name, path " +
                                "FROM educational_material " +
                                "WHERE type = '" + EDUCATIONAL_MATERIAL_TYPE_TEXT + "'"
                )
        );
    }

    public ObservableList<EducationalMaterialEntity> getVideoEducationalMaterials() {
        return getEducationalMaterialsFromResultSet(
                dataBaseHandler.executeQuery(
                        "SELECT id, type, name, path " +
                                "FROM educational_material " +
                                "WHERE type = '" + EDUCATIONAL_MATERIAL_TYPE_VIDEO + "'"
                )
        );
    }

    public ObservableList<EducationalMaterialEntity> getAudioEducationalMaterials() {
        return getEducationalMaterialsFromResultSet(
                dataBaseHandler.executeQuery(
                        "SELECT id, type, name, path " +
                                "FROM educational_material " +
                                "WHERE type = '" + EDUCATIONAL_MATERIAL_TYPE_AUDIO + "'"
                )
        );
    }

    private EducationalMaterialEntity getEducationalMaterialFromResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                if (resultSet.next())
                    return new EducationalMaterialEntity(
                            resultSet.getInt("id"),
                            resultSet.getString("type"),
                            resultSet.getString("name"),
                            resultSet.getString("path")
                    );
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    public void addEducationalMaterial(EducationalMaterialEntity educationalMaterial) {
        dataBaseHandler.executeUpdate(
                "INSERT INTO educational_material(" +
                        "type, " +
                        "name, " +
                        "path" +
                        ") " +
                        "VALUES(" +
                        "'" + educationalMaterial.getType() + "'," +
                        "'" + educationalMaterial.getName() + "'," +
                        "'" + educationalMaterial.getPath() + "'" +
                        ")"
        );
    }

    public void updateEducationalMaterial(EducationalMaterialEntity educationalMaterial) {
        dataBaseHandler.executeUpdate(
                "UPDATE educational_material " +
                        "SET " +
                        "name = '" + educationalMaterial.getName() + "' " +
                        "WHERE id = " + educationalMaterial.getId()
        );
    }

    public void deleteEducationalMaterial(EducationalMaterialEntity educationalMaterial) {
        dataBaseHandler.executeUpdate(
                "DELETE " +
                        "FROM educational_material " +
                        "WHERE id = " + educationalMaterial.getId()
        );
    }

    private ObservableList<EducationalMaterialEntity> getEducationalMaterialsFromResultSet(ResultSet resultSet) {
        ObservableList<EducationalMaterialEntity> educationalMaterials = FXCollections.observableArrayList();
        if (resultSet != null) {
            try {
                while (resultSet.next()) educationalMaterials.add(
                        new EducationalMaterialEntity(
                                resultSet.getInt("id"),
                                resultSet.getString("type"),
                                resultSet.getString("name"),
                                resultSet.getString("path")
                        )
                );
                if (!educationalMaterials.isEmpty()) return educationalMaterials;
            } catch (SQLException ignored) {
            }
        }
        return null;
    }
}
