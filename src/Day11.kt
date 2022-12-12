private data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val op: (Long) -> Long,
    val testDivisibleBy: Long,
    val successReceiverIndex: Int,
    val failureReceiverIndex: Int,
) {
    var inspectedItemsCount: Long = 0
}

private fun parseOperation(input: String): (Long) -> Long {
    val (_, operand1Str, opStr, operand2Str) = Regex("(\\S+) ([+*]) (\\S+)").find(input)!!
        .groupValues
    val op: (Long, Long) -> Long = when (opStr) {
        "+" -> Long::plus
        "*" -> Long::times
        else -> throw IllegalArgumentException("Unknown operation: $input")
    }
    return { x: Long -> op(parseOperand(operand1Str, x), parseOperand(operand2Str, x)) }
}

private fun parseOperand(operandString: String, oldValue: Long): Long = when (operandString) {
    "old" -> oldValue
    else -> operandString.toLong()
}


private fun parseMonkey(input: List<String>): Monkey {
    val id = Regex("Monkey (\\d+):").find(input[0])!!.groupValues[1].toInt()
    val items = input[1].substringAfter("Starting items: ")
        .split(", ")
        .map { it.toLong() }
    val operation = input[2].substringAfter("Operation: new = ").let(::parseOperation)
    val testDivisibleBy = input[3].substringAfter("Test: divisible by ").toLong()
    val successReceiver = input[4].substringAfter("If true: throw to monkey ").toInt()
    val failureReceiverReceiver = input[5].substringAfter("If false: throw to monkey ").toInt()
    return Monkey(
        id,
        items.toMutableList(),
        operation,
        testDivisibleBy,
        successReceiver,
        failureReceiverReceiver
    )
}

private fun List<Monkey>.playRound(worryReducer: (Long) -> Long) {
    forEach {monkey ->
        monkey.items.forEach { worryLevel ->
            monkey.inspectedItemsCount++
            val nextWorryLevel = worryReducer(monkey.op(worryLevel))
            val monkeyToThrowItemIndex = if (nextWorryLevel % monkey.testDivisibleBy == 0L) {
                monkey.successReceiverIndex
            } else {
                monkey.failureReceiverIndex
            }
            this[monkeyToThrowItemIndex].items.add(nextWorryLevel)
        }
        monkey.items.clear()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = input.chunked(7, ::parseMonkey)

        repeat(20) {
            monkeys.playRound { it / 3}
        }

        return monkeys
            .map { it.inspectedItemsCount }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }


    fun part2(input: List<String>): Long {
        val monkeys = input.chunked(7, ::parseMonkey)

        val commonDivider = monkeys.map { it.testDivisibleBy }.reduce(Long::times)
        repeat(10000) {
            monkeys.playRound { it % commonDivider }
        }

        println(monkeys.map { it.inspectedItemsCount })

        return monkeys
            .map { it.inspectedItemsCount }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")

    part1(testInput).let { check(it == 10605L) { it } }

    val input = readInput("Day11")
    println(part1(input))

    part2(testInput).let { check(it == 2713310158) { it } }

    println(part2(input))
}

