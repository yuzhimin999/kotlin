// IGNORE_BACKEND: JVM_IR
// IGNORE_BACKEND_FIR: JVM_IR
// FILE: test.kt

fun checkEqual(x: Any, y: Any) {
    if (x != y || y != x) throw AssertionError("$x and $y should be equal")
    if (x.hashCode() != y.hashCode()) throw AssertionError("$x and $y should have the same hash code")
}

fun checkNotEqual(x: Any, y: Any) {
    if (x == y || y == x) throw AssertionError("$x and $y should NOT be equal")
}

fun interface FunInterface {
    fun invoke()
}

private fun id(f: FunInterface): Any = f

fun nop() {}
fun nop2() {}

fun box(): String {
    checkEqual(id(::nop), id(::nop))
    checkEqual(id(::nop), nopFromOtherFile())

    checkNotEqual(id(::nop), id(::nop2))

    return "OK"
}

// FILE: fromOtherFile.kt

private fun id(f: FunInterface): Any = f

fun nopFromOtherFile(): Any = id(::nop)
