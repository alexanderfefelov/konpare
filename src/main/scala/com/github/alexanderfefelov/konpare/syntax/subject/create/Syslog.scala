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

import com.github.alexanderfefelov.konpare.Parser
import com.github.alexanderfefelov.konpare.syntax.{Syntax, Subject, Complement}

object Syslog extends Subject {

  override val complements = Map(
    Syntax.COMPLEMENT_HOST -> Host
  )

  object Host extends Complement {

    override def process(data: List[String], model: collection.mutable.Map[String, String]) = {
      // create syslog host 1 ipaddress 10.10.10.1 severity informational facility local0 udp_port 514 state enable
      val host = data.head
      Parser.addPairsToModel(data.tail, model, s"${Syntax.SUBJECT_SYSLOG}=${Syntax.COMPLEMENT_HOST}=$host")
    }
  }

}