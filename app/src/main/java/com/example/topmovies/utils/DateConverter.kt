package com.example.topmovies.utils

class DateConverter {

    companion object {

        private const val START_YEAR_INDEX = 0
        private const val END_YEAR_INDEX = 4
        private const val START_MONTH_INDEX = 5
        private const val END_MONTH_INDEX = 7
        private const val START_DAY_INDEX = 8
        private const val END_DAY_INDEX = 10

        fun formatDate(date: String): String {
            val year = date.substring(START_YEAR_INDEX, END_YEAR_INDEX)
            val month = date.substring(START_MONTH_INDEX, END_MONTH_INDEX)
            val day = date.substring(START_DAY_INDEX, END_DAY_INDEX)
            val monthString = when (month) {
                "01" -> "January"
                "02" -> "February"
                "03" -> "March"
                "04" -> "April"
                "05" -> "May"
                "06" -> "June"
                "07" -> "July"
                "08" -> "August"
                "09" -> "September"
                "10" -> "October"
                "11" -> "November"
                "12" -> "December"
                else -> "December"
            }
            return "$monthString $day, $year"
        }
    }
}