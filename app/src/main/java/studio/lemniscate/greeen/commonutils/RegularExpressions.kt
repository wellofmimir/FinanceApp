package studio.lemniscate.greeen.commonutils

val usernameRegex = Regex("^[\\p{L}]+([ _-][\\p{L}]+)*$")
val noEmojis = Regex("^[\\p{L}0-9_-]+$")
val goalRegex = Regex("^[\\p{L}]+([ _-][\\p{L}]+)*$")
val moneyRegex = Regex("^\\d+(\\.\\d{0,2})?\$")