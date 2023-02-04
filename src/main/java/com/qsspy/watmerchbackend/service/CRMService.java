package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.Role;
import com.qsspy.watmerchbackend.model.PaginatorModel;
import com.qsspy.watmerchbackend.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CRMService implements ICRMService{

    private final RoleRepository roleRepository;

    public CRMService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public PaginatorModel getPageIndexes(int currentPage, int lastPage) {
        PaginatorModel paginatorModel = new PaginatorModel();

        int pageBeforeCurrent = currentPage - 1;
        int pageAfterCurrent = currentPage + 1;

        if(pageBeforeCurrent<2) {
            paginatorModel.setShowFirstPage(false);
        }

        if(pageAfterCurrent > lastPage - 1) {
            paginatorModel.setShowLastPage(false);
        }

        paginatorModel.setLastPage(lastPage);

        List<Integer> clickablePages = new ArrayList<>();
        if(currentPage > 1) {
            clickablePages.add(pageBeforeCurrent);
        }

        clickablePages.add(currentPage);

        if(currentPage<lastPage) {
            clickablePages.add(pageAfterCurrent);
        }

        paginatorModel.setPages(clickablePages);

        return paginatorModel;
    }

    @Override
    public List<Integer> getPageSizes() {
        return Arrays.asList(10,20,50);
    }
}
