// TARGET_BACKEND: JVM
// IGNORE_BACKEND: JVM_IR
// IGNORE_BACKEND_FIR: JVM_IR
// WITH_REFLECT

import kotlin.reflect.*
import kotlin.test.assertEquals

class A {
    fun foo(s: String, vararg xs: Long): String = "foo"
}

fun checkNotEqual(x: Any, y: Any) {
    assertNotEqual(x, y)
    assertNotEqual(y, x)
}

fun coercionToUnit(f: (A, String, LongArray) -> Unit): Any = f
fun varargToElement(f: (A, String, Long, Long) -> String): Any = f
fun defaultAndVararg(f: (A) -> String): Any = f
fun allOfTheAbove(f: (A) -> Unit): Any = f

fun box(): String {
    val foo = A::class.members.single { it.name == "foo" }

    checkNotEqual(coercionToUnit(A::foo), foo)
    checkNotEqual(varargToElement(A::foo), foo)
    checkNotEqual(defaultAndVararg(A::foo), foo)
    checkNotEqual(String(A::foo), foo)

    return "OK"
}
