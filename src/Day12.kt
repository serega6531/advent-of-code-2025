fun main() {
    fun parseShape(match: MatchResult): Pair<Int, RegionSegment> {
        val id = match.groupValues[1].toInt()
        val shape = match.groupValues[2].split("\n").map { line ->
            line.map { it == '#' }
        }
        return id to shape
    }

    fun parseRegion(match: MatchResult): RegionDescription {
        val sizeX = match.groupValues[1].toInt()
        val sizeY = match.groupValues[2].toInt()
        val shape = match.groupValues[3].split(" ")
            .mapIndexed { index, value -> index to value.toInt() }
            .toMap()
            .filterValues { it > 0 }

        return RegionDescription(sizeX, sizeY, shape)
    }

    fun parseInput(input: String): Input {
        val shapeRegex = Regex("""(\d+):\n(.+?)\n\n""", RegexOption.DOT_MATCHES_ALL)
        val regionRegex = Regex("""^(\d+)x(\d+): (.+)$""", RegexOption.MULTILINE)

        val shapes = shapeRegex.findAll(input)
            .associate { match -> parseShape(match) }

        val regions = regionRegex.findAll(input)
            .map { match -> parseRegion(match) }
            .toList()

        return Input(shapes, regions)
    }

    fun solve(regionDescription: RegionDescription, shapes: Map<Int, RegionSegment>): Boolean {
        val shapeCellCounts = shapes.mapValues { (_, shape) ->
            shape.sumOf { row -> row.count { it } }
        }

        fun insert(
            region: RegionSegment,
            shape: RegionSegment,
            y: Int,
            x: Int
        ): RegionSegment {
            val shapeHeight = shape.size
            val shapeWidth = shape[0].size

            val newRegion = region.mapIndexed { ry, row ->
                val newRow = row.toMutableList()
                if (ry >= y && ry < y + shapeHeight) {
                    val sy = ry - y
                    for (sx in 0 until shapeWidth) {
                        if (shape[sy][sx]) {
                            newRow[x + sx] = true
                        }
                    }
                }
                newRow
            }
            return newRegion
        }

        fun findEmptySpace(region: RegionSegment, shape: RegionSegment): YX? {
            val regionHeight = regionDescription.sizeY
            val regionWidth = regionDescription.sizeX
            val shapeHeight = shape.size
            val shapeWidth = shape[0].size

            for (y in 0..regionHeight - shapeHeight) {
                for (x in 0..regionWidth - shapeWidth) {
                    var fits = true
                    for (sy in 0 until shapeHeight) {
                        for (sx in 0 until shapeWidth) {
                            if (shape[sy][sx] && region[y + sy][x + sx]) {
                                fits = false
                                break
                            }
                        }
                        if (!fits) break
                    }
                    if (fits) {
                        return YX(y, x)
                    }
                }
            }
            return null
        }

        fun allFit(region: RegionSegment, remaining: Map<Int, Int>): Boolean {
            if (remaining.isEmpty()) {
                println("$regionDescription:")
                println(region.asString())
                println("=====================================")
                return true
            }

            val emptyCells = region.sumOf { row -> row.count { !it } }
            val remainingCells = remaining.entries.sumOf { (shapeIdToUse, num) ->
                shapeCellCounts.getValue(shapeIdToUse) * num
            }
            if (remainingCells > emptyCells) {
                return false
            }

            return remaining.any { (shapeIdToUse, num) ->
                val baseShape = shapes.getValue(shapeIdToUse)
                val rotations = generateSequence(baseShape) { it.rotate() }.take(4).toList()

                rotations.any { shape ->
                    val emptySpace = findEmptySpace(region, shape)
                    val newRemaining = remaining.toMutableMap().also {
                        if (num > 1) {
                            it[shapeIdToUse] = num - 1
                        } else {
                            it.remove(shapeIdToUse)
                        }
                    }

                    if (emptySpace != null) {
                        val newRegion = insert(region, shape, emptySpace.y, emptySpace.x)
                        allFit(newRegion, newRemaining)
                    } else {
                        false
                    }
                }
            }
        }

        val field = List(regionDescription.sizeY) { List(regionDescription.sizeX) { false } }
        val result = allFit(field, regionDescription.requirements)
        println("$regionDescription -> $result")
        return result
    }

    fun part1(input: String): Int {
        val parsed = parseInput(input)
        return parsed.regions.count { solve(it, parsed.shapes) }
    }

    val testInput = readEntireInput("Day12_test")
    val input = readEntireInput("Day12")

    check(part1(testInput) == 2)
    part1(input).println()
}

private fun RegionSegment.rotate(): RegionSegment {
    val rows = this.size
    val cols = this[0].size

    val rotated = List(cols) { col ->
        List(rows) { row ->
            this[rows - 1 - row][col]
        }
    }

    return rotated
}

private fun RegionSegment.asString(): String {
    return this.joinToString("\n") { line ->
        line.joinToString("") { if (it) "#" else "." }
    }
}

private data class Input(
    val shapes: Map<Int, RegionSegment>,
    val regions: List<RegionDescription>
)

private data class RegionDescription(val sizeX: Int, val sizeY: Int, val requirements: Map<Int, Int>)

private typealias RegionSegment = List<List<Boolean>>
