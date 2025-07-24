# eux-acl-generator
<h3>Verktøy for å generere nye versjoner av maler</h3>

Genereringa skjer i flere steg. Den innbefatter også manuelle steg.
Utgangspunktet for hvert steg ligger i main/resources. Resultatet legges under /target.
Det som ligger under /target må ev kopieres til main/resources for videre prossessering.
Dette er med på å hindre at manuelle redigeringer blir overskrevet ved et uhell samt gjør det enkelt å testkjøre hvert enkelt trinn. 

<h3>Lage ny versjon av malene som brukes for å generere SEDer på Acl-format</h3>
1. Leser cdm-meta-filene. 
2. Henter ut stiene i EU-SEDene. Sammenlikner med forrige versjon. Lager tre ulike filer: 
- ei fil med stier som fins i begge versjonene
- ei fil med stier som kun fins i ny versjon
- ei fil med stier som kun fins i forrige versjon
2. kodeverk diff med forrige versjon
 -> endringer: sti, enumnavn, verdier i ny og gammel fil.
3. basisfiler -> mal
<h3> Lage ny versjon av malene som brukes for å generere pdf</h3>
1. Cdm filer -> uttrekk med oversettelser og div annet.

