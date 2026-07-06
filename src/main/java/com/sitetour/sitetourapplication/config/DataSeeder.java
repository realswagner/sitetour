//package com.sitetour.sitetourapplication.config;
//
//import com.sitetour.sitetourapplication.entity.Employee;
//import com.sitetour.sitetourapplication.entity.Team;
//import com.sitetour.sitetourapplication.enums.InterviewStatus;
//import com.sitetour.sitetourapplication.repository.EmployeeRepository;
//import com.sitetour.sitetourapplication.service.TeamService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Component
//public class DataSeeder implements CommandLineRunner {
//
//    private final EmployeeRepository employeeRepository;
//    private final TeamService teamService;
//
//    public DataSeeder(EmployeeRepository employeeRepository,
//                      TeamService teamService) {
//        this.employeeRepository = employeeRepository;
//        this.teamService = teamService;
//    }
//
//    @Override
//    public void run(String... args) {
//
//        if (employeeRepository.count() > 0) {
//            return;
//        }
//
//        List<Team> teams = teamService.getAllTeams();
//
//        Team alpha = teams.get(0);
//        Team beta = teams.get(1);
//        Team gamma = teams.get(2);
//
//        Employee e1 = new Employee();
//        e1.setName("Tanaka");
//        e1.setEmail("tanaka@test.com");
//        e1.setPhoneNumber("090-1111-1111");
//        e1.setSiteLocation("Tokyo Office");
//        e1.setStatus(InterviewStatus.SCHEDULED);
//        e1.setInterviewDate(LocalDate.now().plusDays(1));
//        e1.setInterviewTime(LocalTime.of(10, 0));
//        e1.setZoomLink("https://zoom.us/example1");
//        e1.setProposedDates("6/3, 6/4");
//        e1.setTeam(alpha);
//
//        Employee e2 = new Employee();
//        e2.setName("Sato");
//        e2.setEmail("sato@test.com");
//        e2.setPhoneNumber("090-2222-2222");
//        e2.setSiteLocation("Osaka Office");
//        e2.setStatus(InterviewStatus.CONTACTING);
//        e2.setProposedDates("6/5, 6/6");
//        e2.setTeam(beta);
//
//        Employee e3 = new Employee();
//        e3.setName("Nakamura");
//        e3.setEmail("nakamura@test.com");
//        e3.setPhoneNumber("090-3333-3333");
//        e3.setSiteLocation("Remote");
//        e3.setStatus(InterviewStatus.COMPLETED);
//        e3.setTeam(gamma);
//
//        Employee e4 = new Employee();
//        e4.setName("Yamada");
//        e4.setEmail("yamada@test.com");
//        e4.setPhoneNumber("090-4444-4444");
//        e4.setSiteLocation("Tokyo HQ");
//        e4.setStatus(InterviewStatus.NOT_CONTACTED);
//        e4.setProposedDates("6/7, 6/8");
//        e4.setTeam(alpha);
//
//        Employee e5 = new Employee();
//        e5.setName("Suzuki");
//        e5.setEmail("suzuki@test.com");
//        e5.setPhoneNumber("090-5555-5555");
//        e5.setSiteLocation("Kyoto Office");
//        e5.setStatus(InterviewStatus.AWAITING_REPLY);
//        e5.setProposedDates("6/9");
//        e5.setZoomLink("https://zoom.us/example5");
//        e5.setTeam(beta);
//
//        Employee e6 = new Employee();
//        e6.setName("Ito");
//        e6.setEmail("ito@test.com");
//        e6.setPhoneNumber("090-6666-6666");
//        e6.setSiteLocation("Remote");
//        e6.setStatus(InterviewStatus.CONTACTING);
//        e6.setProposedDates("6/10, 6/11");
//        e6.setTeam(gamma);
//
//        Employee e7 = new Employee();
//        e7.setName("Kobayashi");
//        e7.setEmail("kobayashi@test.com");
//        e7.setPhoneNumber("090-7777-7777");
//        e7.setSiteLocation("Osaka HQ");
//        e7.setStatus(InterviewStatus.SCHEDULED);
//        e7.setInterviewDate(LocalDate.now().plusDays(2));
//        e7.setInterviewTime(LocalTime.of(14, 30));
//        e7.setZoomLink("https://zoom.us/example7");
//        e7.setProposedDates("6/12, 6/13");
//        e7.setTeam(alpha);
//
//        Employee e8 = new Employee();
//        e8.setName("Watanabe");
//        e8.setEmail("watanabe@test.com");
//        e8.setPhoneNumber("090-8888-8888");
//        e8.setSiteLocation("Nagoya Office");
//        e8.setStatus(InterviewStatus.COMPLETED);
//        e8.setInterviewDate(LocalDate.now().minusDays(2));
//        e8.setInterviewTime(LocalTime.of(11, 0));
//        e8.setZoomLink("https://zoom.us/example8");
//        e8.setProposedDates("6/1");
//        e8.setTeam(beta);
//
//        employeeRepository.save(e1);
//        employeeRepository.save(e2);
//        employeeRepository.save(e3);
//        employeeRepository.save(e4);
//        employeeRepository.save(e5);
//        employeeRepository.save(e6);
//        employeeRepository.save(e7);
//        employeeRepository.save(e8);
//    }
//}