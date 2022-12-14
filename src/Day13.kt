import java.lang.Character.isDigit

private sealed interface PacketElement: Comparable<PacketElement> {
    data class Dgt(val value: Int) : PacketElement {
        override fun compareTo(other: PacketElement): Int = when (other) {
            is Dgt -> value.compareTo(other.value)
            is Lst -> Lst(listOf(this)).compareTo(other)
        }

        override fun toString(): String = value.toString()
    }
    data class Lst(val elements: List<PacketElement>) : PacketElement {
        override fun compareTo(other: PacketElement): Int = when (other) {
                is Dgt -> - other.compareTo(this)
                is Lst -> {
//                    println("compare: $this $other")
                    when {
                        other.elements.isEmpty() || elements.isEmpty() -> {
                            elements.size.compareTo(other.elements.size)
                        }
                        else -> {
                            val first = elements.first().compareTo(other.elements.first())
                            if (first != 0) first
                            else Lst(elements.drop(1)).compareTo(Lst(other.elements.drop(1)))
                        }
                    }
                }
            }

        override fun toString(): String = "[${elements.joinToString(",")}]"
    }
}

private fun parsePacket(input: String, startFrom: Int = 0, parentList: MutableList<PacketElement>): Int {
    var i = startFrom
    while (i < input.length) {
        val char = input[i]
        when {
            char == '[' -> {
                val sublist = mutableListOf<PacketElement>()
                val end = parsePacket(input, i + 1, sublist)
                parentList.add(PacketElement.Lst(sublist))
                i = end + 1
            }

            char == ']' -> return i

            isDigit(char) -> {
                val numberStart = i
                while (i < input.length && isDigit(input[i])) {
                    i++
                }
                val value = input.substring(numberStart, i).toInt()
                parentList.add(PacketElement.Dgt(value))
            }

            char == ',' -> {
                i++
            }

            else -> {
                throw IllegalArgumentException("Unexpected char $char")
            }
        }
    }
    return i
}

private fun parseInput(input: List<String>): List<Pair<PacketElement, PacketElement>> =
    input.filter { it.isNotEmpty() }.chunked(2) {
        val (left, right) = it.take(2).map { line ->
            val list = mutableListOf<PacketElement>()
            parsePacket(line, 0, list)
            list.single()
        }
        left to right
    }


fun main() {
    fun part1(input: List<String>): Int {
        return parseInput(input)
//            .onEachIndexed {i, (left, right) ->
//                val result = if (left < right) "right" else "NOT"
//                println("${i + 1}: ${result}, $left $right")
//            }
            .mapIndexed { i, (left, right) -> if (left < right) i + 1 else 0 }
            .sum()
    }


    fun part2(input: List<String>): Int {
        val dividers = listOf("[[2]]", "[[6]]")
        println(input + dividers)
        return parseInput(input + dividers).flatMap { (left, right) ->
            listOf(left, right)
        }.sorted()
            .map { it.toString() }
            .withIndex()
            .filter { it.value in listOf("[[2]]", "[[6]]")}
            .also { check(it.size == 2) }
            .map {it.index + 1}
            .reduce(Int::times)

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")

    part1(testInput).let { check(it == 13) { it } }

    val input = readInput("Day13")
    println(part1(input))

    part2(testInput).let { check(it == 140) { it } }

    println(part2(input))
}

