# F002 / v44

CDM-fila for F002, v44  inneholder i utgangspunktet noen feil. 
De er korrigert i CDM-fila ved å endre tekster og i koden ved å fjerne dubletter. 

---
Feil 1:
Stien:
"F002.InformationOtherPersons.InformationOtherPerson[0].StatusPerson.Statuses.Status[0].NoInformationAboutActivityPerson.PeriodsNonActivity.Period[0].OpenPeriod.typeOpenPeriod.startDate"
har (hadde) følgende "prefix" = "10.1.7.1.1.2.1.1.2.1.2.2."
mens verdien skulle ha vært (er)  = "10.1.7.1.1.3.1.1.2.2."

Dette gjelder linje 55590,og ikke 56144 i CDM-fila. Begge inneholder i utgangspunktet "prefix" = "10.1.7.1.1.2.1.1.2.1.2.2."

---
Feil 2:
CDM-fila for F002 inneholder to elementer med "preLabel" = "10.1" (i linje 41052 og 41348).
Stier:
F002:InformationOtherPersons.InformationOtherPerson[0]
F002:InformationOtherPersons.InformationOtherPerson[0].InformationOtherPerson[0]

Pdf-mal-generatoren er laget slik at den logger dubletter og ignorerer de overskytende. 
På den måten får vi en fungerende Pdf-mal.

---
Feil 3:
Henger sammen med feil 2.
Disse linjene inneholder elementet .InformationOtherPerson[0] to ganger mens 1 gang er korrekt:    
Feil:
F002:InformationOtherPersons.InformationOtherPerson[0].InformationOtherPerson[0].AdditionalInformationAboutNationality.Nationalities[0].nationality.value[0]:$andrepersoner[x].personinfo.statsborgerskap[x].land
F002:InformationOtherPersons.InformationOtherPerson[0].InformationOtherPerson[0].AdditionalInformationAboutNationality.Nationalities[0].since:$andrepersoner[x].personinfo.statsborgerskap[x].siden
Korrekt:
F002:InformationOtherPersons.InformationOtherPerson[0].AdditionalInformationAboutNationality.Nationalities[0].nationality.value[0]:$andrepersoner[x].personinfo.statsborgerskap[x].land
F002:InformationOtherPersons.InformationOtherPerson[0].AdditionalInformationAboutNationality.Nationalities[0].since:$andrepersoner[x].personinfo.statsborgerskap[x].siden

ACL-en fungerer selv om den har 2 liste-nivåer, mens vi her har 3.

# F002 / v45
Feil 2 i v4.4 er ikke rettet. 

Feil 2:
CDM-fila for F002 inneholder to elementer med "preLabel": "10.1" (i linje 40960 og 45241).
Stier:
F002:InformationOtherPersons.InformationOtherPerson[0]
F002:InformationOtherPersons.InformationOtherPerson[0].InformationOtherPerson[0]