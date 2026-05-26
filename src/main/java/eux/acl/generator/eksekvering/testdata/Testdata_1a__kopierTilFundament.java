package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.eksekvering.Versjoner;

import static eux.acl.generator.eksekvering.acl.Acl_1a__kopierInnEuSedOgKodeverkFundament.kopierInnEuSedFundament;
import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;

public class Testdata_1a__kopierTilFundament extends Versjoner {

	public static void main(String[] args) {
		kopierInnKodeverkFundament(NY_MAPPE_VERSJON);
		kopierInnEuSedFundament(NY_MAPPE_VERSJON);
	}
}
