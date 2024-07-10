package com.example.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class ResultsPage extends AppCompatActivity {



        private int correctAnswer = 0;
        private int inCorrectAnswer = 0;
        private PieChart pieChart;
        private TextView incorrect;
        private TextView skip;
        private Button homeResult;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            try {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_results_page);

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    correctAnswer = bundle.getInt("CorrectAnswer");
                    inCorrectAnswer = bundle.getInt("InCorrectAnswer");
                }

                TextView correct = findViewById(R.id.correct);
                homeResult = findViewById(R.id.homeresult);
                incorrect = findViewById(R.id.incoorect);
                skip = findViewById(R.id.skipped);
                pieChart = findViewById(R.id.piechart);

                correct.setText(Integer.toString(correctAnswer));
                incorrect.setText(Integer.toString(inCorrectAnswer));

                int skipNo = correctAnswer + inCorrectAnswer;
                skip.setText(Integer.toString(10 - skipNo));

               homeResult.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i = new Intent(ResultsPage.this, MainActivity.class);
                       startActivity(i);
                   }
               });

                pieChart.setInnerPadding(50F);
                pieChart.addPieSlice(new PieModel("Correct", Float.parseFloat(correct.getText().toString()), Color.GREEN));
                pieChart.addPieSlice(new PieModel("Incorrect", Float.parseFloat(incorrect.getText().toString()), Color.RED));
                pieChart.addPieSlice(new PieModel("Skip", Float.parseFloat(skip.getText().toString()), Color.DKGRAY));
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


