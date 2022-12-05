fun getCommonItems(line: String): Set<Char> {
    if (line.length % 2 != 0) {
        throw IllegalArgumentException("Input size must be even")
    }
    val (left, right) = line.chunked(line.length / 2)

    return left.toSet() intersect right.toSet()
}

fun Char.priority(): Int = when (this) {
    in 'a'..'z' -> this - 'a' + 1
    in 'A'..'Z' -> this - 'A' + 27
    else -> throw IllegalArgumentException("Unknown char: $this")
}

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        getCommonItems(line).sumOf(Char::priority)
    }


    fun part2(input: List<String>): Int = input
        .chunked(3)
        .sumOf { (line1, line2, line3) ->
            (line1.toSet() intersect line2.toSet() intersect line3.toSet())
                .single()
                .priority()
        }


    check('a'.priority() == 1)
    check('A'.priority() == 27) { 'A'.priority() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    val input = readInput("Day03")
    println(part1(input))

    check(part2(testInput) == 70) { part2(testInput) }
    println(part2(input))
}

