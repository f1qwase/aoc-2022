import java.lang.IllegalStateException

private const val COMMAND_PREFIX = "$ "
private val TOTAL_SPACE = 70000000
private val SPACE_REQUIRED = 30000000


sealed interface FSNode {
    val parent: FSNode?
    val name: String
    val size: Int

    class File(
        override val parent: FSNode?,
        override val name: String,
        override val size: Int,
    ) : FSNode

    class Dir(
        override val parent: FSNode?,
        override val name: String,
        var children: List<FSNode>,
        var discovered: Boolean = false,
    ) : FSNode {
        override val size by lazy {
            if (!discovered) {
                throw IllegalStateException("size of $name cannot be calculated, it is not discovered still")
            }
            children.sumOf { it.size }
        }
        fun walk(block: (FSNode) -> Unit) {
            block(this)
            children
                .filterIsInstance<Dir>()
                .forEach { it.walk(block) }
        }
    }
}


typealias Input = String
typealias Output = List<String>

fun parseIo(io: List<String>): List<Pair<Input, Output>> = buildList {
    io.forEach {
        if (it.startsWith(COMMAND_PREFIX)) {
            add(it.substringAfter(COMMAND_PREFIX) to mutableListOf())
        } else {
            (last().second as MutableList).add(it)
        }
    }
}

fun discoverFs(cmdIo: List<Pair<Input, Output>>): FSNode.Dir {
    var currentDir: FSNode.Dir? = null
    cmdIo.forEach { (cmdIn, cmdOut) ->
        val command = cmdIn.substringBefore(" ")
        when (command) {
            "ls" -> {
                currentDir!!.discovered = true
                currentDir!!.children = cmdOut.map {
                    val (sizeOrDir, name) = it.split(" ")
                    when (sizeOrDir) {
                        "dir" -> FSNode.Dir(currentDir, name, mutableListOf())
                        else -> FSNode.File(currentDir, name, sizeOrDir.toInt())
                    }
                }
            }

            "cd" -> {
                val args = cmdIn.substringAfter(" ")
                currentDir = when (args) {
                    "/" -> FSNode.Dir(null, "/", mutableListOf())
                    ".." -> currentDir!!.parent as FSNode.Dir
                    else -> currentDir!!.children.find { it.name == args } as? FSNode.Dir
                        ?: throw (IllegalArgumentException("No such directory: $args"))
                }
            }
        }
    }
    while (currentDir!!.parent != null) {
        currentDir = currentDir!!.parent as FSNode.Dir
    }

    return currentDir!!
}


fun main() {
    fun part1(input: List<String>): Int {
        val commandStrings = parseIo(input)
        val fs = discoverFs(commandStrings)

        return buildList {
            fs.walk {
                if (it is FSNode.Dir && it.size <= 100000) {
                    add(it.size)
                }
            }
        }.sumOf { it }
    }


    fun part2(input: List<String>): Int {
        val commandStrings = parseIo(input)
        val fs = discoverFs(commandStrings)

        val occupiedSpace = fs.size
        val freeSpace = TOTAL_SPACE - occupiedSpace
        val spaceToFree = SPACE_REQUIRED - freeSpace

        return buildList {
            fs.walk {
                if (it is FSNode.Dir && it.size >= spaceToFree) {
                    add(it.size)
                }
            }
        }.minByOrNull { it }!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437) {
        part1(testInput)
    }
    val input = readInput("Day07")
    println(part1(input))

    check(part2(testInput) == 24933642) { part2(testInput) }
    println(part2(input))
}

