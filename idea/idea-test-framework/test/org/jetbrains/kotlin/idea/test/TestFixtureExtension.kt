/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.test

import com.intellij.openapi.module.Module
import com.intellij.util.SmartFMap

interface TestFixtureExtension {
    fun setUp(module: Module)
    fun tearDown()

    companion object {
        @Volatile
        private var instances = SmartFMap.emptyMap<String, TestFixtureExtension>()

        @Suppress("UNCHECKED_CAST")
        fun loadFixtures(classNames: List<String>, module: Module) {
            classNames.mapNotNull { className ->
                if (instances[className] == null) {
                    (Class.forName(className).newInstance() as TestFixtureExtension).apply {
                        instances = instances.plus(className, this)
                    }
                } else null
            }.forEach { it.setUp(module) }
        }

        fun unloadFixtures(classNames: List<String>) {
            classNames.mapNotNull { className ->
                val value = instances[className]
                if (value != null) {
                    instances = instances.minus(className)
                    value
                } else null
            }.forEach { it.tearDown() }
        }
    }
}