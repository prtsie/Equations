package com.example.equations

enum class Op {
    Add,
    Subtract,
    Divide,
    Multiple;

    override fun toString(): String {
        return when(this) {
            Add -> "+"
            Subtract -> "-"
            Divide -> "/"
            Multiple -> "*"
        }
    }
}