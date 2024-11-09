package com.sp.mycalculator;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTextView, solutionTextView;
    MaterialButton buttonC, buttonBracketOpen, buttonBracketClose;
    MaterialButton buttonDivide, buttonMultiply, buttonSubtract, buttonAdd, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing TextViews
        resultTextView = findViewById(R.id.result_textview);
        solutionTextView = findViewById(R.id.solution_textview);

        // Initializing buttons and setting listeners
        buttonBracketOpen = findViewById(R.id.button_open_braket);
        buttonBracketClose = findViewById(R.id.button_close_bracket);
        buttonC = findViewById(R.id.button_c);
        buttonDivide = findViewById(R.id.button_divide);
        buttonMultiply = findViewById(R.id.button_multiply);
        buttonSubtract = findViewById(R.id.button_subtract);
        buttonAdd = findViewById(R.id.button_add);
        buttonEquals = findViewById(R.id.button_equal);
        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        buttonAC = findViewById(R.id.button_ac);
        buttonDot = findViewById(R.id.button_dot);

        // Setting click listeners for buttons
        setButtonOnClickListeners();
    }

    private void setButtonOnClickListeners() {
        buttonC.setOnClickListener(this);
        buttonBracketOpen.setOnClickListener(this);
        buttonBracketClose.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonAC.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTextView.getText().toString();

        // Handling clear all
        if (buttonText.equals("AC")) {
            solutionTextView.setText("");
            resultTextView.setText("0");
            return;
        }
        if (buttonText.equals("C")) {
            if (!dataToCalculate.isEmpty()) {
                // Remove the last character from dataToCalculate
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                solutionTextView.setText(dataToCalculate);

                // Optionally update the result if input isn't empty
                if (!dataToCalculate.isEmpty()) {
                    String interimResult = getResult(dataToCalculate);
                    if (!interimResult.equals("Err")) {
                        resultTextView.setText(interimResult);
                    } else {
                        resultTextView.setText("");
                    }
                } else {
                    resultTextView.setText("0"); // Default result view when input is empty
                }
            }
            return;
        }

        // Handling equals
        if (buttonText.equals("=")) {
            String finalResult = getResult(dataToCalculate);
            if (!finalResult.equals("Err")) {
                solutionTextView.setText(finalResult);
            }
            return;
        }

        // Appending input text
        dataToCalculate += buttonText;
        solutionTextView.setText(dataToCalculate);

        // Updating resultTextView with interim result
        String interimResult = getResult(dataToCalculate);
        if (!interimResult.equals("Err")) {
            resultTextView.setText(interimResult);
        }
    }

    // Method to evaluate mathematical expressions using Rhino
    static String getResult(String data) {
        Context rhino = null;
        try {
            rhino = Context.enter();
            rhino.setOptimizationLevel(-1);
            Scriptable scriptable = rhino.initStandardObjects();
            String finalResult = rhino.evaluateString(scriptable, data, "JavaScript", 1, null).toString();

            // Format result to remove trailing ".0"
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.substring(0, finalResult.length() - 2);
            }
            return finalResult;
        } catch (Exception e) {
            Log.e("MainActivity", "Error evaluating expression: " + e.getMessage());
            return "Err";
        } finally {
            if (rhino != null) {
                Context.exit();
            }
        }
    }
}
