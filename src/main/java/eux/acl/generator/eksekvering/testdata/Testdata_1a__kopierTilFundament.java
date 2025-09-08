package eux.acl.generator.eksekvering.testdata;

import static eux.acl.generator.eksekvering.acl.Acl_1a__kopierInnEuSedOgKodeverkFundament.kopierInnEuSedFundament;
import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;
import static eux.acl.generator.service.FilService.MAPPE_VERSJON_v43;
import static eux.acl.generator.service.FilService.MAPPE_VERSJON_v44;

public class Testdata_1a__kopierTilFundament {


    public static void main(String[] args) {
        kopierInnKodeverkFundament(MAPPE_VERSJON_v43);
        kopierInnKodeverkFundament(MAPPE_VERSJON_v44);
        kopierInnEuSedFundament(MAPPE_VERSJON_v43);
        kopierInnEuSedFundament(MAPPE_VERSJON_v44);
    }
}
