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

import com.github.alexanderfefelov.konpare.Parser
import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject}

object QInQ extends Subject {

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    data.head match {
      case Syntax.PARAMETER_PORTS =>
        // config qinq ports 1-28  missdrop disable outer_tpid 0x88A8
        // config qinq ports 1-28  add_inner_tag disable
        Syntax.expandRange(data(1)).foreach( i =>
          Parser.addPairsToModel(data.drop(2), model, s"${Syntax.SUBJECT_QINQ}=$i")
        )
      case _ =>
        // config qinq inner_tpid 0x8100
        Parser.addPairsToModel(data, model, s"${Syntax.SUBJECT_QINQ}")
    }
  }

}