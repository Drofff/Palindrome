package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ViolationRepository;
import com.drofff.palindrome.type.ViolationsStatistic;
import com.drofff.palindrome.utils.AuthenticationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.DateUtils.inSameMonth;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViolationServiceImplTest {

    private static final String DEFAULT_VIOLATION_TYPE_ID = "testIdOfViolationType";

    private static final LocalDateTime LATE_DATE_TIME = now().minusWeeks(1);

    @Mock
    private ViolationRepository violationRepository;

    @Mock
    private PoliceService policeService;

    @Mock
    private ViolationTypeService violationTypeService;

    @InjectMocks
    private ViolationServiceImpl violationService;

    @Before
    public void init() {
        User currentUser = defaultTestUser();
        AuthenticationUtils.setCurrentUser(currentUser);
    }

    private User defaultTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("test");
        user.setPassword("password");
        user.setRole(POLICE);
        user.setActive(true);
        return user;
    }

    @Test
    public void countViolationsPerLastDaysTest() {
        int expectedResultSize = testViolations().size();
        Map<LocalDate, Integer> violationsPerLastDaysCount = getCountViolationsPerLastDaysMethodResultForArgument(10);
        int resultSize = (int) violationsPerLastDaysCount.values().stream()
                .filter(this::isPositiveNumber)
                .count();
        assertEquals(expectedResultSize, resultSize);
    }

    private boolean isPositiveNumber(int number) {
        return number > 0;
    }

    @Test(expected = ValidationException.class)
    public void countViolationsPerLastDaysZeroTest() {
        getCountViolationsPerLastDaysMethodResultForArgument(0);
    }

    @Test(expected = ValidationException.class)
    public void countViolationsPerLastDaysNegativeTest() {
        getCountViolationsPerLastDaysMethodResultForArgument(-1);
    }

    private Map<LocalDate, Integer> getCountViolationsPerLastDaysMethodResultForArgument(int days) {
        when(violationRepository.findByDateTimeAfterAndOfficerId(any(), any()))
                .thenReturn(testViolations());
        when(policeService.getPoliceByUserId(any())).thenReturn(testPolice());
        return violationService.countViolationsPerLastDays(days);
    }

    private Police testPolice() {
        Police police = new Police();
        police.setId(UUID.randomUUID().toString());
        return police;
    }

    @Test
    public void getViolationsStatisticForDriverTest() {
        Driver testDriver = testDriver();
        List<Violation> testViolations = testViolations();

        when(violationRepository.findByViolatorId(testDriver.getUserId()))
                .thenReturn(testViolations);
        when(violationTypeService.getById(DEFAULT_VIOLATION_TYPE_ID))
                .thenReturn(defaultTestViolationType());

        ViolationsStatistic violationsStatistic = violationService.getViolationsStatisticForDriver(testDriver);

        assertEquals(testViolations.size(), violationsStatistic.getViolationsCount());
        assertEquals(testViolations.size(), violationsStatistic.getUnpaidViolationsCount());
        assertEquals(LATE_DATE_TIME, violationsStatistic.getLongestUnpaidViolationDateTime());
        assertEquals(testViolations.size(), getFrequencyOfDefaultViolationType(violationsStatistic));

        int violationsOfCurrentMonthCount = countViolationsOfCurrentMonth(testViolations);
        assertEquals(violationsOfCurrentMonthCount, getViolationsOfCurrentMonthCountFromStatistic(violationsStatistic));
    }

    private List<Violation> testViolations() {
        return Stream.of(now(), now().minusDays(3), LATE_DATE_TIME)
                .map(this::testViolationWithDateTime)
                .collect(toList());
    }

    private Violation testViolationWithDateTime(LocalDateTime dateTime) {
        Violation violation = new Violation();
        violation.setId(UUID.randomUUID().toString());
        violation.setDateTime(dateTime);
        violation.setViolationTypeId(DEFAULT_VIOLATION_TYPE_ID);
        violation.setPaid(false);
        return violation;
    }

    private ViolationType defaultTestViolationType() {
        ViolationType violationType = new ViolationType();
        violationType.setId(DEFAULT_VIOLATION_TYPE_ID);
        violationType.setName("testViolationTypeName");
        return violationType;
    }

    private int getFrequencyOfDefaultViolationType(ViolationsStatistic violationsStatistic) {
        Map<ViolationType, Integer> mostFrequentViolationTypes = violationsStatistic.getMostFrequentViolationTypes();
        Optional<Integer> defaultViolationTypeFrequencyOptional = mostFrequentViolationTypes.entrySet().stream()
                .filter(entry -> isDefaultViolationType(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
        assertTrue(defaultViolationTypeFrequencyOptional.isPresent());
        return defaultViolationTypeFrequencyOptional.get();
    }

    private boolean isDefaultViolationType(ViolationType violationType) {
        return violationType.getId().equals(DEFAULT_VIOLATION_TYPE_ID);
    }

    private int countViolationsOfCurrentMonth(List<Violation> violations) {
        LocalDate today = LocalDate.now();
        return (int) violations.stream()
                .map(violation -> violation.getDateTime().toLocalDate())
                .filter(violationDate -> inSameMonth(today, violationDate))
                .count();
    }

    private int getViolationsOfCurrentMonthCountFromStatistic(ViolationsStatistic violationsStatistic) {
        LocalDate today = LocalDate.now();
        Optional<Integer> violationsOfCurrentMonthCount = violationsStatistic.getViolationsCountPerLastMonths().entrySet()
                .stream().filter(monthCountEntry -> inSameMonth(monthCountEntry.getKey(), today))
                .map(Map.Entry::getValue)
                .findFirst();
        assertTrue(violationsOfCurrentMonthCount.isPresent());
        return violationsOfCurrentMonthCount.get();
    }

    @Test
    public void getViolationsStatisticForDriverNoViolationsTest() {
        when(violationRepository.findByViolatorId(any())).thenReturn(emptyList());
        ViolationsStatistic violationsStatistic = violationService.getViolationsStatisticForDriver(testDriver());
        assertNotNull(violationsStatistic);
        assertEquals(0, violationsStatistic.getViolationsCount());
    }

    private Driver testDriver() {
        Driver driver = new Driver();
        driver.setId(UUID.randomUUID().toString());
        String testUserId = getCurrentUser().getId();
        driver.setUserId(testUserId);
        return driver;
    }

    @Test(expected = ValidationException.class)
    public void getViolationsStatisticForDriverNullTest() {
        violationService.getViolationsStatisticForDriver(null);
    }

    @Test(expected = ValidationException.class)
    public void getViolationsStatisticForDriverEmptyTest() {
        Driver emptyDriver = new Driver();
        violationService.getViolationsStatisticForDriver(emptyDriver);
    }

}
