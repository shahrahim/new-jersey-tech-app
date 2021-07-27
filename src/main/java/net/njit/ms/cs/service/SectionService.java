package net.njit.ms.cs.service;

import lombok.extern.slf4j.Slf4j;
import net.njit.ms.cs.exception.BadRequestRequestException;
import net.njit.ms.cs.exception.ResourceNotCreatedException;
import net.njit.ms.cs.exception.ResourceNotDeletedException;
import net.njit.ms.cs.exception.ResourceNotFoundException;
import net.njit.ms.cs.model.dto.request.SectionDto;
import net.njit.ms.cs.model.dto.request.SectionUpdateDto;
import net.njit.ms.cs.model.dto.response.SectionResponse;
import net.njit.ms.cs.model.dto.response.SidDto;
import net.njit.ms.cs.model.dto.response.SsnDto;
import net.njit.ms.cs.model.entity.*;
import net.njit.ms.cs.repository.CourseRepository;
import net.njit.ms.cs.repository.RoomRepository;
import net.njit.ms.cs.repository.SectionRepository;
import net.njit.ms.cs.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;
    private final StudentRepository studentRepository;
    private final RoomService roomService;
    private final StudentService studentService;
    private final StaffService staffService;

    private static final String TA = "TA";
    private static final String FACULTY = "Faculty";

    public SectionService(SectionRepository sectionRepository,
                          CourseRepository courseRepository,
                          RoomRepository roomRepository,
                          StudentRepository studentRepository,
                          RoomService roomService,
                          StudentService studentService,
                          StaffService staffService) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
        this.roomService = roomService;
        this.studentService = studentService;
        this.staffService = staffService;
    }

    public List<Section> getAllSections() {
        return this.sectionRepository.findAll();
    }

    public Section getSectionById(SectionId sectionId) {
        Integer sectionNumber = sectionId.getNumber();
        Integer courseNumber = sectionId.getCourseNumber();

        return this.sectionRepository.findById(sectionId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(
                        "Section with number: %s and courseNumber: %s not found",
                        sectionNumber, courseNumber)));
    }

    public Section getCreatedSection(SectionDto sectionDto) {
        Integer sectionNumber = sectionDto.getNumber();
        String facultySsn = sectionDto.getFacultySsn();
        Integer courseNumber = sectionDto.getCourseNumber();
        String semester = sectionDto.getSemester();
        String year = sectionDto.getYear();

        SectionId sectionId = new SectionId(sectionNumber, courseNumber);

        if (this.sectionRepository.existsById(sectionId)) {
            String message = String.format(
                    "Section with number: %s facultySsn: %s and courseNumber: %s already exists",
                    sectionNumber, facultySsn, courseNumber);
            log.error(message);
            throw new BadRequestRequestException(message);
        } else if (semester == null || semester.equals("") || semester.trim().equals("")) {
            String message = String.format("Semester cannot be null or empty: %s", semester);
            log.error(message);
            throw new BadRequestRequestException(message);
        } else if (year == null || year.equals("") || year.trim().equals("")) {
            String message = String.format("Year cannot be null or empty: %s", semester);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.getCreateOrReplacedSection(this.getNewSection(sectionDto));
    }

    public Section getUpdatedSection(SectionUpdateDto sectionDto) {
        SectionId sectionId = new SectionId(sectionDto.getNumber(), sectionDto.getCourseNumber());
        Section section = this.getSectionById(sectionId);
        handleSectionUpdate(section, sectionDto);
        return this.getCreateOrReplacedSection(section);
    }

    public void deleteSection(SectionId sectionId) {
        Section section = this.getSectionById(sectionId);
        try {
            this.sectionRepository.delete(section);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong deleting section with number: %s to backend", section.getNumber());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotDeletedException(message);
        }
    }

    public static SectionResponse getSectionResponse(Section section) {
        SectionResponse sectionResponse = new SectionResponse();

        sectionResponse.setNumber(section.getNumber());
        sectionResponse.setFacultySsn(section.getFacultySsn());
        sectionResponse.setCourseNumber(section.getCourseNumber());
        section.setMaxEnroll(section.getMaxEnroll());
        sectionResponse.setSemester(section.getSemester());
        sectionResponse.setYear(section.getYear());

        Set<RoomId> rooms = new HashSet<>();
        section.getRooms().forEach(room -> {
            RoomId roomId = new RoomId();
            roomId.setRoomNumber(room.getRoomNumber());
            roomId.setBuildingNumber(room.getBuildingNumber());
            rooms.add(roomId);
        });
        sectionResponse.setRooms(rooms);

        Set<SsnDto> teachingAssistants = new HashSet<>();
        section.getTeachingAssistants().forEach(teachingAssistant -> {
            SsnDto ssnDto = new SsnDto();
            ssnDto.setSsn(teachingAssistant.getSsn());
            teachingAssistants.add(ssnDto);
        });
        sectionResponse.setTeachingAssistants(teachingAssistants);

        Set<SidDto> students = new HashSet<>();
        section.getStudents().forEach(student -> {
            SidDto sidDto = new SidDto();
            sidDto.setSid(student.getSid());
            students.add(sidDto);
        });
        sectionResponse.setStudents(students);

        return sectionResponse;
    }

    public Section getCreateOrReplacedSection(Section section) {
        try {
            return this.sectionRepository.save(section);
        } catch (Exception e) {
            String message = String.format(
                    "Something went wrong creating or replacing section with ssn: %s to backend", section.getNumber());
            log.error("{} {}", message, e.getMessage());
            throw new ResourceNotCreatedException(message);
        }
    }

    private void handleSectionUpdate(Section section, SectionUpdateDto sectionUpdateDto) {
        sectionUpdateDto.getRoomsToRemove().forEach(roomNumberToRemove -> {
            Room room = this.roomService.getRoomById(roomNumberToRemove);
            section.getRooms().remove(room);
        });

        Set<RoomId> currentRoomNumbers = new HashSet<>();
        section.getRooms().forEach(room -> currentRoomNumbers.add(
                new RoomId(room.getRoomNumber(), room.getBuildingNumber())));
        sectionUpdateDto.getRoomsToAdd().forEach(roomNumberToAdd -> {
            if (!currentRoomNumbers.contains(roomNumberToAdd)) {
                Room room = this.roomService.getRoomById(roomNumberToAdd);
                section.getRooms().add(room);
            }
        });

        sectionUpdateDto.getTeachingAssistantsToRemove().forEach(teachingAssistantSsn -> {
            Staff teachingAssistant = this.staffService
                    .getStaffById(teachingAssistantSsn);
            section.getTeachingAssistants().remove(teachingAssistant);
        });

        Set<String> currentTeachingAssistants = new HashSet<>();
        section.getTeachingAssistants().forEach(teachingAssistant ->
                currentTeachingAssistants.add(teachingAssistant.getSsn()));
        sectionUpdateDto.getTeachingAssistantsToAdd().forEach(teachingAssistantSsn -> {
            if (!currentTeachingAssistants.contains(teachingAssistantSsn)) {
                Staff teachingAssistant = this.staffService.getStaffById(teachingAssistantSsn);
                section.getTeachingAssistants().add(teachingAssistant);
            }
        });

        sectionUpdateDto.getStudentsToRemove().forEach(studentSidToRemove -> {
            Student student = this.studentService.getStudentById(studentSidToRemove);
            section.getStudents().remove(student);
        });

        Set<String> currentStudentSids = new HashSet<>();
        section.getStudents().forEach(student -> currentStudentSids.add(student.getSid()));
        sectionUpdateDto.getStudentsToAdd().forEach(studentSidToAdd -> {
            if (!currentStudentSids.contains(studentSidToAdd)) {
                Student student = this.studentService.getStudentById(studentSidToAdd);
                section.getStudents().add(student);
            }
        });
    }

    private Section getNewSection(SectionDto sectionDto) {
        Section section = new Section();
        section.setNumber(sectionDto.getNumber());
        section.setFacultySsn(this.getStaffForRequest(sectionDto.getFacultySsn(), FACULTY).getSsn());
        section.setCourseNumber(this.getCourseForRequest(sectionDto.getCourseNumber()).getNumber());
        section.setRooms(this.getRoomsForRequest(sectionDto.getRooms()));
        section.setTeachingAssistants(this.getTeachingAssistantsForRequest(sectionDto.getTeachingAssistantSsns()));
        section.setStudents(this.getStudentsForRequest(sectionDto.getStudents()));
        section.setMaxEnroll(sectionDto.getMaxEnroll());
        section.setSemester(sectionDto.getSemester());
        section.setYear(sectionDto.getYear());
        return section;
    }

    private Set<Staff> getTeachingAssistantsForRequest(Set<String> ssns) {
        Set<Staff> teachingAssistants = new HashSet<>();
        ssns.forEach(ssn -> teachingAssistants.add(this.getStaffForRequest(ssn, TA)));
        return teachingAssistants;
    }

    private Staff getStaffForRequest(String ssn, String type) {
        Staff faculty = this.staffService.getStaffById(ssn);
        String facultyType = faculty.getType();
        System.out.println(String.format("%s %s", ssn, type));
        if (!facultyType.equals(type)) {
            String message = String.format("Staff %s is of type %s and should be of type %s", ssn, facultyType, type);
            log.error(message);
            throw new BadRequestRequestException(message);
        }
        return this.staffService.getStaffById(ssn);
    }

    private Course getCourseForRequest(Integer courseNumber) {
        return this.courseRepository.findById(courseNumber).orElseThrow(
                () -> new ResourceNotFoundException(String.format(
                        "Course with number: %s does not exist", courseNumber)));
    }

    private Set<Room> getRoomsForRequest(Set<RoomId> roomNumbers) {
        Set<Room> rooms = new HashSet<>();
        roomNumbers.forEach(roomNumber -> {
            rooms.add(this.roomRepository.findById(roomNumber).orElseThrow(
                    () -> new ResourceNotFoundException(String.format(
                            "Room with number: %s does not exist", roomNumber))));
        });
        return rooms;
    }

    private Set<Student> getStudentsForRequest(Set<String> sids) {
        Set<Student> students = new HashSet<>();

        sids.forEach(sid -> {
            students.add(this.studentRepository.findById(sid).orElseThrow(
                    () -> new ResourceNotFoundException(String.format(
                            "Student with sid: %s does not exist", sid))));
        });
        return students;
    }

}