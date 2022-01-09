package sample;

public class Quizclass {
    public static int score=0;
    private String question;
    private String answer;
    private String option[];
    public static int noOfQuiz = 0;

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Quizclass.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOption() {
        return option;
    }

    public void setOption(String[] option) {
        this.option = option;
    }
}
