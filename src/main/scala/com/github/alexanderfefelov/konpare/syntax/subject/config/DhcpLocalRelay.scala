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

object DhcpLocalRelay extends Subject {

  override val complements = Map(
    Syntax.COMPLEMENT_OPTION_82 -> Option82
  )

  object Option82 extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
      data.head match {
        case Syntax.PARAMETER_PORTS =>
          // config dhcp_local_relay option_82 ports 1-28 policy keep
          Syntax.expandRange(data(1)).foreach((i: Int) =>
            Parser.addPairsToModel(data.drop(2), model, s"${Syntax.SUBJECT_DHCP_LOCAL_RELAY}=${Syntax.COMPLEMENT_OPTION_82}=$i")
          )
        case _ =>
          // config dhcp_local_relay option_82 circuit_id default
          // config dhcp_local_relay option_82 remote_id default
          // Hack for:
          // config dhcp_local_relay option_82 remote_id user_define 00-12-34-56-78-90
          val filteredData = data
            .filterNot(_ == Syntax.VALUE_USER_DEFINE) // Hack
          Parser.addPairsToModel(filteredData, model, s"${Syntax.SUBJECT_DHCP_LOCAL_RELAY}=${Syntax.COMPLEMENT_OPTION_82}")
      }
    }
  }

}