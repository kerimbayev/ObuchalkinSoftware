package entities;

public class TestAnswerEntity {
    private final int id;
    private final TestQuestionEntity testQuestion;
    private String answer;
    private String isRight;

    public TestAnswerEntity(int id, TestQuestionEntity testQuestion, String answer, String isRight) {
        this.id = id;
        this.testQuestion = testQuestion;
        this.answer = answer;
        this.isRight = isRight;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRight() {
        return isRight.equals("y");
    }

    public void setRight(boolean right) {
        isRight = right ? "y" : "n";
    }

    public String getIsRight() {
        return isRight;
    }

    public TestQuestionEntity getTestQuestion() {
        return testQuestion;
    }

    public String toString() {
        return answer;
    }
}
