package com.example.basiccalculator

import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorLogic {

    fun evaluate(expression: String): String {
        return try {
            val exp = expression
                .replace("÷", "/")
                .replace("×", "*")
                .replace("−", "-")

            val result = ExpressionBuilder(exp).build().evaluate()

            if (result % 1 == 0.0)
                result.toLong().toString()
            else
                result.toString()

        } catch (e: Exception) {
            "Error"
        }
    }
}
