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

object Filter extends Subject {

  override val complements = Map(
    Syntax.COMPLEMENT_DHCP_SERVER -> DhcpServer,
    Syntax.COMPLEMENT_EXTENSIVE_NETBIOS -> ExtensiveNetbios,
    Syntax.COMPLEMENT_NETBIOS -> Netbios,
    Syntax.COMPLEMENT_RPC_PORTMAPPER -> RpcPortmapper
  )

  object DhcpServer extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
      data.head match {
        case Syntax.PARAMETER_PORTS =>
          data(3) match {
            case Syntax.VALUE_ENABLE | Syntax.VALUE_DISABLE =>
              // config filter dhcp_server ports all state disable
              Syntax.expandRange(data(1)).foreach( i =>
                model += s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=$i=${data(2)}" -> data(3)
              )
            case _ =>
          }
        case _ =>
          // config filter dhcp_server illegal_server_log_suppress_duration 5min
          // config filter dhcp_server trap disable
          // config filter dhcp_server log disable
          Parser.addPairsToModel(data, model, s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}")
      }
    }

  }

  object ExtensiveNetbios extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
    }

  }

  object Netbios extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
    }

  }

  object RpcPortmapper extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
    }

  }

}