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

package com.github.alexanderfefelov.konpare.syntax.subject.create

import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject, Complement}

object Snmp extends Subject {

  override val complements = Map(
    Syntax.COMPLEMENT_COMMUNITY -> Community
  )

  object Community extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
      // create snmp community public view CommunityView read_only
      model += s"${Syntax.SUBJECT_SNMP}=${Syntax.COMPLEMENT_COMMUNITY}=${data.head}=${data(3)}" -> Syntax.VALUE_ENABLE
    }
  }

}