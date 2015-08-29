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
import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject, Complement}

object Traffic extends Subject {

  override val complements = Map(
    Syntax.COMPLEMENT_CONTROL -> Control
  )

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    // config traffic trap none
    Parser.addPairsToModel(data, model, s"${Syntax.SUBJECT_TRAFFIC}")
  }

  object Control extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
      data.head.replaceAll("""\d:""", "") match { // Hack for stack. Uh
        case Syntax.RE_RANGE(range) =>
          // config traffic control 1:1 broadcast disable multicast disable unicast disable action drop broadcast_threshold 131072 multicast_threshold 131072 unicast_threshold 131072 countdown 0 time_interval 5
          Syntax.expandRange(data.head).foreach((i: Int) =>
            Parser.addPairsToModel(data.tail, model, s"${Syntax.SUBJECT_TRAFFIC}=${Syntax.COMPLEMENT_CONTROL}=$i")
          )
        case Syntax.COMPLEMENT_LOG =>
          // config traffic control log state enable
          Parser.addPairsToModel(data.tail, model, s"${Syntax.SUBJECT_TRAFFIC}=${Syntax.COMPLEMENT_CONTROL}=${Syntax.COMPLEMENT_LOG}")
        case _ =>
          // config traffic control auto_recover_time 0
          Parser.addPairsToModel(data, model, s"${Syntax.SUBJECT_TRAFFIC}=${Syntax.COMPLEMENT_CONTROL}")
      }
    }
  }

}