import kotlin.math.abs

data class Position(
    val x: Int,
    val y: Int,
) {
    fun neighbours() = listOf(
        Position(x, y + 1),
        Position(x, y - 1),
        Position(x + 1, y),
        Position(x - 1, y),
    )

    override fun toString(): String {
        return "[$x,$y]"
    }
}

private typealias HeightsMap = Map<Position, Char>


//private fun HeightsMap.getShortestWay(
//    from: Position,
//    to: Position,
//    visited: List<Position>,
//    shortestWays: Array<Array<Int?>>
//): Int? {
//    if (from == to) {
//        return 0
//    }
//
//    println("calling for $from")
//
//    return from.neighbours()
//        .asSequence()
//        .filterNot { it in visited }
//        .filter(::contains)
//        .filter { abs(getHeight(it) - getHeight(from)) <= 1 }
//        .mapNotNull { getShortestWay(it, to,visited + from) }
//        .minOrNull()
//        ?.let { it + 1 }
//}


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

