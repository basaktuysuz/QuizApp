package com.example.quizapp;

import android.text.Html;
import android.text.Spanned;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;


    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    // Decode HTML entities
    public Spanned getDecodedQuestionText() {
        return Html.fromHtml(questionText, Html.FROM_HTML_MODE_LEGACY);
    }

    public Spanned getDecodedOption(int index) {
        return Html.fromHtml(options.get(index), Html.FROM_HTML_MODE_LEGACY);
    }

    // Parse method for creating Question objects from JSON
    public static List<Question> parseQuestionsFromJSON(String jsonString) {
        List<Question> questions = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questionObject = jsonArray.getJSONObject(i);
                String questionText = questionObject.getString("question");
                JSONArray incorrectAnswers = questionObject.getJSONArray("incorrect_answers");
                List<String> options = new ArrayList<>();
                for (int j = 0; j < incorrectAnswers.length(); j++) {
                    options.add(incorrectAnswers.getString(j));
                }
                String correctAnswer = questionObject.getString("correct_answer");
                int correctOptionIndex = (int) (Math.random() * (options.size() + 1));
                options.add(correctOptionIndex, correctAnswer);

                Question question = new Question();
                question.setQuestionText(questionText);
                question.setOptions(options);
                question.setCorrectOptionIndex(correctOptionIndex);

                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
}
