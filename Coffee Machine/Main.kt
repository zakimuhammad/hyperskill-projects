package machine
import java.util.*

data class DataCoffee(
        var water: Int,
        var milk: Int,
        var beans: Int,
        var cups: Int,
        var money: Int
)

enum class MachineState {
    PICK_ACTION,
    PICK_COFFEE,
    FILL_WATER,
    FILL_MILK,
    FILL_BEANS,
    FILL_CUPS
}

open class CoffeeBeverage(val water: Int, val milk: Int, val beans: Int, val price: Int)
class Espresso: CoffeeBeverage(250, 0, 16, 4)
class Latte: CoffeeBeverage(350, 75, 20,  7)
class Cappuccino: CoffeeBeverage(200, 100, 12, 6)

class CoffeeMachine {
    private var dataCoffee = DataCoffee(400, 540, 120, 9, 550)
    var state: MachineState = MachineState.PICK_ACTION

    fun executeAction(action: String) {
        when (state) {
            MachineState.PICK_ACTION -> {
                when (action) {
                    "buy" -> {
                        print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: ")
                        state = MachineState.PICK_COFFEE
                    }
                    "fill" -> {
                        print("Write how many ml of water do you want to add: ")
                        state = MachineState.FILL_WATER
                    }
                    "take" -> take()
                    "remaining" -> printState()
                }
            }
            MachineState.PICK_COFFEE -> {
                checkResources(action)
                state = MachineState.PICK_ACTION
            }
            MachineState.FILL_WATER -> {
                dataCoffee.water += action.toInt()
                print("Write how many ml of milk do you want to add: ")
                state = MachineState.FILL_MILK
            }
            MachineState.FILL_MILK -> {
                dataCoffee.milk += action.toInt()
                print("Write how many ml of beans do you want to add: ")
                state = MachineState.FILL_BEANS
            }
            MachineState.FILL_BEANS -> {
                dataCoffee.beans += action.toInt()
                print("Write how many ml of cups do you want to add: ")
                state = MachineState.FILL_CUPS
            }
            MachineState.FILL_CUPS -> {
                dataCoffee.cups += action.toInt()
                println()
                state = MachineState.PICK_ACTION
            }
        }
    }

    private fun makeCoffee(coffee: CoffeeBeverage) {
        dataCoffee.water -= coffee.water
        dataCoffee.milk -= coffee.milk
        dataCoffee.beans -= coffee.beans
        dataCoffee.money += coffee.price
        dataCoffee.cups--
    }

    private fun take() {
        println("I gave you \$${dataCoffee.money}")

        dataCoffee.money = 0
    }

    private fun printState() {
        println()
        println("The coffee machine has:\n" +
                "${dataCoffee.water} of water\n" +
                "${dataCoffee.milk} of milk\n" +
                "${dataCoffee.beans} of coffee beans\n" +
                "${dataCoffee.cups} of disposable cups\n" +
                "${dataCoffee.money} of money")
        println()
    }

    private fun checkResources(choice: String) {
        var message = ""

        val coffee = when(choice) {
            "1" -> Espresso()
            "2" -> Latte()
            "3" -> Cappuccino()
            else -> {
                println("You choose wrong menu!")
                return
            }
        }

        when {
            dataCoffee.water < coffee.water -> message += "Sorry, not enough water!\n"
            dataCoffee.milk < coffee.milk -> message += "Sorry, not enough milk!\n"
            dataCoffee.beans < coffee.beans -> message += "Sorry, not enough beans!\n"
            dataCoffee.cups < 1 -> message += "Sorry not enough cups!\n"
        }

        if (message == "") {
            println("I have enough resources, making you coffee!")
            println()
            makeCoffee(coffee)
        } else {
            println(message)
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)

    val coffeeMachine = CoffeeMachine()

    while (true) {
        if (coffeeMachine.state == MachineState.PICK_ACTION) print("Write action (buy, fill, take, remaining, exit): ")

        val action = scanner.nextLine()
        if (action == "exit") break

        coffeeMachine.executeAction(action)
    }
}
