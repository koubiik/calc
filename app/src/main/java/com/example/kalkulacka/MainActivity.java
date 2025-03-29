package com.example.kalkulacka;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
        TextView resultTv, solutionTv;
        MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
        MaterialButton buttonDivide, buttonMultiply,buttonPlus, buttonMinus,buttonEquals;
MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
        MaterialButton buttonAC, buttonDot;
        //---
        MaterialButton buttonSquare, buttonPower, buttonSqrt, buttonLn;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super .onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    resultTv = findViewById(R.id.result_tv);
    solutionTv = findViewById(R.id.solution_tv);

    assignId(buttonC, R.id.button_c);
    assignId(buttonBrackOpen,R.id.button_open_bracket);
    assignId(buttonBrackClose,R.id.button_close_bracket);
    assignId(buttonDivide,R.id.button_divide);
    assignId(buttonMultiply,R.id.button_times);
    assignId(buttonPlus,R.id.button_plus);
    assignId(buttonMinus,R.id.button_minus);
    assignId(buttonEquals,R.id.button_equals);
    assignId(button0,R.id.button_0);
    assignId(button1,R.id.button_1);
    assignId(button2,R.id.button_2);
    assignId(button3,R.id.button_3);
    assignId(button4,R.id.button_4);
    assignId(button5,R.id.button_5);
    assignId(button6,R.id.button_6);
    assignId(button7,R.id.button_7);
    assignId(button8,R.id.button_8);
    assignId(button9,R.id.button_9);
    assignId(buttonAC, R.id.button_ac);
    assignId(buttonDot,R.id.button_dot);
    //---
    assignId(buttonSquare, R.id.button_sq);
    assignId(buttonPower, R.id.button_pow);
    assignId(buttonSqrt, R.id.button_sqrt);
    assignId(buttonLn, R.id.button_ln);
}
    void assignId(MaterialButton btn, int id){
    btn = findViewById(id);
    btn.setOnClickListener(this);
}
    @Override
    public void onClick(View view) {
        MaterialButton button =(MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if(buttonText.equals("Ac")){
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }
        if(buttonText.equals("=")){
            solutionTv.setText(resultTv.getText());
            return;
        }
        if(buttonText.equals("C")){
            dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
        }
        else if (buttonText.equals("x²")) {
            dataToCalculate += "^2";
        } else if (buttonText.equals("xʸ")) {
            dataToCalculate += "^";
        } else if (buttonText.equals("√")) {
            dataToCalculate += "sqrt(";
        } else if (buttonText.equals("ln")) {
            dataToCalculate += "ln(";}
        else{
            dataToCalculate = dataToCalculate+buttonText;
        }

        solutionTv.setText(dataToCalculate);

        String finalResult = getResult(dataToCalculate);

        if (!finalResult.equals("Error")){
            resultTv.setText(finalResult);
        }
    }

    String getResult(String data){
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();

            // Přidání násobení před odmocninou nebo logaritmem, pokud je před nimi číslo
            data = data.replaceAll("(\\d)(sqrt|ln)\\(", "$1*$2(");

            // Nahrazení mocnin správně pomocí Math.pow()
            data = data.replaceAll("(\\d+)\\^([\\d.]+)", "Math.pow($1,$2)");

            // Odmocnina: sqrt() místo √
            data = data.replace("sqrt(", "Math.sqrt(");

            // Logaritmus přirozeného základu
            data = data.replaceAll("ln\\(([^)]+)\\)", "Math.log($1)");

            // Kontrola dělení nulou
            if (data.contains("/0")) {
                return "Error";
            }

            String finalResult = context.evaluateString(scriptable, data, "JavaScript", 1, null).toString();

            // Odebrání .0 u celých čísel
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }

            return finalResult;
        } catch (Exception e) {
            return "Error";
        }
    }
}