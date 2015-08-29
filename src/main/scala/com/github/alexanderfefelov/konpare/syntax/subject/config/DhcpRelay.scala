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

object DhcpRelay extends Subject {

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    data.head match {
      case Syntax.PARAMETER_PORTS =>
        data(3) match {
          case Syntax.VALUE_ENABLE | Syntax.VALUE_DISABLE =>
            // config dhcp_relay ports 1-28 state enable
            Syntax.expandRange(data(1)).foreach((i: Int) =>
              model += s"${Syntax.SUBJECT_DHCP_RELAY}=$i=${data(2)}" -> data(3)
            )
          case _ =>
        }
      case Syntax.PARAMETER_OPTION_82 =>
        // config dhcp_relay option_82 state disable
        // config dhcp_relay option_82 check disable
        // config dhcp_relay option_82 policy replace
        // config dhcp_relay option_82 remote_id default
        // config dhcp_relay option_82 circuit_id default
        model += s"${Syntax.SUBJECT_DHCP_RELAY}=${data.head}=${data(1)}" -> data(2)
      case Syntax.VERB_ADD if data(1) == Syntax.NOUN_IPIF =>
        // config dhcp_relay add ipif System 10.10.10.1
        model += s"${Syntax.SUBJECT_DHCP_RELAY}=${data(3)}" -> data(2)
      case _ =>
    }
  }

}