private fun findUniqueSymbolsSequence(input: String, uniqueCount: Int): Int =
    input.windowed(uniqueCount) {
        it.toSet().size == uniqueCount
    }.indexOf(true) + uniqueCount

fun main() {
    fun part1(input: List<String>): Int = findUniqueSymbolsSequence(input.single(), 4)


    fun part2(input: List<String>): Int = findUniqueSymbolsSequence(input.single(), 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 10) {
        part1(testInput)
    }
    val input = readInput("Day06")
    println(part1(input))

    check(part2(testInput) == 29) { part2(testInput) }
    println(part2(input))
}

