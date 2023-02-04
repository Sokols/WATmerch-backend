package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.model.PaginatorModel;
import com.qsspy.watmerchbackend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CRMServiceTest {

    @Mock private RoleRepository roleRepository;

    private ICRMService underTest;

    @BeforeEach
    void setUp() {

        underTest = new CRMService(roleRepository);
    }

    @Test
    void getRoles() {
        //given
        //when
        underTest.getRoles();

        //then
        verify(roleRepository).findAll();
    }

    @Test
    void itShouldGetPaginatorWithFiveSelectablePages() {
        //given
        PaginatorModel targetPaginator = new PaginatorModel(
                Arrays.asList(4,5,6),true,true,10
        );

        int currentPage = 5;
        int lastPage = 10;
        //when
        PaginatorModel outputModel = underTest.getPageIndexes(currentPage, lastPage);
        //then
        assertThat(outputModel).isEqualTo(targetPaginator);

    }

    @Test
    void itShouldGetPaginatorWithFourSelectablePagesIncludingFirstPage() {
        //given
        PaginatorModel targetPaginator = new PaginatorModel(
                Arrays.asList(1,2,3),false,true,10
        );

        int currentPage = 2;
        int lastPage = 10;
        //when
        PaginatorModel outputModel = underTest.getPageIndexes(currentPage, lastPage);
        //then
        assertThat(outputModel).isEqualTo(targetPaginator);

    }

    @Test
    void itShouldGetPaginatorWithFourSelectablePagesIncludingLastPage() {
        //given
        PaginatorModel targetPaginator = new PaginatorModel(
                Arrays.asList(8,9,10),true,false,10
        );

        int currentPage = 9;
        int lastPage = 10;
        //when
        PaginatorModel outputModel = underTest.getPageIndexes(currentPage, lastPage);
        //then
        assertThat(outputModel).isEqualTo(targetPaginator);

    }

    @Test
    void itShouldGetPaginatorWithThreeSelectablePages() {
        //given
        PaginatorModel targetPaginator = new PaginatorModel(
                Arrays.asList(1,2,3),false,false,3
        );

        int currentPage = 2;
        int lastPage = 3;
        //when
        PaginatorModel outputModel = underTest.getPageIndexes(currentPage, lastPage);
        //then
        assertThat(outputModel).isEqualTo(targetPaginator);

    }

    @Test
    void itShouldGetPaginatorWithTwoSelectablePages() {
        //given
        PaginatorModel targetPaginator = new PaginatorModel(
                Arrays.asList(1,2),false,false,2
        );

        int currentPage = 1;
        int lastPage = 2;
        //when
        PaginatorModel outputModel = underTest.getPageIndexes(currentPage, lastPage);
        //then
        assertThat(outputModel).isEqualTo(targetPaginator);

    }

    @Test
    void itShouldGetPaginatorWithOneSelectablePages() {
        //given
        PaginatorModel targetPaginator = new PaginatorModel(
                Arrays.asList(1),false,false,1
        );

        int currentPage = 1;
        int lastPage = 1;
        //when
        PaginatorModel outputModel = underTest.getPageIndexes(currentPage, lastPage);
        //then
        assertThat(outputModel).isEqualTo(targetPaginator);

    }

    @Test
    void getPageSizes() {
        //given
        List<Integer> expectedArray = Arrays.asList(10, 20, 50);
        //when
        List<Integer> outputArray = underTest.getPageSizes();
        //then
        assertThat(outputArray).isEqualTo(expectedArray);
    }
}