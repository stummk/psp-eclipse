[Deutsch](#beschreibung) | [English](#description)

# Beschreibung
In diesem Ordner befinden sich der Quellcode des Plugins. Der Quellcode wird in mehrere Projekte unterteilt.
Dies liegt unteranderem an der Verwendung von Maven-Dependencies und der Eclipse-Manifest-Dependencies. Stichpunkt hier ist [Tycho](https://eclipse.org/tycho/), hier wird nicht näher darauf eingegangen.
Hier wird kurz die Benutzung der Projekte beschrieben.

### Systemvorraussetzungen:
Es gelten einige Systemvorrausetzungen, um den Quellcode des Plugins bearbeiten zu können.
* Java der Version 1.8 oder höher muss installiert sein.
* Maven 3.3.9 oder höher muss installiert sein.
* Eclipse in der Version 4.6 (Neon) mit instlliertem Maven Plugin 
* Um den Quellcode zu bearbeiten kann die Eclipse SDK benutzt werden.
* Wird eine Eclipse IDE benutzt müssen die folgenden Eclipse-Plugins installiert sein:
  * Java Development Tools, JDT
  * Plug-in Development Environment, PDE
  * Alle Projekte in dem Source-Ordner müssen sich in dem gleichen Wurzelverzeichnis befinden

### Importieren der Projekte:
* Die Projekte können in Eclipse über File -> Import -> Existing Maven Projects importiert werden.
* Beim importieren muss nur das de.uni.bremen.stummk.psp.parent ausgewählt werden. Alle anderen Projekte werden automatisch nachgeladen.

### Bauen der Projekte:
* Um das Plugin zu bauen kann das de.uni.bremen.stummk.psp.parent Projekt über das Kontextmenü in Eclipse gebaut werden. 
  * Projekt auswählen -> Run As -> Maven build. Hier die Parameter "clean install" angeben.
* Mit Maven kann das Projekt mit dem Befehl "maven clean install" gebaut werden.
* Wenn das de.uni.bremen.stummk.psp.parent Projekt gebaut wird, wird das Plugin gebaut.
* Das gebaute Plugin kann in de.uni.bremen.stummk.psp/target/ gefunden werden.

### Testen:
* Das Projekt de.uni.bremen.stummk.psp.test beinhaltet alle Tests
* Um zu Testen kann über das Kontextmenü: Run as -> JUnit Plug-in Test gewählt werden.

### Maven Dependecies:
* Maven Dependencies können in dem Projekt de.uni.bremen.stummk.psp.jar.dependency, wie üblich in der POM, eingetraen werden.
* Um die Dependencies in dem Plugin-Projekt zu verwenden, müssen folgende Schritte gemacht werden:
  1. Das Projekt de.uni.bremen.stummk.psp.jar.dependency bauen entweder mit "maven clean install" oder in Eclipse über Run As -> Maven build
  2. Die Dependencies werden automatisch nach das Projekt de.uni.bremen.stummk.psp/resources/lib kopiert.
  3. Die Libraries müssen noch manuell beim Projekt de.uni.bremen.stummk.psp in die Classpath eingetragen werden:
    * Dazu plugin.xml über einen Doppelklick auswählen
    * Im Editor die Tab "Runtime" auswählen
    * Unter dem Punkt "Classpath" können die Neuen, aus dem Maven-Repository geladene, Libraries dem Projekt hinzugefügt werden.
		
---
# Description
In this folder are the source code of the plugin. The source code is divided into several projects. This is due to the use of Maven Dependencies and the Eclipse Manifest Dependencies. Key point here is [Tycho](https://eclipse.org/tycho/). No further details are given here. The use of the projects is briefly described here.

### System requirements
There are some system prerequisites for editing the source code of the plugin.
* Java version 1.8 or higher must be installed.
* Maven 3.3.9 or later must be installed.
* Eclipse version 4.6 (neon) with Maven plugin installed
* The Eclipse SDK can be used to edit the source code.
* If Eclipse IDE is used, the following Eclipse plugins must be installed:
  * Java Development Tools, JDT
  * Plug-in Development Environment, PDE
  * All projects in the Source folder must be in the same root directory
  
### Import the projects
* The projects can be imported in Eclipse: File -> Import -> Existing Maven Projects.
* When importing, only the de.uni.bremen.stummk.psp.parent has to be selected. All other projects will be imported automatically.

### Building projects
* To build the plugin, the de.uni.bremen.stummk.psp.parent project can be built via the context menu in Eclipse. 
  * Select Project -> Run As -> Maven build. Enter "clean install" as parameters.
* With Maven, the project can be built with the command "maven clean install".
* If the de.uni.bremen.stummk.psp.parent project is built, the plugin is built as well.
* The built plugin can be found in de.uni.bremen.stummk.psp/target/.

### Testing
* The project de.uni.bremen.stummk.psp.test contains all the tests
* To test, use the context menu: Run as -> JUnit Plug-in Test.

### Maven Dependecies
* Maven dependencies can be entered into the project de.uni.bremen.stummk.psp.jar.dependency, as usual in the POM.
* To use the dependencies in the plugin project, the following steps must be taken:
  1. The project de.uni.bremen.stummk.psp.jar.dependency can be built either with "maven clean install" or in Eclipse via Run As -> Maven build
  2. The dependencies are automatically copied to the project de.uni.bremen.stummk.psp/resources/lib.
  3. The libraries still have to be entered manually into the classpath at the project de.uni.bremen.stummk.psp:
    * To do this, double-click plugin.xml
    * Select the "Runtime" tab in the editor
    * Under "Classpath" the new libraries loaded from the Maven repository can be added to the project.
