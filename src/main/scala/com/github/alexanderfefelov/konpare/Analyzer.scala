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

    val enabledPorts = getPorts(model, s"${Syntax.SUBJECT_PORTS}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE)
    Out.info("enabled ports", enabledPorts)

    val disabledPorts = getPortsNot(model, s"${Syntax.SUBJECT_PORTS}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE)
    Out.info("disabled ports", disabledPorts)

    val trunkPorts = getPorts(model, s"${Syntax.SUBJECT_VLAN}=.*=${Syntax.ADJECTIVE_TAGGED}=(\\d+)", "yes").intersect(enabledPorts)
    Out.info("trunk ports", trunkPorts)

    val accessPorts = getPorts(model, s"${Syntax.SUBJECT_VLAN}=.*=${Syntax.ADJECTIVE_TAGGED}=(\\d+)", "yes").intersect(enabledPorts)
    Out.info("access ports", accessPorts)

    val mixedPorts = accessPorts.intersect(trunkPorts)
    Out.warning("mixed ports", mixedPorts)

    // vlan names
    //
    if (conf.vlanNameRegex.regex.nonEmpty) {
      val pattern = s"${Syntax.SUBJECT_VLAN}=(.*)=${Syntax.PARAMETER_TAG}"
      val r = pattern.r
      val invalidVlanNames = model
        .keys
        .filter(_ matches pattern)
        .map(_ match { case r(g) => g })
        .filter(! _.matches(conf.vlanNameRegex.regex))
        .toList
      Out.warning("invalid vlan names", invalidVlanNames)
    }

    // snmp
    //
    model.get(s"feature=${Syntax.SUBJECT_SNMP}") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("snmp disabled")
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
        val trunkPortsWithLoopdetect = getPortsNot(model, s"${Syntax.SUBJECT_LOOPDETECT}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
        Out.warning("trunk ports with loopdetect", trunkPortsWithLoopdetect)
        val accessPortsWithoutLoopdetect = getPortsNot(model, s"${Syntax.SUBJECT_LOOPDETECT}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
        Out.warning("access ports without loopdetect", accessPortsWithoutLoopdetect)
      case _ =>
        Out.warning("loopdetect disabled")
    }

    // lldp
    //
    model.get(s"feature=${Syntax.SUBJECT_LLDP}") match {
      case Some(Syntax.VALUE_ENABLE) =>
        val trunkPortsWithoutLldp = getPortsNot(model, s"${Syntax.SUBJECT_LLDP}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
        Out.warning("trunk ports without lldp", trunkPortsWithoutLldp)
        val accessPortsWithLldp = getPorts(model, s"${Syntax.SUBJECT_LLDP}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
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
        val trunkPortsWithDhcpRelay = getPorts(model, s"${Syntax.SUBJECT_DHCP_RELAY}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
        Out.warning("trunk ports with dhcp_relay", trunkPortsWithDhcpRelay)
      case _ =>
    }

    // filter dhcp_server
    //
    val trunkPortsWithFilterDhcpServer = getPorts(model, s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(trunkPorts)
    Out.warning("trunk ports with filter dhcp_server", trunkPortsWithFilterDhcpServer)
    val accessPortsWithoutFilterDhcpServer = getPortsNot(model, s"${Syntax.SUBJECT_FILTER}=${Syntax.COMPLEMENT_DHCP_SERVER}=(\\d+)=${Syntax.PARAMETER_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
    Out.warning("access ports without filter dhcp_server", accessPortsWithoutFilterDhcpServer)
    model.get(s"${Syntax.SUBJECT_FILTER}=dhcp_server=trap_log") match {
      case Some(Syntax.VALUE_ENABLE) =>
      case _ =>
        Out.warning("filter dhcp_server trap_log disabled")
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
        val accessPortsWithoutPortSecurity = getPortsNot(model, s"${Syntax.SUBJECT_PORT_SECURITY}=(\\d+)=${Syntax.PARAMETER_ADMIN_STATE}", Syntax.VALUE_ENABLE).intersect(accessPorts)
        Out.warning("access ports without port_security", accessPortsWithoutPortSecurity)
    }

  }

  private def getPorts(model: collection.mutable.Map[String, String], pattern: String, filter: String) = {
    val r = pattern.r
    model
      .filterKeys(_ matches pattern)
      .filter(_._2 matches filter)
      .keys
      .map(_ match { case r(g) => g }).toList
  }

  private def getPortsNot(model: collection.mutable.Map[String, String], pattern: String, filter: String) = {
    val r = pattern.r
    model
      .filterKeys(_ matches pattern)
      .filterNot(_._2 matches filter)
      .keys
      .map(_ match { case r(g) => g }).toList
  }

}