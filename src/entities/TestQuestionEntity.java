package entities;

public class TestQuestionEntity {
    private final int id;
    private final TestEntity test;
    private String question;

    public TestQuestionEntity(int id, TestEntity test, String question) {
        this.id = id;
        this.test = test;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public TestEntity getTest() {
        return test;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String toString() {
        return question;
    }
}
