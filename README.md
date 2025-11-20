# eux-acl-generator

Et litt misvisende navn - koden genererer ikke bare ACL-maler, men også PDF-maler, kodeverksfiler og test-filer. 
Kort og godt de filene som trengs for å presentere SEDene på ulike formater.
Den kan også relativt enkelt utvides til å automatisk generere SED-dokumentasjon - feks type OpenAPI.

### Bakgrunn
Det kommer nye versjoner av SEDene med ujevne mellomrom. Disse har blitt levert på ulike formater og slik kan det også bli i framtida.
Utfordringa er å håndtere disse ulike leveransene på enkleste måte.

### Løsning
Enhver leveranse konverteres til ett gitt - fundamentalt - filformat. Dette filformatet er så utgangspunkt for å generere samtlige andre filsett.

Når det kommer en ny leveranse blir det da kun en oppgave  å lage koden som konverterer de nye filene til dette fundamentale filsettet. 
Koden for å generere acl-maler, pdf-maler osv med utgangspunkt i det fundamentale filsettet er allerede laget. 

### Om denne dokumentasjonen
Dokumentasjonen tar utgangspunkt i siste leveranse som er CDM v4.4, samt i CDM v4.3 som brukes for å sammenlikne. 
I disse versjonene ble dokumentasjonen av SEDene levert på json-format. 

I v4.1 og v4.2 var SEDene dokumentert på HTML-format. Dette er ikke med i beskrivelsen her. 

Dokumentasjonen gir kun en overordnet beskrivelse av hvordan appen er bygd opp og hvordan den er tenkt brukt.
Kun enkelte deler er beskrevet i detalj. Forhåpentligvis gir dette allikevel en tilstrekkelig bakgrunn for å jobbe med koden.

### Feil i leveransen 
Det er oppdaget feil i CDM-leveransen. Disse er nærmere beskrevet i ei .md-fil under src/main/resources.input

# Arkitektur
Om filsett, filer, hva de inneholder og hvor de er passert.

### Input
Input-filer er plassert under *src/main/resources.input*.

**CDM-filer** \
Ei CDM-fil inneholder en detaljert spesifikasjon av den enkelte SED. 
Disse filene er plassert under *.input.cdm-meta*
Det gjøres et uttrekk fra disse filene som brukes til å lage det fundamentale filsettet, ei fil pr SED-type. 

**Norske tekster** \
Vi har også ei fil med tekster på norsk. Tekstene knyttes til en plassering i CDM-filene med en referansekode.
I tilllegg til å inneholder tekster for v4.4 inneholder den også tekster for v4.1, v4.2 og v4.3. 
Fila er plassert under *.input.cdm-oversettelser*.

**Navn på kodeverksfiler** \
CDM-filene inneholder ikke navn på enum-sett/kodeverks-filer. 
I stedet lister CDM-filene opp alle alternative kodeverksverdier hver gang de skal brukes.  
Men vi har ei xml-fil som bla inneholder navn på disse enumene. En tilhørende referansekpode knytter navnet til stedet i CDM-fila hvor enumen brukes. 
Fila er plassert under *.input.cdm.logisk-modell*.   

**Forrige versjon - noe å sammenlikne med** \
Disse filene er hentet direkte fra eux-rina-api under src/main/resources.  
Mappene som kopieres over er /codes, som er versjonsløs samt /sedtemplates og /sedoversettelser som inneholder versjoner

### Fundamentet - utgangspunktet for å generere diverse filsett
Koden trekker ut info fra input-filene og setter den sammen til to ulike filsett.
Det ene er fundamentet for å generere ACL-maler, pdf-maler samt testdata.
Disse filene er plassert under *.fundament.euSed*.

Det andre filsettet er fundamentet for å generere kodeverksfiler.
Disse filene er plassert under *.fundament.kodeverk*.

### Endringer - diff i forhold til forrige versjon.
Koden genererer de filene som inngår i fundamentet også for forrige versjon. 
På den måten kan diffen mellom to versjoner finnes og lagres.
Filene med diffen mellom to EuSed-versjoner vises i *.endringer.euSed*

Diffen mellom kodeverksversjoner vises i *.endringer.kodeverk*. 
Her vises kodeverksfiler som inneholder endringer samt de kodeverksfilene som er nye.

# Hvordan bruke koden - hvordan generere et filsett
Løsningen baserer seg på kjørbare filer plassert i pakken src/main/java/eux.acl.generator.eksekvering.
Her finner vi eksekverbare filer for å generere alle filsett: Acl-filer, pdf-filer, kodeverksfiler samt testdata.
Filene er plassert i pakkene acl, kodeverk pdf, testdata.

### Hvordan koden fungerer
Det er alltid filer som ligger under /resources som blir brukt som input til koden og resultatet blir alltid plassert på /target -katalogen.
Dette er med på å hindre at manuelle redigeringer (i filer under /resources) blir overskrevet ved et uhell samt gjør det enkelt å testkjøre hvert enkelt trinn.

Eksekveringa er delt opp i nummererte trinn der de første trinnene genererer filer som er nødvendige som input for de neste trinnene.
Siden disse output-filene alltid blir plassert i /target -mappa er det nødvendig med et påfølgende trinn der 
de genererte filene blir kopiert fra /target til /resources.

Når alle trinnene er utført blir resultatet plassert i pakken 'resultater' under /resources.

I hvert trinn blir samtlige av de 161 SED-typer som brukes av Nav behandlet.

Jobben med å generere fundament-filer er med i flere jobb-lister. Det er samme jobben over alt, men er tatt med for å gjøre jobb-listene komplette .
Videre er det ikke slik at alle trinnene nødvendigvis må utføres for å få det ønskede resultatet.  

### Lage filsett
1. Velg filsettet som skal lages. 
2. Utfør de nødvendige trinnene nedover. 
3. Finn tilsist resultatet under 'resultater'  

# Hvordan lages fundament-filene - og hvordan ser de ut
Uthenting av norske tekster er relativt rett fram og omtales ikke nærmere. Det samme gjelder enum-navn.

Nedenfor er det vist et lite utsnitt fra ei CDM-fil på json-path-format (flattened). Strukturen er her bygd opp av 'children[#]' - elementer - 
og likner slett ikke på Sed-strukturen.

Stier son slutter med '.key' inneholder tagnavn i  Sed-mal-strukturen. \
Stier som slutter med '.label' inneholder referansekoden til den norske teksten som hører til dette elementet. \
Stier som slutter med '.type' inneholder typen til elementet. 
Men den forteller ikke bare datatypen (tekst/enum/dato/...), men også om elementet skal vises i Sed-malen eller ikke. \
...osv osv med uttrekk av info som trengs for å bygge opp fundament-filene.
Ei CDM-fil for en Sed kan bestå av opptil 30.000 slike linjer:
````
"children[0].children[1].children[0].context.key" -> "dateReceiptApplication"
"children[0].children[1].children[1].fieldType" -> "formArray"
"children[0].children[1].children[1].fieldId" -> "9"
"children[0].children[1].children[1].layout.class" -> "col-12"
"children[0].children[1].children[1].context.type" -> "_rinaArray"
"children[0].children[1].children[1].context.inputs.label" -> "forms.4.3_1FE77E1F-137C-E611-80CF-000D3A22E8CD_label"
"children[0].children[1].children[1].context.inputs.preLabel" -> "2.2"
"children[0].children[1].children[1].context.key" -> "RequestForPeriodInformation"
````
De ulike datatypene som trekkes ut legges i key-value lister der 'children'-stien er nøkkel:
```
cdmSti vs Tagtype;
cdmSti vs Indeks;
cdmSti vs PreIndeks;
cdmSti vs Ledetekst;
cdmSti vs EnumNavn;
cdmSti vs EnumkodeTekst;
cdmSti vs EnumkodeId;
cdmSti vs TekstId;
cdmSti vs Tekstlengde;
```

### Fundament-fil for en Sed
Verdiene fra key-value-listene kombineres basert på nøklene og det dannes elementer som består av Sed-stier med tilhørende beskrivelser, 
som norsk tekst, type, indeks, osv. 
Disse elementet inneholder all info som trengs for å lage en ACL-mal, pdf-mal eller testdata.\
Et eksempel på et element som skal vise en tekst - her navnet på en by:
```
"F001.InformationSpouseOrPartner.PersonIdentification.IfPINNotProvidedForEveryInstitutionPleaseProvide.PlaceBirth.town" : {
    "ledetekst" : "By",
    "verdiType" : "tekst",
    "cdmType" : "_uxTextInput",
    "indeks" : "7.1.8.1.1.",
    "tekstlengde" : "65"
},
```
Et element som skal inneholde en enum-verdi. 'enumNavn' refererer her til ei kodeverksfil som inneholder kodeverksmappinger:
```
"F001.InformationSpouseOrPartner.PersonIdentification.PINPersonInEachInstitution.PersonalIdentificationNumber[0].country.value[0]" : {
    "ledetekst" : "Land",
    "verdiType" : "enum",
    "cdmType" : "_uxSelectAutoCompleteInput",
    "indeks" : "7.1.7.1.1.",
    "enumNavn" : "EESSIEUEFTACountryType",
    "enumNavnId" : "35684f61-9218-e611-80ea-000c292ed0d7"
},
```
Fundament-fila for en Sed kan inneholde 600-700 slike elementer.

Her et lite utsnitt fra en resulterende Sed-mal:
```
      "PersonIdentification" : {
        "PINPersonInEachInstitution" : {
          "PersonalIdentificationNumber" : [ {
            "sector" : {
              "value" : [ "$ektefelle.personinfo.pin[x].sektor" ]
            },
```
Et lite utsnitt fra en resulterende pdf-mal (basert på samme fundament-fil):
```
  "F001.InformationOtherPersons.InformationOtherPerson[0].AddressesPerson.AddressPerson[0].typeAddress" : {
    "index" : "8.1.3.1.1.",
    "prompt" : "Type adresse",
    "valuetype" : "_uxRadioButton",
    "codework" : {
      "01" : "Bostedsland",
      "02" : "Oppholdsland",
      "03" : "Personens kontaktadresse"
    },
    "enumName" : "EESSIAddressType"
  },
  "F001.InformationOtherPersons.InformationOtherPerson[0].AddressesPerson.AddressPerson[0].Address.street" : {
    "index" : "8.1.3.1.2.1.",
    "prompt" : "Gate",
    "valuetype" : "_uxTextInput"
  },
```

### Fundament-fil for kodeverk
Samtlige verdier og tekster som brukes her er hentet fra CDM-filene samt fila med norske tekster. Elementene er nummererte og det gjør det mulig å sammenlikne nøyaktig med forrige versjon.
Enum-verdier i ei fundament-fil kan se slik ut:
````
  "0" : {
    "enumkodeId" : "01",
    "enumkodeBeskrivelse" : "Bostedsland",
    "enumkode" : "bostedsland"
  },
  "1" : {
    "enumkodeId" : "02",
    "enumkodeBeskrivelse" : "Oppholdsland",
    "enumkode" : "oppholdsland"
  }, 
  ...
````
Kodeverksfilene/enum-ene er egentlig mappingsfiler med mapping mellom eu-verdier og nav/acl-verdier.
Eu-verdiene er alltid to-sifferede mens den tilhørende acl-verdien er hentet fra den norske beskrivelsen av verdien. 
Ei endelig kodeverksfil kan se slik ut:
````
bostedsland=01
oppholdsland=02
...
````

# Prosess: Hvordan forsikre seg om at fundament-filene inneholder korrekt info.
Kontrollen gjøres med en rekke sammenlikninger.
### Fundamentet for SEDene
1. **Sammenlikner SEDene i eksisterende ACL-versjon med SEDene uttrekt fra tilsvarende (dvs forrige) CDM-versjon.** \
Det er en forutsetning at eksisterende ACL-maler er korrekte.
Antakelsen er at når disse versjonene er like gjøres uttrekkene fra CDMen korrekt. 
Videre er det en antakelse at når uttrekket av SEDene i forrige CDM-versjon gjøres korrekt 
så vil også uttrekket av SEDene i ny CDM-versjon gjøres korrekt.


2. **Sammenlikner SEDene uttrekt fra forrige CDM-versjon med SEDene uttrekt fra ny CDM-versjon.** \
Vi vil her få en - korrekt - diff mellom de to CDM-versjonene. 


3. **Sammenlikner ACL-SEDer i gammel versjon med SEDer uttrekt fra CDM i ny versjon.** \
Denne diffen skal samsvare med diffen i punktet over.


4. **I tillegg har vi kode som sammenlikner to ulike versjoner av ACL-SEDene** \
Hensikten med ACL-malene er å konvertere samt glatte over kompleksiteter i CDM-SEDene.
Dette gjør det enklere å forstå hva en mal faktisk inneholder. 
Diffen mellom ulike versjoner av ACL-SEDene kan derfor være forskjellig fra CDM-diffen. 
Men det er nyttig å ha muligheten til å se - ikke bare hva som er endringene i CDMen - 
men også hva som ender opp med å bli endringene i ACL-malene  

### Fundamentet for kodeverket
1. **Sammenlikner kodeverket/enumene i forrige CDM-versjon med kodeverket/enumene i ny CDM-versjon.** \
Det aktive kodeverket er laget slik at det i praksis ikke er tilgjengelig for sammenlikning.
Men vi kan anta at dette kodeverket stemmer overens med et uttrekk fra forrige CDM-versjon.
Vi kan da sammenlikne kodeverket fra ny og gammel CDM-versjon og på bakgrunn av dette finne hvilke 
kodeverksfiler som er endret og hvilke som er nye.

# Hvordan vises diffen mellom ny og gammel versjon
### ACL-maler
I pakken .endringer.cdmAclMaler finner vi nederst mapper som har navn etter SED-typene. 
Disse inneholder et sett med filer med info om endringene mellom ny og gammel versjon.

#### Følgende viser resultatet av en sammenlikning mellom ny og gammel CDM-versjon
- endringer.txt - innholder det som er nytt 
- uendret.txt - inneholder det som forblir uendret
- utgaatt-txt - inneholder det som er utgått
#### Følgende viser de samm resultatene, men her er ACL-malene inkludert
- uendret_acl.txt - inneholder det som forblir uendret sammen med ACL-malen.
- utgaatt_acl.txt - inneholder det som utgår fra ACL-malen sammen med ACL-malen.
#### Følgende fil inneholder grunnlaget for å lage den nye versjonen av ACL-malen 
- nyAclMal_basis.txt - inneholder både uendret_acl.txt og endringer.txt.

### Kodeverk
I pakken .endringer.kodeverk finner vi fila _kodeverkDiff.txt i mappa nyeOgEndrede.
Denne fila lister opp alle endringer i alle kodeverksfiler. Den viser nye og utgåtte verdier og filer.

I tillegg blir de kodeverksfilene som er nye eller inneholder endringer lagret i denne mappa. De er laget på fundament-formatet (som json).

# Hvordan gjøre manuelle endringer i den nye versjonen

## ACL-maler
Under /resources.endringer.cdmAclMaler og under versjonen fins en mappe som heter '_manuelleJusteringer'.
Her ligger det ei fil pr sektor. Disse filene inneholder basis for å lage ny ACL-versjon for alle SEDtypene i sektoren.

Ei lite utsnitt kan feks se slik ut:   
````
F001:Claimant.AddressesPerson.AddressPerson[0].Address.street: $bruker.personinfo.adresser[x].gate
F002:Claimant.AddressesPerson.AddressPerson[0].Address.street: $bruker.personinfo.adresser[x].gate
F003:Claimant.AddressesPerson.AddressPerson[0].Address.street: 
````
Det er satt inn kolon to steder i linjene.
Dette gjør det enkelt å splitte linjene  i et regneark og sortere på CDM-stier som er like på tvers av SEDtypen. 
Dette hjelper til med å se hvordan nye ACL-stier bør se ut (i eksempelet er det tenkt at F003 skal få adresse i ny versjon). 
 
### Merk:
Det er denne fila som blir brukt til å generere den nye versjonen av ACL-malene.

## Kodeverksmapping
Kodeverksverdiene for Nav-SEDene er laget automatisk med utgangspunkt i beskrivelsen av dem. 
Her er et eksempel på en kodeverksverdi (enumkode) som blir svært lang:
```
  "0" : {
    "enumkodeId" : "01",
    "enumkodeBeskrivelse" : "Netto gjennomsnittlig inntekt under ansettelsesforhold eller selvstendig næringsvirksomhet",
    "enumkode" : "netto_gjennomsnittlig_inntekt_under_ansettelsesforhold_eller_selvstendig_naeringsvirksomhet"
  },
```
Det er mulig å manuelt lage en kortere og bedre kodeverksverdi.
Dette gjøres ved å kopiere kodeverksfila ned i mappa '_manuelleJusteringer' og endre på enumkoden i denne fila.
```
    "enumkode" : "netto"
```

## Pdf-maler
Ingen manuelle oppgaver. Genereringen gjøres helt automatisk.

## Testdata 
### Testdata for å teste ACL-maler
Nye ACL-maler består av noen deler som er kopiert fra den gamle versjonen og noen deler som er nye og manuelt konstruert.
Her er det rom for feil og behov for tester. 

Testinga gjøres ved at en EU-SED konverteres til Nav-SED og tilbake igjen i samme test.
Antakelsen her er at testene vil feile hvis det er noe ukorrekt i malene. 
Forutsetninga er at EU-SEDen inneholder gode testdata.

EU-SEDer med test-data kan genereres til å inneholde følgende:
 - Tekster: Alle tekster består av 'æøå' etterfulgt av et løpenr. 
På den måten kan vi kontrollere at en verdi returneres til rett felt.
 - Datoer: Alle dato-felt fylles inn med en dato der årstallet inkrementeres. 
På den måten kan vi kontrollere at også datoer returneres korrekt.
- Enum-verdier: Det er mulig å lage et testdata-sett der det alltid er enten første, andre, tredje, osv enum-verdi som brukes.
Når det ikke fins høyere enum-verdier brukes siste enum-verdi. 
På denne måten er det mulig å lage testdata for spesielle tilfeller.
- Lister: Det er mulig å fylle alle lister med mer enn ett element. 
Det har vist seg at dette er nødvendig for å avdekke tilfeller 
der EU-siden og Nav-siden av en mal inneholder ulikt anntall liste-nivåer.
Dette må gjøres ved å endre i (utkommentert) kode. Det er altså ikke parameterstyrt.

### Testdata for kodeverk
Ingen dekkende tester av kodeverket. 
Men kodeverket kan testes implisitt ved at kodeverksverdi nr 1 eller 2 eller.. brukes i mal-testene.

### Testdata for pdf-malene
Ingen tester eller testdata for pdf-malene. De må kontrolleres visuelt.

__

TODO:
 - Slett filer på target-katalogen fra forrige kjøring. Hvis de ikke slettes - og de nye får andre navn vil både nye og gamle filer bli kopiert til /resources.
 

