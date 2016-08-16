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
import com.github.alexanderfefelov.konpare.syntax.{GenericComplement, Syntax, Subject}

object PortSecurity extends Subject {

  override val complements = Map(
    // config port_security log state disable
    Syntax.COMPLEMENT_LOG -> new GenericComplement(s"${Syntax.SUBJECT_PORT_SECURITY}=${Syntax.COMPLEMENT_LOG}"),
    // config port_security system max_learning_addr no_limit
    Syntax.COMPLEMENT_SYSTEM -> new GenericComplement(s"${Syntax.SUBJECT_PORT_SECURITY}=${Syntax.COMPLEMENT_SYSTEM}"),
    // config port_security trap state disable
    Syntax.COMPLEMENT_TRAP -> new GenericComplement(s"${Syntax.SUBJECT_PORT_SECURITY}=${Syntax.COMPLEMENT_TRAP}")
  )

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    data.head match {
      case Syntax.PARAMETER_PORTS =>
        // config port_security ports 1-28 admin_state disable max_learning_addr 1 lock_address_mode DeleteOnTimeout
        Syntax.expandRange(data(1)).foreach( i =>
          Parser.addPairsToModel(data.drop(2), model, s"${Syntax.SUBJECT_PORT_SECURITY}=$i")
        )
      case _ =>
    }
  }

}