/*
 * konpare
 * Copyright (C) 2015 Alexander Fefelov <https://github.com/alexanderfefelov>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.alexanderfefelov.konpare.syntax.subject.config

import com.github.alexanderfefelov.konpare.syntax.{Syntax, GenericComplement, Subject}

object Temperature extends Subject {

  override val complements = Map(
    // config temperature log state enable
    Syntax.COMPLEMENT_LOG -> new GenericComplement(s"${Syntax.SUBJECT_TEMPERATURE}=${Syntax.COMPLEMENT_LOG}"),
    // config temperature threshold high 79
    Syntax.COMPLEMENT_THRESHOLD -> new GenericComplement(s"${Syntax.SUBJECT_TEMPERATURE}=${Syntax.COMPLEMENT_THRESHOLD}"),
    // config temperature trap state enable
    Syntax.COMPLEMENT_TRAP -> new GenericComplement(s"${Syntax.SUBJECT_TEMPERATURE}=${Syntax.COMPLEMENT_TRAP}")
  )

}