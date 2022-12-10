import kotlin.math.abs

private data class Vector(val x: Int, val y: Int) {
    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y)
    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)
}

private data class Rope(
    val head: Vector,
    val tail: Vector,
) {
    fun moveHead(direction: Vector): Rope {
        val nextHead = head + direction
        val diff = nextHead - tail
        val tailMove = when {
            abs(diff.x) <= 1 && abs(diff.y) <= 1 -> Vector(0, 0)
            else -> Vector(diff.x.coerceIn(-1, 1), diff.y.coerceIn(-1, 1))
        }

        return Rope(nextHead, tail + tailMove)
    }

    fun toStr(width: Int = 5, height: Int = 5): String =
        Array(height) { y ->
            Array(width) { x ->
                when (Vector(x, y)) {
                    head -> 'H'
                    tail -> 'T'
                    else -> '*'
                }
            }.joinToString("")
        }.reversed().joinToString("\n")
}

private data class SuperRope(val ropes: List<Rope>) {
    init {
        check(ropes.zipWithNext().all { (a, b) -> a.tail == b.head }) { "Ropes must be connected" }
    }

    fun moveHead(direction: Vector): SuperRope {
        val nextRopes = buildList<Rope> {
            add(ropes.first().moveHead(direction))
            ropes.drop(1).forEach {
                add(it.moveHead(last().tail - it.head))
            }
        }
        return SuperRope(nextRopes)
    }
}

private fun parseMoves(input: List<String>): List<Vector> = buildList {
    input.forEach { line ->
        val (directionCode, stepsCount) = line.split(" ")
        val direction = when (directionCode) {
            "U" -> Vector(0, 1)
            "D" -> Vector(0, -1)
            "R" -> Vector(1, 0)
            "L" -> Vector(-1, 0)
            else -> throw IllegalArgumentException("Unknown direction: $directionCode")
        }
        repeat(stepsCount.toInt()) {
            add(direction)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val startPosition = Vector(0, 0)
        val moves = parseMoves(input)
        return moves.asSequence()
            .runningFold(Rope(startPosition, startPosition)) { rope, move ->
                rope.moveHead(move)
            }
//            .onEach { println(it.toStr()) }
            .map { it.tail }
            .distinct()
            .count()
    }


    fun part2(input: List<String>): Int {
        val startPosition = Vector(0, 0)
        val moves = parseMoves(input)
        val initialSuperRope = SuperRope(List(9) { Rope(startPosition, startPosition) })
        return moves.asSequence()
            .runningFold(initialSuperRope) { rope, move ->
                rope.moveHead(move)
            }
            .map { it.ropes.last().tail }
            .distinct()
            .count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13) {
        part1(testInput)
    }
    val input = readInput("Day09")
    println(part1(input))

    val testInput2 = readInput("Day09_test_2")
    part2(testInput2).let { check(it == 36) { it } }

    println(part2(input))
}

