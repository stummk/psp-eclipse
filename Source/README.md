In diesem Ordner befinden sich der Quellcode des Plugins. Der Quellcode wird in mehrere Projekte unterteilt.
Dies liegt unteranderem an der Verwendung von Maven-Dependencies und der Eclipse-Manifest-Dependencies.
Eine n�here Erl�uterung dazu kann in der BAchelorarbeit gefunden werden. Hier wird kurz die Bnezung der Projekte beschrieben.

Systemvorraussetzungen:
Es gelten einige Systemvorrausetzungen, um den Quellcode des Plugins bearebiten zu k�nnen.
	- Java der Version 1.8 oder h�her muss instlliert sein.
	- Maven 3.3.9 oder h�her muss installiert sein.
	- Eclipse in der Version 4.6 (Neon) mit instlliertem Maven Plugin 
	  Um den Quellcode zu bearbeiten kann die Eclipse SDK benutzt werden.
	  Wird eine Eclipse IDE benutzt m�ssen die folgenden Eclipse-Plugins installiert sein:
		- Java Development Tools, JDT
		- Plug-in Development Environment, PDE
	- Alle Projekte in dem Source-Ordner m�ssen sich in dem gleichen Wurzelverzeichnis befinden

Importieren der Projekte:
- Die Projekte k�nnen in Eclipse �ber File -> Import -> Existing Maven Projects importiert werden.
- Beim importieren muss nur das de.uni.bremen.stummk.psp.parent ausgew�hlt werden. Alle anderen Projekte werden automatisch nachgeladen.

Bauen der Projekte:
- Um das Plugin zu bauen kann das de.uni.bremen.stummk.psp.parent Projekt �ber das Kontextmen� in Eclipse gebaut werden.
	Projekt ausw�hlen -> Run As -> Maven build. Hier die Parameter "clean install" angeben.
- Mit Maven kann das Projekt mit dem Befehl "maven clean install" gebaut werden.
- Wenn das de.uni.bremen.stummk.psp.parent Projekt gebaut wird, wird das Plugin gebaut.
- Das gebaute Plugin kann in de.uni.bremen.stummk.psp/target/ gefunden werden.

testen:
- Das Projekt de.uni.bremen.stummk.psp.test beinhaltet alle Tests
- Um zu testen kann �ber das Kontextmen�: Run as -> JUnit Plug-in Test gew�hlt werden.

Maven Dependecies:
- Maven Dependencies k�nnen in dem Projekt de.uni.bremen.stummk.psp.jar.dependency, wie �blich in der POM, eingetraen werden.
- Um die Dependencies in dem Plugin-Projekt zu verwenden, m�ssen folgende Schritte gemacht werden:
	1. Das Projekt de.uni.bremen.stummk.psp.jar.dependency bauen enweder mit "maven clean install" oder in Eclipse �ber Run As -> Maven build
	2. Die Dependencies werden automatisch nach das Projekt de.uni.bremen.stummk.psp/resources/lib kopiert.
	3. Die Libraries m�ssen noch manuell beim Projekt de.uni.bremen.stummk.psp in die Classpath eingetragen werden:
		- Dazu plugin.xml �ber einen Doppelklick ausw�hlen
		- Im Editor die Tab "Runtime" ausw�hlen
		- Unter dem Punkt "Classpath" k�nnen die Neuen, aus dem Maven-Repository geladene, Libraries dem Projekt hinzugef�gt werden.