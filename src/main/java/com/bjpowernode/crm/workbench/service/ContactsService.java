package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> searchContacts(String aname);

}
