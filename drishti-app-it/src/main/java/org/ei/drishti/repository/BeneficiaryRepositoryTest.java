package org.ei.drishti.repository;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import org.ei.drishti.domain.Beneficiary;
import org.ei.drishti.domain.TimelineEvent;
import org.ei.drishti.util.Session;

import java.util.Date;

import static java.util.Arrays.asList;

public class BeneficiaryRepositoryTest extends AndroidTestCase {
    private BeneficiaryRepository repository;
    private TimelineEventRepository timelineEventRepository;

    @Override
    protected void setUp() throws Exception {
        timelineEventRepository = new TimelineEventRepository();
        repository = new BeneficiaryRepository(timelineEventRepository);
        Session session = new Session().setPassword("password").setRepositoryName("drishti.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, repository, timelineEventRepository);
    }

    public void testShouldInsertMother() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));

        assertEquals(asList(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08")), repository.allBeneficiaries());
        assertEquals(asList(TimelineEvent.forStartOfPregnancy("EC Case 1", "2012-06-08")), timelineEventRepository.allFor("EC Case 1"));
    }

    public void testShouldInsertChildForExistingMother() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));
        repository.addChild("CASE A", "2012-06-09", "CASE X", "female");

        assertEquals(asList(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"), new Beneficiary("CASE A", "EC Case 1", "TC 1", "2012-06-09")), repository.allBeneficiaries());
        assertEquals(asList(TimelineEvent.forStartOfPregnancy("EC Case 1", "2012-06-08"), TimelineEvent.forChildBirth("EC Case 1", "2012-06-09", "female")), timelineEventRepository.allFor("EC Case 1"));
    }

    public void testShouldNotInsertChildForANonExistingMother() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));
        repository.addChild("CASE A", "2012-06-09", "CASE NON-EXISTING-MOTHER", "female");

        assertEquals(asList(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08")), repository.allBeneficiaries());
    }

    public void testShouldFetchBeneficiariesByECCaseId() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));
        repository.addMother(new Beneficiary("CASE Y", "EC Case 1", "TC 2", "2012-06-08"));
        repository.addMother(new Beneficiary("CASE Z", "EC Case 2", "TC 3", "2012-06-08"));
        repository.addChild("CASE A", "2012-06-09", "CASE Z", "female");

        assertEquals(asList(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"), new Beneficiary("CASE Y", "EC Case 1", "TC 2", "2012-06-08")), repository.findByECCaseId("EC Case 1"));
        assertEquals(asList(new Beneficiary("CASE Z", "EC Case 2", "TC 3", "2012-06-08"), new Beneficiary("CASE A", "EC Case 2", "TC 3", "2012-06-09")), repository.findByECCaseId("EC Case 2"));
    }

    public void testShouldCountOnlyPregnantBeneficiariesInRepoAsANCs() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));
        repository.addMother(new Beneficiary("CASE Y", "EC Case 1", "TC 2", "2012-06-08"));
        assertEquals(2, repository.ancCount());

        repository.addMother(new Beneficiary("CASE Z", "EC Case 2", "TC 3", "2012-06-08"));
        assertEquals(3, repository.ancCount());

        repository.addChild("CASE A", "2012-06-09", "CASE Z", "female");
        assertEquals(2, repository.ancCount());

        repository.close("CASE Y");
        assertEquals(1, repository.ancCount());
    }

    public void testShouldCountBeneficiariesAfterBirthOrAbortionAsPNCs() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));
        assertEquals(0, repository.pncCount());

        repository.addChild("CASE A", "2012-06-09", "CASE X", "female");
        assertEquals(1, repository.pncCount());

        repository.close("CASE X");
        assertEquals(0, repository.pncCount());
    }

    public void testShouldCountChildren() throws Exception {
        repository.addMother(new Beneficiary("CASE X", "EC Case 1", "TC 1", "2012-06-08"));
        repository.addMother(new Beneficiary("CASE Y", "EC Case 2", "TC 2", "2012-06-08"));
        assertEquals(0, repository.childCount());

        repository.addChild("CASE A", "2012-06-09", "CASE X", "female");
        assertEquals(1, repository.childCount());

        repository.addChild("CASE B", "2012-06-09", "CASE Y", "female");
        assertEquals(2, repository.childCount());

        repository.close("CASE B");
        assertEquals(1, repository.childCount());
    }
}