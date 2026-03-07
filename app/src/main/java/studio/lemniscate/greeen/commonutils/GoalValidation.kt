package studio.lemniscate.greeen.commonutils

data class ValidationError (
    val title: String,
    val message: String
)

fun validateInput (
    username: String,
    goal: String,
    amountText: String,
    receipt: String
): ValidationError? {

    val name = username.trim()
    val goalText = goal.trim()

    return when {

        name.isBlank() ->
            ValidationError (
                "Almost there...",
                "Please enter your name to continue."
            )

        name.length < 2 ->
            ValidationError (
                "A bit longer.",
                "Your name should have at least 2 characters."
            )

        name.length > 20 ->
            ValidationError (
                "A bit shorter...",
                "Please enter a shorter name ... maybe your nickname?"
            )

        !name.matches(noEmojis) ->
            ValidationError (
                "No emojis...",
                "Please use only letters, numbers, _ or - in your name"
            )

        !name.matches(usernameRegex) ->
            ValidationError (
                "Invalid name...",
                "Please use only letters, numbers, _ or -."
            )

        goalText.isBlank() ->
            ValidationError (
                "No goal set...",
                "Please add a goal to continue."
            )

        goalText.length < 3 ->
            ValidationError (
                "A bit longer...",
                "Please enter a goal with at least 3 characters."
            )

        goalText.length > 35 ->
            ValidationError (
                "A bit shorter...",
                "Please enter a shorter goal."
            )

        goalText.all { it.isDigit() } ->
            ValidationError (
                "Add some words...",
                "Your goal should describe something, not just numbers."
            )

        !goalText.matches(goalRegex) ->
            ValidationError (
                "Invalid characters...",
                "Please avoid special symbols in your goal."
            )

        goalText.equals(name, ignoreCase = true) ->
            ValidationError (
                "Be more specific...",
                "Your goal should describe something you want to achieve."
            )

        amountText.isBlank() -> {
            ValidationError (
                "No amount set...",
                "Please enter an amount to continue."
            )
        }

        amountText.toFloat() <= 0f ->
            ValidationError (
                "Just one more thing...",
                "Please enter an amount above zero."
            )

        amountText.toFloat() > 100_000_000f ->
            ValidationError (
                "Step by step...",
                "You're thinking big ... please enter a smaller amount."
            )

        !amountText.matches(moneyRegex) ->
            ValidationError (
                "Please be positive...",
                "Please enter a value that represents money. (e.g. 2400, 15.99, 950.50)"
            )

        receipt.isBlank() ->
            ValidationError (
                "Almost there...",
                "Please enter your name for your receipt to continue."
            )

        receipt.length < 2 ->
            ValidationError (
                "A bit longer.",
                "Your receipt name should have at least 2 characters."
            )

        receipt.length > 40 ->
            ValidationError (
                "A bit shorter...",
                "Please enter a shorter name for your receipt."
            )

        else -> null
    }
}