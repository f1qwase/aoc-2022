data class Store(val stacks: Array<List<Char>>) {
    fun move(from: Int, to: Int, count: Int) {
        val movedCrates = stacks[from].take(count)
        stacks[from] = stacks[from].drop(count)
        stacks[to] = movedCrates + stacks[to]
    }

    val topCrates: List<Char>
        get() = stacks.map { it.first() }

    companion object {
        fun parse(input: List<String>): Store {
            val stacksCount = (input.maxOf { it.length } + 1) / 4
            val stacks = List(stacksCount) { mutableListOf<Char>() }
            input.forEach { line ->
                line.forEachIndexed { index, c ->
                    if (c.isLetter()) {
                        stacks[index / 4].add(c)
                    }
                }
            }

            return Store(stacks.map { it.toList() }.toTypedArray())
        }
    }
}

data class Move(val from: Int, val to: Int, val count: Int) {
    companion object {
        fun parse(input: String): Move {
            //            "move 6 from 1 to 7"
            val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
            return regex.matchEntire(input)!!.destructured.let { (count, from, to) ->
                Move(from.toInt() - 1, to.toInt() - 1, count.toInt())
            }
        }
    }
}


fun main() {
    fun part1(input: List<String>): String {
        val storeInput = input.takeWhile { it.isNotBlank() }
        val store = Store.parse(storeInput)

        val moves = input.drop(storeInput.size + 1).map { Move.parse(it) }

        moves.forEach { (from, to, count) ->
            repeat(count) {
                store.move(from, to, 1)
            }
        }

        return store.topCrates.joinToString("")
    }


    fun part2(input: List<String>): String {
        val storeInput = input.takeWhile { it.isNotBlank() }
        val store = Store.parse(storeInput)

        val moves = input.drop(storeInput.size + 1).map { Move.parse(it) }

        moves.forEach { (from, to, count) ->
            store.move(from, to, count)
        }

        return store.topCrates.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ") {
        part1(testInput)
    }
    val input = readInput("Day05")
    println(part1(input))

    check(part2(testInput) == "MCD") { part2(testInput) }
    println(part2(input))
}

