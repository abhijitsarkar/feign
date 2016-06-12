/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *  *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 *
 */

package name.abhijitsarkar.feign

import groovy.util.logging.Slf4j
import name.abhijitsarkar.feign.core.model.FeignMapping

import java.util.function.BiFunction

/**
 * @author Abhijit Sarkar
 */
@Slf4j
class AlwaysTrueMatcher implements BiFunction<Request, FeignMapping, Boolean> {
    @Override
    Boolean apply(Request request, FeignMapping feignMapping) {
        log.info("Returning true without matching.")

        return true
    }
}
