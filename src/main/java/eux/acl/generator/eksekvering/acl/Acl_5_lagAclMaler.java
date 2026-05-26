package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.ResultaterFacade;

public class Acl_5_lagAclMaler extends Versjoner {

	public static void main(String[] args) {
		lagAclMaler(NY_VERSJON);
	}

	public static void lagAclMaler(String cdmVersjon) {
		ResultaterFacade resultaterFacade = new ResultaterFacade();
		resultaterFacade.lagAclMaler("A", cdmVersjon);
		resultaterFacade.lagAclMaler("DA", cdmVersjon);
		resultaterFacade.lagAclMaler("F", cdmVersjon);
		resultaterFacade.lagAclMaler("H", cdmVersjon);
		resultaterFacade.lagAclMaler("M", cdmVersjon);
		resultaterFacade.lagAclMaler("P", cdmVersjon);
		resultaterFacade.lagAclMaler("R", cdmVersjon);
		resultaterFacade.lagAclMaler("S", cdmVersjon);
		resultaterFacade.lagAclMaler("U", cdmVersjon);
		resultaterFacade.lagAclMaler("X", cdmVersjon);
	}
}