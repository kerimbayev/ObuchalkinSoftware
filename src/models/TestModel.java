package models;

import entities.TestAnswerEntity;
import entities.TestEntity;
import entities.TestQuestionEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestModel extends BaseModel {
        private static TestModel instance = null;

        public static synchronized TestModel getInstance() {
                if (instance == null)
                        instance = new TestModel();
                return instance;
        }

        private TestModel() {
        }

        public TestEntity getTest(int id) {
                return getTestFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT id, name " +
                                        "FROM test " +
                                        "WHERE id = " + id
                        )
                );
        }

        public TestQuestionEntity getTestQuestion(int id) {
                return getTestQuestionFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT id, id_test, question " +
                                        "FROM test_question " +
                                        "WHERE id = " + id
                        )
                );
        }

        public TestAnswerEntity getTestAnswer(int id) {
                return getTestAnswerFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT id, id_question, answer, isRight " +
                                        "FROM test_answer " +
                                        "WHERE id = " + id
                        )
                );

        }

        public ObservableList<TestEntity> getTests() {
                return getTestsFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT id, name " +
                                        "FROM test"
                        )
                );
        }

        public ObservableList<TestQuestionEntity> getTestQuestions() {
                return getTestQuestionsFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT id, id_test, question " +
                                        "FROM test_question"
                        )
                );
        }

        public ObservableList<TestQuestionEntity> getTestQuestionsByTest(TestEntity test) {
                return getTestQuestionsFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT test_question.id, id_test, question " +
                                        "FROM test_question " +
                                        "INNER JOIN test ON(test.id = test_question.id_test) " +
                                        "WHERE test.id = " + test.getId()
                        )
                );
        }

        public ObservableList<TestAnswerEntity> getTestAnswers() {
                return getTestAnswersFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT id, id_question, answer, isRight " +
                                        "FROM test_answer"
                        )
                );
        }

        public ObservableList<TestAnswerEntity> getTestAnswersByTestQuestion(TestQuestionEntity testQuestion) {
                return getTestAnswersFromResultSet(
                        dataBaseHandler.executeQuery(
                                "SELECT test_answer.id, id_question, answer, isRight " +
                                        "FROM test_answer " +
                                        "INNER JOIN test_question ON(test_question.id = test_answer.id_question) " +
                                        "WHERE test_question.id = " + testQuestion.getId()
                        )
                );
        }

        public void addTest(TestEntity test) {
                dataBaseHandler.executeUpdate(
                        "INSERT INTO test(" +
                                "name" +
                                ") " +
                                "VALUES(" +
                                "'" + test.getName() + "'" +
                                ")"
                );
        }

        public void addTestQuestion(TestQuestionEntity testQuestion) {
                dataBaseHandler.executeUpdate(
                        "INSERT INTO test_question(" +
                                "id_test, " +
                                "question" +
                                ") " +
                                "VALUES(" +
                                testQuestion.getTest().getId() + ", " +
                                "'" + testQuestion.getQuestion() + "'" +
                                ")"
                );
        }

        public void addTestAnswer(TestAnswerEntity testAnswer) {
                dataBaseHandler.executeUpdate(
                        "INSERT INTO test_answer(" +
                                "id_question, " +
                                "answer, " +
                                "isRight" +
                                ") " +
                                "VALUES(" +
                                testAnswer.getTestQuestion().getId() + ", " +
                                "'" + testAnswer.getAnswer() + "', " +
                                "'" + testAnswer.getIsRight() + "'" +
                                ")"
                );
        }

        public void updateTest(TestEntity test) {
                dataBaseHandler.executeUpdate(
                        "UPDATE test " +
                                "SET " +
                                "name = '" + test.getName() + "' " +
                                "WHERE id = " + test.getId()
                );
        }

        public void updateTestQuestion(TestQuestionEntity testQuestion) {
                dataBaseHandler.executeUpdate(
                        "UPDATE test_question " +
                                "SET " +
                                "id_test = " + testQuestion.getTest().getId() + ", " +
                                "question = '" + testQuestion.getQuestion() + "' " +
                                "WHERE id = " + testQuestion.getId()
                );
        }

        public void updateTestAnswer(TestAnswerEntity testAnswer) {
                dataBaseHandler.executeUpdate(
                        "UPDATE test_answer " +
                                "SET " +
                                "id_question = " + testAnswer.getTestQuestion().getId() + ", " +
                                "answer = '" + testAnswer.getAnswer() + "', " +
                                "isRight = '" + testAnswer.getIsRight() + "' " +
                                "WHERE id = " + testAnswer.getId()
                );
        }

        public void deleteTest(TestEntity test) {
                dataBaseHandler.executeUpdate(
                        "DELETE " +
                                "FROM test " +
                                "WHERE id = " + test.getId()
                );
        }

        public void deleteTestQuestion(TestQuestionEntity testQuestion) {
                dataBaseHandler.executeUpdate(
                        "DELETE " +
                                "FROM test_question " +
                                "WHERE id = " + testQuestion.getId()
                );
        }

        public void deleteTestAnswer(TestAnswerEntity testAnswer) {
                dataBaseHandler.executeUpdate(
                        "DELETE " +
                                "FROM test_answer " +
                                "WHERE id = " + testAnswer.getId()
                );
        }

        private TestEntity getTestFromResultSet(ResultSet resultSet) {
                if (resultSet != null) {
                        try {
                                if (resultSet.next())
                                        return new TestEntity(
                                                resultSet.getInt("id"),
                                                resultSet.getString("name")
                                        );
                        } catch (SQLException ignored) {
                        }
                }
                return null;
        }

        private ObservableList<TestEntity> getTestsFromResultSet(ResultSet resultSet) {
                ObservableList<TestEntity> tests = FXCollections.observableArrayList();
                if (resultSet != null) {
                        try {
                                while (resultSet.next()) tests.add(
                                        new TestEntity(
                                                resultSet.getInt("id"),
                                                resultSet.getString("name")
                                        )
                                );
                        } catch (SQLException ignored) {
                        }
                }
                return tests;
        }

        private TestQuestionEntity getTestQuestionFromResultSet(ResultSet resultSet) {
                if (resultSet != null) {
                        try {
                                if (resultSet.next())
                                        return new TestQuestionEntity(
                                                resultSet.getInt("id"),
                                                getTest(resultSet.getInt("id_test")),
                                                resultSet.getString("question")
                                        );
                        } catch (SQLException ignored) {
                        }
                }
                return null;
        }

        private ObservableList<TestQuestionEntity> getTestQuestionsFromResultSet(ResultSet resultSet) {
                ObservableList<TestQuestionEntity> testQuestions = FXCollections.observableArrayList();
                if (resultSet != null) {
                        try {
                                while (resultSet.next()) testQuestions.add(
                                        new TestQuestionEntity(
                                                resultSet.getInt("id"),
                                                getTest(resultSet.getInt("id_test")),
                                                resultSet.getString("question")
                                        )
                                );
                        } catch (SQLException ignored) {
                        }
                }
                return testQuestions;
        }

        private TestAnswerEntity getTestAnswerFromResultSet(ResultSet resultSet) {
                if (resultSet != null) {
                        try {
                                if (resultSet.next())
                                        return new TestAnswerEntity(
                                                resultSet.getInt("id"),
                                                getTestQuestion(resultSet.getInt("id_question")),
                                                resultSet.getString("answer"),
                                                resultSet.getString("isRight")
                                        );
                        } catch (SQLException ignored) {
                        }
                }
                return null;
        }

        private ObservableList<TestAnswerEntity> getTestAnswersFromResultSet(ResultSet resultSet) {
                ObservableList<TestAnswerEntity> testAnswers = FXCollections.observableArrayList();
                if (resultSet != null) {
                        try {
                                while (resultSet.next()) testAnswers.add(
                                        new TestAnswerEntity(
                                                resultSet.getInt("id"),
                                                getTestQuestion(resultSet.getInt("id_question")),
                                                resultSet.getString("answer"),
                                                resultSet.getString("isRight")
                                        )
                                );
                        } catch (SQLException ignored) {
                        }
                }
                return testAnswers;
        }
}
