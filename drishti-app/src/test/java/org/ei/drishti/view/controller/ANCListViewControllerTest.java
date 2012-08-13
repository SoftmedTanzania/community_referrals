package org.ei.drishti.view.controller;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.ei.drishti.domain.EligibleCouple;
import org.ei.drishti.domain.Mother;
import org.ei.drishti.repository.AllBeneficiaries;
import org.ei.drishti.repository.AllEligibleCouples;
import org.ei.drishti.view.contract.ANC;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ANCListViewControllerTest {
    @Mock
    private AllEligibleCouples allEligibleCouples;
    @Mock
    private AllBeneficiaries allBeneficiaries;
    @Mock
    private Context context;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldSortANCsByName() throws Exception {
        when(allBeneficiaries.allANCs()).thenReturn(asList(new Mother("Case 3", "EC Case 3", "TC 3", "2032-03-03"), new Mother("Case 1", "EC Case 1", "TC 1", "2012-01-01"), new Mother("Case 2", "EC Case 2", "TC 2", "2022-02-02")));
        when(allEligibleCouples.findByCaseID("EC Case 2")).thenReturn(new EligibleCouple("EC Case 2", "woman B", "Husband B", "EC Number 2", "IUD", "Bherya", "Bherya SC"));
        when(allEligibleCouples.findByCaseID("EC Case 3")).thenReturn(new EligibleCouple("EC Case 3", "Woman C", "Husband C", "EC Number 3", "IUD", "Bherya", "Bherya SC"));
        when(allEligibleCouples.findByCaseID("EC Case 1")).thenReturn(new EligibleCouple("EC Case 1", "Woman A", "Husband A", "EC Number 1", "IUD", "Bherya", "Bherya SC"));

        ANCListViewController controller = new ANCListViewController(allBeneficiaries, allEligibleCouples, context);
        List<ANC> ancs = new Gson().fromJson(controller.get(), new TypeToken<List<ANC>>() {
        }.getType());

        assertEquals("Woman A", ancs.get(0).womanName());
        assertEquals("woman B", ancs.get(1).womanName());
        assertEquals("Woman C", ancs.get(2).womanName());
    }
}