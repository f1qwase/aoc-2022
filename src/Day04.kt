private fun parseLine(line: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val (left, right)= line.split(",")
        .map {
            val (start, end) = it.split("-")
                .map(String::toInt)
            start to end
        }

    return left to right
}



fun main() {
    fun part1(input: List<String>): Int = input.count { line ->
        val (left, right) = parseLine(line)
        (left.first >= right.first && left.second <= right.second) ||
                (left.first <= right.first && left.second >= right.second)
    }


    fun part2(input: List<String>): Int = input.count { line ->
        val (left, right) = parseLine(line)
        !(left.second < right.first || right.second < left.first )
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2) { part1(testInput) }
    val input = readInput("Day04")
    println(part1(input))

    check(part2(testInput) == 4) { part2(testInput) }
    println(part2(input))
}

