fun main() {
    fun part1(input: List<String>): Int {
        var currentCount = 0
        var maxCount = 0
        input.forEach {
            if (it.isEmpty()) {
                if (currentCount > maxCount) {
                    maxCount = currentCount
                }
                currentCount = 0
            } else {
                currentCount += it.toInt()
            }
        }
        if (currentCount > maxCount) {
            maxCount = currentCount
        }
        currentCount = 0
        return maxCount
    }

    fun part2(input: List<String>): Int {
        val elvesFood = mutableListOf(0)
        var current = 0
        input.forEach {
            if (it.isEmpty()) {
                elvesFood.add(current)
                current = 0
            } else {
                current += it.toInt()
            }
        }
        if (current != 0) {
            elvesFood.add(current)
        }
        return elvesFood
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 9)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
