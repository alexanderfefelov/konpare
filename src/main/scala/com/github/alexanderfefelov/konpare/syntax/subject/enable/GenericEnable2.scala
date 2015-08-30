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

package com.github.alexanderfefelov.konpare.syntax.subject.enable

import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject}

class GenericEnable2(val predicate: String) extends Subject {

  override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
    if (data.nonEmpty) {
      // enable password encryption
      model += s"feature=$predicate=${data.mkString("=")}" -> Syntax.VALUE_ENABLE
    } else {
      // enable snmp
      model += s"feature=$predicate" -> Syntax.VALUE_ENABLE
    }
  }

}