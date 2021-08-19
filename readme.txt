Hinweise:

- Das Projekt ist im zur Verfügung gestellten Zustand nicht vollständig ausführbar
- Die Klassen für den Aufbau der Connection sind als SingletonFactory realisiert, so dass Sie immer nur eine Instanz der Datenbankverbindung offen haben. Den Aufruf dieser Klassen können Sie im Konstruktor des MovieCompanyDAOImpl sehen.
- ggf. müssen Sie den Oracle-JDBC-Treiber noch der Library in netbeans hinzufügen, falls netbeans diesen nach dem Import nicht findet.
