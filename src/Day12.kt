private typealias HeightsMap = Map<Position, Char>


private data class Input2(
    val heightsMap: HeightsMap,
    val start: Position,
    val finish: Position,
)


private fun parseState(input: List<String>): Input2 {
    lateinit var position: Position
    lateinit var target: Position
    val heights = buildMap {
        input.forEachIndexed { y, line ->
            line
                .mapIndexed { x, char ->
                    when (char) {
                        'S' -> 'a'.also { position = Position(x, y) }
                        'E' -> 'z'.also { target = Position(x, y) }
                        in 'a'..'z' -> char
                        else -> throw IllegalArgumentException("Unknown char $char")
                    }
                }
                .forEachIndexed { x, char ->
                    set(Position(x, y), char)
                }
        }
    }

    return Input2(heights, position, target)
}


fun main() {
    fun part1(input: List<String>): Int {
        val (heights, start, finish) = parseState(input)

        val visited = mutableSetOf(start)

        val positions = generateSequence(setOf(start)) { lastStepPositions ->
            if (lastStepPositions.isEmpty()) return@generateSequence null

            visited += lastStepPositions
            lastStepPositions.flatMap {
                    position -> position.neighbours()
                        .filter { it in heights }
                        .filter { heights[it]!! - heights[position]!! <= 1 }
            }.toSet() - visited
        }

        return positions
            .take(10000)
            .indexOfFirst {
                it.contains(finish)
            }
    }


    fun part2(input: List<String>): Int {
        val (heights, start, finish) = parseState(input)

        val visited = mutableSetOf(start)

        val positions = generateSequence(heights.filterValues { it == 'a' }.keys) { lastStepPositions ->
            if (lastStepPositions.isEmpty()) return@generateSequence null

            visited += lastStepPositions
            lastStepPositions.flatMap {
                    position -> position.neighbours()
                .filter { it in heights }
                .filter { heights[it]!! - heights[position]!! <= 1 }
            }.toSet() - visited
        }

        return positions
            .take(10000)
            .indexOfFirst {
                it.contains(finish)
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")

    part1(testInput).let { check(it == 31) { it } }

    val input = readInput("Day12")
    println(part1(input))

    part2(testInput).let { check(it == 29) { it } }

    println(part2(input))
}

