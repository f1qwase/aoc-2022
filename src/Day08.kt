private data class ViewingDistance(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
) {
    fun score(): Int {
        return (left * top * right * bottom)
    }
}

private class Tree(val height: Int) {
    var left: Tree? = null
    var top: Tree? = null
    var right: Tree? = null
    var bottom: Tree? = null

    val viewingDistance: ViewingDistance
        get() {
            fun getViewingDistance(s: Sequence<Tree>): Int = s.indexOfFirst { it.height >= height }.let {
                if (it == -1) s.count() else it + 1
            }
            return ViewingDistance(
                left = generateSequence(left) { it.left }.let(::getViewingDistance),
                top = generateSequence(top) { it.top }.let(::getViewingDistance),
                right = generateSequence(right) { it.right }.let(::getViewingDistance),
                bottom = generateSequence(bottom) { it.bottom }.let(::getViewingDistance),
            )
        }

    override fun toString(): String {
        return "Tree(height=$height, left=${left?.height}, top=${top?.height}, right=${right?.height}, bottom=${bottom?.height})"
    }
}

private fun parseTrees(input: List<String>): Array<Array<Tree>> {
    val trees = input.map { it.toCharArray() }
    val width = input.map { it.length }.distinct().singleOrNull()
        ?: throw IllegalArgumentException("Input is not rectangular")
    val height = input.size
    val treeMatrix = Array(height) { y -> Array(width) { x -> Tree(input[y][x].toString().toInt()) } }

    for (y in trees.indices) {
        for (x in trees[y].indices) {
            val tree = treeMatrix[y][x]
            if (x > 0) {
                tree.left = treeMatrix[y][x - 1]
            }
            if (y > 0) {
                tree.top = treeMatrix[y - 1][x]
            }
            if (x < trees[y].size - 1) {
                tree.right = treeMatrix[y][x + 1]
            }
            if (y < trees.size - 1) {
                tree.bottom = treeMatrix[y + 1][x]
            }
        }
    }

    return treeMatrix
}


fun main() {
    fun part1(input: List<String>): Int {
        val width = input.map { it.length }.distinct().singleOrNull()
            ?: throw IllegalArgumentException("Input is not rectangular")
        val height = input.size
        val visibilityMap = Array(height) { Array(width) { false } }
        val treesHeightMap = input.map { it.map { char -> char.toString().toInt() } }

        treesHeightMap.forEachIndexed { y, line ->
            var highestFromLeft = -1
            line.forEachIndexed { x, height ->
                if (height > highestFromLeft) {
                    visibilityMap[y][x] = true
                    highestFromLeft = height
                }
            }

            var highestFromRight = -1
            line.indices.reversed().forEach { x ->
                if (line[x] > highestFromRight) {
                    visibilityMap[y][x] = true
                    highestFromRight = line[x]
                }
            }
        }

        (0 until width).forEach { x ->
            var highestFromTop = -1
            (0 until height).forEach { y ->
                if (treesHeightMap[y][x] > highestFromTop) {
                    visibilityMap[y][x] = true
                    highestFromTop = treesHeightMap[y][x]
                }
            }

            var highestFromBottom = -1
            (height - 1 downTo 0).forEach { y ->
                if (treesHeightMap[y][x] > highestFromBottom) {
                    visibilityMap[y][x] = true
                    highestFromBottom = treesHeightMap[y][x]
                }
            }
        }

        return visibilityMap.sumOf { line -> line.count { it } }
    }


    fun part2(input: List<String>): Int {
        val trees = parseTrees(input)
        trees.joinToString("\n") {
            it.joinToString("") { it.viewingDistance.score().toString() }
        }.let(::println)
//        println(trees[3][2].viewingDistance)
        println(trees[1][2].viewingDistance)
        var bestTree: Tree? = null
        var bestTreeCoords: Pair<Int, Int>? = null
        trees.forEach { line->
            line.forEach {
                if (bestTree == null || it.viewingDistance.score() > bestTree!!.viewingDistance.score()) {
                    bestTree = it
                    bestTreeCoords = Pair(trees.indexOf(line), line.indexOf(it))
                }
            }
        }
        println(bestTree)
        println(bestTreeCoords)
        return trees.flatten().map { it.viewingDistance.score() }.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21) {
        part1(testInput)
    }
    val input = readInput("Day08")
    println(part1(input))

    check(part2(testInput) == 8) { part2(testInput) }
    println(part2(input))
}

