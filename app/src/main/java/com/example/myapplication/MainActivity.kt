package com.example.basiccalculator

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var ans = "0"
    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private val logic = CalculatorLogic()
    private var expression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        val grid = findViewById<GridLayout>(R.id.grid)

        for (i in 0 until grid.childCount) {
            val btn = grid.getChildAt(i) as Button
            btn.setOnClickListener {
                handleInput(btn.text.toString())
            }
        }
    }

    // ---------- helpers ----------
    private fun isOperator(ch: Char): Boolean {
        return ch == '+' || ch == '−' || ch == '×' || ch == '÷'
    }

    private fun lastChar(): Char? {
        return if (expression.isNotEmpty()) expression.last() else null
    }

    private fun autoEvaluate() {
        if (expression.isEmpty()) return
        if (lastChar()?.let { isOperator(it) } == true) return

        val result = logic.evaluate(expression)
        if (result != "Error") {
            tvResult.text = result
        }
    }

    // ---------- main input handler ----------
    private fun handleInput(value: String) {
        when (value) {

            // Clear all
            "AC" -> {
                expression = ""
                ans = "0"
                tvExpression.text = ""
                tvResult.text = "0"
            }

            // Backspace
            "C" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    tvExpression.text = expression
                    autoEvaluate()
                }
            }

            // Use stored answer
            "ANS" -> {
                expression += ans
                tvExpression.text = expression
                autoEvaluate()
            }

            // Percentage (basic calculator behavior)
            "%" -> {
                if (expression.isEmpty()) return
                if (lastChar()?.let { isOperator(it) } == true) return

                // Convert current number to percentage
                val value = logic.evaluate(expression)
                if (value != "Error") {
                    expression = value.toDouble().div(100).toString()
                    tvExpression.text = expression
                    autoEvaluate()
                }
            }

            // Equal → evaluate and store ANS
            "=" -> {
                if (expression.isEmpty()) return
                if (lastChar()?.let { isOperator(it) } == true) return

                val result = logic.evaluate(expression)
                if (result != "Error") {
                    ans = result                // 🔥 store ANS
                    expression = result
                    tvExpression.text = result
                    tvResult.text = result
                }
            }

            // Decimal validation
            "." -> {
                var i = expression.length - 1
                while (i >= 0 && !isOperator(expression[i])) {
                    if (expression[i] == '.') return
                    i--
                }
                expression += "."
                tvExpression.text = expression
                autoEvaluate()
            }

            // Operators
            "+", "−", "×", "÷" -> {
                if (expression.isEmpty()) return
                if (lastChar()?.let { isOperator(it) } == true) return

                expression += value
                tvExpression.text = expression
            }

            // Numbers
            else -> {
                expression += value
                tvExpression.text = expression
                autoEvaluate()
            }
        }
    }

}
