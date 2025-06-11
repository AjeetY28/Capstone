package com.myShop.domain;

public enum AccountStatus {

    PENDING_VERIFICATION, //Account is created but not yet verified
    ACTIVE,               //Account is active and in good standing
    SUSPENDED,            //Account is temporarily suspended,possibly due to violations
    DEACTIVATED,          //Account is permanently deactivated,user may have chosen to deactivate it
    BANNED,               //Account is banned, possibly due to violations
    CLOSED                //Account is permanently closed//

}
