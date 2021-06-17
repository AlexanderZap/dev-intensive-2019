package ru.skillbranch.devintensive.models

import java.util.*

class Bender (var status: Status = Status.NORMAL, var question: Question = Question.NAME ) {

    fun askQuestion() : String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer (answer: String) : Pair<String, Triple<Int, Int, Int>> = when (question) {
            Question.IDLE -> question.question to status.color
            else -> "${checkAnswer(answer)}\n${question.question}" to status.color
        }

    private fun checkAnswer(answer: String): String {
        val isValid = question.validate(answer)
        return if (isValid) {
            if (question.answers.contains(answer.lowercase(Locale.getDefault()))) {
                question = question.nextQuestion()
                "Отлично - ты справился"
            } else {
                when (status) {
                    Status.CRITICAL -> {
                        status = Status.NORMAL
                        question = Question.NAME
                        "Это неправильный ответ. Давай все по новой"
                    }
                    else -> {
                        status = status.nextStatus()
                        "Это неправильный ответ"
                    }
                }
            }
        } else {
            when (question) {
                Question.NAME -> "Имя должно начинаться с заглавной буквы"
                Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
                Question.MATERIAL -> "Материал не должен содержать цифр"
                Question.BDAY -> "Год моего рождения должен содержать только цифры"
                Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
                else -> "На этом все, вопросов больше нет"
            }
        }
    }

    enum class Status (val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255,255,255)),
        WARNING(Triple(255,120,0)),
        DANGER(Triple(255,60,60)),
        CRITICAL(Triple(255,255,0));

        fun nextStatus() :Status {
            return if (this.ordinal < values().lastIndex){
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?",  listOf("бендер","bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validate(text: String): Boolean = text.trim()[0].isUpperCase()
        },
        PROFESSION ("Назови мою профессию?",  listOf("сгибальщик","bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validate(text: String): Boolean = text.trim()[0].isLowerCase()
        },
        MATERIAL ("Из чего я сделан?",  listOf("металл","дерево", "iron", "wood", "metal")) {
            override fun nextQuestion(): Question = BDAY
            override fun validate(text: String): Boolean = text.trim().contains(Regex("\\d")).not()
        },
        BDAY ("Когда меня создали?",  listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validate(text: String): Boolean = text.trim().contains(Regex("^[0-9]*$"))
        },
        SERIAL ("Мой серийный номер?",  listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validate(text: String): Boolean = text.trim().contains(Regex("^[0-9]{7}$"))
        },
        IDLE ("Вопросов больше нет",  listOf()){
            override fun nextQuestion(): Question = IDLE
            override fun validate(text: String): Boolean = false
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(text: String): Boolean
    }
}