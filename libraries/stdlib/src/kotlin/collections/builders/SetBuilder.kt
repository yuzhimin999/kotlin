/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin.collections.builders

/*@PublishedApi
internal */class SetBuilder<E> private constructor(
    private var backing: LinkedHashSet<E>?
) : MutableSet<E>, AbstractMutableCollection<E>() {

    constructor() : this(LinkedHashSet<E>())

    constructor(initialCapacity: Int) : this(LinkedHashSet<E>(initialCapacity))

    fun build(): Set<E> {
        val backing = this.backing ?: throw IllegalStateException()
        this.backing = null
        return ImmutableSet(backing)
    }

    override val size: Int
        get() = (backing ?: throw IllegalStateException()).size

    override fun contains(element: E): Boolean =
        (backing ?: throw IllegalStateException()).contains(element)

    override fun clear() =
        (backing ?: throw IllegalStateException()).clear()

    override fun add(element: E): Boolean =
        (backing ?: throw IllegalStateException()).add(element)

    override fun remove(element: E): Boolean =
        (backing ?: throw IllegalStateException()).remove(element)

    override fun iterator(): MutableIterator<E> =
        (backing ?: throw IllegalStateException()).iterator()

    override fun containsAll(elements: Collection<E>): Boolean =
        (backing ?: throw IllegalStateException()).containsAll(elements)

    override fun addAll(elements: Collection<E>): Boolean =
        (backing ?: throw IllegalStateException()).addAll(elements)

    override fun equals(other: Any?): Boolean =
        (backing ?: throw IllegalStateException()) == other

    override fun hashCode(): Int =
        (backing ?: throw IllegalStateException()).hashCode()
}

private class ImmutableSet<E>(
    private val backing: LinkedHashSet<E>
) : Set<E>, AbstractCollection<E>() {
    override val size: Int get() = backing.size
    override fun contains(element: E): Boolean = backing.contains(element)
    override fun containsAll(elements: Collection<E>): Boolean = backing.containsAll(elements)
    override fun iterator(): Iterator<E> = backing.iterator()

    override fun hashCode(): Int = backing.hashCode()
    override fun equals(other: Any?): Boolean = backing == other
    override fun toString(): String = backing.toString()
}

internal fun <E> Collection<E>.collectionToString(): String {
    val sb = StringBuilder(2 + size * 3)
    sb.append("[")
    var i = 0
    val it = iterator()
    while (it.hasNext()) {
        if (i > 0) sb.append(", ")
        val next = it.next()
        if (next == this) sb.append("(this Collection)") else sb.append(next)
        i++
    }
    sb.append("]")
    return sb.toString()
}