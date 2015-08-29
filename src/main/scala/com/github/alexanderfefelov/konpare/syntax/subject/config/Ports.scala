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

object Ports extends Subject {

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    // config ports 1-24 speed auto flow_control disable learning enable state enable mdix auto
    // Hack for:
    // config ports 1-19 speed auto  capability_advertised  10_half 10_full 100_half 100_full 1000_full  flow_control disable learning enable state enable mdix auto
    // config ports 1,3,9,19,23 speed auto flow_control disable state disable clear_description
    val filteredData = data.tail
      .dropWhile(_ != Syntax.PARAMETER_FLOW_CONTROL) // Hack
      .filterNot(_ == Syntax.VERB_CLEAR_DESCRIPTION) // Hack
    Syntax.expandRange(data.head).foreach((i: Int) =>
      Parser.addPairsToModel(filteredData, model, s"${Syntax.SUBJECT_PORTS}=$i")
    )
  }

}