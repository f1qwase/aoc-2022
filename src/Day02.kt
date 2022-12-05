enum class RpsFigure(val value: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun play(other: RpsFigure): PlayResult {
        return when ((this.ordinal - other.ordinal + 3) % 3) {
            0 -> PlayResult.DRAW
            1 -> PlayResult.WIN
            2 -> PlayResult.LOSE
            else -> throw IllegalStateException("Unexpected value: ${(this.ordinal - other.ordinal) % 3}")
        }
    }

    companion object {
        fun fromCode(s: String): RpsFigure = when (s) {
            "A", "X" -> ROCK
            "B", "Y" -> PAPER
            "C", "Z" -> SCISSORS
            else -> throw IllegalArgumentException("Unknown figure code: $s")
        }
    }
}

enum class PlayResult(val value: Int) {
    LOSE(0),
    DRAW(3),
    WIN(6);

    companion object {
        fun fromCode(s: String): PlayResult = when (s) {
            "X" -> LOSE
            "Y" -> DRAW
            "Z" -> WIN
            else -> throw IllegalArgumentException("Unknown result code: $s")
        }
    }
}


fun main() {
    fun part1(input: List<String>): Int = input.sumOf {
        val (theirs, mine) = it.split(" ").map(RpsFigure::fromCode)
        val result = mine.play(theirs)
        result.value + mine.value
    }

    fun part2(input: List<String>): Int = input.sumOf {
        val (theirsCode, resultCode) = it.split(" ")
        val result = PlayResult.fromCode(resultCode)
        val theirs = RpsFigure.fromCode(theirsCode)
        val rpsVals = RpsFigure.values()
        val mine = rpsVals.first { figure -> figure.play(theirs) == result }
        result.value + mine.value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    check(part1(input) == 11873)
    println(part2(input))
}
