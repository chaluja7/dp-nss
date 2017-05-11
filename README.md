# Diplomová práce DP-NSS #

Webová aplikace pro vyhledávání spojů veřejné dopravy. Dostupná na https://dp-nss.org.
   
* Dokumentace REST API k dispozici na Apiary
   * http://docs.dpnssapi.apiary.io
   * http://docs.dpnssprivateapi.apiary.io
   
## Konfigurace ##
Jednotlivé moduly obsahují konfigurační soubory ``config.properties``, kde je nutné vždy upravit hodnoty tak, aby odpovídaly aktuální situaci. Pro spouštění testů a lokální build jsou určené konfigurační soubory v ``test/resources`` a pro nasazení aplikace na Apache Tomcat jsou určené konfigurační soubory v ``/config``.

Konfigurační soubory obsahují zejména parametry pro připojení k databázi PostgreSQL a databázím Neo4j. Dále obsahují cesty k adresářům pro ukládání pomocných souborů nutných pro import a export jízdních řádů ve formátu GTFS.

Adresář ``/config`` - respektive pouze jednu jeho konkrétní podsložku určenou pro jedno konkrétní prostředí - je nutné pro nasazení dostat na classpath. Toho je možné docílit například přidáním absolutní cesty k tomuto adresáři do proměnné ``shared.loader`` v souboru ``${TOMCAT_HOME}/conf/catalina.properties``.    
   
### PostgreSQL ###
V databázi ``dp-nss`` musí být vytvořeno schéma ``global`` a schéma pro každý jednotlivý jízdní řád. Databáze a schémata musí být vytvořena před buildem aplikace, tabulky je ale možné nechat vygenerovat Hibernate automaticky nastavením ``hibernate.auto = create`` v souboru ``config.properties``.
 
Tabulky jízdních řádů ale budou vygenerovány pouze do schématu ``public``. Pokud tedy chceme pracovat s jízdním řádem jiného jména (což chceme), je nutné schéma zkopírovat a přejmenovat, k čemuž můžeme využít například aplikaci PgAdmin.
  
Obecně platí, že schémata existující v databázi PostgreSQL musí odpovídat schématům definovaným v souboru ``SchemaThreadLocal.java``. Do tohoto souboru je také nutné každé nově zamýšlené schéma přidat.
   
### Neo4j ###
Pro každý jízdní řád je nutné mít spuštěnou novou instanci Neo4j (verze 3.1.1) na dedikovaných portech, které nastavíme v ``${NEO4J_HOME}/conf/neo4j.conf``. Adresy pro připojení k databázím přes protokol BOLT poté nastavujeme v souboru ``config.properties``.

Každou novou databázi pro nový jízdní řád je také nutné definovat ve třídě ``Neo4jConfig.java`` pod příslušným jménem, které odpovídá názvu jízdního řádu.
   
## Build ##
* Pro správný build je nutné mít nainstalovanou Javu 1.8

* Aplikace je buildovatelná mavenem. Sestavení provedeme příkazem

    ``mvn clean install -DskipTests=true``
    
* Pro build včetně provedení kompletní sady testů je nutné mít v konfiguračních souborech správně nastavené přístupy k databázím PostgreSQL a Neo4j a databáze Neo4j musí běžet.

* Pro správné vykonání testů importu a exportu jízdních řádů je také nutné mít na filesystému dle property ``gtfs.test.in.location`` uložený rozbalený adresář se soubory GTFS balíku dat.     
    
## Deploy ##
* Aplikace testována na Tomcat 8.5.6
* Pro správné nasazení je kromě validního přiřazení souboru ``config.properties`` na classpath (viz výše) nutné nastavit VM options:

    ``-Djavax.servlet.request.encoding=UTF-8``
    
    ``-Dfile.encoding=UTF-8``

    ``-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector``
