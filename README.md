# Настало время навести порядок!

__konpare__ анализирует конфигурационные файлы коммутаторов D-Link и предупреждает об обнаружении в них следующих проблем:

* `port-based` `loopdetect`
* SNMP read community не соответствует образцу
* SNMP write community не соответствует образцу
* VLAN без портов
* абонент и магистраль в одном порту
* абонентские порты без `filter dhcp_server`
* абонентские порты без `loopdetect`
* абонентские порты без `port_security`
* абонентские порты в нескольких VLAN
* абонентские порты с `fbpdu`
* абонентские порты с `lldp`
* включен `dhcp_local_relay`
* выключен `address_binding trap_log`
* выключен `filter dhcp_server trap_log`/`filter dhcp_server log`
* выключен `lldp`
* выключен `loopdetect`
* выключен `loopdetect log`
* выключен `password encryption`
* выключен `port_security trap_log`/`port_security log`
* выключен `snmp`
* выключен `sntp`
* выключен `syslog`
* выключен `traffic control log`
* магистральные порты без `lldp`
* магистральные порты с `lldp` с выключенным `basic_tlvs`
* магистральные порты с `lldp` без `system_name` TLV
* магистральные порты с `dhcp_relay`
* магистральные порты с `filter dhcp_server`
* магистральные порты с `loopdetect`
* название VLAN не соответствует образцу
* не указан правильный DHCP relay
* не указан правильный SNTP-сервер
* не указан правильный syslog-сервер
* порты в default VLAN
* порты без VLAN
* порты с `flow_control`
* порты с `traffic control action shutdown`
* порты со значениями `tx_rate` или `rx_rate` не по умолчанию
* указаны неправильные DHCP relay
* указаны неправильные SNTP-серверы
* указаны неправильные syslog-серверы

## Требования

Для компиляции необходимы:

* git <https://git-scm.com/downloads>
* Oracle JDK 8 <http://www.oracle.com/technetwork/java/javase/downloads/index.html>

Для выполнения необходимы:

* Oracle JRE 8 <http://www.oracle.com/technetwork/java/javase/downloads/index.html>

## Компиляция

Для компиляции выполните команды

    git clone https://github.com/alexanderfefelov/konpare.git
    cd konpare
    activator assembly

В случае успеха в каталоге `target\scala-2.11` будет создан файл `konpare.jar`.

## Запуск

Для запуска выполните команду

    java -jar konpare.jar [параметры]

Результаты анализа будут выведены в консоль.

Для получения списка всех параметров выполните команду

    java -jar konpare.jar --help

## Поддерживаемое оборудование

* DES-1228/ME
* DES-3200
* DES-3526
* DGS-3120
* DGS-3420
* DGS-3612G
* DGS-3620

## Ограничения и известные проблемы

### loopdetect log

Коммутаторы DES-1228/ME, DES-3200 H/W A1 и B1 и DES-3526 не поддерживают команду `config loopdetect log state enable`. Для таких коммутаторов будет выведено сообщение:

    WARNING loopdetect log disabled

### traffic control log

Коммутаторы DES-1228/ME, DES-3200 H/W A1 и B1 и DES-3526 не поддерживают команду `config traffic control log state enable`. Для таких коммутаторов будет выведено сообщение:

    WARNING traffic control log disabled

### SNMP

Коммутаторы DES-1228/ME, DES-3200 H/W A1 и B1 и DES-3526 не поддерживают команду `enable snmp`. Для таких коммутаторов будет выведено сообщение:

    WARNING snmp disabled

### Описание порта

Коммутатор DGS-3620-28TC H/W A1 F/W 2.63.B005 не обрамляет кавычками значение параметра `description` команды `config ports`, если значение содержит пробел(ы). При обработке таких значений будет выдано сообщение об ошибке:

    Exception 'java.lang.IndexOutOfBoundsException: 1' thrown while processing config ports ...

### Стек

Не полностью поддерживается анализ конфигурационных файлов коммутаторов, собранных в стек.

## Лицензия

Copyright (C) 2015 Alexander Fefelov <https://github.com/alexanderfefelov>

This program comes with ABSOLUTELY NO WARRANTY; see LICENSE file for details.

This is free software, and you are welcome to redistribute it under certain conditions; see LICENSE file for details.