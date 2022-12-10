import kotlin.math.abs

private sealed interface Command {
    object Noop : Command
    class Add(val value: Int) : Command
}

private fun parseCommand(input: String): Command = when (input.substringBefore(' ')) {
    "noop" -> Command.Noop
    "addx" -> Command.Add(input.substringAfter(' ').toInt())
    else -> throw IllegalArgumentException("Unknown command: $input")
}


fun getCpuRegisterValues(input: List<String>) = input
    .map(::parseCommand)
    .flatMap {
        when (it) {
            Command.Noop -> listOf(it)
            is Command.Add -> {
                listOf(Command.Noop, it)
            }
        }
    }
    .runningFold(1) { value, command ->
        when (command) {
            Command.Noop -> value
            is Command.Add -> value + command.value
        }
    }


fun main() {
    fun part1(input: List<String>): Int = getCpuRegisterValues(input)
        .foldIndexed(0) { index, strengthSum, registerValue ->
            val cycle = index + 1 // cast to 1-based cycle
            if (cycle % 40 == 20) {
                strengthSum + cycle * registerValue
            } else {
                strengthSum
            }
        }


    fun part2(input: List<String>): String {
        return getCpuRegisterValues(input)
            .mapIndexed { cycle, spritePosition ->
                if (abs(cycle % 40 - spritePosition) <= 1) {
                    '#'
                } else {
                    '.'
                }
            }
            .chunked(40)
            .joinToString("\n") { it.joinToString("") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    part1(testInput).let { check(it == 13140) { it } }
    val input = readInput("Day10")
    println(part1(input))

    println(part2(input))
}

