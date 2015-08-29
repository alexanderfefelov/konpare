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

object Lldp extends Subject {

  override def process2(data: List[String], model: collection.mutable.Map[String, String]) = {
    data.head match {
      case Syntax.PARAMETER_PORTS =>
        data(3) match {
          case Syntax.VALUE_ENABLE | Syntax.VALUE_DISABLE =>
            // config lldp ports 1-28 notification disable
            Syntax.expandRange(data(1)).foreach((i: Int) =>
              model += s"${Syntax.SUBJECT_LLDP}=$i=${data(2)}" -> data(3)
            )
          case _ if data(2) == Syntax.PARAMETER_ADMIN_STATUS =>
            // config lldp ports 1-28 admin_status tx_and_rx
            Syntax.expandRange(data(1)).foreach((i: Int) =>
              model += s"${Syntax.SUBJECT_LLDP}=$i=${data(2)}" -> data(3)
            )
          case _ =>
            // config lldp ports 26 basic_tlvs port_description system_name system_description system_capabilities enable
        }
      case Syntax.PARAMETER_MESSAGE_TX_INTERVAL | Syntax.PARAMETER_TX_DELAY | Syntax.PARAMETER_MESSAGE_TX_HOLD_MULTIPLIER
           | Syntax.PARAMETER_REINIT_DELAY | Syntax.PARAMETER_NOTIFICATION_INTERVAL =>
        // config lldp message_tx_interval 30
        // config lldp tx_delay 2
        // config lldp message_tx_hold_multiplier 4
        // config lldp reinit_delay 2
        // config lldp notification_interval 5
        model += s"${Syntax.SUBJECT_LLDP}=${data.head}" -> data(1)
      case _ =>
    }
  }

}