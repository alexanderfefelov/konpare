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

package com.github.alexanderfefelov.konpare.syntax.predicate

import com.github.alexanderfefelov.konpare.syntax.{Syntax, Predicate}
import com.github.alexanderfefelov.konpare.syntax.subject.config._

object Config extends Predicate {

  override val subjects = Map(
    Syntax.SUBJECT_ARP_AGING -> ArpAging,
    Syntax.SUBJECT_BANDWIDTH_CONTROL -> BandwidthControl,
    Syntax.SUBJECT_DHCP_LOCAL_RELAY -> DhcpLocalRelay,
    Syntax.SUBJECT_DHCP_RELAY -> DhcpRelay,
    Syntax.SUBJECT_DOS_PREVENTION -> DosPrevention,
    Syntax.SUBJECT_FDB -> Fdb,
    Syntax.SUBJECT_FILTER -> Filter,
    Syntax.SUBJECT_JUMBO_FRAME -> JumboFrame,
    Syntax.SUBJECT_LLDP -> Lldp,
    Syntax.SUBJECT_LOOPDETECT -> LoopDetect,
    Syntax.SUBJECT_PORT_SECURITY -> PortSecurity,
    Syntax.SUBJECT_PORT_VLAN -> PortVlan,
    Syntax.SUBJECT_PORTS -> Ports,
    Syntax.SUBJECT_QINQ -> QInQ,
    Syntax.SUBJECT_SERIAL_PORT -> SerialPort,
    Syntax.SUBJECT_SNTP -> Sntp,
    Syntax.SUBJECT_SYSTEM_SEVERITY -> SystemSeverity,
    Syntax.SUBJECT_TEMPERATURE -> Temperature,
    Syntax.SUBJECT_TERMINAL -> Terminal,
    Syntax.SUBJECT_TRAFFIC -> Traffic,
    Syntax.SUBJECT_TRAFFIC_SEGMENTATION -> TrafficSegmentation,
    Syntax.SUBJECT_VLAN -> Vlan
  )

}