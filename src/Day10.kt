import com.google.ortools.Loader
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.CpSolverStatus
import com.google.ortools.sat.LinearExpr
import java.util.*
import kotlin.math.pow

fun main() {

    val machineRegex = Regex("""^\[([.#]+)] ((?:\(\d+(?:,\d+)*\) *)+) \{([\d,]+)}$""")
    val buttonRegex = Regex("""\((\d+(?:,\d+)*)\)""")

    fun parseTargetLights(targetLightsStr: String): BitSet {
        val targetLights = BitSet()
        targetLightsStr.forEachIndexed { index, c ->
            targetLights.set(index, c == '#')
        }
        return targetLights
    }

    fun parseButtons(buttonsStr: String): List<BitSet> {
        return buttonRegex.findAll(buttonsStr).map { buttonMatch ->
            val bits = BitSet()
            buttonMatch.groupValues[1].split(',').map { it.toInt() }.forEach { bits.set(it) }
            bits
        }.toList()
    }

    fun parseJoltages(joltagesStr: String): List<Int> {
        return joltagesStr.split(',').map { it.toInt() }
    }

    fun parseMachine(line: String): Machine {
        val match = machineRegex.matchEntire(line) ?: error("Invalid machine format: $line")

        val targetLights = parseTargetLights(match.groupValues[1])
        val buttons = parseButtons(match.groupValues[2])
        val joltages = parseJoltages(match.groupValues[3])

        return Machine(targetLights, buttons, joltages)
    }

    fun parseInput(input: List<String>): List<Machine> {
        return input.map { parseMachine(it) }
    }

    fun bitMaskToBitSet(mask: Int): BitSet {
        val bitSet = BitSet()
        var currentMask = mask
        var index = 0
        while (currentMask != 0) {
            if ((currentMask and 1) != 0) {
                bitSet.set(index)
            }
            currentMask = currentMask ushr 1
            index++
        }
        return bitSet
    }

    fun matchesTargetLights(machine: Machine, pressesMask: BitSet): Boolean {
        val targetLights = machine.targetLights
        val lights = BitSet()

        machine.buttons.forEachIndexed { index, button ->
            if (pressesMask.get(index)) {
                lights.xor(button)
            }
        }

        return lights == targetLights
    }

    fun getFewestPressesForLights(machine: Machine): Int {
        val maxTries = 2.0.pow(machine.buttons.size).toInt()

        return (0..<maxTries)
            .map { bitMaskToBitSet(it) }
            .filter { mask -> matchesTargetLights(machine, mask) }
            .minOfOrNull { it.cardinality() } ?: 0
    }

    fun getFewestPressesForCounters(machine: Machine): Int {
        Loader.loadNativeLibraries()

        val model = CpModel()

        val buttons = machine.buttons.mapIndexed { buttonIndex, buttonMask ->
            val maxPresses = machine.joltages
                .filterIndexed { counterIndex, _ -> buttonMask.get(counterIndex) }
                .min()

            model.newIntVar(0L, maxPresses.toLong(), "button_$buttonIndex")
        }

        machine.joltages.forEachIndexed { counterIndex, joltage ->
            val presses = machine.buttons
                .withIndex()
                .filter { it.value.get(counterIndex) }
                .map { buttons[it.index] }
                .toTypedArray()

            model.addEquality(LinearExpr.sum(presses), joltage.toLong())
        }

        model.minimize(LinearExpr.sum(buttons.toTypedArray()))

        val solver = CpSolver()
        val status = solver.solve(model)

        return if (status == CpSolverStatus.OPTIMAL) {
            val total = buttons.sumOf { solver.value(it).toInt() }
            println("Solved $machine -> $total")
            total
        } else {
            error("No solution found")
        }
    }

    fun part1(input: List<String>): Int {
        val machines = parseInput(input)

        return machines.sumOf { getFewestPressesForLights(it) }
    }

    fun part2(input: List<String>): Int {
        val machines = parseInput(input)

        return machines.sumOf { getFewestPressesForCounters(it) }
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 7)
    part1(input).println()

    check(part2(testInput) == 33)
    part2(input).println()
}

private data class Machine(val targetLights: BitSet, val buttons: List<BitSet>, val joltages: List<Int>)