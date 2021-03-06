/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.plugins;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.Incubating;
import org.gradle.api.Nullable;
import org.gradle.internal.HasInternalProtocol;

import java.util.Map;

/**
 * An interface implemented by something that can be extended by plugins.
 * <p>
 * Plugins can be applied by one of the {@link #apply} methods.
 * The {@link #findPlugin(String)}, {@link #hasPlugin(String)} and {@link #withPlugin(String, org.gradle.api.Action)} methods can be used
 * for detecting whether plugins have been applied.
 * <p>
 * There are different types of plugins that can be applied.
 * Please see {@link org.gradle.api.Plugin} and {@link org.gradle.model.RuleSource}.
 * <p>
 * For more on writing and applying plugins, see {@link org.gradle.api.Plugin}.
 */
@HasInternalProtocol
public interface PluginAware {

    // TODO provide more information about different types of plugins, justifying preference of not using PluginContainer

    /**
     * The container of {@link org.gradle.api.Plugin} style plugins.
     * <p>
     * While not deprecated, it is preferred to use the methods of this interface than use the plugin container directly.
     * The plugin container only supports {@link org.gradle.api.Plugin} style plugins, which is a subset of the potentially applied plugins.
     * The plugin container will be deprecated in future versions of Gradle.
     * <p>
     * Use {@link #apply(java.util.Map)} to apply plugins instead of applying via the plugin container.
     * <p>
     * Use {@link #hasPlugin(String)} or similar to query for the application of plugins instead of doing so via the plugin container.
     *
     * @return the plugin container
     * @see #apply
     * @see #hasPlugin(String)
     */
    PluginContainer getPlugins();

    /**
     * Applies one or more plugins.
     * <p>
     * The given closure is used to configure an {@link ObjectConfigurationAction}, which “builds” the plugin application.
     * <p>
     * This method differs from {@link #apply(java.util.Map)} in that it allows methods of the configuration action to be invoked more than once.
     *
     * @param closure the closure to configure an {@link ObjectConfigurationAction} before “executing” it
     * @see #apply(java.util.Map)
     */
    void apply(Closure closure);

    /**
     * Applies one or more plugins.
     * <p>
     * The given map is applied as a series of method calls to a newly created {@link ObjectConfigurationAction}.
     * That is, each key in the map is expected to be the name of a method {@link ObjectConfigurationAction} and the value to be compatible arguments to that method.
     *
     * @param options the options to use to configure and {@link ObjectConfigurationAction} before “executing” it
     */
    void apply(Map<String, ?> options);

    // TODO define semantics around qualified IDs for methods below
    // Currently core plugins can be queried by name - this should be removed

    /**
     * Returns the information about the plugin that has been applied with the given ID, or null if no plugin has been applied with the given ID.
     *
     * @param id the plugin ID
     * @return information about the applied plugin, or {@code null} if no plugin has been applied with the given ID
     * @since 2.3
     */
    @Nullable
    @Incubating
    AppliedPlugin findPlugin(String id);

    /**
     * Returns {@code true} if a plugin with the given ID has already been applied, otherwise {@code false}.
     *
     * @param id the plugin ID
     * @return {@code true} if the plugin has been applied
     * @since 2.3
     */
    @Incubating
    boolean hasPlugin(String id);

    /**
     * Executes the given action, potentially in the future, if/when the plugin has been applied.
     * <p>
     * If a plugin with the given ID has already been applied, the given action will be executed immediately.
     * Otherwise, the action will be executed sometime in the future if a plugin with the given ID is applied.
     *
     * @param id the plugin ID
     * @param action the action to execute if/when the plugin is applied
     * @since 2.3
     */
    @Incubating
    void withPlugin(String id, Action<? super AppliedPlugin> action);
}
