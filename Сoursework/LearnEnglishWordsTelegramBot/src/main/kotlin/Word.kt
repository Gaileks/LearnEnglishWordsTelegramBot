package coursework

data class Word(
    val text: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
) {
    override fun toString(): String {
        return "Слово: $text, Перевод: $translate, Правильных ответов: $correctAnswersCount"
    }
}
