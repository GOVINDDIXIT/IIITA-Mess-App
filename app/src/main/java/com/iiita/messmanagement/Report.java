package com.iiita.messmanagement;

public class Report {
    private String question, answer;
    private boolean checked;

    public Report() {
    }

    public Report(String question, String answer, boolean checked) {
        this.answer = answer;
        this.question = question;
        this.checked = checked;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
