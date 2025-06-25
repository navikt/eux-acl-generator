package eux.acl;

import lombok.Builder;

@Builder
public class Brukeradresse implements Adresse {
    private String gate;
    private String bygning;

    @Override
    public String gate() {
        return "S040.AddressPerson.Address.street";
//             "S048.PersonAddress.street";
    }

    @Override
    public String bygning() {
        return bygning;
    }
    public void bygning(String bygning) {
         this.bygning = bygning;
    }


}
