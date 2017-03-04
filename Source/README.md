[Deutsch](#beschreibung) | [English](#description)

# Beschreibung
In diesem Ordner befinden sich der Quellcode des Plugins. Der Quellcode wird in mehrere Projekte unterteilt.
Dies liegt unteranderem an der Verwendung von Maven-Dependencies und der Eclipse-Manifest-Dependencies. Stichpunkt hier ist [Tycho](https://eclipse.org/tycho/), hier wird nicht näher darauf eingegangen.
Hier wird kurz die Benutzung der Projekte beschrieben.

### Systemvorraussetzungen:
Es gelten einige Systemvorrausetzungen, um den Quellcode des Plugins bearbeiten zu können.
* Java der Version 1.8 oder höher muss instlliert sein.
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
  1. Das Projekt de.uni.bremen.stummk.psp.jar.dependency bauen enweder mit "maven clean install" oder in Eclipse über Run As -> Maven build
  2. Die Dependencies werden automatisch nach das Projekt de.uni.bremen.stummk.psp/resources/lib kopiert.
  3. Die Libraries müssen noch manuell beim Projekt de.uni.bremen.stummk.psp in die Classpath eingetragen werden:
    * Dazu plugin.xml über einen Doppelklick auswählen
    * Im Editor die Tab "Runtime" auswählen
    * Unter dem Punkt "Classpath" können die Neuen, aus dem Maven-Repository geladene, Libraries dem Projekt hinzugefügt werden.
		
---
# Description
In diesem Ordner befinden sich der Quellcode des Plugins. Der Quellcode wird in mehrere Projekte unterteilt.
Dies liegt unteranderem an der Verwendung von Maven-Dependencies und der Eclipse-Manifest-Dependencies. Stichpunkt hier ist [Tycho](https://eclipse.org/tycho/), hier wird nicht näher darauf eingegangen.
Hier wird kurz die Benutzung der Projekte beschrieben.

### System requirements
Es gelten einige Systemvorrausetzungen, um den Quellcode des Plugins bearbeiten zu können.
* Java der Version 1.8 oder höher muss instlliert sein.
* Maven 3.3.9 oder höher muss installiert sein.
* Eclipse in der Version 4.6 (Neon) mit instlliertem Maven Plugin 
* Um den Quellcode zu bearbeiten kann die Eclipse SDK benutzt werden.
* Wird eine Eclipse IDE benutzt müssen die folgenden Eclipse-Plugins installiert sein:
  * Java Development Tools, JDT
  * Plug-in Development Environment, PDE
  * Alle Projekte in dem Source-Ordner müssen sich in dem gleichen Wurzelverzeichnis befinden
  
### Import the projects
* Die Projekte können in Eclipse über File -> Import -> Existing Maven Projects importiert werden.
* Beim importieren muss nur das de.uni.bremen.stummk.psp.parent ausgewählt werden. Alle anderen Projekte werden automatisch nachgeladen.

### Building projects
* Um das Plugin zu bauen kann das de.uni.bremen.stummk.psp.parent Projekt über das Kontextmenü in Eclipse gebaut werden. 
  * Projekt auswählen -> Run As -> Maven build. Hier die Parameter "clean install" angeben.
* Mit Maven kann das Projekt mit dem Befehl "maven clean install" gebaut werden.
* Wenn das de.uni.bremen.stummk.psp.parent Projekt gebaut wird, wird das Plugin gebaut.
* Das gebaute Plugin kann in de.uni.bremen.stummk.psp/target/ gefunden werden.

### Testing
* Das Projekt de.uni.bremen.stummk.psp.test beinhaltet alle Tests
* Um zu Testen kann über das Kontextmenü: Run as -> JUnit Plug-in Test gewählt werden.

### Maven Dependecies
* Maven Dependencies können in dem Projekt de.uni.bremen.stummk.psp.jar.dependency, wie üblich in der POM, eingetraen werden.
* Um die Dependencies in dem Plugin-Projekt zu verwenden, müssen folgende Schritte gemacht werden:
  1. Das Projekt de.uni.bremen.stummk.psp.jar.dependency bauen enweder mit "maven clean install" oder in Eclipse über Run As -> Maven build
  2. Die Dependencies werden automatisch nach das Projekt de.uni.bremen.stummk.psp/resources/lib kopiert.
  3. Die Libraries müssen noch manuell beim Projekt de.uni.bremen.stummk.psp in die Classpath eingetragen werden:
    * Dazu plugin.xml über einen Doppelklick auswählen
    * Im Editor die Tab "Runtime" auswählen
    * Unter dem Punkt "Classpath" können die Neuen, aus dem Maven-Repository geladene, Libraries dem Projekt hinzugefügt werden.
