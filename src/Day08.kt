import java.util.*
import kotlin.math.pow

fun main() {

    fun parseJunction(line: String): Junction {
        return line.split(',')
            .let { (x, y, z) -> Junction(x.toInt(), y.toInt(), z.toInt()) }
    }

    fun buildDistancesQueue(junctions: List<Junction>): PriorityQueue<Pair<Junction, Junction>> {
        val queue = PriorityQueue<Pair<Junction, Junction>> { a, b ->
            a.first.distanceSquared(a.second).compareTo(b.first.distanceSquared(b.second))
        }

        junctions.forEachIndexed { index, j1 ->
            junctions.subList(index, junctions.size).forEach { j2 ->
                if (j1 != j2) {
                    queue.add(Pair(j1, j2))
                }
            }
        }

        return queue
    }

    fun initCircuits(junctions: List<Junction>): Pair<MutableMap<Circuit, Set<Junction>>, MutableMap<Junction, Circuit>> {
        val circuits = mutableMapOf<Circuit, Set<Junction>>()
        val circuitByJunctions = mutableMapOf<Junction, Circuit>()

        junctions.forEach { junction ->
            val circuit = UUID.randomUUID()
            circuits[circuit] = setOf(junction)
            circuitByJunctions[junction] = circuit
        }

        return Pair(circuits, circuitByJunctions)
    }

    fun mergeCircuits(
        left: Junction,
        right: Junction,
        circuits: MutableMap<Circuit, Set<Junction>>,
        circuitByJunctions: MutableMap<Junction, Circuit>
    ) {
        val leftCircuit = circuitByJunctions.getValue(left)
        val rightCircuit = circuitByJunctions.getValue(right)

        if (leftCircuit == rightCircuit) {
            return
        }

        val newCircuit = UUID.randomUUID()
        val newJunctions = circuits.getValue(leftCircuit) + circuits.getValue(rightCircuit)

        circuits.remove(leftCircuit)
        circuits.remove(rightCircuit)
        circuits[newCircuit] = newJunctions

        newJunctions.forEach {
            circuitByJunctions[it] = newCircuit
        }
    }

    fun part1(input: List<String>, connections: Int): Int {
        val junctions = input.map(::parseJunction)

        val distancesQueue = buildDistancesQueue(junctions)

        val (circuits, circuitByJunctions) = initCircuits(junctions)

        repeat(connections) {
            val (first, second) = distancesQueue.remove()
            mergeCircuits(first, second, circuits, circuitByJunctions)
        }

        val circuitsSizeQueue = PriorityQueue<Circuit> { a, b ->
            circuits.getValue(b).size.compareTo(circuits.getValue(a).size)
        }

        circuitsSizeQueue.addAll(circuits.keys)

        return (0..2)
            .map { circuits.getValue(circuitsSizeQueue.remove()).size }
            .product()

    }

    fun part2(input: List<String>): Int {
        val junctions = input.map(::parseJunction)

        val distancesQueue = buildDistancesQueue(junctions)

        val (circuits, circuitByJunctions) = initCircuits(junctions)

        while(true) {
            val (first, second) = distancesQueue.remove()
            mergeCircuits(first, second, circuits, circuitByJunctions)

            if (circuits.size == 1) {
                return first.x * second.x
            }
        }
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    check(part1(testInput, 10) == 40)
    part1(input, 1000).println()

    check(part2(testInput) == 25272)
    part2(input).println()
}

private data class Junction(val x: Int, val y: Int, val z: Int) {
    fun distanceSquared(other: Junction): Double {
        return (x - other.x).toDouble().pow(2) +
                (y - other.y).toDouble().pow(2) +
                (z - other.z).toDouble().pow(2)
    }
}

private typealias Circuit = UUID