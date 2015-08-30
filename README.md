__konpare__ анализирует конфигурационные файлы коммутаторов D-Link и предупреждает об обнаружении в них следующих проблем:

* port-based loopdetect
* абонент и магистраль в одном порту
* абонентские порты без filter dhcp_server
* абонентские порты без loopdetect
* абонентские порты без port_security
* абонентские порты с lldp
* включен dhcp_local_relay
* выключен address_binding trap_log
* выключен filter dhcp_server trap_log
* выключен lldp
* выключен loopdetect
* выключен loopdetect log
* выключен password_encryption
* выключен port_security trap_log/port_security log
* выключен snmp
* выключен sntp
* выключен syslog
* магистральные порты без lldp
* магистральные порты с dhcp_relay
* магистральные порты с filter dhcp_server
* магистральные порты с loopdetect
* название VLAN не соответствует образцу
* не указан правильный SNTP-сервер
* не указан правильный syslog-сервер
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

## Ограничения и известные проблемы

### Стек

Не поддерживается анализ конфигурационных файлов коммутаторов, собранных в стек.

### Описание порта

Коммутатор DGS-3620-28TC H/W A1 F/W Build 2.63.B005 не обрамляет кавычками значение параметра `description` команды `config ports`, если значение содержит пробел(ы). При обработке таких значений будет выдано сообщене об ошибке:

    Exception 'java.lang.IndexOutOfBoundsException: 1' thrown while processing config ports ...

## Лицензия

Copyright (C) 2015 Alexander Fefelov <https://github.com/alexanderfefelov>

This program comes with ABSOLUTELY NO WARRANTY; see LICENSE file for details.

This is free software, and you are welcome to redistribute it under certain conditions; see LICENSE file for details.