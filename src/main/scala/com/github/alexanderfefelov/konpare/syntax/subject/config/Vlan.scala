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

import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject}

object Vlan extends Subject {

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    val vlan = data.head
    vlan match {
      case Syntax.VALUE_DEFAULT =>
      case _ =>
        (data(1), data(2)) match {
          case (Syntax.VERB_ADD, Syntax.ADJECTIVE_TAGGED) =>
            // config vlan v5 add tagged 24-26,28
            Syntax.expandRange(data(3)).foreach((i: Int) =>
              model += s"${Syntax.SUBJECT_VLAN}=$vlan=${Syntax.ADJECTIVE_TAGGED}=$i" -> "yes"
            )
          case (Syntax.VERB_ADD, Syntax.ADJECTIVE_UNTAGGED) =>
            // config vlan v78 add untagged 22,24-25
            Syntax.expandRange(data(3)).foreach((i: Int) =>
              model += s"${Syntax.SUBJECT_VLAN}=$vlan=${Syntax.ADJECTIVE_UNTAGGED}=$i" -> "yes"
            )
          case _ =>
        }
    }
  }

}