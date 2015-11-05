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

package com.github.alexanderfefelov.konpare.syntax

import com.github.alexanderfefelov.konpare.syntax.predicate._

object Syntax {

  final val FEATURE = "feature"
  final val FW = "f/w"
  final val MODEL = "model"

  final val PREDICATE_CONFIG = "config"
  final val PREDICATE_CREATE = "create"
  final val PREDICATE_DEBUG = "debug"
  final val PREDICATE_DISABLE = "disable"
  final val PREDICATE_ENABLE = "enable"

  final val SUBJECT_ADDRESS_BINDING = "address_binding"
  final val SUBJECT_ARP_AGING = "arp_aging"
  final val SUBJECT_BANDWIDTH_CONTROL = "bandwidth_control"
  final val SUBJECT_CLIPAGING = "clipaging"
  final val SUBJECT_COMMAND = "command"
  final val SUBJECT_CONFIG = "config"
  final val SUBJECT_DHCP_LOCAL_RELAY = "dhcp_local_relay"
  final val SUBJECT_DHCP_RELAY = "dhcp_relay"
  final val SUBJECT_DOS_PREVENTION = "dos_prevention"
  final val SUBJECT_FDB = "fdb"
  final val SUBJECT_FILTER = "filter"
  final val SUBJECT_GVRP = "gvrp"
  final val SUBJECT_JUMBO_FRAME = "jumbo_frame"
  final val SUBJECT_LLDP = "lldp"
  final val SUBJECT_LOOPDETECT = "loopdetect"
  final val SUBJECT_MIRROR = "mirror"
  final val SUBJECT_PASSWORD = "password"
  final val SUBJECT_PASSWORD_RECOVERY = "password_recovery"
  final val SUBJECT_PORT_SECURITY = "port_security"
  final val SUBJECT_PORT_VLAN = "port_vlan"
  final val SUBJECT_PORTS = "ports"
  final val SUBJECT_QINQ = "qinq"
  final val SUBJECT_SERIAL_PORT = "serial_port"
  final val SUBJECT_SFLOW = "sflow"
  final val SUBJECT_SNMP = "snmp"
  final val SUBJECT_SNTP = "sntp"
  final val SUBJECT_SSL = "ssl"
  final val SUBJECT_SYSLOG = "syslog"
  final val SUBJECT_SYSTEM_SEVERITY = "system_severity"
  final val SUBJECT_TELNET = "telnet"
  final val SUBJECT_TEMPERATURE = "temperature"
  final val SUBJECT_TERMINAL = "terminal"
  final val SUBJECT_TRAFFIC = "traffic"
  final val SUBJECT_TRAFFIC_SEGMENTATION = "traffic_segmentation"
  final val SUBJECT_VLAN = "vlan"
  final val SUBJECT_VLAN_TRUNK = "vlan_trunk"
  final val SUBJECT_WEB = "web"

  final val COMPLEMENT_COMMUNITY = "community"
  final val COMPLEMENT_CONTROL = "control"
  final val COMPLEMENT_DHCP_SERVER = "dhcp_server"
  final val COMPLEMENT_DHCP_SNOOP = "dhcp_snoop"
  final val COMPLEMENT_ENCRYPTION = "encryption"
  final val COMPLEMENT_EXTENSIVE_NETBIOS = "extensive_netbios"
  final val COMPLEMENT_HOST = "host"
  final val COMPLEMENT_LOG = "log"
  final val COMPLEMENT_NETBIOS = "netbios"
  final val COMPLEMENT_OPTION_82 = "option_82"
  final val COMPLEMENT_RPC_PORTMAPPER = "rpc_portmapper"
  final val COMPLEMENT_SYSTEM = "system"
  final val COMPLEMENT_THRESHOLD = "threshold"
  final val COMPLEMENT_TRAP = "trap"
  final val COMPLEMENT_TRAP_LOG = "trap_log"
  final val COMPLEMENT_VLAN_LEARNING = "vlan_learning"

  final val PARAMETER_ADMIN_STATE = "admin_state"
  final val PARAMETER_ADMIN_STATUS = "admin_status"
  final val PARAMETER_DOS_TYPE = "dos_type"
  final val PARAMETER_FLOW_CONTROL = "flow_control"
  final val PARAMETER_IPADDRESS = "ipaddress"
  final val PARAMETER_MESSAGE_TX_HOLD_MULTIPLIER = "message_tx_hold_multiplier"
  final val PARAMETER_MESSAGE_TX_INTERVAL = "message_tx_interval"
  final val PARAMETER_MODE = "mode"
  final val PARAMETER_NOTIFICATION_INTERVAL = "notification_interval"
  final val PARAMETER_OPTION_82 = "option_82"
  final val PARAMETER_PORTS = "ports"
  final val PARAMETER_PRIMARY = "primary"
  final val PARAMETER_REINIT_DELAY = "reinit_delay"
  final val PARAMETER_RX_RATE = "rx_rate"
  final val PARAMETER_SECONDARY = "secondary"
  final val PARAMETER_STATE = "state"
  final val PARAMETER_TAG = "tag"
  final val PARAMETER_TX_DELAY = "tx_delay"
  final val PARAMETER_TX_RATE = "tx_rate"
  final val PARAMETER_VLANID = "vlanid"

  final val VALUE_ALL = "all"
  final val VALUE_DEFAULT = "default"
  final val VALUE_DISABLE = "disable"
  final val VALUE_ENABLE = "enable"
  final val VALUE_NO_LIMIT = "no_limit"
  final val VALUE_PORT_BASED = "port-based"
  final val VALUE_READ_ONLY = "read_only"
  final val VALUE_READ_WRITE = "read_write"
  final val VALUE_USER_DEFINE = "user_define"

  final val VERB_ADD = "add"
  final val VERB_CLEAR_DESCRIPTION = "clear_description"
  final val VERB_DELETE = "delete"

  final val NOUN_IPIF = "ipif"

  final val ADJECTIVE_TAGGED = "tagged"
  final val ADJECTIVE_UNTAGGED = "untagged"

  final val RE_COMMENT = "^([#!].*)".r
  final val RE_FW = ".*Firmware: Build ([0-9]+.[0-9]+.[0-9A-Z]+)".r
  final val RE_MODEL = ".*(D[EG]S-[0-9]{4}[-\\/0-9A-Z]*+)".r
  final val RE_RANGE = """(?:\d+(?:-\d+)?)(?:,(?:\d+(-\d+)?))*""".r

  val predicates = Map(
    PREDICATE_CONFIG -> Config,
    PREDICATE_CREATE -> Create,
    PREDICATE_DEBUG -> Debug,
    PREDICATE_DISABLE -> Disable,
    PREDICATE_ENABLE -> Enable
  )

  def expandRange(range: String) = {
    range.replaceAll(VALUE_ALL, "0").split(',') flatMap expandRangeElement
  }

  private def expandRangeElement(element: String) = {
    val range = """(\d+)-(\d+)""".r
    val number = """(\d+)""".r
    element.replaceAll("""\d:""", "").replaceAll("\\(", "").replaceAll("\\)", "") match { // Hack for stack. Uh
      case range(first, last) => first.toInt to last.toInt toList
      case number(n) => List(n.toInt)
    }
  }

}