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

package com.github.alexanderfefelov.konpare

import java.net.InetAddress

import com.github.alexanderfefelov.konpare.syntax.Syntax

object Analyzer {

  def analyze(conf: Conf, model: collection.mutable.Map[String, String]) = {

    val enabledPorts = cut(model, s"${Syntax.SUBJECT_PORTS}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE)
    Out.info("enabled ports", enabledPorts)

    val disabledPorts = cutNot(model, s"${Syntax.SUBJECT_PORTS}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE)
    Out.info("disabled ports", disabledPorts)

    val trunkPorts = cut(model, s"${Syntax.SUBJECT_VLAN}=.*=${Syntax.ADJECTIVE_TAGGED}=(\\d+)", "yes").intersect(enabledPorts)
    Out.info("trunk ports", trunkPorts)

    val accessPorts = cut(model, s"${Syntax.SUBJECT_VLAN}=.*=${Syntax.ADJECTIVE_UNTAGGED}=(\\d+)", "yes").intersect(enabledPorts)
    Out.info("access ports", accessPorts)

    val vlanTags = model.filterKeys(_ matches s"${Syntax.SUBJECT_VLAN}=(.*)=${Syntax.PARAMETER_TAG}").values.toList
    Out.info("vlan tags", vlanTags)

    val mixedPorts = accessPorts.intersect(trunkPorts)
    Out.warning("mixed ports", mixedPorts)

    // bandwidth_control
    //
    val rxLimitedPorts = cutNot(model, s"${Syntax.SUBJECT_BANDWIDTH_CONTROL}=(\\d+)=${Syntax.PARAMETER_RX_RATE}", Syntax.VALUE_NO_LIMIT)
    Out.warning("ports with manual rx rate", rxLimitedPorts)
    val txLimitedPorts = cutNot(model, s"${Syntax.SUBJECT_BANDWIDTH_CONTROL}=(\\d+)=${Syntax.PARAMETER_TX_RATE}", Syntax.VALUE_NO_LIMIT)
    Out.warning("ports with manual tx rate", txLimitedPorts)

    // flow_control
    //
    val portsWithFlowControl = cutNot(model, s"${Syntax.SUBJECT_PORTS}=(\\d+)=${Syntax.PARAMETER_FLOW_CONTROL}", Syntax.VALUE_DISABLE)
    Out.warning("ports with flow control", portsWithFlowControl)

    // vlan names
    //
    if (conf.vlanNameRegex.regex.nonEmpty) {
      val invalidVlanNames = cut(model, s"${Syntax.SUBJECT_VLAN}=(.*)=${Syntax.PARAMETER_TAG}", ".*")
        .filter(! _.matches(conf.vlanNameRegex.regex))
      Out.warning("invalid vlan names", invalidVlanNames)
    }

    // snmp
    //
    model.get(s"feature=${Syntax.SUBJECT_SNMP}") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("snmp disabled")
    }
    if (conf.snmpReadRegex.regex.nonEmpty) {
      val invalidCommunities = cut(model, s"${Syntax.SUBJECT_SNMP}=${Syntax.COMPLEMENT_COMMUNITY}=(.*)=${Syntax.VALUE_READ_ONLY}", Syntax.VALUE_ENABLE)
        .filter(! _.matches(conf.snmpReadRegex.regex))
      Out.warning("invalid snmp read communities", invalidCommunities)
    }
    if (conf.snmpWriteRegex.regex.nonEmpty) {
      val invalidCommunities = cut(model, s"${Syntax.SUBJECT_SNMP}=${Syntax.COMPLEMENT_COMMUNITY}=(.*)=${Syntax.VALUE_READ_WRITE}", Syntax.VALUE_ENABLE)
        .filter(! _.matches(conf.snmpWriteRegex.regex))
      Out.warning("invalid snmp write communities", invalidCommunities)
    }

    // syslog
    //
    model.get(s"feature=${Syntax.SUBJECT_SYSLOG}") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("syslog disabled")
    }
    if (conf.syslogServers.nonEmpty) {
      val pattern = s"${Syntax.SUBJECT_SYSLOG}=${Syntax.COMPLEMENT_HOST}=(\\d+)=${Syntax.PARAMETER_IPADDRESS}"
      val servers = model.filterKeys(_ matches pattern).values.toList.map(InetAddress.getByName)
      val validServers = servers.intersect(conf.syslogServers)
      if (validServers.isEmpty) {
        Out.warning("valid syslog servers not found")
      }
      val invalidServers = servers.filterNot(conf.syslogServers contains _).map(_.getHostAddress)
      Out.warning("invalid syslog servers", invalidServers)
    }

    // sntp
    //
    model.get(s"feature=${Syntax.SUBJECT_SNTP}") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("sntp disabled")
    }
    if (conf.sntpServers.nonEmpty) {
      val pattern = s"${Syntax.SUBJECT_SNTP}=(${Syntax.PARAMETER_PRIMARY}|${Syntax.PARAMETER_SECONDARY})"
      val servers = model.filterKeys(_ matches pattern).values.toList.map(InetAddress.getByName)
      val validServers = servers.intersect(conf.sntpServers)
      if (validServers.isEmpty) {
        Out.warning("valid SNTP servers not found")
      }
      val invalidServers = servers.filterNot(conf.sntpServers contains _).map(_.getHostAddress)
      Out.warning("invalid SNTP servers", invalidServers)
    }

    // loopdetect
    //
    model.get(s"feature=${Syntax.SUBJECT_LOOPDETECT}") match {
      case Some(Syntax.VALUE_ENABLE) =>
        model.get(s"${Syntax.SUBJECT_LOOPDETECT}=${Syntax.PARAMETER_MODE}") match {
          case Some(Syntax.VALUE_PORT_BASED) =>
            Out.warning("port-based loopdetect")
          case _ =>
        }
        model.get(s"${Syntax.SUBJECT_LOOPDETECT}=${Syntax.COMPLEMENT_LOG}=${Syntax.PARAMETER_STATE}") match {
          case Some(Syntax.VALUE_ENABLE) =>
          case _ =>
            Out.warning("loopdetect log disabled")
        }
        val trunkPortsWithLoopdetect = cutNot(model, s"${Syntax.SUBJECT_LOOPDETECT}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
        Out.warning("trunk ports with loopdetect", trunkPortsWithLoopdetect)
        val accessPortsWithoutLoopdetect = cutNot(model, s"${Syntax.SUBJECT_LOOPDETECT}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
        Out.warning("access ports without loopdetect", accessPortsWithoutLoopdetect)
      case _ =>
        Out.warning("loopdetect disabled")
    }

    // lldp
    //
    model.get(s"feature=${Syntax.SUBJECT_LLDP}") match {
      case Some(Syntax.VALUE_ENABLE) =>
        val trunkPortsWithoutLldp = cutNot(model, s"${Syntax.SUBJECT_LLDP}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
        Out.warning("trunk ports without lldp", trunkPortsWithoutLldp)
        val accessPortsWithLldp = cut(model, s"${Syntax.SUBJECT_LLDP}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
        Out.warning("access ports with lldp", accessPortsWithLldp)
      case _ =>
        Out.warning("lldp disabled")
    }

    // dhcp_local_relay
    //
    model.get(s"feature=${Syntax.SUBJECT_DHCP_LOCAL_RELAY}") match {
      case Some(Syntax.VALUE_ENABLE) =>
        Out.warning("dhcp_local_relay enabled")
      case _ =>
    }

    // dhcp_relay
    //
    model.get(s"feature=${Syntax.SUBJECT_DHCP_RELAY}") match {
      case Some(Syntax.VALUE_ENABLE) =>
        val trunkPortsWithDhcpRelay = cut(model, s"${Syntax.SUBJECT_DHCP_RELAY}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
        Out.warning("trunk ports with dhcp_relay", trunkPortsWithDhcpRelay)
      case _ =>
    }

    // filter dhcp_server
    //
    val trunkPortsWithFilterDhcpServer = cut(model, s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
    Out.warning("trunk ports with filter dhcp_server", trunkPortsWithFilterDhcpServer)
    val accessPortsWithoutFilterDhcpServer = cutNot(model, s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
    Out.warning("access ports without filter dhcp_server", accessPortsWithoutFilterDhcpServer)
    (model.get(s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=${Syntax.COMPLEMENT_TRAP_LOG}"),
      model.get(s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=${Syntax.COMPLEMENT_LOG}")) match {
      case (None, None) | (Some(Syntax.VALUE_DISABLE), None) | (None, Some(Syntax.VALUE_DISABLE)) =>
        Out.warning("filter dhcp_server trap_log/filter dhcp_server log disabled")
      case _ =>
    }

    // password_encryption
    //
    model.get(s"feature=${Syntax.SUBJECT_PASSWORD_ENCRYPTION}") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("password_encryption disabled")
    }

    // address_binding
    //
    model.get(s"feature=${Syntax.SUBJECT_ADDRESS_BINDING}=${Syntax.COMPLEMENT_TRAP_LOG}") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("address_binding trap_log disabled")
    }

    // port_security
    //
    (model.get(s"feature=${Syntax.SUBJECT_PORT_SECURITY}=${Syntax.COMPLEMENT_TRAP_LOG}"),
      model.get(s"${Syntax.SUBJECT_PORT_SECURITY}=${Syntax.COMPLEMENT_LOG}=${Syntax.PARAMETER_STATE}")) match {
      case (None, None) | (Some(Syntax.VALUE_DISABLE), None) | (None, Some(Syntax.VALUE_DISABLE)) =>
        Out.warning("port_security trap_log/port_security log disabled")
      case _ =>
        val accessPortsWithoutPortSecurity = cutNot(model, s"${Syntax.SUBJECT_PORT_SECURITY}=(\\d+)=${Syntax.PARAMETER_ADMIN_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
        Out.warning("access ports without port_security", accessPortsWithoutPortSecurity)
    }

  }

  private def cut(model: collection.mutable.Map[String, String], keyPattern: String, valueFilter: String) = {
    val r = keyPattern.r
    model
      .filterKeys(_ matches keyPattern)
      .filter(_._2 matches valueFilter)
      .keys
      .map(_ match { case r(g) => g })
      .toList
  }

  private def cutNot(model: collection.mutable.Map[String, String], keyPattern: String, valueFilter: String) = {
    val r = keyPattern.r
    model
      .filterKeys(_ matches keyPattern)
      .filterNot(_._2 matches valueFilter)
      .keys
      .map(_ match { case r(g) => g })
      .toList
  }

}