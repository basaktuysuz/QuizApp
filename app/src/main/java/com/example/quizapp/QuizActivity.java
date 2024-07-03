package com.example.quizapp;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    private TextView questionTextView;
    private TextView scoreTextView;
    private TextView questionNumberTextView;
    private Button[] optionButtons;
    private View mainLayout;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = FirebaseFirestore.getInstance();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        questionTextView = findViewById(R.id.question_text);
        scoreTextView = findViewById(R.id.score_text);
        questionNumberTextView = findViewById(R.id.question_number);
        optionButtons = new Button[]{
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3),
                findViewById(R.id.option4)
        };
        mainLayout = findViewById(R.id.main_layout);

        fetchQuestions();
    }

    private void fetchQuestions() {
        Bundle bundle = getIntent().getExtras();
        String id= (String) bundle.get("id");
        String apiUrl =  "https://opentdb.com/api.php?amount=10&category="+id;
        System.out.println(apiUrl);
        NetworkUtils.fetchQuestions(apiUrl, jsonString -> {
            questions = Question.parseQuestionsFromJSON(jsonString);
            runOnUiThread(() -> {
                displayQuestion();
                updateQuestionNumber();
            });
        });
    }

    private void displayQuestion() {
        if (questions != null && !questions.isEmpty()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getDecodedQuestionText());

            List<String> options = currentQuestion.getOptions();
            for (int i = 0; i < optionButtons.length; i++) {
                if (i < options.size()) {
                    optionButtons[i].setText(currentQuestion.getDecodedOption(i));
                    optionButtons[i].setBackgroundColor(Color.WHITE); // Reset background color
                    optionButtons[i].setTextColor(Color.BLACK); // Reset text color
                    optionButtons[i].setEnabled(true); // Re-enable button
                    optionButtons[i].setVisibility(View.VISIBLE); // Make button visible
                    int finalI = i;
                    optionButtons[i].setOnClickListener(view -> onOptionSelected(finalI));
                } else {
                    optionButtons[i].setVisibility(View.GONE); // Hide unused buttons
                }
            }

            // Change background color
            changeBackgroundColor();
        }
    }

    private void updateQuestionNumber() {
        int questionNumber = currentQuestionIndex + 1;
        int totalQuestions = questions.size();
        String questionNumberText = "Question " + questionNumber + " of " + totalQuestions;
        questionNumberTextView.setText(questionNumberText);
    }

    private void changeBackgroundColor() {
        // Change background color after displaying each question
        int[] colors = {
                Color.parseColor("#90CAF9"),  // Açık Mavi
                Color.parseColor("#FFCC80"),  // Açık Portakal
                Color.parseColor("#A5D6A7"),  // Açık Yeşil
                Color.parseColor("#FFAB91"),  // Açık Kırmızı
                Color.parseColor("#B39DDB"),  // Açık Mor
                Color.parseColor("#FFD180"),  // Açık Sarı
                Color.parseColor("#81D4FA"),  // Açık Mavi
                Color.parseColor("#FFCCBC"),  // Açık Pembe
                Color.parseColor("#C5E1A5"),  // Açık Yeşil
                Color.parseColor("#FFE082")   // Açık Sarı
        };
        int colorIndex = currentQuestionIndex % colors.length;
        mainLayout.setBackgroundColor(colors[colorIndex]);
    }

    private void onOptionSelected(int selectedOptionIndex) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        int correctOptionIndex = currentQuestion.getCorrectOptionIndex();

        if (selectedOptionIndex == correctOptionIndex) {
            score++;
            correctAnswers++;
            MediaPlayer mp = MediaPlayer.create(QuizActivity.this, R.raw.correct);
            mp.start();
            optionButtons[selectedOptionIndex].setBackgroundColor(getResources().getColor(R.color.correct_answer));
        } else {
            incorrectAnswers++;
            MediaPlayer mp = MediaPlayer.create(QuizActivity.this, R.raw.wrong);
            mp.start();
            optionButtons[selectedOptionIndex].setBackgroundColor(getResources().getColor(R.color.incorrect_answer));
            optionButtons[correctOptionIndex].setBackgroundColor(getResources().getColor(R.color.correct_answer));
        }

        updateScoreText();

        for (Button button : optionButtons) {
            button.setEnabled(false); // Disable all buttons after selection
        }

        currentQuestionIndex++;
        optionButtons[3].postDelayed(() -> {
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
                updateQuestionNumber(); // Update question number
            } else {
                showScore();
            }
        }, 2000); // Delay for 2 seconds before showing the next question
    }

    private void updateScoreText() {
        String scoreText = "Score: " + score + " Correct: " + correctAnswers + " Incorrect: " + incorrectAnswers;
        scoreTextView.setText(scoreText);
    }

    private void showScore() {
        Toast.makeText(this, "Your score: " + score, Toast.LENGTH_LONG).show();
        sendScoreToFirebase(score);
    }

    private void sendScoreToFirebase(int score) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userReference = db.collection("users").document(userId);

            // Önce kullanıcı verisini alıp gerekli güncelleme yapabiliriz, eğer kullanıcı verisi gerekiyorsa.
            userReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Kullanıcı verisi varsa burada işlem yapabiliriz
                        UserHelperClass userHelperClass = document.toObject(UserHelperClass.class);
                        if (userHelperClass != null) {
                            // Örneğin, skoru güncelleyebiliriz
                            userHelperClass.setHighestScore(score);

                            // Sadece highestScore alanını güncellemek için update() metodunu kullanabiliriz
                            userReference.update("highestScore", score)
                                    .addOnSuccessListener(aVoid -> {
                                        // Başarılı bir şekilde güncellendiğini bildirme
                                        Log.d(TAG, "Kullanıcı verisi güncellendi.");
                                    })
                                    .addOnFailureListener(e -> {

                                        Log.w(TAG, "Kullanıcı verisi güncellenirken hata oluştu.", e);
                                    });
                        }
                    } else {
                        Log.d(TAG, "Belirtilen kullanıcı bulunamadı.");
                    }
                } else {
                    Log.w(TAG, "Kullanıcı verisini alma başarısız oldu.", task.getException());
                }
            });
        } else {
            Log.d(TAG, "Oturum açmış bir kullanıcı bulunamadı.");
        }
    }


}
