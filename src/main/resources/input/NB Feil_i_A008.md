CDM-fila for A008v44  inneholder to feil.
Den ene resulterer i at fundament-fila for A008 inneholder to definisjoner for samme linje. 
Den andre feilen er at to linje-definisjoner mangler.

Dette har kun konsekvens for pdf-genereringa.

## Merk! Hvis fundament-fila for A008 blir overskrevet må følgende korrigeres manuelt 

### - To definisjoner for samme linje

Denne brukes når pdf-malen blir generert:

    "A008.PurposeofSED" : {
    "ledetekst" : "Formål med SED",
    "verdiType" : "tittel",
    "cdmType" : "_uxFieldSetContainer",
    "indeks" : "3."
    },

Slettes manuelt: Denne er feil og overføldig og brukes ikke:

    "A008.PurposeofSED.NotificationChangesInRelevantData" : {
    "ledetekst" : "Person",
    "verdiType" : "tittel",
    "cdmType" : "_uxHiddenGroupContainer",
    "indeks" : "3."
    },

### - To linje-definisjoner mangler:
Følgende linje-definisjoner må legges inn manuelt.  
Linje 3.1:

    "A008.PurposeofSED.NotificationChangesInRelevantData" : {
    "indeks" : "3.1.",
    "ledetekst" : "Informasjon om endring i relevante data",
    "verdiType" : "_uxFieldSetContainer"
    },

Linje 3.2:

    "A008.PurposeofSED.InformationWorkingInTwoOrMoreMemberStates" : {
    "indeks" : "3.2.",
    "ledetekst" : "Informasjon om arbeid i to eller flere medlemsland",
    "verdiType" : "_uxFieldSetContainer"
    },
