package eux.acl;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

public class S040_SedKode {

    private final String tittel = "Forespørsel om perioder - trygdeytelse: sykdom, svangerskap og fødsel.";
    private String berOmOversiktOverYtelserAvType;
    //private List<Periode> berOmOversiktForDissePeriodene;
    private BrukerPersonInfo brukerPersonInfo;
    private String botidStartdato;
    private Boolean erKontantYtelse;
    private String ytterligereInformasjon;

    private String euSedSti(String jsonPath, String euSedJson) {
        return JsonPath.read(euSedJson, jsonPath);
    }

    public String generate() {
        // lesTemplate
        // "bruker.adresse.gate"
        Map<String, String> kodeVsEuSti = null; // getEuSedMap("S040");

        // Objekter lages ei gang for alle i domenet
        Adresse adresse1 = Brukeradresse.builder()
                .gate(euSti("bruker.adresse.gate"))
                .bygning(kodeVsEuSti.get("bruker.adresse.bygning"))
                .build();

        BrukerPersonInfo brukerPersonInfo1 = BrukerPersonInfo.builder()
                .adresse(adresse1)
                .fornavn(euSti("bruker.fornavn"))
                //-----------------------------------------------------------
                .kjoenn(euSti("bruker.kjoenn" /* , Enum*/)) // enum kun i Java?
                //-----------------------------------------------------------

                .build();
return null;
    }

    public String euSti(String euSedJson) {
        Brukeradresse adress = new Brukeradresse("g","b");
        adress.bygning(euSedSti("$.AddressPerson.Address.street", euSedJson));


        String ss = euSedSti("$.AddressPerson.Address.street", euSedJson);
        String dd = euSedSti("$.AddressPerson.Address.buildingName", euSedJson);

        Adresse adresse = new Adresse() {
            @Override
            public String gate() {
                return euSedSti("$.AddressPerson.Address.street", euSedJson);
            }

            @Override
            public String bygning() {
                return euSedSti("$.AddressPerson.Address.buildingName", euSedJson);
            }


            String bygning = euSedSti("$.AddressPerson.Address.buildingName", euSedJson);

            // 1: values
            // 2: enums
            // 3: lists
            List<Object> authors = JsonPath.read(euSedJson, "$.store.book[*].author");
            String author = JsonPath.read(euSedJson, "$.store.book[1].author");
        };

            return"";
        }

    private Adresse finnAdresse_S040() {
    return Brukeradresse.builder()
            .bygning(euSti("AddressPerson.Address.buildingName"))
            .gate(euSti("AddressPerson.Address.street"))
            .build();

//            "AddressPerson.Address.country",
//            "AddressPerson.Address.postalCode",
//            "AddressPerson.Address.region",
//            "AddressPerson.Address.street",
//            "AddressPerson.Address.town"

    }

}
