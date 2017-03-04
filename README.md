# PSP-Eclipse
[Deutsch](#beschreibung) | [English](#description)

## Beschreibung

Dieses Projekt ist im Zuge einer Bachelorarbeit entstanden. Es liefert eine Implementierung eines Eclipse-Plugins zur Integration des persönlichen Softwareprozesses. Der persönliche Softwareprozess (PSP) wurde von Watts S. Humphrey entwickelt und soll Softwareentwicklern dabei helfen den eigenen Entwicklungsprozess zu optimieren. PSP basiert auf historischen Daten, deren Erhebung ziemlich aufwendig ist. Dieses Plugin automatisiert die Datenerhebung zum Teil und ermöglicht somit eine einfachere Erfassung der Daten.

Das Plugin unterstützt die Programmiersprache Java. Für weitere Programmiersprachen kann der komplette Funktionsumfang nicht garantiert werden. Es werden folgende Funktionalitäten angeboten:

#### Backup und Datensynchronisation
Die erfassten Daten können bei der Verwendung einer Versionsverwaltung mit verschiedenen Versionen oder auch mit anderen Geräten synchronisert werden. Dafür können die erfassten Daten manuell in die psp.csv Datei gespeichert werden. Diese kann zum Synchronisieren der Daten benutzt werden.

#### Tasks
Tasks stellen Aufgaben in einem Softwareprojekt dar. Beim Anlegen einer Task können hier Schätzwerte zu dieser angegeben werden. Schätzungen zur Zeit, der eingefügten und entfernten Defekte, sowie Anzahl der geschriebenen Codezeilen beim Ausführen der Task.

#### Zeiterfassung
Das Plugin bietet eine automatisierte Zeiterfassung an. Die Zeiterfassung wird gestartet, wenn eine Task ausgeführt wird.

#### Defekterfassung
Defekte können in diesem Plugin manuell hinzugefügt werden. Defekte können entweder einer einzelnen Datei, einem Ordner oder einem ganzen Projekt hinzugefügt werden.

#### Zählen der Codezeilen
Die Anzahl der geschriebenen Codezeilen werden in diesem Plugin automatisch gezählt, wenn eine ausgeführte Task gestoppt wird.

#### Test Report
Der Test-Report stellt die ausgeführten Unit-Tests in einem Softwareprojekt dar. Dieser wird automatisch generiert, wenn Unit-Tests ausgeführt werden. Zur Zeit werden nur Java-Unit Tests unterstützt.

#### Earned Value Tracking
Das Plugin ermöglicht die automatisierte Erfassung des Projektfortschrittes. Die Punktzahl wird beim erstellen einer Task berechnet und beim Abschließen der Task gutgeschrieben.

#### Schedule Plan Generierung
Der Schedule Plan, der eine Wochenübersicht in einem Softwareprojekt darstellt wird komplett automatisch generiert.

#### Berechnungen für Project Plan Summary
Die Project Plan Summary stellt eine Gesamtübersicht über einen Projekt dar. Es wird automatisch generiert und alle Berechnungen werden automatisch ausgeführt.



Das [Benutzerhandbuch](https://github.com/stummk/psp-eclipse/wiki) beschreibt eine nähere Benutzung des Plugins.

Eine gebaute Version kann in dem Ordner [Binary](./Binary) gefunden werden. Nähere Informationen zur Installation und Midnestanforderungen können [hier](./Binary/README.md) entnommen werden. 

Der Quellcode kann in dem Ordner [Source](./Source) gefunden werden. Wie dieser zu Benutzen ist, kann aus der [README](./Source/README.md) entnommen werden.

---

## Description

This project is part of a Bachelor thesis. It provides an implementation of an Eclipse-Plugin to integrate the personal software process. The personal software process (PSP) was developed by Watts S. Humphrey and is intended to help software developers to optimize their own development process. PSP is based on historical data, the capturing of which is quite complex. This plugin partly automates the data capturing and thus enabling simple data acquisition.

The plugin supports the programming language Java. The complete functional range can not be guaranteed for other programming languages. The following functionalities are offered:

#### Backup and data synchronization
The captured data can be synchronized with different devices. For this, the captured data can be stored manually in the psp.csv file. This can be used to synchronize the data.

#### Tasks
Tasks represent tasks in a software project. When creating a task, you can specify estimated values for this task. Estimation of the spend time, inserted and removed defects, as well as the number of written code lines when the task is executed.

#### Time recording
The plugin provides an automated time recording. The time recording is started when a task is executed.

#### Defect detection
Defects can be added manually in this plug-in. Defects can be added to either a single file, a folder, or an entire project.

#### Counting code lines
The number of written code lines is automatically counted in this plugin when a running task is stopped.

#### Test Report
The test report represents the executed unit tests in a software project. This is generated automatically when unit tests are run. Currently, only Java unit tests are supported.

#### Earned Value Tracking
The plug-in enables the automated recording of project progress. The score is calculated when a task is created and credited when the task is completed.

#### Schedule plan generation
The Schedule plan, which is a weekly overview in a software project, is generated completely automatically.

#### Calculations for Project Plan Summary
The Project Plan Summary provides a general overview of a project. It is automatically generated and all calculations are executed automatically.




The [User manual](https://github.com/stummk/psp-eclipse/wiki) describes a closer use of the plugin.

A built version can be found in the folder [Binary](./Binary). More detailed information on installation and mid-term requirements can be found [here](./Binary/README.md).

Sourcecode can be found in the folder [Source](./Source). How to use this can be found in the [README](./Source/README.md).
